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
     * file extensions
     */
    private String fileType;
    /**
     * app display title
     */
    private String title;

    public FastInsertDocParams(String taskUUID, String taskToken, String fileType) {
        this(fileType, taskUUID, taskToken, null);
    }

    public FastInsertDocParams(String taskUUID, String taskToken, String fileType, @Nullable String title) {
        this.fileType = fileType;
        this.taskUUID = taskUUID;
        this.taskToken = taskToken;
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
}
