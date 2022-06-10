package io.agora.board.fast.sample.misc;

public class CloudFile {
    public String type;
    public String name;
    public String url;

    // for test, Production environments use separate classes as Doc, Image
    // for doc
    public String taskUUID;
    public String taskToken;
    public Boolean projectorDoc;

    // for image
    public Integer width;
    public Integer height;
}
