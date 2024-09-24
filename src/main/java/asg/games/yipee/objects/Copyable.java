package asg.games.yipee.objects;

public interface Copyable<T> {
 T copy();
 T deepCopy();
}