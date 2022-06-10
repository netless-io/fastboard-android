package io.agora.board.fast.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FastInsertDocParamsTest {
    private static String taskUUID = "fddaeb908e0b11ecb94f39bd66b92986";
    private static String taskToken = "NETLESSTASK_YWs9NWJod2NUeXk2MmRZWC11WiZub25jZT1mZTFlZjk3MC04ZTBiLTExZWMtYTMzNS01MWEyMGJkNzRiZjYmcm9sZT0yJnNpZz1jZGQwMzMyZTFlZTkwNGEyNjhlMjQ0NDc0NWQ4MTY0ZTAzNzNiOTIxZmI4ZDY0YTE0MTJiZTU5MmUwMjM3MzM4JnV1aWQ9ZmRkYWViOTA4ZTBiMTFlY2I5NGYzOWJkNjZiOTI5ODY";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void old_three_params_constructor() {
        String fileType = "pptx";
        FastInsertDocParams params = new FastInsertDocParams(taskUUID, taskToken, fileType);
        Assert.assertEquals(params.getTaskUUID(), taskUUID);
        Assert.assertEquals(params.getTaskToken(), taskToken);
        Assert.assertEquals(params.getFileType(), fileType);
    }

    @Test
    public void old_four_params_constructor() {
        String fileType = "pptx";
        String title = "演示文稿";
        FastInsertDocParams params = new FastInsertDocParams(taskUUID, taskToken, fileType, title);
        Assert.assertEquals(params.getTaskUUID(), taskUUID);
        Assert.assertEquals(params.getTaskToken(), taskToken);
        Assert.assertEquals(params.getFileType(), fileType);
        Assert.assertEquals(params.getTitle(), title);
    }

    @Test
    public void old_constructor_dynamic() {
        String[] fileTypes = new String[]{"pptx", "ppt", "pdf", "docx"};
        boolean[] dynamicList = new boolean[]{true, true, false, false};
        for (int i = 0; i < fileTypes.length; i++) {
            FastInsertDocParams params = new FastInsertDocParams(taskUUID, taskToken, fileTypes[i]);
            Assert.assertEquals(dynamicList[i], params.isDynamicDoc());
        }
    }

    @Test
    public void new_constructor() {
        FastInsertDocParams params = new FastInsertDocParams(taskUUID, taskToken, true);
        Assert.assertEquals(params.getTaskUUID(), taskUUID);
        Assert.assertEquals(params.getTaskToken(), taskToken);
        Assert.assertEquals(params.isDynamicDoc(), true);
        Assert.assertEquals(params.getConverterType(), ConverterType.WhiteboardConverter);

        params = new FastInsertDocParams(taskUUID, taskToken, false);
        Assert.assertEquals(params.getTaskUUID(), taskUUID);
        Assert.assertEquals(params.getTaskToken(), taskToken);
        Assert.assertEquals(params.isDynamicDoc(), false);
        Assert.assertEquals(params.getConverterType(), ConverterType.WhiteboardConverter);
    }
}