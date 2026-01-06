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

import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.AssertJUnit.*;

public class WireContractTest {

    private static final String WIRE_PACKAGE = "asg.games.yipee.net.wire";

    private static final Set<String> ALLOW_ABSTRACT =
            new HashSet<String>(Arrays.asList(
                    "asg.games.yipee.net.wire.AbstractNetObjectDTO"
            ));

    @Test
    public void allWireDtosAreLibGdxSafe() throws Exception {
        List<Class<?>> classes = findClasses(WIRE_PACKAGE);
        assertFalse("No classes found in wire package", classes.isEmpty());

        for (Class<?> c : classes) {
            // skip synthetic / package-info
            if (c.getName().endsWith("package-info")) continue;

            // 1. Must be a class, not interface
            assertFalse("Wire DTO must not be interface: " + c.getName(),
                    c.isInterface());

            // 2. Must not be enum
            assertFalse("Wire DTO must not be enum: " + c.getName(),
                    c.isEnum());

            // 3. Must be concrete (unless explicitly allowed)
            boolean isAbstract = Modifier.isAbstract(c.getModifiers());
            if (isAbstract && !ALLOW_ABSTRACT.contains(c.getName())) {
                fail("Wire DTO must be concrete: " + c.getName());
            }

            // 4. Must have public no-arg constructor
            if (!isAbstract) {
                assertHasNoArgConstructor(c);
            }

            // 5. Fields must be wire-safe
            assertWireSafeFields(c);
        }
    }

    private void assertHasNoArgConstructor(Class<?> c) {
        try {
            Constructor<?> ctor = c.getDeclaredConstructor();
            assertTrue("No-arg constructor must be public or protected: " + c.getName(),
                    Modifier.isPublic(ctor.getModifiers()) || Modifier.isProtected(ctor.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Missing no-arg constructor: " + c.getName());
        }
    }

    private void assertWireSafeFields(Class<?> c) {
        Class<?> current = c;
        while (current != null && current != Object.class) {
            for (Field f : current.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;

                Class<?> t = f.getType();

                // No interfaces
                if (t.isInterface()) {
                    fail("Field type must not be interface: " + c.getName() + "#" + f.getName());
                }

                // No abstract types
                if (Modifier.isAbstract(t.getModifiers())
                        && !ALLOW_ABSTRACT.contains(t.getName())) {
                    fail("Field type must not be abstract: " + c.getName() + "#" + f.getName());
                }

                // No Map
                if (Map.class.isAssignableFrom(t)) {
                    fail("Map is not wire-safe: " + c.getName() + "#" + f.getName());
                }

                // No UUID / Instant
                if (t.getName().equals("java.util.UUID")) {
                    fail("UUID is not wire-safe; use String: " + c.getName() + "#" + f.getName());
                }
                if (t.getName().equals("java.time.Instant")) {
                    fail("Instant is not wire-safe; use long epoch: " + c.getName() + "#" + f.getName());
                }
            }
            current = current.getSuperclass();
        }
    }

    private List<Class<?>> findClasses(String pkg) throws Exception {
        String path = pkg.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = cl.getResources(path);

        List<Class<?>> classes = new ArrayList<Class<?>>();

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            File dir = new File(filePath);
            if (!dir.exists()) continue;

            classes.addAll(findClassesRecursive(dir, pkg));
        }
        return classes;
    }

    private List<Class<?>> findClassesRecursive(File dir, String pkg)
            throws ClassNotFoundException {

        List<Class<?>> classes = new ArrayList<Class<?>>();
        File[] files = dir.listFiles();
        if (files == null) return classes;

        for (File f : files) {
            if (f.isDirectory()) {
                classes.addAll(findClassesRecursive(f, pkg + "." + f.getName()));
            } else if (f.getName().endsWith(".class")) {
                String name = f.getName().substring(0, f.getName().length() - 6);
                classes.add(Class.forName(pkg + "." + name));
            }
        }
        return classes;
    }
}
