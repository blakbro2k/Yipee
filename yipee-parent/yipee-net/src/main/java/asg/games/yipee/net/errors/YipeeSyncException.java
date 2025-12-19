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
package asg.games.yipee.net.errors;

/**
 * Thrown when the client and server game state are detected to be out of sync.
 * <p>
 * This exception indicates a synchronization or ordering failure rather than
 * an authentication, session, or rule-validation issue.
 * <p>
 * Typical causes include:
 * <ul>
 *   <li>Client submitting actions for an incorrect or outdated game tick</li>
 *   <li>Missing or reordered state updates</li>
 *   <li>Desynchronization caused by dropped packets or prediction rollback errors</li>
 * </ul>
 * <p>
 * When propagated through the networking layer, this exception is mapped
 * to {@link ErrorCode#OUT_OF_SYNC} by {@link ErrorMapper}.
 */
public class YipeeSyncException extends YipeeException {

    /**
     * Constructs a new {@code YipeeSyncException} with a descriptive message
     * explaining the synchronization failure.
     *
     * @param msg human-readable explanation of the out-of-sync condition
     */
    public YipeeSyncException(String msg) {
        super(msg);
    }
}
