package io.agora.board.fast.sample.cases.flat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.extension.FastResult;
import io.agora.board.fast.model.DocPage;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.misc.CloudFile;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.ui.ResourceFetcher;
import io.agora.board.fast.ui.RoomController;

public class CloudFilesController extends LinearLayoutCompat implements RoomController {

    private RecyclerView recyclerView;

    private FastRoom fastRoom;

    public CloudFilesController(@NonNull Context context) {
        this(context, null);
    }

    public CloudFilesController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudFilesController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayoutCompat.VERTICAL);
        setupView(context);
    }

    private void setupView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_cloud_files_controller, this, true);

        recyclerView = findViewById(R.id.cloud_files);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CloudFilesAdapter adapter = new CloudFilesAdapter(Repository.get().getCloudFiles());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(cloudFile -> {
            switch (cloudFile.type) {
                case "png":
                case "jpg":
                    insertImage(cloudFile);
                    break;
                case "mp4":
                    insertVideo(cloudFile);
                    break;
                case "pptx":
                    insertProjectorPptx(cloudFile);
                    break;
                case "ppt":
                case "pdf":
                    insertStaticDoc(cloudFile);
                    break;
            }
            hide();
        });
    }

    private void insertImage(CloudFile cloudFile) {
        fastRoom.insertImage(cloudFile.url, cloudFile.width, cloudFile.height);
    }

    private void insertVideo(CloudFile cloudFile) {
        fastRoom.insertVideo(cloudFile.url, cloudFile.name);
    }

    private void insertProjectorPptx(CloudFile cloudFile) {
        String taskUuid = cloudFile.taskUuid;
        String prefixUrl = cloudFile.prefixUrl;
        fastRoom.insertPptx(taskUuid, prefixUrl, "Pptx Display Title", new FastResult<String>() {
            @Override
            public void onSuccess(String value) {
                // insert projector pptx success
            }

            @Override
            public void onError(Exception exception) {
                // insert projector pptx fail
            }
        });
    }

    private void insertStaticDoc(CloudFile file) {
        DocPage[] pages = Repository.get().getDocPages(file.taskUuid);
        fastRoom.insertStaticDoc(pages, "Static Doc Title", new FastResult<String>() {
            @Override
            public void onSuccess(String value) {
                // insert static doc success
            }

            @Override
            public void onError(Exception exception) {
                // insert static doc fail
            }
        });
    }

    @Override
    public View getBindView() {
        return this;
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackground(ResourceFetcher.get().getLayoutBackground(style.isDarkMode()));
    }
}
