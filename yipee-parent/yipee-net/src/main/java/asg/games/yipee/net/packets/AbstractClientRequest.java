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
package asg.games.yipee.net.packets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractClientRequest implements YipeeSerializable {
    /**
     * A unique identifier for the client instance (may be generated or assigned).
     */
    private String clientId;

    /**
     * A JWT token issued by the Web CMS (e.g., WordPress) for authentication.
     * It should include the player object as well.
     */
    @JsonIgnore
    private String sessionKey;

    /**
     * The game tick at which this request was issued.
     * Used for synchronizing actions in the server's authoritative game loop.
     */
    private int tick;

    /**
     * The local system time in milliseconds when this request was sent.
     * Used for latency diagnostics (not for game logic).
     */
    private long timestamp;

    @Override
    public String toString() {
        return "ClientRequest[" + clientId + ", tick=" + tick + ", ts=" + timestamp + "]";
    }
}
