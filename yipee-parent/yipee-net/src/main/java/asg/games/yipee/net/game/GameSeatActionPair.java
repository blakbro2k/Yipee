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
package asg.games.yipee.net.game;

import asg.games.yipee.common.game.PlayerAction;
import asg.games.yipee.net.packets.AbstractServerResponse;
import lombok.Getter;

@Getter
public class GameSeatActionPair extends AbstractServerResponse {

    private final int seat;
    private final int tick;
    private final PlayerAction action;

    public GameSeatActionPair(int seat, PlayerAction action, int tick) {
        this.seat = seat;
        this.action = action;
        this.tick = tick;
    }
}
