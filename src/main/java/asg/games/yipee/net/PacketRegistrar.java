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

import asg.games.yipee.tools.Util;
import com.esotericsoftware.kryo.Kryo;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Dynamically scans and registers all packet classes in the `asg.games.yipee.objects` package.
 */
public class PacketRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(PacketRegistrar.class);

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

        Set<String> packages = new HashSet<>();
        packages.add("asg.games.yipee.objects");
        packages.add("asg.games.yipee.net");
        packages.add("asg.games.yipee.tools");

        // Scan the package for all classes
        Set<Class<?>> registeredClasses = new HashSet<>();

        for (String packageName : Util.safeIterable(packages)) {
            // Register each class
            for (Class<?> clazz : Util.safeIterable(getClassesToRegister(getReflectionsFromPackage(packageName)))) {
                registerClass(kryo, clazz, registeredClasses);
            }
        }

        // Register primitive arrays
        registerPrimitiveArrays(kryo);
    }

    private static void registerClass(Kryo kryo, Class<?> clazz, Set<Class<?>> registeredClasses) {
        if (clazz == null || registeredClasses.contains(clazz) || clazz.isPrimitive()) {
            logger.info("Skipping already registered or primitive class: {}", (clazz == null ? null : clazz.getName()));
            return; // Skip if already registered or primitive
        }

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

    private static Set<Class<?>> getClassesToRegister(Reflections reflections) {
        return reflections != null ? reflections.getSubTypesOf(Object.class) : new HashSet<>();
    }

    private static Reflections getReflectionsFromPackage(String packageName) {
        return new Reflections(packageName, Scanners.SubTypes, Scanners.TypesAnnotated);
    }

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