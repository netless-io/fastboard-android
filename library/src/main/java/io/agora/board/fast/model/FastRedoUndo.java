package io.agora.board.fast.model;

/**
 * @author fenglibin
 */
public class FastRedoUndo {
    private final long redo;
    private final long undo;

    public FastRedoUndo(long redo, long undo) {
        this.redo = redo;
        this.undo = undo;
    }

    public long getRedo() {
        return redo;
    }

    public long getUndo() {
        return undo;
    }
}
