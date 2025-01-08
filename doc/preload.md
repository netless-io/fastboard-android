### Documentation for `FastboardConfig`

#### Overview

The `FastboardConfig` class is a configuration utility for initializing the Fastboard library. It
provides various settings related to preloading WebViews for enhanced performance.

---

### Class: `FastboardConfig`

#### Fields

- **`context`** (`Context`):  
  The application context used for initializing Fastboard.

- **`enablePreload`** (`boolean`):  
  Flag indicating whether preloading of WebViews is enabled.

- **`preloadCount`** (`int`):  
  The number of WebViews to preload. This is constrained by available memory resources.

- **`autoPreload`** (`boolean`):  
  Flag indicating whether automatic preloading of WebViews is enabled.

---

#### Default Values

- **`DEFAULT_ENABLE_PRELOAD`**: `false`  
  The default value for `enablePreload` if not explicitly set.

- **`DEFAULT_PRELOAD_COUNT`**: `0`  
  The default number of WebViews to preload if not explicitly set.

- **`DEFAULT_AUTO_PRELOAD`**: `true`  
  The default value for `autoPreload` if not explicitly set.

---

#### Constructor

The constructor is private and is invoked through the nested `Builder` class.

---

### Methods

#### `getContext()`

- **Description**: Retrieves the application context used for initializing Fastboard.
- **Return Type**: `Context`

#### `isEnablePreload()`

- **Description**: Checks if preloading of WebViews is enabled.
- **Return Type**: `boolean`

#### `getPreloadCount()`

- **Description**: Gets the number of WebViews to preload.
- **Return Type**: `int`

#### `isAutoPreload()`

- **Description**: Checks if auto-preloading is enabled.
- **Return Type**: `boolean`

---

### Nested Class: `Builder`

#### Overview

The `Builder` class facilitates the creation of `FastboardConfig` instances through a fluent
interface.

#### Fields

- **`context`** (`Context`):  
  The application context required for initialization.

- **`enablePreload`** (`boolean`):  
  Determines whether preloading is enabled (default: `false`).

- **`preloadCount`** (`int`):  
  Sets the number of WebViews to preload (default: `0`).

- **`autoPreload`** (`boolean`):  
  Configures automatic preloading behavior (default: `true`).

#### Constructor

- **`Builder(Context context)`**:  
  Initializes the builder with the required application context.

#### Methods

- **`enablePreload(boolean enablePreload)`**:  
  Enables or disables preloading.  
  **Returns**: `Builder`

- **`preloadCount(int preloadCount)`**:  
  Sets the number of WebViews to preload. Memory constraints should be considered, and values
  above `3` are discouraged. mostly `1` is enough.  
  **Returns**: `Builder`

- **`autoPreload(boolean autoPreload)`**:  
  Configures whether WebViews are automatically preloaded.  
  **Returns**: `Builder`

- **`build()`**:  
  Constructs and returns a `FastboardConfig` instance.  
  **Returns**: `FastboardConfig`

---

### Usage Example

```java
// Initialize FastboardConfig using Builder
FastboardConfig config = new FastboardConfig.Builder(context)
        .enablePreload(true)
        .preloadCount(1)
        .autoPreload(false)
        .build();
```