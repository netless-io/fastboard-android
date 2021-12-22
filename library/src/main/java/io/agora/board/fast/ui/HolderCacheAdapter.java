package io.agora.board.fast.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

public abstract class HolderCacheAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    Set<VH> holdersCache = new HashSet<>();

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = onChildCreateViewHolder(parent, viewType);
        holdersCache.add(holder);
        return holder;
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);
        holdersCache.remove(holder);
        onChildViewRecycled(holder);
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    abstract VH onChildCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void onChildViewRecycled(@NonNull VH holder) {

    }
}
