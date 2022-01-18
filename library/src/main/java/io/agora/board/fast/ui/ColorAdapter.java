package io.agora.board.fast.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private List<Integer> colors;
    private int curColor;
    private int mainColor;
    private OnColorClickListener onColorClickListener;

    public ColorAdapter(List<Integer> colors) {
        this.colors = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_tool_color, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int color = colors.get(position);

        viewHolder.colorDisplay.setBackground(ResourceFetcher.get().createColorBackground(mainColor));
        viewHolder.colorDisplay.setImageDrawable(ResourceFetcher.get().getColorDrawable(color));
        viewHolder.colorDisplay.setSelected(color == curColor);
        viewHolder.itemView.setOnClickListener(v -> {
            if (onColorClickListener != null) {
                onColorClickListener.onColorClick(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void setOnColorClickListener(OnColorClickListener onColorClickListener) {
        this.onColorClickListener = onColorClickListener;
    }

    public void setColor(Integer color) {
        curColor = color;

        notifyDataSetChanged();
    }

    public void setStyle(FastStyle style) {
        this.mainColor = style.getMainColor();

        notifyDataSetChanged();
    }

    public interface OnColorClickListener {
        void onColorClick(Integer color);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView colorDisplay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorDisplay = itemView.findViewById(R.id.color_display);
        }
    }
}
