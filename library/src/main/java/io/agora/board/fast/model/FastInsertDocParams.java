package io.agora.board.fast.model;

import androidx.annotation.Nullable;

public class FastInsertDocParams {
    /**
     * file extensions
     */
    private String fileType;
    /**
     * file remote url
     */
    private String resource;
    /**
     * file convert taskUUID
     */
    private String taskUUID;
    /**
     * file convert taskToken
     */
    private String taskToken;
    /**
     * app display title
     */
    private String title;

    public FastInsertDocParams(String resource, String fileType, String taskUUID, String taskToken) {
        this(resource, fileType, taskUUID, taskToken, null);
    }

    public FastInsertDocParams(String resource, String fileType, String taskUUID, String taskToken, @Nullable String title) {
        this.resource = resource;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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
