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
 * Response packet sent by the server after processing a {@link JoinTableRequest}.
 *
 * <p>Indicates whether the client's attempt to join the specified table was
 * accepted or rejected, along with a status message for context.</p>
 *
 * <p>This may be triggered due to reasons such as:
 * <ul>
 *   <li>Table not found</li>
 *   <li>Table is full or locked</li>
 *   <li>Player lacks permission</li>
 * </ul>
 * </p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see JoinTableRequest
 */
@Data
@NoArgsConstructor
public class JoinTableResponse implements YipeeSerializable {

    /**
     * Indicates whether the table join was successful.
     */
    private boolean accepted;

    /**
     * A human-readable message explaining the result
     * (e.g., "Joined successfully", "Table is full").
     */
    private String message;

    /**
     * The ID of the table this response corresponds to.
     */
    private String tableId;
}
