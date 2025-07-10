# Yipee Network Packets

This module contains the core packet definitions for client-server communication in the **Yipee** multiplayer game.
These packets define the standard protocol used to manage rooms, players, seating, game state, and real-time gameplay
events.

---

## 📦 Package

`asg.games.yipee.net`

All packets implement `YipeeSerializable`, making them compatible with KryoNet serialization. These classes are designed
to be shared between client and server modules.

---

## 🔧 Dependencies

- **Lombok**: For generating boilerplate (`@Data`, `@NoArgsConstructor`)
- **KryoNet**: For client-server transport and serialization
- **YipeeSerializable**: Your marker interface for scan/registration

---

## 📜 Usage

### Kryo Registration (Static Example)

```java
public static void registerPackets(Kryo kryo) {
    kryo.register(ClientHandshakeRequest.class);
    kryo.register(ClientHandshakeResponse.class);
    kryo.register(JoinTableRequest.class);
    kryo.register(JoinTableResponse.class);
    kryo.register(RoomStateUpdate.class);
    kryo.register(SeatRequest.class);
    kryo.register(SeatReleaseRequest.class);
    kryo.register(SeatUpdateBroadcast.class);
    kryo.register(PlayerReadyStatus.class);
    kryo.register(PlayerReadyBroadcast.class);
    kryo.register(StartGameRequest.class);
    kryo.register(StartGameBroadcast.class);
    kryo.register(MappedKeyUpdate.class);
    kryo.register(PlayerActionPacket.class);
    kryo.register(GameBoardStateTick.class);
    kryo.register(TableSettingsUpdate.class);
    kryo.register(TableSettingsBroadcast.class);
    kryo.register(DisconnectRequest.class);
    kryo.register(PlayerExitBroadcast.class);
    kryo.register(ErrorMessagePacket.class);
    kryo.register(HeartbeatPacket.class);
}