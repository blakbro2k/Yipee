package asg.games.yipee.persistence;

import asg.games.yipee.objects.YokelObject;

public interface Resolver {
    /** Gets Object given name */
    <T extends YokelObject> T getObjectByName(Class<T> clazz, String name);

    /** Gets Object given id */
    <T extends YokelObject> T getObjectById(Class<T> clazz, String id);

    /** Gets All Objects given class */
    <T extends YokelObject> Iterable<T> getObjects(Class<T> clazz);

    /** returns the number of All Objects given class */
    <T extends YokelObject> int countObjects(Class<T> clazz);
}
