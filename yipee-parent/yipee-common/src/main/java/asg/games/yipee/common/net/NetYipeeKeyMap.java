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
package asg.games.yipee.common.net;

/**
 * Marker interface representing a serializable client-side key mapping configuration
 * used for gameplay input.
 * <p>
 * This interface is part of the {@code common} module to enable serialization of
 * key mapping data across both LibGDX clients (including GWT) and server-side
 * Java environments.
 * <p>
 * Implementations should define player input bindings such as key-to-action mappings
 * in a platform-agnostic format suitable for transmission over the network.
 *
 * @see asg.games.yipee.common.packets.PlayerAction
 */
public interface NetYipeeKeyMap {
}
