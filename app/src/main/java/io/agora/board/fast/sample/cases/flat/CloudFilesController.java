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
import java.util.ArrayList;
import java.util.List;
import io.agora.board.fast.ui.ResourceFetcher;
import io.agora.board.fast.ui.RoomController;
import io.agora.board.fast.sample.cases.helper.RoomOperationsKT;

public class CloudFilesController extends LinearLayoutCompat implements RoomController {

    private RecyclerView recyclerView;

    private FastRoom fastRoom;
    private RoomOperationsKT roomOperations;

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

        // 获取云端文件列表并添加自定义 URL 输入项
        List<CloudFile> cloudFiles = new ArrayList<>(Repository.get().getCloudFiles());
        cloudFiles.add(createCloudFileItem("video_url", "添加视频 URL"));
        cloudFiles.add(createCloudFileItem("youtube_url", "添加 YouTube"));

        CloudFilesAdapter adapter = new CloudFilesAdapter(cloudFiles);
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
                case "video_url":
                    if (roomOperations != null) {
                        roomOperations.showVideoInputDialog();
                    }
                    break;
                case "youtube_url":
                    if (roomOperations != null) {
                        roomOperations.showYoutubeInputDialog();
                    }
                    break;
            }
            hide();
        });
    }

    private CloudFile createCloudFileItem(String type, String name) {
        CloudFile item = new CloudFile();
        item.type = type;
        item.name = name;
        return item;
    }

    private void insertImage(CloudFile cloudFile) {
        fastRoom.insertImage(cloudFile.url, cloudFile.width, cloudFile.height);
    }

    private void insertVideo(CloudFile cloudFile) {
        fastRoom.insertVideo(cloudFile.url, cloudFile.name);
    }

    private void insertProjectorPptx(CloudFile file) {
        String taskUuid = file.taskUuid;
        String prefixUrl = file.prefixUrl;
        fastRoom.insertPptx(taskUuid, prefixUrl, file.name, new FastResult<String>() {
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
        fastRoom.insertStaticDoc(pages, file.name, new FastResult<String>() {
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

    public void setRoomOperations(RoomOperationsKT roomOperations) {
        this.roomOperations = roomOperations;
    }
}
