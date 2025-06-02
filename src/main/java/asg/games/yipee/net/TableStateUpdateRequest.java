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

import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeTable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by a client (usually the table owner) to request an update to a table's state.
 *
 * This packet allows the client to propose changes to table settings, player slots,
 * or other mutable fields within the {@link YipeeTable} structure.
 *
 * <p>The server will:</p>
 * <ul>
 *   <li>Validate that the requester has sufficient permissions</li>
 *   <li>Apply allowed changes to the authoritative table</li>
 *   <li>Broadcast updates via {@link TableStateBroadcastResponse}</li>
 * </ul>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class TableStateUpdateRequest extends AbstractClientRequest {

    /**
     * The identifier of the table to be updated.
     */
    private String tableId;

    /**
     * The player making the update request.
     * Should match the table owner for most types of updates.
     */
    private YipeePlayer requestedBy;

    /**
     * A partial or full {@link YipeeTable} object representing the proposed changes.
     * The server should apply only those fields deemed safe and allowed.
     */
    private YipeeTable partialTableUpdate;

    /**
     * Describes the kind of update being requested, such as seating changes or game start.
     */
    private TableUpdateType updateType;
}
