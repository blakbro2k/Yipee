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
 * Sent by the server to notify the client of an error that occurred while
 * processing a previous request.
 *
 * <p>This packet allows the server to return structured feedback when
 * something fails due to invalid input, permission issues, or game state violations.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see TableStateUpdateRequest
 * @see ClientHandshakeRequest
 */
@Data
@NoArgsConstructor
public class ErrorMessagePacket implements YipeeSerializable {

    /**
     * A short machine-readable error string (e.g., "INVALID_SESSION", "NOT_AUTHORIZED").
     * Can be used on the client for conditionals or logs.
     */
    private String error;

    /**
     * A human-readable explanation of the context or reason for the error.
     * Suitable for UI display or user debugging.
     */
    private String context;
}
