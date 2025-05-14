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

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent periodically by the client to inform the server that it is still active.
 *
 * <p>This packet helps maintain long-lived connections by:
 * <ul>
 *   <li>Preventing timeouts or idle disconnects</li>
 *   <li>Allowing the server to monitor client activity</li>
 *   <li>Supporting basic latency measurement (RTT)</li>
 * </ul>
 * </p>
 *
 * <p>This is typically sent on a fixed interval (e.g., every 5–10 seconds) from the client,
 * especially in games with infrequent input.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class HeartbeatPacket implements YipeeSerializable {

    /**
     * The unique identifier of the client sending the heartbeat.
     */
    private String clientId;

    /**
     * The session ID associated with the active connection.
     */
    private String sessionId;

    /**
     * The client-side timestamp of when this packet was sent.
     * Can be used to estimate round-trip latency or detect staleness.
     */
    private long timestamp;
}
