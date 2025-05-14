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
package asg.games.yipee;

import asg.games.yipee.net.PacketRegistrar;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PacketRegistrarTest {

    private static Kryo kryo;

    @BeforeAll
    public static void setup() {
        kryo = new Kryo();
        PacketRegistrar.registerAllArrayTypes(kryo);
        PacketRegistrar.registerPackets(kryo);
    }

    @Test
    public void testPrimitiveAndBoxedArraysRegistered() {
        assertAll(
            () -> assertNotNull(kryo.getRegistration(int[].class), "int[] should be registered"),
            () -> assertNotNull(kryo.getRegistration(Integer[].class), "Integer[] should be registered"),
            () -> assertNotNull(kryo.getRegistration(int[][].class), "int[][] should be registered"),
            () -> assertNotNull(kryo.getRegistration(Integer[][].class), "Integer[][] should be registered"),
            () -> assertNotNull(kryo.getRegistration(String[].class), "String[] should be registered"),
            () -> assertNotNull(kryo.getRegistration(String[][].class), "String[][] should be registered")
        );
    }

    @Test
    public void testExplicitMappingsRegistered() {
        PacketRegistrar.getExplicitMappings().forEach((className, id) -> {
            try {
                Class<?> clazz = Class.forName(className);
                Registration reg = kryo.getRegistration(clazz);
                assertNotNull(reg, "Class should be registered: " + className);
            } catch (ClassNotFoundException e) {
                fail("Explicit class not found during registration: " + className);
            }
        });
    }

    @Test
    public void testReloadConfigurationDoesNotThrow() {
        Exception exception = assertThrows(ParserConfigurationException.class, () -> {
            PacketRegistrar.reloadConfiguration("nonexistent-file.xml");
        });
        StringBuilder path = new StringBuilder();
        path.append("src").append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).append("packets.xml");
        assertTrue(exception.getMessage().contains("No packets.xml"));
        assertDoesNotThrow(() -> PacketRegistrar.reloadConfiguration(path.toString()));
    }
}
