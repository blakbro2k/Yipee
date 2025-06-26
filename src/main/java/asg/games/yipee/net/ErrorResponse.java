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

/**
 * Response sent by the server when a client request fails due to an error.
 * <p>
 * Contains a machine-readable error code, a human-readable message, and
 * optional debug details for client display or logging.
 * </p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 */
@Data
@NoArgsConstructor
public class ErrorResponse extends AbstractServerResponse {

    /**
     * The standard error code indicating the type of failure.
     */
    private ErrorCode code;

    /**
     * A human-readable description of the error.
     * Useful for debugging or client-side error messaging.
     */
    private String message;

    /**
     * Optional technical or contextual information.
     * May include internal state, request details, or debug hints.
     */
    private String details;

    /**
     * Static factory method for building a basic error response.
     */
    public static ErrorResponse of(ErrorCode code, String message, String details) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(code);
        error.setMessage(message);
        error.setDetails(details);
        return error;
    }
}
