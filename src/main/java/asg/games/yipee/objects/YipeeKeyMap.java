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
package asg.games.yipee.objects;

import asg.games.yipee.game.PlayerAction;
import asg.games.yipee.net.YipeeSerializable;
import asg.games.yipee.tools.Input;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps game actions to user-defined input keys for one specific player.
 * The keys are limited to predefined {@link PlayerAction.ActionType} values,
 * ensuring safe and consistent control configurations.
 * <p>
 * This map is serializable and copyable, making it suitable for storing
 * player preferences to disk or syncing over a network.
 */
@Getter
@Setter
public class YipeeKeyMap implements Copyable<YipeeKeyMap>, Disposable, YipeeSerializable {

    /**
     * Internal mapping from actions to keycodes.
     * This map is deliberately hidden from Lombok accessors to ensure controlled access.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<PlayerAction.ActionType, Integer> actionToKeycode = new HashMap<>();

    /**
     * Optional player ID to associate this key map with a player (useful for storage, audits).
     */
    private String playerId;

    /**
     * No Arg Constructor for Kryo
     */
    public YipeeKeyMap() {
        this("_NOARG_");
    }

    /**
     * Constructs a new key map and initializes default bindings.
     *
     * @param playerId optional player identifier
     */
    public YipeeKeyMap(String playerId) {
        this.playerId = playerId;
        initDefaultBindings();
    }

    /**
     * Sets the default key bindings used in the game.
     * These represent the full set of allowed control mappings.
     */
    private void initDefaultBindings() {
        actionToKeycode.put(PlayerAction.ActionType.P_MOVE_LEFT, Input.Keys.LEFT);
        actionToKeycode.put(PlayerAction.ActionType.P_MOVE_RIGHT, Input.Keys.RIGHT);
        actionToKeycode.put(PlayerAction.ActionType.P_MOVE_DOWN_START, Input.Keys.DOWN);
        actionToKeycode.put(PlayerAction.ActionType.P_CYCLE_UP, Input.Keys.UP);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_RANDOM, Input.Keys.SPACE);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET1, Input.Keys.NUM_1);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET2, Input.Keys.NUM_2);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET3, Input.Keys.NUM_3);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET4, Input.Keys.NUM_4);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET5, Input.Keys.NUM_5);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET6, Input.Keys.NUM_6);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET7, Input.Keys.NUM_7);
        actionToKeycode.put(PlayerAction.ActionType.P_ATTACK_TARGET8, Input.Keys.NUM_8);
    }

    /**
     * Returns a read-only view of all key bindings.
     * Used for debug display or config UI rendering.
     */
    @JsonIgnore
    public Map<PlayerAction.ActionType, Integer> getAllBindings() {
        return Collections.unmodifiableMap(actionToKeycode);
    }

    /**
     * Remaps a specific action to a different keycode.
     *
     * @param newAction the action to rebind
     * @param keycode   the new key
     */
    public void rebind(PlayerAction.ActionType newAction, int keycode) {
        actionToKeycode.put(newAction, keycode);
    }

    /**
     * @return a deep copy of the entire key map.
     */
    @Override
    public YipeeKeyMap copy() {
        return deepCopy();
    }

    /** @return a deep clone of this object, preserving all bindings. */
    @Override
    public YipeeKeyMap deepCopy() {
        YipeeKeyMap copy = new YipeeKeyMap(this.playerId);
        copy.actionToKeycode.clear();
        copy.actionToKeycode.putAll(this.actionToKeycode);
        return copy;
    }

    /** Clears all bindings from memory. Should only be called when disposing the object. */
    @Override
    public void dispose() {
        actionToKeycode.clear();
    }

    // -- Strictly typed individual accessors ensure only known actions are bound --

    public int getRightKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_MOVE_RIGHT);
    }

    public void setRightKey(int keycode) {
        rebind(PlayerAction.ActionType.P_MOVE_RIGHT, keycode);
    }

    public int getLeftKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_MOVE_LEFT);
    }

    public void setLeftKey(int keycode) {
        rebind(PlayerAction.ActionType.P_MOVE_LEFT, keycode);
    }

    public int getCycleDownKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_CYCLE_DOWN);
    }

    public void setCycleDownKey(int keycode) {
        rebind(PlayerAction.ActionType.P_CYCLE_DOWN, keycode);
    }

    public int getDownKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_MOVE_DOWN_START);
    }

    public void setDownKey(int keycode) {
        rebind(PlayerAction.ActionType.P_MOVE_DOWN_START, keycode);
    }

    public int getCycleUpKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_CYCLE_UP);
    }

    public void setCycleUpKey(int keycode) {
        rebind(PlayerAction.ActionType.P_CYCLE_UP, keycode);
    }

    public int getTarget1() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET1);
    }

    public void setTarget1(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET1, keycode);
    }

    public int getTarget2() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET2);
    }

    public void setTarget2(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET2, keycode);
    }

    public int getTarget3() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET3);
    }

    public void setTarget3(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET3, keycode);
    }

    public int getTarget4() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET4);
    }

    public void setTarget4(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET4, keycode);
    }

    public int getTarget5() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET5);
    }

    public void setTarget5(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET5, keycode);
    }

    public int getTarget6() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET6);
    }

    public void setTarget6(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET6, keycode);
    }

    public int getTarget7() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET7);
    }

    public void setTarget7(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET7, keycode);
    }

    public int getTarget8() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_TARGET8);
    }

    public void setTarget8(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_TARGET8, keycode);
    }

    public int getRandomAttackKey() {
        return actionToKeycode.get(PlayerAction.ActionType.P_ATTACK_RANDOM);
    }

    public void setRandomAttackKey(int keycode) {
        rebind(PlayerAction.ActionType.P_ATTACK_RANDOM, keycode);
    }

    /**
     * Finds the action type bound to the given keycode.
     *
     * @param keycode a raw key input
     * @return the corresponding game action, or null if unbound
     */
    public PlayerAction.ActionType getActionForKey(int keycode) {
        if (actionToKeycode.isEmpty()) return null;
        for (Map.Entry<PlayerAction.ActionType, Integer> entry : actionToKeycode.entrySet()) {
            if (entry.getValue() == keycode) return entry.getKey();
        }
        return null;
    }

    /**
     * Serializes this key map into JSON.
     * Used for saving to disk or syncing player preferences.
     */
    public String toJson() {
        try {
            return Util.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize YipeeKeyMap", e);
        }
    }

    /**
     * Parses a key map from JSON.
     *
     * @param json a serialized string
     * @return the corresponding {@code YipeeKeyMap}
     */
    public static YipeeKeyMap fromJson(String json) {
        try {
            return Util.readValue(json, YipeeKeyMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize YipeeKeyMap", e);
        }
    }
}
