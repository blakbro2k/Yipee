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

import com.esotericsoftware.kryo.Kryo;

/**
 * A utility class responsible for registering all packet classes for Kryo serialization.
 * <p>
 * This class ensures that both the client and server register packet classes in a consistent order,
 * preventing deserialization issues due to mismatched registration.
 * </p>
 * <p>
 * To use this, call {@link #registerPackets(Kryo)} and pass the Kryo instance from either the client or server.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *     PacketRegistrar.registerPackets(client.getKryo());  // On the client
 *     PacketRegistrar.registerPackets(server.getKryo());  // On the server
 * </pre>
 *
 * @author Blakbro2k
 * @version 1.0
 */
public class PacketRegistrar {

    /**
     * An ordered list of packet classes to be registered with Kryo.
     * <p>
     * The order must be identical on both the client and server to prevent serialization errors.
     * </p>
     */
    private static final Class<?>[] PACKET_CLASSES = {
            asg.games.yipee.objects.YipeeKeyMap.class,
            java.util.ArrayList.class,
            java.util.HashMap.class,
            ConnectionRequest.class,
            ConnectionResponse.class,
            asg.games.yipee.objects.YipeePlayer.class,
            int[].class,
            DisconnectRequest.class
    };

    /**
     * Registers all packet classes in a consistent order with the provided Kryo instance.
     * <p>
     * This method should be called on both the client and server to ensure matching serialization rules.
     * </p>
     *
     * @param kryo The Kryo instance to register classes with. Must not be null.
     */
    public static void registerPackets(Kryo kryo) {
        if (kryo != null) {
            for (Class<?> packetClass : PACKET_CLASSES) {
                kryo.register(packetClass);
            }
        }
    }
}