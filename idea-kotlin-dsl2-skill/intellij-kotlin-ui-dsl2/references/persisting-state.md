# Persisting State

## Scope

这份参考整理自以下官方页面和它们在主文档里的关联链接：

- Persisting State of Components
- Services
- Persisting Sensitive Data
- Persistent State Component in Split Mode

主页面：

- <https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html>

## 什么时候读这份参考

- 你已经用 Kotlin UI DSL v2 做了 settings/dialog form，接下来要保存值
- 用户问 `PersistentStateComponent`、`SerializablePersistentStateComponent`、`SimplePersistentStateComponent`
- 用户问 `@State`、`@Storage`、roaming、Backup and Sync
- 用户问“这个值该存在 application 还是 project 作用域”
- 用户问 token、password、server credentials 该怎么存
- 用户在 Remote Development / split mode 场景下需要前后端同步设置

## 核心结论

- 表单 UI 和状态持久化分开做。
  UI 用 Kotlin UI DSL v2；状态保存放到单独的 application/project service。
- Kotlin 下优先使用 `SerializablePersistentStateComponent`。
  官方文档明确写了它自 2022.2 起可用且推荐，适合不可变 data class 状态和 copy-on-write 更新。
- 只有在你明确想利用 `BaseState` 委托、并能接受集合修改计数细节时，再考虑 `SimplePersistentStateComponent`。
- 扩展点实例不要自己实现 `PersistentStateComponent`。
  如果 extension 需要状态，定义单独 service 管理它。
- 敏感数据不要进 `PersistentStateComponent` 或 `PropertiesComponent`。
  密码、token、credentials 走 `PasswordSafe`。

## 先做的设计判断

### 1. 选作用域

- 应用级设置：
  跨项目共享，适合插件全局偏好。
- 项目级设置：
  只属于某个 project，适合项目相关配置。

来自 Services 文档的约束：

- service 按需加载
- 不要在构造器里做重初始化
- 不要提前获取和缓存别的 service
- module-level service 不推荐

### 2. 选持久化方案

- 正式插件设置：
  `PersistentStateComponent`
- 少量、简单、非 roamable 的临时值：
  `PropertiesComponent`
- 密码或 token：
  `PasswordSafe`

## 推荐模式 1: Kotlin settings 用 `SerializablePersistentStateComponent`

这是当前最稳的默认方案。

```kotlin
@Service(Service.Level.APP)
@State(
  name = "MyPluginSettings",
  storages = [Storage("my-plugin.xml")]
)
class MyPluginSettings :
  SerializablePersistentStateComponent<MyPluginSettings.State>(State()) {

  data class State(
    @JvmField val endpoint: String = "",
    @JvmField val enableFeature: Boolean = false,
  )

  var endpoint: String
    get() = state.endpoint
    set(value) {
      updateState { it.copy(endpoint = value) }
    }

  var enableFeature: Boolean
    get() = state.enableFeature
    set(value) {
      updateState { it.copy(enableFeature = value) }
    }
}
```

为什么优先它：

- 不可变 state 更容易推理
- `updateState { copy(...) }` 原子更新
- 内部修改跟踪更完整

适配 Kotlin UI DSL v2 的常见方式：

```kotlin
row("Endpoint:") {
  textField()
    .bindText(settings::endpoint)
}
row {
  checkBox("Enable feature")
    .bindSelected(settings::enableFeature)
}
```

## 推荐模式 2: `SimplePersistentStateComponent` 适合委托式可变状态

```kotlin
@Service(Service.Level.PROJECT)
@State(
  name = "MyProjectSettings",
  storages = [Storage("my-project-settings.xml")]
)
class MyProjectSettings :
  SimplePersistentStateComponent<MyProjectSettings.State>(State()) {

  class State : BaseState() {
    var executablePath by string("")
    var autoSync by property(false)
  }

  var executablePath: String
    get() = state.executablePath
    set(value) { state.executablePath = value }

  var autoSync: Boolean
    get() = state.autoSync
    set(value) { state.autoSync = value }
}
```

限制：

- `BaseState` 的简单属性能自动跟踪修改
- 集合的增量修改可能需要手动 `incrementModificationCount()`

所以如果你的状态里有复杂集合，优先回到 `SerializablePersistentStateComponent`

## 存储位置规则

优先记这些：

- 项目级插件设置优先放独立 XML 文件，而不是混进 workspace
- 应用级设置强烈建议自定义文件
- `other.xml` 已不推荐；会影响 roaming
- 缓存值用 `StoragePathMacros.CACHE_FILE`

最常见写法：

```kotlin
@State(
  name = "MyProjectSettings",
  storages = [Storage("my-project-settings.xml")]
)
```

如果你明确要放工作区文件：

```kotlin
@State(
  name = "MyWorkspaceSettings",
  storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
```

## Roaming 和同步

`@Storage` 的 `roamingType` 决定设置是否共享：

- `RoamingType.DEFAULT`
- `RoamingType.PER_OS`
- `RoamingType.DISABLED`

实务规则：

- 会因机器不同而变化的路径、可执行文件位置，不要共享
- 一份 XML 文件里的多个 persistent components，`roamingType` 必须一致
- application-level light service 如果实现了 `PersistentStateComponent`，Services 文档要求 roaming 必须禁用

如果你想接入 Backup and Sync：

- `roamingType` 不能是 `DISABLED`
- `@State.category` 不能是 `OTHER`
- 同文件里的其他 persistent components 不能有不同 roaming 配置

## 简单值用 `PropertiesComponent`

只在下面这些情况使用：

- 只有少量简单 key-value
- 不需要 roam
- 更像“本机记忆”而不是正式插件设置

```kotlin
val appProps = PropertiesComponent.getInstance()
appProps.setValue("com.example.myPlugin.lastAccount", "alice")

val projectProps = PropertiesComponent.getInstance(project)
projectProps.setValue("com.example.myPlugin.lastBranch", "main")
```

规则：

- key 必须带插件前缀，避免命名冲突
- 不要拿它存正式配置模型
- 不要拿它存敏感信息

## 敏感数据用 `PasswordSafe`

来自 Persisting Sensitive Data 页面：

- `PasswordSafe.get()` / `set()` 是阻塞的，不要在 EDT 调
- 凭证名称用 `generateServiceName()` 生成，方便密码管理器识别
- 远程开发场景下，2025.3 起支持 `getAsync()`

示例：

```kotlin
private fun credentialAttributes(key: String): CredentialAttributes {
  return CredentialAttributes(generateServiceName("MyPlugin", key))
}

fun saveToken(key: String, username: String, token: String) {
  PasswordSafe.instance.set(
    credentialAttributes(key),
    Credentials(username, token)
  )
}
```

## Split Mode 要补同步元数据

来自 Persistent State Component in Split Mode 页面：

- 只做 `PersistentStateComponent` 还不够
- 还需要 `RemoteSettingInfoProvider`
- 还需要 `<applicationSettings ...>` 或 `<projectSettings ...>` XML 声明
- 还要选初始同步方向

最小示例：

```kotlin
class MySettingsRemoteInfoProvider : RemoteSettingInfoProvider {
  override fun getRemoteSettingsInfo() = mapOf(
    "MySettings" to RemoteSettingInfo(direction = Direction.InitialFromFrontend)
  )
}
```

```xml
<extensions defaultExtensionNs="com.intellij">
  <rdct.remoteSettingProvider implementation="com.example.MySettingsRemoteInfoProvider"/>
</extensions>

<applicationSettings service="com.example.MySettings"/>
```

方向选择规则：

- application-level 设置默认 `InitialFromFrontend`
- project-level 设置默认 `InitialFromBackend`

如果你在 split mode 下使用 `SimplePersistentStateComponent`，官方页建议重写 `noStateLoaded()`，以便远端传来空状态时回到默认值。

## 和 Kotlin UI DSL v2 的组合方式

推荐数据流：

1. `service` 持有持久化状态
2. Kotlin UI DSL v2 面板直接绑定 service 暴露出来的属性
3. `DialogPanel.apply()` 触发绑定写回
4. 平台在后续保存阶段调用 `getState()`

也就是说：

- Kotlin UI DSL v2 负责“编辑”
- `PersistentStateComponent` 负责“保存”
- 不要把 XML、storage 或 secrets 逻辑塞进 UI 代码里

## 快速决策表

- 正式插件设置：
  `SerializablePersistentStateComponent`
- 可变委托式简单状态：
  `SimplePersistentStateComponent`
- 少量临时值：
  `PropertiesComponent`
- 密码/token：
  `PasswordSafe`
- split mode 远程同步：
  `PersistentStateComponent` + `RemoteSettingInfoProvider` + settings XML 声明
