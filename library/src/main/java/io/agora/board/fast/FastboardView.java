package io.agora.board.fast;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.herewhite.sdk.WhiteboardView;
import io.agora.board.fast.internal.WhiteboardViewManager;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.FastUiSettings;
import io.agora.board.fast.ui.ResourceFetcher;

/**
 * `FastboardView` 类，用于配置白板界面。
 *
 * @author fenglibin
 */
public class FastboardView extends FrameLayout {

    Fastboard fastboard;

    FastUiSettings fastUiSettings;

    WhiteboardView whiteboardView;

    public FastboardView(@NonNull Context context) {
        this(context, null);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupWhiteboardViewManager();
        setupResourceFetcher(context);
        setupStyle(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupWhiteboardViewManager() {
        FastboardConfig config = Fastboard.getConfig();
        if (config == null) {
            config = new FastboardConfig.Builder(getContext()).build();
        }
        WhiteboardViewManager.get().init(config);
    }

    private void setupResourceFetcher(Context context) {
        ResourceFetcher.get().init((Application) context.getApplicationContext());
    }

    private void setupStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastboardView, defStyleAttr,
            R.style.DefaultFastboardView);
        int mainColor = a.getColor(R.styleable.FastboardView_fbv_main_color,
            ContextCompat.getColor(context, R.color.fast_default_main_color));
        boolean darkMode = a.getBoolean(R.styleable.FastboardView_fbv_dark_mode, false);
        a.recycle();

        FastStyle fastStyle = new FastStyle();
        fastStyle.setMainColor(mainColor);
        fastStyle.setDarkMode(darkMode);
        getFastboard().setFastStyle(fastStyle);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fastboard_view, this, true);
        setupWhiteboardView((FrameLayout) root);
        updateFastStyle(getFastboard().getFastStyle());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(() -> fastboard.updateWhiteboardLayout(w, h));
    }

    private void setupWhiteboardView(FrameLayout root) {
        whiteboardView = WhiteboardViewManager.get().obtain();
        whiteboardView.setId(R.id.fast_whiteboard_view);
        whiteboardView.setAutoResize(false);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        // whiteboard view should be the first child
        root.addView(whiteboardView, 0, lp);
    }

    void updateFastStyle(FastStyle style) {
        setBackgroundColor(ResourceFetcher.get().getBackgroundColor(style.isDarkMode()));
        whiteboardView.setBackgroundColor(ResourceFetcher.get().getBoardBackgroundColor(style.isDarkMode()));
        // workaround update window manager color if existed, this should be in [FastRoom] when method support
        whiteboardView.loadUrl(String.format(
            "javascript:if(window.manager) { window.manager.setPrefersColorScheme(\"%s\") }",
            style.isDarkMode() ? "dark" : "light")
        );
    }

    public Fastboard getFastboard() {
        if (fastboard == null) {
            fastboard = new Fastboard(this);
        }
        return fastboard;
    }

    public FastUiSettings getUiSettings() {
        if (fastUiSettings == null) {
            fastUiSettings = new FastUiSettings(this);
        }
        return fastUiSettings;
    }

    public WhiteboardView getWhiteboardView() {
        return whiteboardView;
    }
}
