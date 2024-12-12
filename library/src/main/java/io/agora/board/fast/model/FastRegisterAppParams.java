package io.agora.board.fast.model;

import java.util.Map;

/**
 * 用于注册 App 参数的配置类。
 * <p>
 * 提供两种注册方式：
 * 1. **本地脚本注册**：通过本地的 JavaScript 字符串注册（推荐方式，可靠性高）。
 * - 使用 `FastRegisterAppParams(String javascriptString, String kind, String variable, Map<String, Object> appOptions)` 构造函数。
 * 2. **远程包注册**：通过发布包的 URL 进行注册（可能因网络原因失败）。
 * - 使用 `FastRegisterAppParams(String url, String kind, Map<String, Object> appOptions)` 构造函数。
 */
public class FastRegisterAppParams {
    // 用本地 js 代码注册
    private String javascriptString;
    // 注册的 app 名称
    private String kind;
    // 用发布包代码注册
    private String url;
    // 初始化 app 实例时，会被传入的参数。这段配置不会被同步其他端，属于本地设置。常常用来设置 debug 的开关。
    private Map<String, Object> appOptions;
    // 挂载在 window 上的变量名，挂在后为 window.variable
    private String variable;

    /**
     * 使用本地脚本方式构建注册参数。
     *
     * @param javascriptString 本地 JS 脚本代码
     * @param kind             App 的类型名称
     * @param variable         挂载到 window 的变量名称
     * @param appOptions       App 参数（本地设置）
     */
    public FastRegisterAppParams(String javascriptString, String kind, String variable, Map<String, Object> appOptions) {
        this.javascriptString = javascriptString;
        this.kind = kind;
        this.appOptions = appOptions;
        this.variable = variable;
    }

    /**
     * 使用远程 URL 方式构建注册参数。
     *
     * @param url        远程注册包的 URL
     * @param kind       App 的类型名称
     * @param appOptions App 参数（本地设置）
     */
    public FastRegisterAppParams(String url, String kind, Map<String, Object> appOptions) {
        this.url = url;
        this.kind = kind;
        this.appOptions = appOptions;
    }

    public String getJavascriptString() {
        return javascriptString;
    }

    public String getKind() {
        return kind;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getAppOptions() {
        return appOptions;
    }

    public String getVariable() {
        return variable;
    }
}
