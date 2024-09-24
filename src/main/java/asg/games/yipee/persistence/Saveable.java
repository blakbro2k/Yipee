package asg.games.yipee.persistence;

/**
 * Database Saving Interface
 *
 * @param <T>
 */
public interface Saveable<T> {

    /**
     * Saves an persistence object
     */
    void saveObject(T object);

    /**
     * Commit transactions
     */
    void commitTransactions();

    /**
     * Delete transactions
     */
    void rollBackTransactions();
}
