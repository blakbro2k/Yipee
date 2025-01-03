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
package asg.games.yipee.persistence.examples;

import asg.games.yipee.objects.YipeeObject;
import asg.games.yipee.persistence.AbstractStorage;
import asg.games.yipee.tools.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class MemoryStorage extends AbstractStorage {
    private Map<String, Object> _idStore;
    private Map<String, Object> _nameStore;
    private Queue<Object> _transactions;

    protected MemoryStorage(){
        _idStore = new HashMap<>();
        _nameStore = new HashMap<>();
        _transactions = new LinkedList<>();
    }

    @Override
    public void dispose() {
        _transactions.clear();
        _idStore.clear();
        _nameStore.clear();
    }

    @Override
    public <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name) {
        try{
            return clazz.cast(_nameStore.get(name));
        } catch (ClassCastException c){
            throw new RuntimeException(c);
        }
    }

    @Override
    public <T extends YipeeObject> T getObjectById(Class<T> clazz, String id) {
        try{
            return clazz.cast(_idStore.get(id));
        } catch (ClassCastException c){
            throw new RuntimeException(c);
        }
    }

    @Override
    public <T extends YipeeObject> List<T> getAllObjects(Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends YipeeObject> int countAllObjects(Class<T> clazz) {
        return 0;
    }

    @Override
    public <T extends YipeeObject> void saveObject(T object) {
        _transactions.offer(object);
    }

    @Override
    public void commitTransactions() {
        try {
            Iterator iterator = _transactions.iterator();

            while(iterator.hasNext()){
                Object o = iterator.next();
                putName(o);
                putId(o);
                iterator.remove();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void putName(Object o){
        if(o != null){
            _nameStore.put(getKey(o, true), o);
        }
    }

    private void putId(Object o){
        if(o != null){
            _idStore.put(getKey(o, false), o);
        }
    }

    public String getKey(Object o, boolean getName) {
        return getNameOrIdFromInstance(o, getName);
    }

    @Override
    public void rollBackTransactions() {
        _transactions.clear();
    }


    public int getTransactions(){
        return Util.size(_transactions);
    }

}