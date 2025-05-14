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

import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeTable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by a client (usually the table owner) to request an update to the current table's state.
 * This model-driven packet allows the client to propose changes to table settings, player slots,
 * or any other mutable part of the {@link YipeeTable} structure.
 * <p>
 * The server is responsible for:
 * <ul>
 *   <li>Validating that the requester has permission to perform the update</li>
 *   <li>Applying the proposed change to the authoritative table object</li>
 *   <li>Broadcasting the change to other clients using {@link TableStateBroadcast}</li>
 * </ul>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class TableStateUpdateRequest implements YipeeSerializable {

    /**
     * The identifier of the table to be updated.
     */
    private String tableId;

    /**
     * The player making the update request.
     * Should match the table owner for most update types.
     */
    private YipeePlayer requestedBy;

    /**
     * A partial or full {@link YipeeTable} object that includes the proposed changes.
     * The server should selectively apply only allowed changes.
     */
    private YipeeTable partialTableUpdate;

    /**
     * The type of update being requested (e.g., settings changed, player moved).
     * Used by the server for routing and by the client UI for contextual response.
     */
    private TableStateBroadcast.TableUpdateType updateType;
}
