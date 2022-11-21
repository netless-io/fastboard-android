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
   ```
   sed -i "" \
   "s/VERSION = \".*\"/VERSION = \"$RELEASE_VERSION\"/g" \
   "library/src/main/java/io/agora/board/fast/Fastboard.java"
   sed -i "" \
   "s/\"com.github.netless-io:\([^\:]*\):[^\"]*\"/\"com.github.netless-io:\1:$RELEASE_VERSION\"/g" \
   README.md README_zh_CN.md
    ```
4. Tag the release and push to GitHub.
   ```
   git commit -am "Prepare for release $RELEASE_VERSION"
   git tag -a $RELEASE_VERSION -m "Version $RELEASE_VERSION"
   git push -v origin refs/heads/main:refs/heads/main
   git push origin $RELEASE_VERSION
   ```

5. Trigger jitpack build
   ```shell
   curl https://jitpack.io/api/builds/com.github.netless-io/fastboard-android/$RELEASE_VERSION
   ```