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
package asg.games.yipee.net;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AbstractServerResponse implements YipeeSerializable {
    /**
     * A unique identifier for the client instance (may be generated or assigned).
     */
    private String serverId;

    /**
     * A JWT token issued by the Web CMS (e.g., WordPress) for authentication.
     * It should include the player object as well.
     */
    private String sessionKey;

    /**
     * The game tick at which this request was issued.
     * Used for synchronizing actions in the server's authoritative game loop.
     */
    private int serverTick;

    /**
     * The local system time in milliseconds when this request was sent.
     * Used for latency diagnostics (not for game logic).
     */
    private long timestamp;

    /**
     * The local tick rate set on the server
     * Used to sync the rates with the Client.
     */
    private int tickRate; // Optional


    @Override
    public String toString() {
        return "ServerRequest[" + serverId + ", serverTick=" + serverTick + ", ts=" + timestamp + "]";
    }
}
