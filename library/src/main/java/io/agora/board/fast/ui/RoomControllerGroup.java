package io.agora.board.fast.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.SceneState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

public abstract class RoomControllerGroup implements RoomController {

    protected final ViewGroup root;
    protected final Context context;
    private CopyOnWriteArrayList<RoomController> controllers = new CopyOnWriteArrayList<>();

    public RoomControllerGroup(ViewGroup container) {
        this.root = container;
        this.context = container.getContext();
    }

    abstract public void setupView();

    public void addController(RoomController controller) {
        controllers.addIfAbsent(controller);
    }

    public void removeController(RoomController controller) {
        controllers.remove(controller);
    }

    public void removeAll() {
        controllers.clear();
    }

    public void setFastRoom(FastRoom fastRoom) {
        for (RoomController controller : controllers) {
            controller.setFastRoom(fastRoom);
        }
    }

    public void updateSceneState(SceneState sceneState) {
        for (RoomController controller : controllers) {
            controller.updateSceneState(sceneState);
        }
    }

    public void updateMemberState(MemberState memberState) {
        for (RoomController controller : controllers) {
            controller.updateMemberState(memberState);
        }
    }

    @Override
    public void updateWindowBoxState(String windowBoxState) {
        for (RoomController controller : controllers) {
            controller.updateWindowBoxState(windowBoxState);
        }
    }

    public void updateRedoUndo(FastRedoUndo count) {
        for (RoomController controller : controllers) {
            controller.updateRedoUndo(count);
        }
    }

    @Override
    public void updateOverlayChanged(int key) {
        for (RoomController controller : controllers) {
            controller.updateOverlayChanged(key);
        }
    }

    public void updateFastStyle(FastStyle style) {
        for (RoomController controller : controllers) {
            controller.updateFastStyle(style);
        }
    }

    @Override
    public View getBindView() {
        return root;
    }
}