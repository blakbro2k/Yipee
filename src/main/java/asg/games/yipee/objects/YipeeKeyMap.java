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

import asg.games.yipee.net.YipeeSerializable;
import asg.games.yipee.tools.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class YipeeKeyMap implements Copyable<YipeeKeyMap>, Disposable, YipeeSerializable {
    private static final Logger logger = LoggerFactory.getLogger(YipeeKeyMap.class);

    private final int[] keyMap = {
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.P,
            Input.Keys.NUM_1,
            Input.Keys.NUM_2,
            Input.Keys.NUM_3,
            Input.Keys.NUM_4,
            Input.Keys.NUM_5,
            Input.Keys.NUM_6,
            Input.Keys.NUM_7,
            Input.Keys.NUM_8,
            Input.Keys.SPACE
    };

    public int getRightKey(){
        return keyMap[0];
    }

    public void setRightKey(int key){
        keyMap[0] = key;
    }

    public int getLeftKey(){
        return keyMap[1];
    }

    public void setLeftKey(int key){
        keyMap[1] = key;
    }

    public int getCycleDownKey(){
        return keyMap[2];
    }

    public void setCycleDownKey(int key){
        keyMap[2] = key;
    }

    public int getCycleUpKey(){
        return keyMap[4];
    }

    public void setCycleUpKey(int key){
        keyMap[4] = key;
    }

    public int getDownKey(){
        return keyMap[3];
    }

    public void setDownKey(int key){
        keyMap[3] = key;
    }

    public int getRandomAttackKey(){
        return keyMap[13];
    }

    public void setRandomAttackKey(int key) {
        keyMap[13] = key;
    }

    public int getTarget1(){
        return keyMap[5];
    }

    public void setTarget1(int key){
        keyMap[5] = key;
    }

    public int getTarget2(){
        return keyMap[6];
    }

    public void setTarget2(int key){
        keyMap[6] = key;
    }

    public int getTarget3(){
        return keyMap[7];
    }

    public void setTarget3(int key){
        keyMap[7] = key;
    }

    public int getTarget4(){
        return keyMap[8];
    }

    public void setTarget4(int key){
        keyMap[8] = key;
    }

    public int getTarget5(){
        return keyMap[9];
    }

    public void setTarget5(int key){
        keyMap[9] = key;
    }

    public int getTarget6(){
        return keyMap[10];
    }

    public void setTarget6(int key){
        keyMap[10] = key;
    }

    public int getTarget7(){
        return keyMap[11];
    }

    public void setTarget7(int key){
        keyMap[11] = key;
    }

    public int getTarget8() {
        return keyMap[12];
    }

    public void setTarget8(int key) {
        keyMap[12] = key;
    }

    @Override
    public YipeeKeyMap copy() {
        YipeeKeyMap copy = new YipeeKeyMap();
        System.arraycopy(this.keyMap, 0, new int[keyMap.length], 0, keyMap.length);
        return copy;
    }

    @Override
    public YipeeKeyMap deepCopy() {
        return copy();
    }

    @Override
    public void dispose() {
        Arrays.fill(keyMap, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeeKeyMap)) return false;
        YipeeKeyMap that = (YipeeKeyMap) o;
        return Arrays.equals(keyMap, that.keyMap);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keyMap);
    }
}
