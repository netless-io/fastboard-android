Releasing
=========

Cutting a Release
-----------------

1. Update `CHANGELOG.md`.
   > Fix:
   > New: new api, feature, etc.
   > Update: bump dependencies
2. Set versions:

    ```
    export RELEASE_VERSION=X.Y.Z
    ```
3. Update versions:
   ```shell
   sed -i "" \
   "s/VERSION = \".*\"/VERSION = \"$RELEASE_VERSION\"/g" \
   "library/src/main/java/io/agora/board/fast/Fastboard.java"
   sed -i "" \
   "s/\"com.github.netless-io:\([^\:]*\):[^\"]*\"/\"com.github.netless-io:\1:$RELEASE_VERSION\"/g" \
   README.md README_zh_CN.md
   
   # app denpendencies
   sed -i "" \
   "s/\"com.github.netless-io:fastboard-android:[^\"]*\"/\"com.github.netless-io:fastboard-android:$RELEASE_VERSION\"/g" \
   app/build.gradle
    ```
4. Tag the release and push to GitHub.
   ```shell
   git commit -am "Prepare for release $RELEASE_VERSION"
   git tag -a $RELEASE_VERSION -m "Version $RELEASE_VERSION"
   git push -v origin refs/heads/main:refs/heads/main
   git push origin $RELEASE_VERSION
   ```

5. Trigger jitpack build
   ```shell
   curl https://jitpack.io/api/builds/com.github.netless-io/fastboard-android/$RELEASE_VERSION
   ```

JitPack 发布限制
-----------------

> **注意**: 对于单一 GitHub 仓库，JitPack **无法生成独立可单独引用的多模块 artifact**。
>
> 在 `netless-io/fastboard-android` 项目中，尝试发布 `library` 和 `library-ktx` 两个模块时，引用方式会变成：
>
> ```text
> com.github.netless-io:fastboard-android
> ----com.github.netless-io:fastboard-android:fastboard-android
> ----com.github.netless-io:fastboard-android:fastboard-android-ktx
> ```
>
> 这种格式与通用 Maven / Gradle artifact 引用习惯不一致，容易造成混淆。
>
> **解决方案**：
>
> 1. 如果希望每个模块都能像普通库一样单独引用，需要将模块拆分到 **独立的 Git 仓库**；
> 2. 或者选择其他发布渠道（如 Maven Central、Sonatype OSSRH）来支持多模块独立发布。