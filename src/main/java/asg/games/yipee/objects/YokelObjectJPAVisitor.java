package asg.games.yipee.objects;

import asg.games.yokel.persistence.YokelStorageAdapter;

/**
 * Visitor Class to save child YokelObjects
 */
public interface YokelObjectJPAVisitor {
    void visitSave(YokelStorageAdapter adapter);
}
