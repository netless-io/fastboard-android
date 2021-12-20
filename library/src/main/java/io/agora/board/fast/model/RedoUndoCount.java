package io.agora.board.fast.model;

/**
 * @author fenglibin
 */
public class RedoUndoCount {
    private final int redoCount;
    private final int undoCount;

    public RedoUndoCount(int redoCount, int undoCount) {
        this.redoCount = redoCount;
        this.undoCount = undoCount;
    }
}
