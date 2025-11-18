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
package asg.games.yipee.net.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response sent by the server after processing a {@link MappedKeyUpdateRequest}.
 *
 * <p>Indicates whether the key mapping update was accepted, and optionally includes
 * a message explaining the result (e.g., success, validation error, or failure to persist).</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see MappedKeyUpdateRequest
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MappedKeyUpdateResponse extends AbstractServerResponse {

    /**
     * Indicates whether the key mapping update was successfully applied.
     */
    private boolean accepted;

    /**
     * A message describing the result of the request.
     * For example: "Update successful", "Invalid key mapping", etc.
     */
    private String message;

    /**
     * The player sending the request. Typically validated server-side using JWT.
     */
    private String playerId;
}
