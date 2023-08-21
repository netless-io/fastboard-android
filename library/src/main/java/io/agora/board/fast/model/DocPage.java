package io.agora.board.fast.model;

/**
 * @author fenglibin
 */
public class DocPage {

    // image url
    private String src;

    // image preview url
    private String preview;

    // image width
    private Double width;

    // image height
    private Double height;

    public DocPage(String src, Double width, Double height) {
        this(src, width, height, null);
    }

    public DocPage(String src, Double width, Double height, String preview) {
        this.src = src;
        this.width = width;
        this.height = height;
        this.preview = preview;
    }

    public String getSrc() {
        return src;
    }

    public String getPreview() {
        return preview;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }
}
