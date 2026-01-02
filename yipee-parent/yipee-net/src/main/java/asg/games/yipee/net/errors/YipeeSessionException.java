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
package asg.games.yipee.net.errors;

/**
 * Thrown when a request is associated with an invalid or inactive session.
 * <p>
 * This exception indicates a failure in the client session lifecycle rather
 * than an authentication or authorization problem.
 * <p>
 * Typical causes include:
 * <ul>
 *   <li>Using an expired or unknown session ID</li>
 *   <li>Issuing requests after disconnecting or leaving a game</li>
 *   <li>Session-to-player or session-to-game mismatches</li>
 * </ul>
 * <p>
 * When propagated through the networking layer, this exception is mapped
 * to {@link ErrorCode#INVALID_SESSION} by {@link ErrorMapper}.
 */
public class YipeeSessionException extends YipeeException {

    /**
     * Constructs a new {@code YipeeSessionException} with a descriptive message
     * explaining the session failure.
     *
     * @param msg human-readable explanation of the invalid session condition
     */
    public YipeeSessionException(String msg) {
        super(msg);
    }
}
