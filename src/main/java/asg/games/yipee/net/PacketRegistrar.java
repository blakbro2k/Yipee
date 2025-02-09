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

        // Scan the package for all classes
        Set<Class<?>> classesToRegister = new HashSet<>();

        for (String packageName : Util.safeIterable(packages)) {
            // Register each class
            for (Class<?> clazz : Util.safeIterable(getClassesToRegister(getReflectionsFromPackage(packageName)))) {
                kryo.register(clazz);
                logger.info("Registered class: " + clazz.getName());
            }
        }
    }

    private static Set<Class<?>> getClassesToRegister(Reflections reflections) {
        Set<Class<?>> classes = null;
        if (reflections != null) {
            classes = reflections.getSubTypesOf(Object.class);
        }
        return classes;
    }

    private static Reflections getReflectionsFromPackage(String packageName) {
        return new Reflections(packageName, Scanners.SubTypes.filterResultsBy(c -> true));
    }
}