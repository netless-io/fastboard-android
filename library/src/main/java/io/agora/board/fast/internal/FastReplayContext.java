package io.agora.board.fast.internal;

import android.content.Context;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.FastException;
import io.agora.board.fast.FastReplay;
import io.agora.board.fast.FastReplayListener;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastReplayContext {
    final FastboardView fastboardView;
    Context context;
    FastReplay fastReplay;
    ResourceFetcher resourceFetcher;
    CopyOnWriteArrayList<FastReplayListener> listeners = new CopyOnWriteArrayList<>();

    public FastReplayContext(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
        this.context = fastboardView.getContext();
        this.resourceFetcher = ResourceFetcher.get();
        this.resourceFetcher.init(context);
    }

    public void addListener(FastReplayListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(FastReplayListener listener) {
        listeners.remove(listener);
    }

    public void notifyReplayReadyChanged(FastReplay fastReplay) {
        this.fastReplay = fastReplay;
        notifyListeners(listener -> listener.onReplayReadyChanged(fastReplay));
    }

    public void notifyFastError(FastException error) {
        notifyListeners(listener -> listener.onFastError(error));
    }

    private void notifyListeners(ListenerInvocation listenerInvocation) {
        CopyOnWriteArrayList<FastReplayListener> listenerSnapshot = new CopyOnWriteArrayList<>(listeners);
        for (FastReplayListener listener : listenerSnapshot) {
            listenerInvocation.invokeListener(listener);
        }
    }

    protected interface ListenerInvocation {
        void invokeListener(FastReplayListener listener);
    }
}
