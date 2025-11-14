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
package asg.games.yipee.common.dto;

/**
 * Marker interface for a network-serializable Yipee player object.
 *
 * <p>This interface exists to unify different player representations across platforms
 * (e.g., core, LibGDX, or GWT) without introducing tight coupling. It allows shared
 * serialization and polymorphism in the networking layer while decoupling platform-specific
 * implementations like {@code YipeePlayer} and {@code GdxYipeePlayer}.
 *
 * <p>Typically used in packet definitions and cross-platform communication. Configuration
 * objects such as {@code NetYipeeKeyMap} are intentionally not exposed here and should be
 * synchronized via explicit configuration update requests/responses.
 */
public interface NetYipeePlayer extends NetYipeeObject {

    /**
     * Returns the current rating of this player (e.g., for matchmaking or table sorting).
     *
     * @return the player rating value
     */
    int getRating();

    /**
     * Sets the current rating of this player.
     *
     * @param rating the rating value to assign
     */
    void setRating(int rating);

    /**
     * Returns the icon identifier associated with this player.
     *
     * @return the icon ID
     */
    int getIcon();

    /**
     * Sets the icon identifier associated with this player.
     *
     * @param icon the icon ID to assign
     */
    void setIcon(int icon);
}
