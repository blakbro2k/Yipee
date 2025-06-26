/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee;

import asg.games.yipee.game.PlayerAction;
import asg.games.yipee.net.AbstractClientRequest;
import asg.games.yipee.net.AbstractServerResponse;
import asg.games.yipee.net.ClientHandshakeRequest;
import asg.games.yipee.net.ClientHandshakeResponse;
import asg.games.yipee.net.DisconnectRequest;
import asg.games.yipee.net.DisconnectResponse;
import asg.games.yipee.net.ErrorCode;
import asg.games.yipee.net.ErrorResponse;
import asg.games.yipee.net.MappedKeyUpdateRequest;
import asg.games.yipee.net.MappedKeyUpdateResponse;
import asg.games.yipee.net.PlayerActionRequest;
import asg.games.yipee.net.PlayerActionResponse;
import asg.games.yipee.net.SeatSelectionRequest;
import asg.games.yipee.net.SeatSelectionResponse;
import asg.games.yipee.net.TableStateBroadcastResponse;
import asg.games.yipee.net.TableStateUpdateRequest;
import asg.games.yipee.net.TableUpdateType;
import asg.games.yipee.objects.YipeeKeyMap;
import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeTable;
import org.junit.jupiter.api.Assertions;
import org.testng.TestException;

public class TestYipeeNetworkObjects {
    static YipeePlayer player = new YipeePlayer("TestPlayer", 1500, 1);

    public static <T> void assertTestOn(T original, T copy) {
        Assertions.assertEquals(original.getClass(), copy.getClass(), "Deserialized class mismatch");

        if (original instanceof AbstractClientRequest) {
            AbstractClientRequest o = (AbstractClientRequest) original;
            AbstractClientRequest c = (AbstractClientRequest) copy;
            Assertions.assertAll("AbstractClientRequest",
                () -> Assertions.assertEquals(o.getClientId(), c.getClientId(), "ClientId mismatch"),
                () -> Assertions.assertEquals(o.getSessionKey(), c.getSessionKey(), "SessionKey mismatch"),
                () -> Assertions.assertEquals(o.getPacketType(), c.getPacketType(), "PacketType name mismatch"),
                () -> Assertions.assertEquals(o.getTick(), c.getTick(), "Tick mismatch"),
                () -> Assertions.assertEquals(o.getTimestamp(), c.getTimestamp(), "Timestamp mismatch")
            );
        } else if (original instanceof AbstractServerResponse) {
            AbstractServerResponse o = (AbstractServerResponse) original;
            AbstractServerResponse c = (AbstractServerResponse) copy;
            Assertions.assertAll("AbstractServerResponse",
                () -> Assertions.assertEquals(o.getServerId(), c.getServerId(), "ServerId mismatch"),
                () -> Assertions.assertEquals(o.getSessionKey(), c.getSessionKey(), "SessionKey mismatch"),
                () -> Assertions.assertEquals(o.getPacketType(), c.getPacketType(), "PacketType name mismatch"),
                () -> Assertions.assertEquals(o.getServerTick(), c.getServerTick(), "Player name mismatch"),
                () -> Assertions.assertEquals(o.getTimestamp(), c.getTimestamp(), "Timestamp mismatch")
            );
        }

        if (original instanceof ClientHandshakeRequest) {
            ClientHandshakeRequest o = (ClientHandshakeRequest) original;
            ClientHandshakeRequest c = (ClientHandshakeRequest) copy;
            Assertions.assertAll("ClientHandshakeRequest",
                () -> Assertions.assertEquals(o.getAuthToken(), c.getAuthToken(), "AuthToken mismatch"),
                () -> Assertions.assertEquals(o.getPlayer().getName(), c.getPlayer().getName(), "Player name mismatch")
            );
        } else if (original instanceof ClientHandshakeResponse) {
            ClientHandshakeResponse o = (ClientHandshakeResponse) original;
            ClientHandshakeResponse c = (ClientHandshakeResponse) copy;
            Assertions.assertAll("ClientHandshakeResponse",
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch"),
                () -> Assertions.assertEquals(o.isConnected(), c.isConnected(), "Connection status mismatch")
            );
        } else if (original instanceof DisconnectRequest) {
            DisconnectRequest o = (DisconnectRequest) original;
            DisconnectRequest c = (DisconnectRequest) copy;
            Assertions.assertAll("DisconnectRequest",
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch")
            );
        } else if (original instanceof DisconnectResponse) {
            DisconnectResponse o = (DisconnectResponse) original;
            DisconnectResponse c = (DisconnectResponse) copy;
            Assertions.assertAll("DisconnectResponse",
                () -> Assertions.assertEquals(o.isSuccessful(), c.isSuccessful(), "Success mismatch"),
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch")
            );
        } else if (original instanceof ErrorResponse) {
            ErrorResponse o = (ErrorResponse) original;
            ErrorResponse c = (ErrorResponse) copy;
            Assertions.assertAll("ErrorResponse",
                () -> Assertions.assertEquals(o.getCode(), c.getCode(), "Error code mismatch"),
                () -> Assertions.assertEquals(o.getDetails(), c.getDetails(), "Error details mismatch")
            );
        } else if (original instanceof MappedKeyUpdateRequest) {
            MappedKeyUpdateRequest o = (MappedKeyUpdateRequest) original;
            MappedKeyUpdateRequest c = (MappedKeyUpdateRequest) copy;
            Assertions.assertAll("MappedKeyUpdateRequest",
                () -> Assertions.assertEquals(o.getKeyConfig(), c.getKeyConfig(), "KeyConfig mismatch")
            );
        } else if (original instanceof MappedKeyUpdateResponse) {
            MappedKeyUpdateResponse o = (MappedKeyUpdateResponse) original;
            MappedKeyUpdateResponse c = (MappedKeyUpdateResponse) copy;
            Assertions.assertAll("MappedKeyUpdateResponse",
                () -> Assertions.assertEquals(o.isAccepted(), c.isAccepted(), "Acceptance mismatch"),
                () -> Assertions.assertEquals(o.getMessage(), c.getMessage(), "Message mismatch")
            );
        } else if (original instanceof PlayerActionRequest) {
            PlayerActionRequest o = (PlayerActionRequest) original;
            PlayerActionRequest c = (PlayerActionRequest) copy;
            Assertions.assertAll("PlayerActionRequest",
                () -> Assertions.assertEquals(o.getPlayerAction(), c.getPlayerAction(), "Player Action mismatch")
            );
        } else if (original instanceof PlayerActionResponse) {
            PlayerActionResponse o = (PlayerActionResponse) original;
            PlayerActionResponse c = (PlayerActionResponse) copy;
            Assertions.assertAll("PlayerActionResponse",
                () -> Assertions.assertEquals(o.getMessage(), c.getMessage(), "Message mismatch"),
                () -> Assertions.assertEquals(o.getPlayerAction(), c.getPlayerAction(), "Player Action mismatch"),
                () -> Assertions.assertEquals(o.isAccepted(), c.isAccepted(), "Accepted mismatch")
            );
        } else if (original instanceof TableStateUpdateRequest) {
            TableStateUpdateRequest o = (TableStateUpdateRequest) original;
            TableStateUpdateRequest c = (TableStateUpdateRequest) copy;
            Assertions.assertAll("TableStateUpdateRequest",
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.getRequestedBy(), c.getRequestedBy(), "RequestedBy mismatch"),
                () -> Assertions.assertEquals(o.getPartialTableUpdate(), c.getPartialTableUpdate(), "PartialTableUpdate mismatch"),
                () -> Assertions.assertEquals(o.getUpdateType(), c.getUpdateType(), "Update Type mismatch")
            );
        } else if (original instanceof TableStateBroadcastResponse) {
            TableStateBroadcastResponse o = (TableStateBroadcastResponse) original;
            TableStateBroadcastResponse c = (TableStateBroadcastResponse) copy;
            Assertions.assertAll("TableStateBroadcastResponse",
                () -> Assertions.assertEquals(o.getTable(), c.getTable(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.getUpdateType(), c.getUpdateType(), "Update Type mismatch")
            );
        } else if (original instanceof SeatSelectionResponse) {
            SeatSelectionResponse o = (SeatSelectionResponse) original;
            SeatSelectionResponse c = (SeatSelectionResponse) copy;
            Assertions.assertAll("SeatSelectionResponse",
                () -> Assertions.assertEquals(o.getSeatIndex(), c.getSeatIndex(), "Seat Index mismatch"),
                () -> Assertions.assertEquals(o.getMessage(), c.getMessage(), "Message mismatch"),
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.isAccepted(), c.isAccepted(), "Is accepted mismatch")
            );
        } else if (original instanceof SeatSelectionRequest) {
            SeatSelectionRequest o = (SeatSelectionRequest) original;
            SeatSelectionRequest c = (SeatSelectionRequest) copy;
            Assertions.assertAll("SeatSelectionRequest",
                () -> Assertions.assertEquals(o.getSeatIndex(), c.getSeatIndex(), "Seat Index mismatch"),
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch"),
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.isSpectator(), c.isSpectator(), "Is spectator mismatch")
            );
        } else {
            throw new TestException("Object does not have an Assertion Test configured. (" + original.getClass().getSimpleName() + ")");
        }
    }

    private static void setUpAbstractPacketRequest(AbstractClientRequest request) {
        if (request != null) {
            request.setClientId("client-abc");
            request.setTick(15);
            request.setTimestamp(System.currentTimeMillis());
            request.setSessionKey("testing-session-123");
        }
    }

    private static void setUpAbstractPacketResponse(AbstractServerResponse response) {
        if (response != null) {
            response.setServerId("server-abc");
            response.setServerTick(15);
            response.setTimestamp(System.currentTimeMillis());
            response.setSessionKey("testing-session-123");
        }
    }

    public static ClientHandshakeResponse getClientHandshakeResponseObject() {
        ClientHandshakeResponse response = new ClientHandshakeResponse();
        setUpAbstractPacketResponse(response);
        response.setConnected(true);
        return response;
    }

    public static ClientHandshakeRequest getClientHandshakeRequestObject() {
        ClientHandshakeRequest request = new ClientHandshakeRequest();
        setUpAbstractPacketRequest(request);
        request.setPlayer(player);
        request.setClientId("Client123");
        request.setAuthToken("AuthToken");
        return request;
    }

    public static DisconnectRequest getDisconnectRequestObject() {
        DisconnectRequest request = new DisconnectRequest();
        setUpAbstractPacketRequest(request);
        request.setPlayer(player);
        return request;
    }

    public static DisconnectResponse getDisconnectResponseObject() {
        DisconnectResponse response = new DisconnectResponse();
        setUpAbstractPacketResponse(response);
        response.setPlayer(player);
        response.setSuccessful(true);
        return response;
    }

    public static ErrorResponse getErrorResponseObject() {
        ErrorResponse response = new ErrorResponse();
        setUpAbstractPacketResponse(response);
        response.setCode(ErrorCode.BAD_REQUEST);
        response.setMessage("incorrect invocation: HTTP-WE");
        response.setDetails("Command does not exist");
        return response;
    }

    public static SeatSelectionRequest getSeatSelectionRequestObject() {
        SeatSelectionRequest request = new SeatSelectionRequest();
        setUpAbstractPacketRequest(request);
        request.setTableId("Table42");
        request.setSeatIndex(1);
        request.setSpectator(false);
        request.setPlayer(player);
        return request;
    }

    public static SeatSelectionResponse getSeatSelectionResponseObject() {
        SeatSelectionResponse response = new SeatSelectionResponse();
        setUpAbstractPacketResponse(response);
        response.setAccepted(true);
        response.setMessage("Welcome to the table");
        response.setTableId("Table42");
        response.setSeatIndex(1);
        return response;
    }

    public static MappedKeyUpdateRequest getMappedKeyUpdateRequestObject() {
        MappedKeyUpdateRequest request = new MappedKeyUpdateRequest();
        YipeeKeyMap map = new YipeeKeyMap("2");
        setUpAbstractPacketRequest(request);
        request.setKeyConfig(player.getKeyConfig());
        return request;
    }

    public static MappedKeyUpdateResponse getMappedKeyUpdateResponseObject() {
        MappedKeyUpdateResponse response = new MappedKeyUpdateResponse();
        setUpAbstractPacketResponse(response);
        response.setAccepted(true);
        response.setMessage("Updated Up Key;");
        return response;
    }

    public static PlayerActionResponse getPlayerActionResponseObject() {
        PlayerActionResponse response = new PlayerActionResponse();
        PlayerAction action = new PlayerAction(1, PlayerAction.ActionType.A_CLUMP, 2, 3, null);
        setUpAbstractPacketResponse(response);
        response.setAccepted(true);
        response.setPlayerAction(action);
        response.setMessage("player 1 attacked player 2");
        return response;
    }

    public static PlayerActionRequest getPlayerActionRequestObject() {
        PlayerActionRequest request = new PlayerActionRequest();
        PlayerAction action = new PlayerAction(1, PlayerAction.ActionType.A_CLUMP, 3, 3, null);
        setUpAbstractPacketRequest(request);
        request.setPlayerAction(action); // Replace with actual PlayerAction if needed
        return request;
    }

    public static TableStateBroadcastResponse getTableStateBroadcastResponseObject() {
        TableStateBroadcastResponse obj = new TableStateBroadcastResponse();
        YipeeTable table = new YipeeTable();
        setUpAbstractPacketResponse(obj);
        obj.setTable(table); // Replace with YipeeTable test instance
        obj.setUpdateType(TableUpdateType.PLAYER_READY);
        return obj;
    }

    public static TableStateUpdateRequest getTableStateUpdateRequestObject() {
        YipeeTable table = new YipeeTable();
        TableStateUpdateRequest request = new TableStateUpdateRequest();
        setUpAbstractPacketRequest(request);
        request.setTableId("Table42");
        request.setRequestedBy(player);
        request.setPartialTableUpdate(table);
        request.setUpdateType(TableUpdateType.PLAYER_SEATED);
        return request;
    }

}
