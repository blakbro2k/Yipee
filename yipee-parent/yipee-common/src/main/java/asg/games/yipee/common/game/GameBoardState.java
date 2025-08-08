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
package asg.games.yipee.common.game;

/**
 * Represents a generic game board state.
 * <p>
 * This interface serves as a common type for game board state objects used in the Yipee game.
 * Implementations of this interface should encapsulate the full state of a game board at a
 * specific point in time, including all relevant game data needed for rendering, simulation,
 * and network synchronization.
 * </p>
 *
 * <p>
 * It is typically used for both authoritative server state and predicted client state handling.
 * </p>
 */
public interface GameBoardState {
}
