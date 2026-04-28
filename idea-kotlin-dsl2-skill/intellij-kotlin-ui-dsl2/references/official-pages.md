# Official Pages

## 主文档

- Kotlin UI DSL Version 2:
  <https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html>

这页定义了 Kotlin UI DSL v2 的核心定位：

- 用 Kotlin 代码声明 IntelliJ 表单 UI
- 产物是 `DialogPanel`
- 重点对象是 `Panel`、`Row`、`Cell`
- 通过 showcase demo 学习布局、间距、标签、注释、绑定和联动状态

## 先用这些官方页面做宿主界面判断

### 1. DialogWrapper

- <https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html>

何时先看它：

- 用户明确要对话框
- 你需要提交/取消/校验按钮生命周期
- 你准备把 Kotlin UI DSL v2 产出的 `DialogPanel` 放进 `createCenterPanel()`

读法：

- 先确定是否真的是对话框场景
- 再把 DSL 当作中心内容，而不是重写对话框框架本身

### 2. Settings

- <https://plugins.jetbrains.com/docs/intellij/settings.html>

何时先看它：

- 用户说“插件设置页”“Configurable”“Preferences / Settings”
- 你需要知道 UI 放在哪个扩展点、何时持久化

读法：

- 先确定是 `Configurable` / `SearchableConfigurable` 场景
- 再把 DSL 面板接到设置页逻辑里

### 3. Tool Window

- <https://plugins.jetbrains.com/docs/intellij/tool-window.html>

何时先看它：

- 用户说侧边栏、持久面板、可长期停留的工作区

读法：

- 先判断是不是 Tool Window，而不是 Dialog / Popup / Settings
- 真要用 DSL 时，只把它用于工具窗口内部的表单区域

### 4. User Interface Components

- <https://plugins.jetbrains.com/docs/intellij/user-interface-components.html>

何时先看它：

- 需求描述还很抽象，不确定是 dialog、popup、tool window 还是别的组件面

读法：

- 先定 UI surface，再回到 Kotlin UI DSL v2

### 5. Persisting State of Components

- <https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html>

何时先看它：

- 用户已经决定要做 settings / dialog form，并开始问“这些值怎么保存”
- 你需要判断该用 `PersistentStateComponent` 还是 `PropertiesComponent`
- 你需要确定 `@State` / `@Storage`、roaming、split mode 或敏感数据策略

读法：

- 先把 UI 和持久化分层：UI 仍然用 Kotlin UI DSL v2，状态保存交给独立 service
- 再回 `persisting-state.md` 看具体模式和示例

## 调试与自查页面

### Internal Actions Intro

- <https://plugins.jetbrains.com/docs/intellij/internal-actions-intro.html>

用于找到 IntelliJ 自带 UI 示例入口，便于对照 JetBrains 自身实现。

### Internal UI Inspector

- <https://plugins.jetbrains.com/docs/intellij/internal-ui-inspector.html#added-at-property>

用于检查某个 UI 组件来源于哪里，帮助你定位是哪个 DSL、哪个类或哪个添加点生成了当前界面。

## 语言和旧版页面

### Using Kotlin

- <https://plugins.jetbrains.com/docs/intellij/using-kotlin.html>

只在你需要确认插件项目里的 Kotlin 配置、JVM 目标或 Kotlin 插件支持时再看；它不是 DSL 使用说明本身。

### Kotlin UI DSL v1

- <https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl.html>

只在迁移旧代码或比对历史写法时才看。优先按 v2 设计，不要把 v1 写法直接搬进新实现。

## 已检查的原始页面链接

下面这些链接都来自主文档页面，我已经在构建这个 skill 时逐个检查并归类：

### 官方文档链接

- `using-kotlin.html`
- `dialog-wrapper.html`
- `settings.html`
- `tool-window.html`
- `user-interface-components.html`
- `persisting-state-of-components.html`
- `internal-actions-intro.html`
- `internal-ui-inspector.html#added-at-property`
- `kotlin-ui-dsl.html`

### JetBrains 源码入口

- `DialogPanel.kt`
- `Panel.kt`
- `Row.kt`
- `Cell.kt`
- `com/intellij/ui/dsl/builder` 包目录

### JetBrains showcase 示例

- `DemoBasics.kt`
- `DemoGaps.kt`
- `DemoGroups.kt`
- `DemoRowLayout.kt`
- `DemoComments.kt`
- `DemoAvailability.kt`
- `DemoTips.kt`
- `DemoComponentLabels.kt`
- `DemoBinding.kt`

真正写代码时，优先在 `api-surface.md` 和 `showcase-examples.md` 找对应模式，不要每次重新翻整页文档。
