package io.agora.board.fast.model;

import androidx.annotation.Nullable;

public class FastInsertDocParams {
    /**
     * file convert taskUUID
     */
    private String taskUUID;
    /**
     * file convert taskToken
     */
    private String taskToken;

    /**
     * region flag that task is initiated
     */
    private FastRegion region;

    /**
     * type of document conversion service that task is initiated
     * <p>
     * {@link ConverterType#WhiteboardConverter }
     * {@link ConverterType#Projector }
     */
    private ConverterType converterType = ConverterType.WhiteboardConverter;

    /**
     * filename extensions.
     * e.g. pdf, docx, ppt, pptx
     */
    private String fileType;

    /**
     * document type flag that task is initiated
     */
    private Boolean dynamicDoc;

    /**
     * app display title
     */
    private String title;

    /**
     * @param taskUUID
     * @param taskToken
     * @param dynamicDoc
     * @since 1.2.1
     */
    public FastInsertDocParams(String taskUUID, String taskToken, Boolean dynamicDoc) {
        this(taskUUID, taskToken, dynamicDoc, null);
    }

    /**
     * @param taskUUID
     * @param taskToken
     * @param dynamicDoc
     * @param title
     * @since 1.2.1
     */
    public FastInsertDocParams(String taskUUID, String taskToken, Boolean dynamicDoc, @Nullable String title) {
        this.taskUUID = taskUUID;
        this.taskToken = taskToken;
        this.dynamicDoc = dynamicDoc;
        this.title = title;
    }

    public FastInsertDocParams(String taskUUID, String taskToken, String fileType) {
        this(taskUUID, taskToken, fileType, null);
    }

    public FastInsertDocParams(String taskUUID, String taskToken, String fileType, @Nullable String title) {
        this.taskUUID = taskUUID;
        this.taskToken = taskToken;
        this.fileType = fileType;
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public String getTaskToken() {
        return taskToken;
    }

    public void setTaskToken(String taskToken) {
        this.taskToken = taskToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isDynamicDoc() {
        if (dynamicDoc == null) {
            return isDynamicDoc(fileType);
        }
        return dynamicDoc;
    }

    public void setDynamicDoc(Boolean dynamicDoc) {
        this.dynamicDoc = dynamicDoc;
    }

    public ConverterType getConverterType() {
        return converterType;
    }

    public void setConverterType(ConverterType converterType) {
        this.converterType = converterType;
    }

    public FastRegion getRegion() {
        return region;
    }

    public void setRegion(FastRegion region) {
        this.region = region;
    }

    private boolean isDynamicDoc(String fileType) {
        switch (fileType) {
            case "pptx":
            case "ppt":
                return true;
            default:
                return false;
        }
    }
}
