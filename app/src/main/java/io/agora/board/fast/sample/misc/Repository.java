package io.agora.board.fast.sample.misc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import com.google.gson.reflect.TypeToken;

import io.agora.board.fast.model.DocPage;
import io.agora.board.fast.sample.cases.hione.HiOneActivity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.cases.QuickStartActivity;
import io.agora.board.fast.sample.cases.RoomActivity;
import io.agora.board.fast.sample.cases.drawsth.DrawSthActivity;
import io.agora.board.fast.sample.cases.flat.FlatRoomActivity;

/**
 * a singleton class to provider mock data
 *
 * @author fenglibin
 */
public class Repository {

    private static Repository instance;

    private Context context;

    private String TEST_CLOUD_FILES_JSON = "" +
        "[" +
        "    {\n" +
        "        \"type\":\"pdf\",\n" +
        "        \"name\":\"开始使用 Flat\",\n" +
        "        \"taskUuid\":\"8da4cdc71a9845d385a5b58ddfa10b7e\"" +
        "    },\n"
        +
        "    {\n" +
        "        \"type\":\"pptx\",\n" +
        "        \"name\":\"声网白板 Fastboard\",\n" +
        "        \"taskUuid\":\"dc01ee126edc4ce7be8da3f7361a2f70\",\n" +
        "        \"prefixUrl\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/demo/dynamicConvert\"" +
        "    },\n"
        +
        "    {\n" +
        "        \"type\":\"pptx\",\n" +
        "        \"name\":\"Agora Fastboard\",\n" +
        "        \"taskUuid\":\"3e3a2b8845194f998e6e05adab70e1a1\",\n" +
        "        \"prefixUrl\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/demo/dynamicConvert\"" +
        "    },\n"
        +
        "    {\n" +
        "        \"type\":\"mp4\",\n" +
        "        \"name\":\"oceans.mp4\",\n" +
        "        \"url\":\"https://flat-storage.oss-accelerate.aliyuncs.com/cloud-storage/2022-02/15/55509848-5437-463e-b52c-f81d1319c837/55509848-5437-463e-b52c-f81d1319c837.mp4\"\n"
        +
        "    },\n"
        +
        "    {\n" +
        "        \"type\":\"png\",\n" +
        "        \"name\":\"lena_color.png\",\n" +
        "        \"url\":\"https://flat-storage.oss-accelerate.aliyuncs.com/cloud-storage/2022-02/15/ebe8320a-a90e-4e03-ad3a-a5dc06ae6eda/ebe8320a-a90e-4e03-ad3a-a5dc06ae6eda.png\",\n"
        +
        "        \"width\": 512,\n" +
        "        \"height\": 512\n" +
        "    },\n"
        +
        "    {\n" +
        "        \"type\":\"png\",\n" +
        "        \"name\":\"lena_gray.png\",\n" +
        "        \"url\":\"https://flat-storage.oss-accelerate.aliyuncs.com/cloud-storage/2022-02/15/8d487d84-e527-4760-aeb6-e13235fd541f/8d487d84-e527-4760-aeb6-e13235fd541f.png\",\n"
        +
        "        \"width\": 512,\n" +
        "        \"height\": 512\n" +
        "    }\n" +
        "]";

    // for test, fetch api from local
    private HashMap<String, String> docJsonMap = new HashMap<String, String>() {{
        put("8da4cdc71a9845d385a5b58ddfa10b7e",
            "{\"uuid\":\"8da4cdc71a9845d385a5b58ddfa10b7e\",\"type\":\"static\",\"status\":\"Finished\",\"convertedPercentage\":100,\"pageCount\":12,\"images\":{\"1\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/1.png\"},\"2\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/2.png\"},\"3\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/3.png\"},\"4\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/4.png\"},\"5\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/5.png\"},\"6\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/6.png\"},\"7\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/7.png\"},\"8\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/8.png\"},\"9\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/9.png\"},\"10\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/10.png\"},\"11\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/11.png\"},\"12\":{\"width\":1152,\"height\":648,\"url\":\"https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/staticConvert/8da4cdc71a9845d385a5b58ddfa10b7e/12.png\"}}}");
    }};

    private Repository() {
    }

    public synchronized static Repository get() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void getRemoteData(int delay, Callback callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> callback.onSuccess(new Object()), delay);
    }

    public List<CloudFile> getCloudFiles() {
        Type type = new TypeToken<ArrayList<CloudFile>>() {
        }.getType();
        ArrayList<CloudFile> result = Utils.fromJson(TEST_CLOUD_FILES_JSON, type);
        return result;
    }

    public String getDocJson(String uuid) {
        return docJsonMap.get(uuid);
    }

    public List<TestCase> getTestCases() {
        ArrayList<TestCase> result = new ArrayList<>();
        result.add(new TestCase(
            "Quick Start",
            "Start fastboard with a sample",
            QuickStartActivity.class,
            new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
        ));

        result.add(new TestCase(
            "Draw Something",
            "Use fastboard in case with several people",
            DrawSthActivity.class,
            new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
        ));

        result.add(new TestCase(
            "Extension Apis",
            "Test fastboard more extension api",
            RoomActivity.class,
            new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
        ));

        result.add(new TestCase(
            "Flat Sample",
            "Test apis with cloud file and window-manager",
            FlatRoomActivity.class,
            new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
        ));

        result.add(new TestCase(
            "HiOne Sample",
            "Extension Sample 1",
            HiOneActivity.class,
            new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
        ));

        return result;
    }

    public DocPage[] getDocPages(String taskUuid) {
        StaticDoc doc = Utils.fromJson(Repository.get().getDocJson(taskUuid), StaticDoc.class);
        DocPage[] pages = new DocPage[doc.pageCount];
        for (int i = 0; i < doc.pageCount; i++) {
            String index = String.valueOf(i + 1);
            Image image = doc.images.get(index);
            DocPage page = new DocPage(
                image.url,
                image.width,
                image.height
            );
            pages[i] = page;
        }
        return pages;
    }

    interface Callback {

        void onSuccess(Object object);

        void onFailure(Exception e);
    }
}
