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
package asg.games.yipee.persistence;

import asg.games.yipee.objects.AbstractYipeeObject;
import asg.games.yipee.objects.YipeeObject;
import asg.games.yipee.tools.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractStorage implements Storage {
    private final static String STORAGE_NAME_METHOD = "getName";
    private final static String STORAGE_ID_METHOD = "getId";

    @Override
    abstract public <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name);

    @Override
    abstract public <T extends YipeeObject> T getObjectById(Class<T> clazz, String id);

    @Override
    public abstract <T extends YipeeObject> Iterable<T> getAllObjects(Class<T> clazz);

    @Override
    public abstract <T extends YipeeObject> void saveObject(T object);

    @Override
    abstract public void commitTransactions();

    @Override
    abstract public void rollBackTransactions();

    protected String getNameOrIdFromInstance(Object o, boolean getName) {
        Object var;
        try {
            Class<?> c = getClassFromSuper(AbstractYipeeObject.class, o);
            String methodName = getName ? STORAGE_NAME_METHOD : STORAGE_ID_METHOD;
            Method m = c.getDeclaredMethod(methodName);
            var = m.invoke(o);
        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
        return Util.otos(var);
    }

    public Class<?> getClassFromSuper(Class<?> klass, Object o){
        if(o != null){
            Class<?> var = o.getClass().getSuperclass();
            int thresh = 0;
            do {
                if (var.getTypeName().equals(klass.getTypeName()) || var.getTypeName().equals(Object.class.getTypeName())) {
                    return var;
                }
                var = var.getSuperclass();
            } while (++thresh <= 1000);
        }
        return null;
    }
}