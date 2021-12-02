package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.agora.board.fast.library.R;

/**
 * @author fenglibin
 */
public class ColorAdapter extends HolderCacheAdapter<ColorAdapter.ViewHolder> {
    private List<Integer> colors;
    private int curColor;
    private int mainColor;
    private OnColorClickListener onColorClickListener;

    public ColorAdapter(List<Integer> colors) {
        this.colors = colors;
    }

    @Override
    ViewHolder onChildCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_tool_color, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int color = colors.get(position);
        Context context = viewHolder.colorDisplay.getContext();

        viewHolder.colorDisplay.setImageDrawable(FastColorDrawableManager.getDrawable(context, color));
        viewHolder.colorDisplay.setSelected(color == curColor);
        viewHolder.itemView.setOnClickListener(v -> {
            curColor = color;
            if (onColorClickListener != null) {
                onColorClickListener.onColorClick(color);
            }
            updateSelected();
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void setOnColorClickListener(OnColorClickListener onColorClickListener) {
        this.onColorClickListener = onColorClickListener;
    }

    public void setCurrentColor(Integer color) {
        curColor = color;
        updateSelected();
    }

    private void updateSelected() {
        for (ViewHolder viewHolder : holdersCache) {
            int position = viewHolder.getAdapterPosition();
            int color = colors.get(position);
            viewHolder.colorDisplay.setSelected(color == curColor);
            viewHolder.colorDisplay.setBackground(makeSelector(viewHolder.colorDisplay.getContext(), mainColor));
        }
    }

    public static StateListDrawable makeSelector(Context context, int color) {
        StateListDrawable res = new StateListDrawable();
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.fast_bg_color_item_radius));
        shape.setStroke(context.getResources().getDimensionPixelSize(R.dimen.fast_bg_color_item_stroke_width), color);
        res.addState(new int[]{android.R.attr.state_selected}, shape);
        return res;
    }

    public void setMainColor(Integer mainColor) {
        this.mainColor = mainColor;
        updateSelected();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView colorDisplay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorDisplay = itemView.findViewById(R.id.color_display);
        }
    }

    public interface OnColorClickListener {
        void onColorClick(Integer color);
    }
}
