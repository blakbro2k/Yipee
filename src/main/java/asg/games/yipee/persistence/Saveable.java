package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeObject;

/**
 * Database Saving Interface
 */
public interface Saveable {

    /**
     * Saves an persistence object
     */
    <T extends YipeeObject> void saveObject(T object);

    /**
     * Commit transactions
     */
    void commitTransactions();

    /**
     * Delete transactions
     */
    void rollBackTransactions();
}
