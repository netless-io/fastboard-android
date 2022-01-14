package io.agora.board.fast.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolboxAdapter extends RecyclerView.Adapter<ToolboxAdapter.ViewHolder> {
    private List<ToolboxItem> items;

    private FastAppliance curAppliance;
    private ColorStateList iconColor;
    private boolean isDarkMode;
    private OnToolboxClickListener onToolboxClickListener;

    public ToolboxAdapter(List<ToolboxItem> appliances) {
        this.items = appliances;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toolbox, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolboxItem item = items.get(position);

        holder.appliance.setImageResource(ResourceFetcher.get().getApplianceIcon(item.appliance));
        holder.appliance.setImageTintList(iconColor);
        holder.appliance.setSelected(item.appliance == curAppliance);
        holder.appliance.setBackground(ResourceFetcher.get().createApplianceBackground(isDarkMode));

        holder.expand.setVisibility(item.expandable ? View.VISIBLE : View.GONE);
        holder.expand.setImageTintList(ResourceFetcher.get().getIconColor(isDarkMode));

        holder.itemView.setOnClickListener(v -> {
            if (onToolboxClickListener == null) {
                return;
            }

            if (curAppliance == item.appliance) {
                onToolboxClickListener.onToolboxReClick(item);
            } else {
                onToolboxClickListener.onSwitchToolbox(item, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * TODO here It's confusing, key is a place for multiple appliance.
     *
     * @param key
     * @param appliance
     */
    public void updateToolAppliance(int key, FastAppliance appliance) {
        List<ToolboxItem> target = new ArrayList<>(items);
        for (ToolboxItem toolboxItem : target) {
            if (key == toolboxItem.key) {
                toolboxItem.appliance = appliance;
                break;
            }
        }
        items = target;

        notifyDataSetChanged();
    }

    public void setAppliance(FastAppliance appliance) {
        curAppliance = appliance;

        notifyDataSetChanged();
    }

    public void updateFastStyle(FastStyle style) {
        iconColor = ResourceFetcher.get().getIconColor(style.isDarkMode());
        isDarkMode = style.isDarkMode();

        notifyDataSetChanged();
    }

    public void setOnToolboxClickListener(OnToolboxClickListener onToolboxClickListener) {
        this.onToolboxClickListener = onToolboxClickListener;
    }

    public interface OnToolboxClickListener {
        void onToolboxReClick(ToolboxItem item);

        void onSwitchToolbox(ToolboxItem item, ToolboxItem oldItem);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appliance;
        public ImageView expand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appliance = itemView.findViewById(R.id.appliance);
            expand = itemView.findViewById(R.id.expand);
        }
    }
}
