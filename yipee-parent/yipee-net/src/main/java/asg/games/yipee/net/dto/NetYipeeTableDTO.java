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
package asg.games.yipee.net.dto;

import asg.games.yipee.common.enums.ACCESS_TYPE;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Network-facing representation of a Yipee game table.
 *
 * <p>This interface enables polymorphic serialization of platform-specific table
 * implementations (e.g., core or LibGDX) within the networking layer, without introducing
 * cross-platform dependencies or GWT-incompatible types.
 *
 * <p>Primarily used in multiplayer synchronization, matchmaking, and table-state exchange
 * between server and client.
 */
@Data
@NoArgsConstructor
public class NetYipeeTableDTO extends AbstractNetObjectDTO {
    private boolean isRated;
    private boolean isSoundOn;
    private String loungeName;
    private int tableNumber;
    private ACCESS_TYPE accessType;
    private String roomId;
    private int watchersCount;
    private int seatsCount;
}