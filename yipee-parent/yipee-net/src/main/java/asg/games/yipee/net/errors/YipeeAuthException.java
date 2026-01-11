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
 * Thrown when a request fails authentication or authorization checks.
 * <p>
 * This exception indicates that the client is not permitted to perform
 * the requested operation due to missing, expired, or invalid credentials.
 * It is distinct from session, synchronization, or game-rule violations.
 * <p>
 * Typical causes include:
 * <ul>
 *   <li>Missing or malformed authentication tokens</li>
 *   <li>Expired or revoked session credentials</li>
 *   <li>Client identity mismatch or unauthorized access attempt</li>
 * </ul>
 * <p>
 * When propagated through the networking layer, this exception is mapped
 * to {@link ErrorCode#UNAUTHORIZED} by {@link ErrorMapper}.
 */
public class YipeeAuthException extends YipeeException {
    /**
     * Constructs a new {@code YipeeAuthException} with a descriptive message
     * explaining the authentication failure.
     *
     * @param msg human-readable explanation of the authorization error
     */
    public YipeeAuthException(String msg) {
        super(msg);
    }
}
