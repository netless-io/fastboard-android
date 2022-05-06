package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.MemberState;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ExtensionButton extends FrameLayout implements RoomController {
    private static final int TYPE_HIDE = 1;
    private static final int TYPE_DELETE = 2;
    private static final int TYPE_COLOR = 3;

    private ImageView subToolImage;
    private ImageView subToolExpand;

    private int type;
    private int color;
    private OnSubToolClickListener onSubToolClickListener;

    private final OnClickListener onClickListener = v -> {
        if (onSubToolClickListener == null) {
            return;
        }
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
    };

    public ExtensionButton(@NonNull Context context) {
        this(context, null);
    }

    public ExtensionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtensionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public View getBindView() {
        return this;
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_extension_button, this, true);
        subToolImage = root.findViewById(R.id.delete);
        subToolExpand = root.findViewById(R.id.sub_tool_expand);

        setOnClickListener(onClickListener);
        updateUi();
    }

    public void setAppliance(FastAppliance fastAppliance) {
        changeTypeByAppliance(fastAppliance);
        updateUi();
    }

    private void changeTypeByAppliance(FastAppliance fastAppliance) {
        switch (fastAppliance) {
            case SELECTOR:
                type = TYPE_DELETE;
                break;
            case PENCIL:
            case TEXT:
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
                subToolImage.setImageResource(R.drawable.fast_ic_tool_delete);
                break;
            case TYPE_COLOR:
                setVisibility(VISIBLE);
                subToolImage.setImageDrawable(ResourceFetcher.get().getColorDrawable(color));
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

    @Override
    public void updateMemberState(MemberState memberState) {
        updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
        setAppliance(FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType()));
    }

    private void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        this.setColor(color);
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackground(ResourceFetcher.get().getButtonBackground(style.isDarkMode()));
        subToolImage.setImageTintList(ResourceFetcher.get().getIconColor(style.isDarkMode()));
        subToolExpand.setImageTintList(ResourceFetcher.get().getIconColor(style.isDarkMode()));
    }

    public interface OnSubToolClickListener {
        void onDeleteClick();

        void onColorClick();
    }
}
