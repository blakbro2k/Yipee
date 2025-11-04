package asg.games.yipee.core.persistence;

public interface Updatable<T> {
    void updateFrom(T source);
}
