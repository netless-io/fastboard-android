package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.agora.board.fast.library.R;
import io.agora.board.fast.model.ApplianceItem;

/**
 * @author fenglibin
 */
public class SubToolButton extends FrameLayout {
    private static final int TYPE_HIDE = 1;
    private static final int TYPE_DELETE = 2;
    private static final int TYPE_COLOR = 3;

    private ImageView subToolImage;

    private int type;
    private int color;
    private OnSubToolClickListener onSubToolClickListener;

    private OnClickListener localOnClickListener = v -> {
        if (onSubToolClickListener != null) {
            switch (type) {
                case TYPE_DELETE:
                    onSubToolClickListener.onDeleteClick();
                    break;
                case TYPE_COLOR:
                    onSubToolClickListener.onColorClick();
                    break;
                default:
                    break;
            }
        }
    };

    public SubToolButton(@NonNull Context context) {
        this(context, null);
    }

    public SubToolButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubToolButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_sub_tool_button, this, true);
        subToolImage = root.findViewById(R.id.sub_tool_button_image);

        setOnClickListener(localOnClickListener);
        updateUi();
    }

    public void setApplianceItem(ApplianceItem item) {
        changeTypeByAppliance(item);
        updateUi();
    }

    private void changeTypeByAppliance(ApplianceItem item) {
        switch (item) {
            case SELECTOR:
                type = TYPE_DELETE;
                break;
            case PENCIL:
            case ARROW:
            case RECTANGLE:
            case ELLIPSE:
                type = TYPE_COLOR;
                break;
            default:
                type = TYPE_HIDE;
                break;
        }
    }

    private void updateUi() {
        switch (type) {
            case TYPE_DELETE:
                setVisibility(VISIBLE);
                subToolImage.setImageResource(R.drawable.fast_ic_delete);
                break;
            case TYPE_COLOR:
                setVisibility(VISIBLE);
                subToolImage.setImageDrawable(FastColorDrawableManager.getDrawable(getContext(), color));
                break;
            default:
                setVisibility(INVISIBLE);
                break;
        }
    }

    public void setOnSubToolClickListener(OnSubToolClickListener onSubToolClickListener) {
        this.onSubToolClickListener = onSubToolClickListener;
    }

    public void setColor(Integer color) {
        this.color = color;
        updateUi();
    }

    public interface OnSubToolClickListener {
        void onDeleteClick();

        void onColorClick();
    }
}
