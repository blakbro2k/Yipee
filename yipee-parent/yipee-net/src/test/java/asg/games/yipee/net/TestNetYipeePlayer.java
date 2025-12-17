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


import asg.games.yipee.common.dto.NetYipeePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestNetYipeePlayer extends TestNetYipeeObject implements NetYipeePlayer {
    int rating;
    int icon;

    public TestNetYipeePlayer() {
    }

    public TestNetYipeePlayer(String playerName, int rating, int iconNumber) {
        setName(playerName);
        setRating(rating);
        setIcon(iconNumber);
    }
}
