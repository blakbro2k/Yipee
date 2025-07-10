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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response sent by the server after processing a {@link PlayerActionRequest}.
 *
 * <p>This allows the server to confirm or reject the action, provide updated state,
 * or trigger a rollback/retry in case of desync.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlayerActionResponse extends AbstractServerResponse {

    /**
     * Whether the action was accepted and applied successfully.
     */
    private boolean accepted;

    /**
     * Optional message describing the result (e.g., "OK", "Invalid move", "Out of sync").
     */
    private String message;

    /**
     * Echo of the original action for reference or reconciliation.
     */
    private PlayerAction playerAction;
}
