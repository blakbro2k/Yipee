/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.net.packets;

import asg.games.yipee.common.enums.YipeeSerializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Base type for all server-to-client response packets.
 *
 * <p>This abstract class defines the standard envelope metadata that accompanies
 * every message sent from the Yipee server to a connected client:
 *
 * <ul>
 *   <li>{@code serverId} – identifies the running server instance.</li>
 *   <li>{@code gameId} – identifies the specific game/table context.</li>
 *   <li>{@code sessionId} – identifies the authorized session receiving the message.</li>
 *   <li>{@code serverTick} – authoritative tick counter at send time.</li>
 *   <li>{@code serverTimestamp} – server-local send time in milliseconds.</li>
 *   <li>{@code tickRate} – server's configured tick frequency (optional field).</li>
 * </ul>
 *
 * <p>Concrete response types (e.g., handshakes, errors, table updates, broadcast
 * state packets) extend this class and supply their own payloads. This guarantees
 * consistent metadata across the entire network layer and simplifies client-side
 * synchronization and logging.
 *
 * <p>The Lombok-generated equality intentionally excludes {@code serverTimestamp}
 * so that responses with identical semantic content but different send times are
 * treated as equivalent.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"serverTimestamp"})
public class AbstractServerResponse implements YipeeSerializable {

    /**
     * Identifier of the server instance that generated this response.
     *
     * <p>This value is stable for the lifetime of the running YipeeWebServer and
     * can be used by clients during debugging or multi-server deployments.
     */
    private String serverId;

    /**
     * Identifier of the game/table context associated with this response.
     *
     * <p>If the response does not pertain to a specific game (e.g., handshake),
     * this field may be null.
     */
    private String gameId;

    /**
     * Session identifier for the client receiving this response.
     *
     * <p>Assigned by the server during handshake, this ID ensures that subsequent
     * game messages are routed to the correct logical session even across
     * reconnections or multiple clients sharing an account.
     */
    private String sessionId;

    /**
     * The authoritative server tick at the time this message was generated.
     *
     * <p>Clients use this to align prediction, rollback, or interpolation logic
     * with the server's deterministic game loop.
     */
    private long serverTick;

    /**
     * Local system time (server-side) in milliseconds when the response was sent.
     *
     * <p>Used exclusively for latency measurement and analytics. Never used for
     * authoritative game logic due to possible clock drift between peers.
     */
    private long serverTimestamp;

    /**
     * The server's configured tick rate (in updates per second).
     *
     * <p>Provided as an optional convenience for clients performing rate matching,
     * interpolation, or local simulation tuning.
     */
    private int tickRate;

    @Override
    public String toString() {
        return "ServerResponse[" + serverId +
            ", serverTick=" + serverTick +
            ", serverTimestamp=" + serverTimestamp +
            "]";
    }
}
