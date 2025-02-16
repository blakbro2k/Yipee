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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * PacketRegistrar dynamically scans and registers all packet classes in the specified packages.
 * It supports reading from an XML configuration file (packets.xml) to extend or override
 * default package and exclusion lists.
 */
public class PacketRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(PacketRegistrar.class);
    private static final String CONFIG_FILE = "packets.xml";
    private static File packetFile = getPacketFile();
    private static Set<String> packages = getPackages();
    private static Set<String> excludedClasses = getExcludedClasses();
    private static Document packetsXMLDocument = null;

    static {
        try {
            if (packetFile.exists()) {
                packetsXMLDocument = getXMLDocument();
                packages = getPackages();
                excludedClasses = getExcludedClasses();
            } else {
                logger.warn("'packets.xml' not found. No packets will be loaded.");
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error("Error loading 'packets.xml'", e);
        }
    }

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
        Set<Class<?>> registeredClasses = new HashSet<>();
        for (String packageName : Util.safeIterable(packages)) {
            logger.info("Registering classes from the following package: {}", packageName);
            // Register each class
            for (Class<?> clazz : Util.safeIterable(getClassesToRegister(getReflectionsFromPackage(packageName)))) {
                registerClass(kryo, clazz, registeredClasses);
            }
        }

        logger.info("Registering individual classes from included list.");
        // Register each class
        for (Class<?> clazz : Util.safeIterable(getIncludedClasses())) {
            registerClass(kryo, clazz, registeredClasses);
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
    private static File getPacketFile() {
        return getPacketFile(CONFIG_FILE);
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
        Set<String> entries = new HashSet<>();
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
        Set<String> packages = new HashSet<>();
        packages.add("asg.games.yipee.objects");
        packages.add("asg.games.yipee.net");
        packages.addAll(loadPackages());
        return packages;
    }

    /**
     * Retrieves the excluded class names, combining defaults with XML configurations.
     *
     * @return A set of class names to exclude.
     */
    private static Set<String> getExcludedClasses() {
        Set<String> excluded = new HashSet<>();
        excluded.add("asg.games.yipee.net.PacketRegistrar");
        excluded.add("java.lang.Enum");
        excluded.add("java.io.ObjectStreamField");
        excluded.add("java.lang.reflect.Constructor");
        excluded.add("java.lang.WeakPairMap");
        excluded.add("java.lang.Module$ArchivedData");
        excluded.add("java.security.ProtectionDomain");
        excluded.add("java.lang.ref.SoftReference");
        excluded.add("sun.reflect.generics.repository.ClassRepository");
        excluded.add("jdk.internal.reflect.ReflectionFactory");
        excluded.add("java.lang.Class$AnnotationData");
        excluded.add("sun.reflect.annotation.AnnotationType");
        excluded.add("java.lang.ClassValue$Entry");
        excluded.add("java.lang.Class$ReflectionData");
        excluded.add("java.lang.Class$EnclosingMethodInfo");
        excluded.addAll(loadExcludedClasses());
        return excluded;
    }

    /**
     * Retrieves the included class names, combining defaults with XML configurations.
     *
     * @return A set of class names to include.
     */
    private static Set<Class<?>> getIncludedClasses() {
        Set<Class<?>> included = new HashSet<>();
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
     * Registers a class and its related fields with Kryo.
     *
     * @param kryo              Kryo instance.
     * @param clazz             The class to register.
     * @param registeredClasses A set of already registered classes.
     */
    private static void registerClass(Kryo kryo, Class<?> clazz, Set<Class<?>> registeredClasses) {
        if (clazz == null || registeredClasses.contains(clazz) || clazz.isPrimitive()) {
            logger.debug("Skipping already registered or primitive class: {}", (clazz == null ? null : clazz.getName()));
            return; // Skip if already registered or primitive
        }

        if (excludedClasses.contains(clazz.getName())) {
            logger.warn("Excluding class: {}", clazz.getName());
            return; // Skip if excluded
        }

        // Register the class
        logger.info("Registering class: {}", clazz);
        kryo.register(clazz);
        registeredClasses.add(clazz);

        // Recursively register field types
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();

            if (fieldType.isArray()) {
                // Register array class and its component type if it's not null
                registerClass(kryo, fieldType, registeredClasses);
                if (fieldType.getComponentType() != null) {
                    registerClass(kryo, fieldType.getComponentType(), registeredClasses);
                }
            } else if (!registeredClasses.contains(fieldType)) {
                registerClass(kryo, fieldType, registeredClasses);
            }
        }

        // Also register inner and nested classes (including static nested classes)
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            if (!registeredClasses.contains(innerClass)) {
                registerClass(kryo, innerClass, registeredClasses);
            }
        }
    }

    /**
     * Retrieves all classes to register within a given package.
     *
     * @param reflections Reflections instance for scanning.
     * @return A set of classes found in the package.
     */
    private static Set<Class<? extends YipeeSerializable>> getClassesToRegister(Reflections reflections) {
        return reflections != null ? reflections.getSubTypesOf(YipeeSerializable.class) : new HashSet<>();
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
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        kryo.register(boolean[].class);
        kryo.register(byte[].class);
        kryo.register(short[].class);
        kryo.register(long[].class);
        kryo.register(char[].class);
        kryo.register(String[].class);
        logger.info("Registered primitive array types.");
    }
}