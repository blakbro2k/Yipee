package asg.games.yipee.persistence;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;

import java.util.Iterator;
import java.util.List;

import asg.games.yokel.objects.YokelObject;

public class MemoryStorage extends AbstractStorage {
    private ObjectMap<String, Object> _idStore;
    private ObjectMap<String, Object> _nameStore;
    private Queue<Object> _transactions;

    protected MemoryStorage(){
        _idStore = new ObjectMap<>();
        _nameStore = new ObjectMap<>();
        _transactions = new Queue<>();
    }

    @Override
    public void dispose() {
        _transactions.clear();
        _idStore.clear();
        _nameStore.clear();
    }

    @Override
    public <T extends YokelObject> T getObjectByName(Class<T> clazz, String name) {
        try{
            return clazz.cast(_nameStore.get(name));
        } catch (ClassCastException c){
            throw new RuntimeException(c);
        }
    }

    @Override
    public <T extends YokelObject> T getObjectById(Class<T> clazz, String id) {
        try{
            return clazz.cast(_idStore.get(id));
        } catch (ClassCastException c){
            throw new RuntimeException(c);
        }
    }

    @Override
    public <T extends YokelObject> List<T> getObjects(Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends YokelObject> int countObjects(Class<T> clazz) {
        return 0;
    }

    @Override
    public void saveObject(Object object) {
        _transactions.addFirst(object);
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
        return _transactions.size;
    }

}