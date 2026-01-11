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
 * Sent by a client (usually the table owner) to request an update to a table's state.
 * <p>
 *
 * <p>The server will:</p>
 * <ul>
 *   <li>Validate that the requester has sufficient permissions</li>
 *   <li>Apply allowed changes to the authoritative table</li>
 *   <li>Broadcast updates via {@link SeatStateUpdateResponse}</li>
 * </ul>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SeatStateUpdateRequest extends AbstractClientRequest {

    /**
     * Unique identifier of the table this snapshot describes.
     *
     * <p>Used by the client to:
     * <ul>
     *   <li>Verify table context and routing</li>
     *   <li>Associate subsequent updates with the correct table</li>
     *   <li>Detect stale or mismatched responses</li>
     * </ul>
     */
    private String tableId;

    /**
     * The index of the seat being requested (0–7).
     */
    private int seatIndex;

    /**
     * The player making the update request.
     * Should match the table owner for most types of updates.
     */
    private String requestedById;
}
