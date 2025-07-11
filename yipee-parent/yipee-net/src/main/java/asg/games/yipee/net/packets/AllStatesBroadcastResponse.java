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
package asg.games.yipee.net.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Broadcast response sent by the server to all clients seated at a table,
 * notifying them of a change in the table's state.
 *
 * <p>Common triggers include player seat changes, readiness status updates,
 * game start/end events, or table configuration changes.</p>
 *
 * <p><b>Direction:</b> Server → Clients</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllStatesBroadcastResponse extends AbstractServerResponse {
    private TableStateBroadcastResponse tableState1;
    private TableStateBroadcastResponse tableState2;
    private TableStateBroadcastResponse tableState3;
    private TableStateBroadcastResponse tableState4;
    private TableStateBroadcastResponse tableState5;
    private TableStateBroadcastResponse tableState6;
    private TableStateBroadcastResponse tableState7;
    private TableStateBroadcastResponse tableState8;
}
