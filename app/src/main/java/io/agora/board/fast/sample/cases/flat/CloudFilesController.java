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

import com.herewhite.sdk.ConverterCallbacks;
import com.herewhite.sdk.converter.ConvertType;
import com.herewhite.sdk.converter.ConverterV5;
import com.herewhite.sdk.domain.ConversionInfo;
import com.herewhite.sdk.domain.ConvertException;
import com.herewhite.sdk.domain.ConvertedFiles;
import com.herewhite.sdk.domain.WindowAppParam;

import java.util.UUID;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.misc.CloudFile;
import io.agora.board.fast.sample.misc.Repository;
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
                    insertDynamicDoc(cloudFile);
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
        fastRoom.insertVideo(cloudFile.name, cloudFile.url);
    }

    private void insertDynamicDoc(CloudFile cloudFile) {
        insertDocs(cloudFile, true);
    }

    private void insertStaticDoc(CloudFile cloudFile) {
        insertDocs(cloudFile, false);
    }

    private void insertDocs(CloudFile cloudFile, Boolean dynamic) {
        ConverterV5 convert = new ConverterV5.Builder()
                .setResource(cloudFile.url)
                .setType(dynamic ? ConvertType.Dynamic : ConvertType.Static)
                .setTaskUuid(cloudFile.taskUUID)
                .setTaskToken(cloudFile.taskToken)
                .setCallback(new ConverterCallbacks() {
                    @Override
                    public void onProgress(Double progress, ConversionInfo convertInfo) {
                    }

                    @Override
                    public void onFinish(ConvertedFiles converted, ConversionInfo convertInfo) {
                        WindowAppParam param = WindowAppParam.createSlideApp(generateUniqueDir(cloudFile.taskUUID), converted.getScenes(), cloudFile.name);
                        fastRoom.getRoom().addApp(param, null);
                    }

                    private String generateUniqueDir(String taskUUID) {
                        String uuid = UUID.randomUUID().toString();
                        return String.format("/%s/%s", taskUUID, uuid);
                    }

                    @Override
                    public void onFailure(ConvertException e) {
                    }
                }).build();
        convert.startConvertTask();
    }

    @Override
    public View getBindView() {
        return this;
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }
}
