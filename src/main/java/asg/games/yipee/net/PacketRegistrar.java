package asg.games.yipee.net;

import com.esotericsoftware.kryo.Kryo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PacketRegistrar dynamically scans and registers all packet classes in the specified packages.
 * It supports reading from an XML configuration file (packets.xml) to extend or override
 * default package and exclusion lists.
 */
public class PacketRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(PacketRegistrar.class);
    private static final String CONFIG_FILE = "packets.xml";
    private static final AtomicInteger atomicIdCounter = new AtomicInteger(2000);
    private static final Map<String, Integer> explicitClassIds = new LinkedHashMap<>();
    private static Set<String> excludedClasses = new LinkedHashSet<>();
    private static Document packetsXMLDocument = null;

    /**
     * Reloads the configuration from the specified path.
     *
     * @param path Configuration file path.
     * @throws ParserConfigurationException if XML parser is misconfigured.
     * @throws IOException                  if an IO error occurs.
     * @throws SAXException                 if an XML parsing error occurs.
     */
    public static void reloadConfiguration(String path) throws ParserConfigurationException, IOException, SAXException {
        String localFilePath = path != null ? path : CONFIG_FILE;
        File packetFile = getPacketFile(localFilePath);
        if (packetFile.exists()) {
            packetsXMLDocument = loadXMLDocument(packetFile);
            Set<String> packages = loadEntries("packages", "package");
            excludedClasses = loadEntries("excludedClasses", "class");
            loadExplicitMappings();
            logger.info("PacketRegistrar initialized with {} packages and {} explicit mappings.", packages.size(), explicitClassIds.size());
        } else {
            logger.error("No packets.xml found at {}", packetFile.getAbsolutePath());
            throw new ParserConfigurationException("No packets.xml found at " + packetFile.getAbsolutePath());
        }
    }

    /**
     * Reloads the configuration from a classpath resource.
     *
     * @throws ParserConfigurationException if XML parser is misconfigured.
     * @throws IOException                  if an IO error occurs.
     * @throws SAXException                 if an XML parsing error occurs.
     */
    public static void reloadConfigurationFromResource() throws ParserConfigurationException, IOException, SAXException {
        try (InputStream inputStream = PacketRegistrar.class.getClassLoader().getResourceAsStream("packets.xml")) {
            if (inputStream == null) {
                throw new IOException("packets.xml not found in classpath.");
            }
            packetsXMLDocument = loadXMLDocument(inputStream);
            Set<String> packages = loadEntries("packages", "package");
            excludedClasses = loadEntries("excludedClasses", "class");
            loadExplicitMappings();
            logger.info("PacketRegistrar initialized from classpath with {} packages and {} explicit mappings.", packages.size(), explicitClassIds.size());
        }
    }

    /**
     * Loads explicit class-ID mappings from the XML configuration.
     */
    static void loadExplicitMappings() {
        if (packetsXMLDocument == null) return;

        NodeList mappings = packetsXMLDocument.getElementsByTagName("mappings");
        if (mappings.getLength() > 0) {
            NodeList list = mappings.item(0).getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("mapping")) {
                    Element element = (Element) node;
                    String className = element.getAttribute("class").trim();
                    String idText = element.getAttribute("id").trim();
                    if (!className.isEmpty() && !idText.isEmpty()) {
                        try {
                            int id = Integer.parseInt(idText);
                            if (!explicitClassIds.containsKey(className)) {
                                explicitClassIds.put(className, id);
                                logger.info("Mapped {} => ID {}", className, id);
                            } else {
                                logger.warn("Duplicate mapping ignored for class: {}", className);
                            }
                        } catch (NumberFormatException e) {
                            logger.error("Invalid ID for class {}", className);
                        }
                    }
                }
            }
        }
    }

    /**
     * Registers all packet classes and array types to the provided Kryo instance.
     * @param kryo Kryo instance to register classes.
     */
    public static void registerPackets(Kryo kryo) {
        if (kryo == null) {
            logger.error("Kryo instance is null");
            return;
        }

        Set<Class<?>> registered = new HashSet<>();
        registerAllArrayTypes(kryo);

        for (Map.Entry<String, Integer> entry : explicitClassIds.entrySet()) {
            try {
                Class<?> clazz = Class.forName(entry.getKey());
                if (!excludedClasses.contains(clazz.getName())) {
                    kryo.register(clazz, entry.getValue());
                    registered.add(clazz);
                    registerFieldTypes(kryo, clazz, registered);
                }
            } catch (ClassNotFoundException e) {
                logger.warn("Explicit class not found: {}", entry.getKey());
            }
        }
    }

    /**
     * Registers Serializable field types of the specified class.
     * @param kryo Kryo instance to register fields.
     * @param clazz Class whose fields are to be registered.
     * @param registered Set of already registered classes.
     */
    private static void registerFieldTypes(Kryo kryo, Class<?> clazz, Set<Class<?>> registered) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            if (!type.isPrimitive() && !registered.contains(type) && Serializable.class.isAssignableFrom(type)) {
                try {
                    kryo.register(type);
                    registered.add(type);
                    logger.debug("Auto-registered field type: {}", type.getName());
                } catch (Exception e) {
                    logger.warn("Could not auto-register type: {}", type.getName());
                }
            }
        }
    }

    /**
     * Converts a file path string into a File object.
     * @param path File path.
     * @return File object.
     */
    private static File getPacketFile(String path) {
        return new File(path);
    }

    /**
     * Loads and parses the XML document from the specified file.
     * @param file XML configuration file.
     * @return Parsed XML Document.
     * @throws ParserConfigurationException if a parser cannot be created.
     * @throws IOException if an IO error occurs.
     * @throws SAXException if parsing fails.
     */
    private static Document loadXMLDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Loads and parses the XML document from an InputStream.
     *
     * @param inputStream XML input stream.
     * @return Parsed XML Document.
     * @throws ParserConfigurationException if a parser cannot be created.
     * @throws IOException                  if an IO error occurs.
     * @throws SAXException                 if parsing fails.
     */
    private static Document loadXMLDocument(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputStream);
        document.getDocumentElement().normalize();
        return document;
    }

    /**
     * Loads entries from a specific parent and child XML structure.
     * @param parentTag Parent XML tag name.
     * @param childTag Child XML tag name.
     * @return Set of loaded entries.
     */
    private static Set<String> loadEntries(String parentTag, String childTag) {
        Set<String> entries = new LinkedHashSet<>();
        if (packetsXMLDocument == null) return entries;

        NodeList nodes = packetsXMLDocument.getElementsByTagName(parentTag);
        if (nodes.getLength() > 0) {
            Node parent = nodes.item(0);
            NodeList children = parent.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(childTag)) {
                    entries.add(node.getTextContent().trim());
                }
            }
        }
        return entries;
    }

    /**
     * Registers primitive and boxed array types (including 2D arrays) in Kryo.
     * @param kryo Kryo instance to register array types.
     */
    public static void registerAllArrayTypes(Kryo kryo) {
        List<Class<?>> baseTypes = Arrays.asList(
            int.class, float.class, double.class, boolean.class,
            char.class, byte.class, short.class, long.class, String.class,
            Integer.class, Float.class, Double.class, Boolean.class,
            Character.class, Byte.class, Short.class, Long.class
        );

        int idCounter = 0;

        for (Class<?> base : baseTypes) {
            try {
                Class<?> oneDimArray = java.lang.reflect.Array.newInstance(base, 0).getClass();
                kryo.register(oneDimArray, ++idCounter);
                logger.debug("Registered array type: {} with ID {}", oneDimArray.getTypeName(), idCounter);

                Class<?> twoDimArray = java.lang.reflect.Array.newInstance(oneDimArray, 0).getClass();
                kryo.register(twoDimArray, ++idCounter);
                logger.debug("Registered 2D array type: {} with ID {}", twoDimArray.getTypeName(), idCounter);
            } catch (Exception e) {
                logger.error("Failed to register array type for: {}", base.getName(), e);
            }
        }

        logger.info("All primitive and boxed arrays (1D and 2D) registration complete.");
    }

    /**
     * Returns explicitClassIds
     *
     * @return
     */
    public static Map<String, Integer> getExplicitMappings() {
        return Collections.unmodifiableMap(explicitClassIds);
    }

    /**
     * Returns a formatted string listing all registered packets.
     *
     * @return Formatted list of packet IDs and classes.
     */
    public static String dumpRegisteredPackets() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== Registered Packets ===\n");
        explicitClassIds.forEach((className, id) -> builder.append(
            String.format("ID: %-5d  Class: %s\n", id, className)
        ));
        builder.append("===========================\n");
        return builder.toString();
    }

    /**
     * Prints the registered packets to System.out.
     */
    public static void printRegisteredPackets() {
        System.out.print(dumpRegisteredPackets());
    }

}
