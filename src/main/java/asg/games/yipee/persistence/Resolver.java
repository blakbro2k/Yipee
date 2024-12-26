package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeObject;

public interface Resolver {
    /**
     * Gets Object given name
     */
    <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name);

    /**
     * Gets Object given id
     *
     * @return
     */
    <T extends YipeeObject> Object getObjectById(Class<T> clazz, String id) throws Exception;

    /**
     * Gets All Objects given class
     */
    <T extends YipeeObject> Iterable<T> getAllObjects(Class<T> clazz);

    /**
     * returns the number of All Objects given class
     */
    <T extends YipeeObject> int countAllObjects(Class<T> clazz);
}
