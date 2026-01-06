package asg.games.yipee.net.tools;

import asg.games.yipee.net.wire.CreateTableRequest;
import asg.games.yipee.net.wire.CreateTableResponse;
import asg.games.yipee.net.wire.GameWhoAmIResponse;
import asg.games.yipee.net.wire.JoinRoomRequest;
import asg.games.yipee.net.wire.JoinRoomResponse;
import asg.games.yipee.net.wire.JoinTableRequest;
import asg.games.yipee.net.wire.JoinTableResponse;
import asg.games.yipee.net.wire.LaunchTokenRequest;
import asg.games.yipee.net.wire.LaunchTokenResponse;
import asg.games.yipee.net.wire.LeaveRoomRequest;
import asg.games.yipee.net.wire.LeaveRoomResponse;
import asg.games.yipee.net.wire.LeaveTableRequest;
import asg.games.yipee.net.wire.LeaveTableResponse;
import asg.games.yipee.net.wire.PlayerProfileResponse;
import asg.games.yipee.net.wire.PlayerSummary;
import asg.games.yipee.net.wire.RegisterPlayerRequest;
import asg.games.yipee.net.wire.RoomPlayersResponse;
import asg.games.yipee.net.wire.RoomSummary;
import asg.games.yipee.net.wire.SeatDetailSummary;
import asg.games.yipee.net.wire.SeatSummary;
import asg.games.yipee.net.wire.ServerStatusResponse;
import asg.games.yipee.net.wire.SitDownRequest;
import asg.games.yipee.net.wire.SitDownResponse;
import asg.games.yipee.net.wire.StandUpRequest;
import asg.games.yipee.net.wire.StandUpResponse;
import asg.games.yipee.net.wire.TableDetailResponse;
import asg.games.yipee.net.wire.TableDetailsSummary;
import asg.games.yipee.net.wire.TableSummary;
import asg.games.yipee.net.wire.TableWatchersResponse;

import java.util.List;

public class NetUtil {
    private NetUtil() {
    }

    public static CreateTableRequest newCreateTableRequest(
            String roomId,
            boolean rated,
            boolean soundOn,
            String accessType
    ) {
        CreateTableRequest request = new CreateTableRequest();
        request.setRoomId(roomId);
        request.setAccessType(accessType);
        request.setRated(rated);
        request.setSoundOn(soundOn);
        return request;
    }

    public static CreateTableResponse newCreateTableResponse(
            String roomId,
            String roomName,
            String tableId,
            int tableNumber,
            String playerId,
            boolean created
    ) {
        CreateTableResponse request = new CreateTableResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setTableId(tableId);
        request.setTableNumber(tableNumber);
        request.setPlayerId(playerId);
        request.setCreated(created);
        return request;
    }

    public static GameWhoAmIResponse newGameWhoAmIResponse(
            String playerId,
            String name,
            int icon,
            int rating,
            String clientId,
            String sessionId,
            String gameId,
            String tableId,
            int seatIndex,
            long expiresAt,
            String serverId,
            long serverTick,
            long serverTimestamp,
            float tickRate
    ) {
        GameWhoAmIResponse request = new GameWhoAmIResponse();
        request.setPlayerId(playerId);
        request.setName(name);
        request.setIcon(icon);
        request.setRating(rating);
        request.setClientId(clientId);
        request.setSessionId(sessionId);
        request.setGameId(gameId);
        request.setTableId(tableId);
        request.setSeatIndex(seatIndex);
        request.setExpiresAt(expiresAt);
        request.setPlayerId(serverId);
        request.setServerTick(serverTick);
        request.setServerTimestamp(serverTimestamp);
        request.setTickRate(tickRate);
        return request;
    }

    public static JoinRoomRequest newJoinRoomRequest(
            String roomId
    ) {
        JoinRoomRequest request = new JoinRoomRequest();
        request.setRoomId(roomId);
        return request;
    }

    public static JoinTableRequest newJoinTableRequest(
            String roomId,
            int tableNumber,
            boolean createIfMissing
    ) {
        JoinTableRequest request = new JoinTableRequest();
        request.setRoomId(roomId);
        request.setTableNumber(tableNumber);
        request.setCreateIfMissing(createIfMissing);
        return request;
    }

    public static JoinTableResponse newJoinTableResponse(
            String roomId,
            String roomName,
            String tableId,
            String playerId
    ) {
        JoinTableResponse request = new JoinTableResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setTableId(tableId);
        request.setPlayerId(playerId);
        return request;
    }

    public static JoinRoomResponse newJoinRoomResponse(
            String roomId,
            String roomName,
            String loungeName,
            List<TableSummary> tables
    ) {
        JoinRoomResponse request = new JoinRoomResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setLoungeName(loungeName);
        request.setTables(tables);
        return request;
    }

    public static LaunchTokenRequest newLaunchTokenRequest(
            String tableId,
            int seatNo
    ) {
        LaunchTokenRequest request = new LaunchTokenRequest();
        request.setTableId(tableId);
        request.setSeatNo(seatNo);
        return request;
    }

    public static LaunchTokenResponse newLaunchTokenResponse(
            String launchToken,
            long expiresAt,
            String wsUrl
    ) {
        LaunchTokenResponse request = new LaunchTokenResponse();
        request.setLaunchToken(launchToken);
        request.setExpiresAt(expiresAt);
        request.setWsUrl(wsUrl);
        return request;
    }

    public static LeaveRoomRequest newLeaveRoomRequest(
            String roomId
    ) {
        LeaveRoomRequest request = new LeaveRoomRequest();
        request.setRoomId(roomId);
        return request;
    }

    public static LeaveRoomResponse newLeaveRoomResponse(
            String roomId,
            String playerId,
            boolean leftRoom
    ) {
        LeaveRoomResponse request = new LeaveRoomResponse();
        request.setRoomId(roomId);
        request.setPlayerId(playerId);
        request.setLeftRoom(leftRoom);
        return request;
    }

    public static LeaveTableRequest newLeaveTableRequest(
            String tableId
    ) {
        LeaveTableRequest request = new LeaveTableRequest();
        request.setTableId(tableId);
        return request;
    }

    public static LeaveTableResponse newLeaveTableResponse(
            String tableId,
            String playerId,
            boolean leftTable,
            boolean wasSeated,
            boolean wasWatcher
    ) {
        LeaveTableResponse request = new LeaveTableResponse();
        request.setTableId(tableId);
        request.setPlayerId(playerId);
        request.setLeftTable(leftTable);
        request.setWasSeated(wasSeated);
        request.setWasWatcher(wasWatcher);
        return request;
    }

    public static PlayerProfileResponse newPlayerProfileResponse(
            String playerId,
            String name,
            int icon,
            int rating,
            String sessionId
    ) {
        PlayerProfileResponse request = new PlayerProfileResponse();
        request.setPlayerId(playerId);
        request.setName(name);
        request.setIcon(icon);
        request.setRating(rating);
        request.setSessionId(sessionId);
        return request;
    }

    public static PlayerSummary newPlayerSummary(
            String playerId,
            String name,
            int icon,
            int rating
    ) {
        PlayerSummary request = new PlayerSummary();
        request.setPlayerId(playerId);
        request.setName(name);
        request.setIcon(icon);
        request.setRating(rating);
        return request;
    }

    public static RegisterPlayerRequest newRegisterPlayerRequest(
            String playerName,
            int icon,
            int rating,
            String clientId
    ) {
        RegisterPlayerRequest request = new RegisterPlayerRequest();
        request.setPlayerName(playerName);
        request.setIcon(icon);
        request.setRating(rating);
        request.setClientId(clientId);
        return request;
    }

    public static RoomPlayersResponse newRoomPlayersResponse(
            String roomId,
            String roomName,
            String loungeName,
            java.util.List<PlayerSummary> players
    ) {
        RoomPlayersResponse request = new RoomPlayersResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setLoungeName(loungeName);
        request.setPlayers(players);
        return request;
    }

    public static RoomSummary newRoomSummary(
            String roomId,
            String name,
            String loungeName,
            int playerCount,
            int tableCount
    ) {
        RoomSummary request = new RoomSummary();
        request.setRoomId(roomId);
        request.setName(name);
        request.setLoungeName(loungeName);
        request.setPlayerCount(playerCount);
        request.setTableCount(tableCount);
        return request;
    }

    public static SeatDetailSummary newSeatDetailSummary(
            String seatId,
            int seatNumber,
            boolean seatReady,
            boolean occupied,
            PlayerSummary playerSummary
    ) {
        SeatDetailSummary request = new SeatDetailSummary();
        request.setSeatId(seatId);
        request.setSeatNumber(seatNumber);
        request.setSeatReady(seatReady);
        request.setOccupied(occupied);
        request.setPlayerSummary(playerSummary);
        return request;
    }

    public static SeatSummary newSeatSummary(
            String seatId,
            int seatNumber,
            boolean seatReady,
            boolean occupied,
            String playerId,
            String playerName
    ) {
        SeatSummary request = new SeatSummary();
        request.setSeatId(seatId);
        request.setSeatNumber(seatNumber);
        request.setSeatReady(seatReady);
        request.setOccupied(occupied);
        request.setPlayerId(playerId);
        request.setPlayerName(playerName);
        return request;
    }

    public static ServerStatusResponse newServerStatusResponse(
            String status,
            String service,
            String serverId,
            String timestamp,
            String version,
            String motd
    ) {
        ServerStatusResponse request = new ServerStatusResponse();
        request.setStatus(status);
        request.setService(service);
        request.setServerId(serverId);
        request.setTimestamp(timestamp);
        request.setVersion(version);
        request.setMotd(motd);
        return request;
    }

    public static SitDownRequest newSitDownRequest(
            String tableId,
            int seatNumber
    ) {
        SitDownRequest request = new SitDownRequest();
        request.setTableId(tableId);
        request.setSeatNumber(seatNumber);
        return request;
    }

    public static SitDownResponse newSitDownResponse(
            String roomId,
            String roomName,
            String tableId,
            String playerId,
            int tableNumber,
            String seatId,
            int seatNumber,
            boolean seatReady,
            boolean occupied
    ) {
        SitDownResponse request = new SitDownResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setTableId(tableId);
        request.setPlayerId(playerId);
        request.setTableNumber(tableNumber);
        request.setRoomId(seatId);
        request.setSeatNumber(seatNumber);
        request.setSeatReady(seatReady);
        request.setOccupied(occupied);
        return request;
    }

    public static StandUpRequest newStandUpRequest(
            String tableId
    ) {
        StandUpRequest request = new StandUpRequest();
        request.setTableId(tableId);
        return request;
    }

    public static StandUpResponse newStandUpResponse(
            String roomId,
            String roomName,
            String tableId,
            int tableNumber,
            String playerId,
            String seatId,
            int seatNumber,
            boolean success
    ) {
        StandUpResponse request = new StandUpResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setTableId(tableId);
        request.setTableNumber(tableNumber);
        request.setPlayerId(playerId);
        request.setRoomName(seatId);
        request.setSeatNumber(seatNumber);
        request.setSuccess(success);

        return request;
    }

    public static TableDetailResponse newTableDetailResponse(
            String roomId,
            String roomName,
            TableDetailsSummary tableDetailsSummary
    ) {
        TableDetailResponse request = new TableDetailResponse();
        request.setRoomId(roomId);
        request.setRoomName(roomName);
        request.setTableDetailsSummary(tableDetailsSummary);
        return request;
    }

    public static TableDetailsSummary newTableDetailsSummary(
            TableSummary table,
            List<SeatDetailSummary> seats,
            List<PlayerSummary> watchers
    ) {
        TableDetailsSummary request = new TableDetailsSummary();
        request.setTable(table);
        request.setSeats(seats);
        request.setWatchers(watchers);
        return request;
    }

    public static TableSummary newTableSummary(
            String tableId,
            int tableNumber,
            String accessType,
            boolean created,
            boolean rated,
            boolean soundOn,
            int watcherCount
    ) {
        TableSummary request = new TableSummary();
        request.setTableId(tableId);
        request.setAccessType(accessType);
        request.setCreated(created);
        request.setTableNumber(tableNumber);
        request.setRated(rated);
        request.setSoundOn(soundOn);
        request.setWatcherCount(watcherCount);
        return request;
    }

    public static TableWatchersResponse newTableWatchersResponse(
            String tableId,
            int watcherCount,
            List<PlayerSummary> watchers
    ) {
        TableWatchersResponse request = new TableWatchersResponse();
        request.setTableId(tableId);
        request.setWatcherCount(watcherCount);
        request.setWatchers(watchers);
        return request;
    }
}
