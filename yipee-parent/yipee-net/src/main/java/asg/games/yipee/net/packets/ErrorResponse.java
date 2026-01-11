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

import asg.games.yipee.net.errors.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response sent by the server when a client request cannot be processed.
 *
 * <p>This packet is used for all classes of failures including:
 * <ul>
 *   <li>authentication errors (invalid or expired auth tokens)</li>
 *   <li>authorization or session validation failures</li>
 *   <li>invalid game ticks or desynchronized client state</li>
 *   <li>malformed or illegal action requests</li>
 *   <li>server-side processing failures</li>
 * </ul>
 *
 * <p>The client may use the {@link ErrorCode} to determine whether to retry,
 * resync, display a message, or disconnect.</p>
 *
 * <p><b>Direction:</b> Server → Client
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends AbstractServerResponse {

    /**
     * Machine-readable code describing the category of failure.
     *
     * <p>This value is guaranteed to be stable and should be used by the
     * client to drive programmatic handling (e.g., resync, logout, UI alert).</p>
     */
    private ErrorCode code;

    /**
     * A human-readable explanation of what went wrong.
     *
     * <p>This message is intended for client UI display or logs. It should
     * avoid exposing internal server details, but should be descriptive enough
     * for the player to understand the issue.</p>
     */
    private String message;

    /**
     * Optional technical or contextual information to assist debugging.
     *
     * <p>This may include internal state checks, offending values, or
     * additional hints for troubleshooting. Clients should treat this as
     * diagnostic data, not user-facing text.</p>
     */
    private String details;

    /**
     * Creates a simple {@code ErrorResponse} with the provided fields populated.
     *
     * <p>Server-side helper to simplify building standardized error payloads.</p>
     *
     * @param code    the type of error that occurred
     * @param message user-friendly explanation
     * @param details optional technical metadata for logs or debugging
     * @return error
     */
    public static ErrorResponse of(ErrorCode code, String message, String details) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(code);
        error.setMessage(message);
        error.setDetails(details);
        return error;
    }
}
