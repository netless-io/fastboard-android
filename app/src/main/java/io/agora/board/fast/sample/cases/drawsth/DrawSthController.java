package io.agora.board.fast.sample.cases.drawsth;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.ShapeType;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastListener;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastSdk;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.ui.RedoUndoLayout;
import io.agora.board.fast.ui.RoomController;
import io.agora.board.fast.ui.SubToolButton;
import io.agora.board.fast.ui.SubToolsLayout;
import io.agora.board.fast.ui.ToolButton;
import io.agora.board.fast.ui.ToolsLayout;

public class DrawSthController extends LinearLayoutCompat implements RoomController, FastListener {
    private static final List<ApplianceItem> DRAW_STH_APPLIANCES = new ArrayList<ApplianceItem>() {
        {
            add(ApplianceItem.PENCIL);
            add(ApplianceItem.RECTANGLE);
            add(ApplianceItem.ELLIPSE);
            add(ApplianceItem.STRAIGHT);
        }
    };
    private final int SHOW_NO = 0;
    private final int SHOW_TOOLS = 1;
    private final int SHOW_COLORS = 2;

    FastRoom fastRoom;

    private ImageView clearScenes;

    private RedoUndoLayout redoUndoLayout;
    private ToolButton toolButton;
    private ToolsLayout toolsLayout;

    private SubToolButton subToolButton;
    private SubToolsLayout subToolsLayout;

    private int showFlag = SHOW_NO;

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fastRoom == null) {
                return;
            }

            if (v == toolButton) {
                updateOverlay((showFlag == SHOW_TOOLS) ? SHOW_NO : SHOW_TOOLS);
            } else if (v == subToolButton) {
                updateOverlay((showFlag == SHOW_COLORS) ? SHOW_NO : SHOW_COLORS);
            } else if (v == clearScenes) {
                fastRoom.cleanScene();
            }
        }
    };

    private void updateOverlay(int flag) {
        if (flag == showFlag) {
            return;
        }
        showFlag = flag;
        toolsLayout.setShown(showFlag == SHOW_TOOLS);
        subToolsLayout.setShown(showFlag == SHOW_COLORS);
    }

    public DrawSthController(@NonNull Context context) {
        this(context, null);
    }

    public DrawSthController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawSthController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_draw_sth_controller, this, true);
        toolButton = root.findViewById(R.id.tools_button);
        subToolButton = root.findViewById(R.id.colors_button);
        clearScenes = root.findViewById(R.id.clear_scenes);
        redoUndoLayout = root.findViewById(R.id.redo_undo_layout);

        toolsLayout = root.findViewById(R.id.tools_layout);
        toolsLayout.setAppliances(DRAW_STH_APPLIANCES);
        toolsLayout.setOnApplianceClickListener(item -> {
            toolButton.setApplianceItem(item);
            subToolButton.setApplianceItem(item);

            fastRoom.setAppliance(item);
            updateOverlay(SHOW_NO);
        });
        subToolsLayout = root.findViewById(R.id.sub_tools_layout);
        subToolsLayout.setType(SubToolsLayout.TYPE_TEXT);
        subToolsLayout.setOnColorClickListener(color -> {
            subToolButton.setColor(color);
            subToolsLayout.setColor(color);

            fastRoom.setColor(color);
            updateOverlay(SHOW_NO);
        });

        toolButton.setOnClickListener(onClickListener);
        subToolButton.setOnClickListener(onClickListener);
        clearScenes.setOnClickListener(onClickListener);
    }

    @Override
    public void attachSdk(FastSdk fastSdk) {
        fastSdk.addListener(this);
        redoUndoLayout.attachSdk(fastSdk);
    }

    @Override
    public void onFastRoomCreated(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void onMemberStateChanged(MemberState memberState) {
        updateAppliance(memberState.getCurrentApplianceName(), memberState.getShapeType());
        updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    public void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance, shapeType);
        toolButton.setApplianceItem(item);
        toolsLayout.setApplianceItem(item);
    }

    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        subToolButton.setColor(color);
        subToolsLayout.setColor(color);
    }

    @Override
    public void updateFastStyle(FastStyle style) {

    }
}
