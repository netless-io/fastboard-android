package io.agora.board.fast.model;

/**
 * @author fenglibin
 */
public class RedoUndoCount {
    private final long redo;
    private final long undo;

    public RedoUndoCount(long redoCount, long undoCount) {
        this.redo = redoCount;
        this.undo = undoCount;
    }
}
