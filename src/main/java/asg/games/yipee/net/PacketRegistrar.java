/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.net;

import asg.games.yipee.objects.YipeeSerializable;
import asg.games.yipee.tools.Util;
import com.esotericsoftware.kryo.Kryo;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PacketRegistrar dynamically scans and registers all packet classes in the specified packages.
 * It supports reading from an XML configuration file (packets.xml) to extend or override
 * default package and exclusion lists.
 */
public class PacketRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(PacketRegistrar.class);
    private static final String CONFIG_FILE = "packets.xml";
    private static File packetFile;
    private static final AtomicInteger atomicIdCounter = new AtomicInteger(1001); // Start from 1001 for dynamic classes
    private static final Map<String, Integer> explicitClassIds = new LinkedHashMap<>(); // Store explicit class IDs from XML
    private static Set<String> packages = getPackages();
    private static Set<String> excludedClasses = getExcludedClasses();
    private static Document packetsXMLDocument = null;

    static {
        try {
            packetFile = getPacketFile();
            logger.info("PacketFile={} loaded.", packetFile);
            if (packetFile.exists()) {
                packetsXMLDocument = getXMLDocument();
                packages = getPackages();
                excludedClasses = getExcludedClasses();
                loadExplicitClassIds(packetsXMLDocument); // Load explicit IDs from XML
            } else {
                logger.warn("'packets.xml' not found. No packets will be loaded.");
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error("Error loading 'packets.xml'", e);
        }
    }

    /**
     * Loads classes with given Ids or an atomic Ids
     *
     * @param xmlDocument XML document with Packet registrations
     */
    private static void loadExplicitClassIds(Document xmlDocument) {
        NodeList mappingNodes = xmlDocument.getElementsByTagName("mapping");

        for (int i = 0; i < mappingNodes.getLength(); i++) {
            Node node = mappingNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String className = element.getAttribute("class").trim();
                String idString = element.getAttribute("id").trim();

                if (!className.isEmpty() && !idString.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idString);
                        if (explicitClassIds.containsValue(id)) {
                            logger.warn("Duplicate ID {} detected for class '{}'. Skipping.", id, className);
                        } else {
                            explicitClassIds.put(className, id);
                            logger.info("Loaded explicit mapping: {} -> {}", className, id);
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid ID format for class '{}'. Skipping.", className);
                    }
                } else {
                    logger.warn("Skipping entry with missing attributes: class='{}', id='{}'", className, idString);
                }
            }
        }
    }

    /**
     * Register a class with an explicit or generated ID
     *
     * @param kryo              Kryo instance to register classes with.
     * @param clazz             Class to register
     * @param registeredClasses Unique Set of already registered Classes
     */
    private static void registerClassWithId(Kryo kryo, Class<?> clazz, Set<Class<?>> registeredClasses) {
        if (clazz == null || registeredClasses.contains(clazz) || clazz.isPrimitive()) {
            logger.debug("Skipping already registered or primitive class: {}", clazz != null ? clazz.getName() : null);
            return;
        }

        // Skip excluded classes
        if (excludedClasses.contains(clazz.getName())) {
            logger.warn("Excluding class: {}", clazz.getName());
            return;
        }

        // Determine the class ID
        int classId = getClassId(clazz);

        // Register the class with the determined ID
        logger.info("Registering class: {} with ID: {}", clazz.getName(), classId);
        kryo.register(clazz, classId);
        registeredClasses.add(clazz);

        // Recursively register field types and nested classes
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType.isArray()) {
                registerClassWithId(kryo, fieldType, registeredClasses);
                if (fieldType.getComponentType() != null) {
                    registerClassWithId(kryo, fieldType.getComponentType(), registeredClasses);
                }
            } else if (!registeredClasses.contains(fieldType)) {
                registerClassWithId(kryo, fieldType, registeredClasses);
            }
        }

        // Register nested classes
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            if (!registeredClasses.contains(innerClass)) {
                registerClassWithId(kryo, innerClass, registeredClasses);
            }
        }
    }

    /**
     * Method to get the ID for a class (either from XML, hardcoded, or atomic counter)
     *
     * @param clazz Class to get an ID
     * @return an incremental unique Integer ID
     */
    private static int getClassId(Class<?> clazz) {
        // First check if the class has an explicitly defined ID from XML
        Integer explicitId = explicitClassIds.get(clazz.getName());
        if (explicitId != null) {
            return explicitId;
        }

        // If no explicit ID, generate an atomic incremental ID
        return atomicIdCounter.getAndIncrement();
    }

    /**
     * Reloads the configurations
     *
     * @param path Path of configuration
     * @throws ParserConfigurationException Parsing Exception
     * @throws IOException                  I/O error
     * @throws SAXException
     */
    public static void reloadConfiguration(String path) throws ParserConfigurationException, IOException, SAXException {
        String localFilePath = path;
        if (localFilePath == null) {
            localFilePath = CONFIG_FILE;
        }

        packetFile = getPacketFile(localFilePath);
        logger.debug("packetFile={}", packetFile);
        packetsXMLDocument = getXMLDocument();
        logger.debug("packetsXMLDocument={}", packetsXMLDocument);
        packages = getPackages();
        logger.debug("packages={}", packages);
        excludedClasses = getExcludedClasses();
        logger.debug("excludedClasses={}", excludedClasses);
    }

    /**
     * Scans the package and registers all classes for Kryo serialization.
     *
     * @param kryo Kryo instance to register classes with.
     */
    public static void registerPackets(Kryo kryo) {
        if (kryo == null) {
            logger.error("Kryo instance is null!");
            return;
        }

        // Scan the package for all classes
        Set<Class<?>> registeredClasses = new LinkedHashSet<>();

        // Register each class
        for (String packageName : Util.safeIterable(packages)) {
            logger.info("Registering classes from the following package: {}", packageName);
            // Register each class
            for (Class<?> clazz : Util.safeIterable(getClassesToRegister(getReflectionsFromPackage(packageName)))) {
                registerClassWithId(kryo, clazz, registeredClasses);
            }
        }
        
        // Register primitive arrays
        registerPrimitiveArrays(kryo);
    }

    /**
     * Loads the package names from the XML configuration file.
     *
     * @return A set of package names to scan for packets.
     */
    private static Set<String> loadPackages() {
        return parseXmlEntries("packages", "package");
    }

    /**
     * Loads the excluded class names from the XML configuration file.
     *
     * @return A set of class names to exclude from registration.
     */
    private static Set<String> loadExcludedClasses() {
        return parseXmlEntries("excludedClasses", "class");
    }

    /**
     * Loads the excluded class names from the XML configuration file.
     *
     * @return A set of class names to exclude from registration.
     */
    private static Set<String> loadIncludedClasses() {
        return parseXmlEntries("includedClasses", "class");
    }

    /**
     * Retrieves the file reference for the XML configuration file using the default path.
     *
     * @return The packets.xml file.
     */
    private static File getPacketFile() throws IOException {
        String resourcePath = getResourcePath();
        logger.debug("resourcePath={}", resourcePath);
        return getPacketFile(resourcePath + File.separator + CONFIG_FILE);
    }

    private static String getResourcePath() throws IOException {
        ClassLoader classLoader = PacketRegistrar.class.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources("");
        String path = "";
        String classesPathString = "/classes/";
        logger.debug("classesPathString: {}", classesPathString);
        while (resources.hasMoreElements()) {
            URL resourceElement = resources.nextElement();
            String resourceElementPath = resourceElement.getPath();
            logger.debug("resourceElement: {}", resourceElement);
            logger.debug("resourceElementPath: {}", resourceElementPath);
            if (resourceElementPath != null && resourceElementPath.contains(classesPathString)) {
                path = resourceElementPath;
                break;
            }
        }
        return path;
    }

    /**
     * Retrieves the file reference for the XML configuration file.
     *
     * @return The packets.xml file.
     */
    private static File getPacketFile(String path) {
        return new File(path);
    }

    /**
     * Parses the XML configuration file and returns a normalized document.
     * Ensures thread-safe access.
     *
     * @return A parsed XML document representing the packet configuration.
     *
     * @throws ParserConfigurationException If a DocumentBuilder cannot be created.
     * @throws IOException                  If an I/O error occurs.
     * @throws SAXException                 If a parsing error occurs.
     */
    private static synchronized Document getXMLDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        logger.info("packetFile={}", packetFile);
        Document document = builder.parse(packetFile);
        document.getDocumentElement().normalize();
        return document;
    }

    /**
     * Parses XML entries for the given parent and child tag names.
     *
     * @param parentTag The parent XML tag (e.g., "packages").
     * @param childTag  The child XML tag within the parent (e.g., "package").
     * @return A set of extracted values from the XML.
     */
    private static Set<String> parseXmlEntries(String parentTag, String childTag) {
        Set<String> entries = new LinkedHashSet<>();
        try {
            if (packetsXMLDocument == null) {
                logger.warn("packetsXMLDocument was not loaded. Using defaults.");
                return entries; // Return empty set instead of throwing an error
            }

            NodeList nodeList = packetsXMLDocument.getElementsByTagName(parentTag);
            if (nodeList.getLength() > 0) {
                Node parentNode = nodeList.item(0);
                NodeList childNodes = parentNode.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(childTag)) {
                        entries.add(node.getTextContent().trim());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error reading XML file '{}': {}", CONFIG_FILE, e.getMessage(), e);
        }
        return entries;
    }

    /**
     * Retrieves the default package names to scan, supplemented by XML configurations.
     *
     * @return A set of package names for scanning.
     */
    private static Set<String> getPackages() {
        Set<String> packages = new LinkedHashSet<>();
        packages.addAll(loadPackages());
        return packages;
    }

    /**
     * Retrieves the excluded class names, combining defaults with XML configurations.
     *
     * @return A set of class names to exclude.
     */
    private static Set<String> getExcludedClasses() {
        Set<String> excluded = new LinkedHashSet<>();
        excluded.addAll(loadExcludedClasses());
        return excluded;
    }

    /**
     * Retrieves the included class names, combining defaults with XML configurations.
     *
     * @return A set of class names to include.
     */
    private static Set<Class<?>> getIncludedClasses() {
        Set<Class<?>> included = new LinkedHashSet<>();
        included.add(asg.games.yipee.game.PlayerAction.class);
        Set<String> classes = loadIncludedClasses();
        for (String classString : classes) {
            try {
                included.add(Class.forName(classString));
            } catch (Exception e) {
                logger.error("Error converting included classes.", e);
            }
        }
        return included;
    }

    /**
     * Retrieves all classes to register within a given package.
     *
     * @param reflections Reflections instance for scanning.
     * @return A set of classes found in the package.
     */
    private static Set<Class<? extends YipeeSerializable>> getClassesToRegister(Reflections reflections) {
        return reflections != null ? reflections.getSubTypesOf(YipeeSerializable.class) : new LinkedHashSet<>();
    }

    /**
     * Creates a Reflections instance for scanning a package.
     *
     * @param packageName The package to scan.
     * @return A Reflections instance.
     */
    private static Reflections getReflectionsFromPackage(String packageName) {
        return new Reflections(packageName, Scanners.SubTypes, Scanners.TypesAnnotated);
    }

    /**
     * Registers primitive array types in Kryo.
     *
     * @param kryo Kryo instance.
     */
    private static void registerPrimitiveArrays(Kryo kryo) {
        logger.info("Registering primitive array types....");
        kryo.register(int[].class, 1);
        kryo.register(float[].class, 2);
        kryo.register(double[].class, 3);
        kryo.register(boolean[].class, 4);
        kryo.register(char[].class, 5);
        kryo.register(Object[].class, 6);
        kryo.register(byte[].class, 7);
        kryo.register(short[].class, 8);
        kryo.register(long[].class, 9);
        kryo.register(char[].class, 10);
        kryo.register(String[].class, 11);
    }
}