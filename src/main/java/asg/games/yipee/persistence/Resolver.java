package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeObject;

public interface Resolver {
    /** Gets Object given name */
    <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name);

    /** Gets Object given id */
    <T extends YipeeObject> T getObjectById(Class<T> clazz, String id);

    /** Gets All Objects given class */
    <T extends YipeeObject> Iterable<T> getObjects(Class<T> clazz);

    /** returns the number of All Objects given class */
    <T extends YipeeObject> int countObjects(Class<T> clazz);
}
