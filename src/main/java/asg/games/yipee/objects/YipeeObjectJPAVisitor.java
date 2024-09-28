package asg.games.yipee.objects;

import asg.games.yipee.persistence.YipeeStorageAdapter;

/**
 * Visitor Class to save child complex YipeeObjects
 */
public interface YipeeObjectJPAVisitor {
    void visitSave(YipeeStorageAdapter adapter);
}
