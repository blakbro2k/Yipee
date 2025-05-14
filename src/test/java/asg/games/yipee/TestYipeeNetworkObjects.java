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

import asg.games.yipee.net.ClientHandshakeRequest;
import asg.games.yipee.net.ClientHandshakeResponse;
import asg.games.yipee.net.DisconnectRequest;
import asg.games.yipee.net.ErrorMessagePacket;
import asg.games.yipee.net.GameBoardStateTick;
import asg.games.yipee.net.HeartbeatPacket;
import asg.games.yipee.net.JoinTableRequest;
import asg.games.yipee.net.JoinTableResponse;
import asg.games.yipee.net.MappedKeyUpdateRequest;
import asg.games.yipee.net.PlayerActionPacket;
import asg.games.yipee.net.TableStateBroadcast;
import asg.games.yipee.net.TableStateUpdateRequest;
import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeTable;
import org.junit.jupiter.api.Assertions;
import org.testng.TestException;

public class TestYipeeNetworkObjects {
    static YipeePlayer player = new YipeePlayer("TestPlayer", 1500, 1);

    public static <T> void assertTestOn(T original, T copy) {
        Assertions.assertEquals(original.getClass(), copy.getClass(), "Deserialized class mismatch");

        // Add special checks for complex packets here
        if (original instanceof ClientHandshakeRequest) {
            ClientHandshakeRequest o = (ClientHandshakeRequest) original;
            ClientHandshakeRequest c = (ClientHandshakeRequest) copy;
            Assertions.assertAll("ClientHandshakeRequest",
                () -> Assertions.assertEquals(o.getClientId(), c.getClientId(), "ClientId mismatch"),
                () -> Assertions.assertEquals(o.getAuthToken(), c.getAuthToken(), "AuthToken mismatch"),
                () -> Assertions.assertEquals(o.getPlayer().getName(), c.getPlayer().getName(), "Player name mismatch")
            );
        } else if (original instanceof ClientHandshakeResponse) {
            ClientHandshakeResponse o = (ClientHandshakeResponse) original;
            ClientHandshakeResponse c = (ClientHandshakeResponse) copy;
            Assertions.assertAll("ClientHandshakeResponse",
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch"),
                () -> Assertions.assertEquals(o.getSessionId(), c.getSessionId(), "Session ID mismatch"),
                () -> Assertions.assertEquals(o.getServerId(), c.getServerId(), "Server ID mismatch"),
                () -> Assertions.assertEquals(o.getTimeStamp(), c.getTimeStamp(), "Timestamp mismatch"),
                () -> Assertions.assertEquals(o.isConnected(), c.isConnected(), "Connection status mismatch")
            );
        } else if (original instanceof DisconnectRequest) {
            DisconnectRequest o = (DisconnectRequest) original;
            DisconnectRequest c = (DisconnectRequest) copy;
            Assertions.assertAll("DisconnectRequest",
                () -> Assertions.assertEquals(o.getClientId(), c.getClientId(), "ClientId mismatch"),
                () -> Assertions.assertEquals(o.getSessionId(), c.getSessionId(), "SessionId mismatch"),
                () -> Assertions.assertEquals(o.getTimeStamp(), c.getTimeStamp(), "Timestamp mismatch"),
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch")
            );
        } else if (original instanceof ErrorMessagePacket) {
            ErrorMessagePacket o = (ErrorMessagePacket) original;
            ErrorMessagePacket c = (ErrorMessagePacket) copy;
            Assertions.assertAll("ErrorMessagePacket",
                () -> Assertions.assertEquals(o.getError(), c.getError(), "Error message mismatch"),
                () -> Assertions.assertEquals(o.getContext(), c.getContext(), "Context mismatch")
            );
        } else if (original instanceof HeartbeatPacket) {
            HeartbeatPacket o = (HeartbeatPacket) original;
            HeartbeatPacket c = (HeartbeatPacket) copy;
            Assertions.assertAll("HeartbeatPacket",
                () -> Assertions.assertEquals(o.getClientId(), c.getClientId(), "ClientId mismatch"),
                () -> Assertions.assertEquals(o.getSessionId(), c.getSessionId(), "SessionId mismatch"),
                () -> Assertions.assertEquals(o.getTimestamp(), c.getTimestamp(), "Timestamp mismatch")
            );
        } else if (original instanceof JoinTableRequest) {
            JoinTableRequest o = (JoinTableRequest) original;
            JoinTableRequest c = (JoinTableRequest) copy;
            Assertions.assertAll("JoinTableRequest",
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch")
            );
        } else if (original instanceof JoinTableResponse) {
            JoinTableResponse o = (JoinTableResponse) original;
            JoinTableResponse c = (JoinTableResponse) copy;
            Assertions.assertAll("JoinTableResponse",
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.getMessage(), c.getMessage(), "Message mismatch"),
                () -> Assertions.assertEquals(o.isAccepted(), c.isAccepted(), "Acceptance mismatch")
            );
        } else if (original instanceof MappedKeyUpdateRequest) {
            MappedKeyUpdateRequest o = (MappedKeyUpdateRequest) original;
            MappedKeyUpdateRequest c = (MappedKeyUpdateRequest) copy;
            Assertions.assertAll("MappedKeyUpdateRequest",
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch"),
                () -> Assertions.assertEquals(o.getKeyConfig(), c.getKeyConfig(), "KeyConfig mismatch")
            );
        } else if (original instanceof PlayerActionPacket) {
            PlayerActionPacket o = (PlayerActionPacket) original;
            PlayerActionPacket c = (PlayerActionPacket) copy;
            Assertions.assertAll("PlayerActionPacket",
                () -> Assertions.assertEquals(o.getPlayer(), c.getPlayer(), "Player mismatch"),
                () -> Assertions.assertEquals(o.getActionType(), c.getActionType(), "ActionType mismatch"),
                () -> Assertions.assertEquals(o.getTargetBoardIndex(), c.getTargetBoardIndex(), "TargetBoardIndex mismatch"),
                () -> Assertions.assertEquals(o.getPlayerAction(), c.getPlayerAction(), "PlayerAction mismatch")
            );
        } else if (original instanceof TableStateBroadcast) {
            TableStateBroadcast o = (TableStateBroadcast) original;
            TableStateBroadcast c = (TableStateBroadcast) copy;
            Assertions.assertAll("TableStateBroadcast",
                () -> Assertions.assertEquals(o.getUpdateType(), c.getUpdateType(), "UpdateType mismatch"),
                () -> Assertions.assertEquals(o.getTable(), c.getTable(), "Table mismatch")
            );
        } else if (original instanceof TableStateUpdateRequest) {
            TableStateUpdateRequest o = (TableStateUpdateRequest) original;
            TableStateUpdateRequest c = (TableStateUpdateRequest) copy;
            Assertions.assertAll("TableStateUpdateRequest",
                () -> Assertions.assertEquals(o.getTableId(), c.getTableId(), "TableId mismatch"),
                () -> Assertions.assertEquals(o.getRequestedBy(), c.getRequestedBy(), "RequestedBy mismatch"),
                () -> Assertions.assertEquals(o.getPartialTableUpdate(), c.getPartialTableUpdate(), "PartialTableUpdate mismatch"),
                () -> Assertions.assertEquals(o.getUpdateType(), c.getUpdateType(), "UpdateType mismatch")
            );
        } else if (original instanceof GameBoardStateTick) {
            GameBoardStateTick o = (GameBoardStateTick) original;
            GameBoardStateTick c = (GameBoardStateTick) copy;
            Assertions.assertAll("GameBoardStateTick",
                () -> Assertions.assertEquals(o.getTickNumber(), c.getTickNumber(), "TickNumber mismatch"),
                () -> Assertions.assertEquals(o.getGameBoardState(), c.getGameBoardState(), "GameBoardState mismatch")
            );
        } else {
            // Fallback
            throw new TestException("Object does not have an Assertion Test configured. (" + original.getClass().getSimpleName() + ")");
        }
    }

    public static ClientHandshakeResponse getClientHandshakeResponseObject() {
        ClientHandshakeResponse response = new ClientHandshakeResponse();
        response.setSessionId("session-xyz");
        response.setServerId("server-abc");
        response.setTimeStamp(System.currentTimeMillis());
        response.setPlayer(player);
        response.setConnected(true);
        return response;
    }

    public static ClientHandshakeRequest getClientHandshakeRequestObject() {
        ClientHandshakeRequest request = new ClientHandshakeRequest();
        request.setPlayer(player);
        request.setClientId("Client123");
        request.setAuthToken("AuthToken");
        return request;
    }

    public static DisconnectRequest getDisconnectRequestObject() {
        DisconnectRequest obj = new DisconnectRequest();
        obj.setClientId("ClientXYZ");
        obj.setSessionId("SessionABC");
        obj.setTimeStamp(System.currentTimeMillis());
        obj.setPlayer(player);
        return obj;
    }

    public static ErrorMessagePacket getErrorMessagePacketObject() {
        ErrorMessagePacket obj = new ErrorMessagePacket();
        obj.setError("An error occurred");
        obj.setContext("While testing serialization");
        return obj;
    }

    public static GameBoardStateTick getGameBoardStateTickObject() {
        GameBoardStateTick obj = new GameBoardStateTick();
        obj.setTickNumber(42);
        obj.setGameBoardState(null); // Replace with actual board state if needed
        return obj;
    }

    public static HeartbeatPacket getHeartbeatPacketObject() {
        HeartbeatPacket obj = new HeartbeatPacket();
        obj.setClientId("Client123");
        obj.setSessionId("Session456");
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }

    public static JoinTableRequest getJoinTableRequestObject() {
        JoinTableRequest obj = new JoinTableRequest();
        obj.setTableId("Table42");
        obj.setPlayer(player);
        return obj;
    }

    public static JoinTableResponse getJoinTableResponseObject() {
        JoinTableResponse obj = new JoinTableResponse();
        obj.setAccepted(true);
        obj.setMessage("Welcome to the table");
        obj.setTableId("Table42");
        return obj;
    }

    public static MappedKeyUpdateRequest getMappedKeyUpdateRequestObject() {
        MappedKeyUpdateRequest obj = new MappedKeyUpdateRequest();
        obj.setPlayer(player);
        obj.setKeyConfig(player.getKeyConfig());
        return obj;
    }

    public static PlayerActionPacket getPlayerActionPacketObject() {
        PlayerActionPacket obj = new PlayerActionPacket();
        obj.setPlayer(player);
        obj.setActionType("ATTACK");
        obj.setTargetBoardIndex(1);
        obj.setPlayerAction(null); // Replace with actual PlayerAction if needed
        return obj;
    }

    public static TableStateBroadcast getTableStateBroadcastObject() {
        TableStateBroadcast obj = new TableStateBroadcast();
        obj.setTable(null); // Replace with YipeeTable test instance
        obj.setUpdateType(TableStateBroadcast.TableUpdateType.PLAYER_READY);
        return obj;
    }

    public static TableStateUpdateRequest getTableStateUpdateRequestObject() {
        YipeeTable table = new YipeeTable();
        TableStateUpdateRequest obj = new TableStateUpdateRequest();
        obj.setTableId("Table42");
        obj.setRequestedBy(player);
        obj.setPartialTableUpdate(table);
        obj.setUpdateType(TableStateBroadcast.TableUpdateType.PLAYER_SEATED);
        return obj;
    }

}
