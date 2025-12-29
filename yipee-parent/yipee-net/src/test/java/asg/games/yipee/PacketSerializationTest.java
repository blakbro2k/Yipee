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

import asg.games.yipee.net.TestYipeeNetworkObjects;
import asg.games.yipee.net.packets.ClientHandshakeRequest;
import asg.games.yipee.net.packets.ClientHandshakeResponse;
import asg.games.yipee.net.packets.DisconnectRequest;
import asg.games.yipee.net.packets.DisconnectResponse;
import asg.games.yipee.net.packets.ErrorResponse;
import asg.games.yipee.net.packets.MappedKeyUpdateRequest;
import asg.games.yipee.net.packets.MappedKeyUpdateResponse;
import asg.games.yipee.net.packets.PlayerActionRequest;
import asg.games.yipee.net.packets.PlayerActionResponse;
import asg.games.yipee.net.packets.SeatSelectionRequest;
import asg.games.yipee.net.packets.SeatSelectionResponse;
import asg.games.yipee.net.packets.SeatStateUpdateResponse;
import asg.games.yipee.net.packets.TableStateUpdateRequest;
import asg.games.yipee.net.tools.PacketRegistrar;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PacketSerializationTest {

    private <T> T serializeAndDeserialize(Kryo kryo, T original, Class<T> clazz) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output, original);
        output.close();

        Input input = new Input(new ByteArrayInputStream(bos.toByteArray()));
        return kryo.readObject(input, clazz);
    }

    public static Object[][] yipeeSerializableObjects() {
        return new Object[][]{
            {TestYipeeNetworkObjects.getClientHandshakeRequestObject(), ClientHandshakeRequest.class},
            {TestYipeeNetworkObjects.getClientHandshakeResponseObject(), ClientHandshakeResponse.class},
            {TestYipeeNetworkObjects.getDisconnectRequestObject(), DisconnectRequest.class},
            {TestYipeeNetworkObjects.getDisconnectResponseObject(), DisconnectResponse.class},
            {TestYipeeNetworkObjects.getErrorResponseObject(), ErrorResponse.class},
            {TestYipeeNetworkObjects.getMappedKeyUpdateRequestObject(), MappedKeyUpdateRequest.class},
            {TestYipeeNetworkObjects.getMappedKeyUpdateResponseObject(), MappedKeyUpdateResponse.class},
            {TestYipeeNetworkObjects.getPlayerActionRequestObject(), PlayerActionRequest.class},
            {TestYipeeNetworkObjects.getPlayerActionResponseObject(), PlayerActionResponse.class},
            {TestYipeeNetworkObjects.getSeatSelectionRequestObject(), SeatSelectionRequest.class},
            {TestYipeeNetworkObjects.getSeatSelectionResponseObject(), SeatSelectionResponse.class},
            {TestYipeeNetworkObjects.getTableStateBroadcastResponseObject(), SeatStateUpdateResponse.class},
            {TestYipeeNetworkObjects.getTableStateUpdateRequestObject(), TableStateUpdateRequest.class}
        };
    }

    @ParameterizedTest
    @MethodSource("yipeeSerializableObjects")
    public <T> void testMappedKeyUpdateSerialization(T original, Class<T> clazz) {
        Kryo kryo = new Kryo();
        PacketRegistrar.registerPackets(kryo); // Use your centralized registration

        T copy = serializeAndDeserialize(kryo, original, clazz);

        Assertions.assertNotNull(copy, "Deserialized object is null");
        Assertions.assertNotNull(original, "Original object is null");
        Assertions.assertEquals(original, copy, "Objects not equal");
        Assertions.assertEquals(original.getClass(), copy.getClass());
        TestYipeeNetworkObjects.assertTestOn(original, copy);
    }
}
