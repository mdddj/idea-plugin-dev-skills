# IDEA Plugin Dev Skills

[English](README.md)

这是一组面向 IDEA / IntelliJ Platform 插件开发的 Codex skill 集合。仓库把 JetBrains 插件开发中的常见主题集中整理，便于查找、复用和继续扩展。

## 仓库说明

- 当前包含: `28` 个顶层 skill 目录

## 顶层目录与功能模块

| 目录 | 模块功能 |
| --- | --- |
| `idea-file-and-code-templates` | IntelliJ 文件模板与代码模板能力，覆盖 `File and Code Templates`、模板变量、模板分组、自定义 `File | New` 模板动作等。 |
| `idea-intention-preview` | Intention / Quick Fix 预览模块，聚焦 `generatePreview()`、`IntentionPreviewInfo` 和预览测试。 |
| `idea-jewel-skill` | JetBrains Jewel 与 Compose 风格 UI 相关技能包，包含 IntelliJ 插件 UI 组件路线和 `platform/jewel` 源码导向能力。 |
| `idea-kotlin-dsl2-skill` | Kotlin UI DSL v2 技能包，聚焦 IntelliJ 插件设置页、表单和 `DialogWrapper` 中的 DSL UI 构建。 |
| `idea-live-templates` | IntelliJ Live Templates 模块，覆盖模板 XML、上下文、宏、Surround With 模板和插件导出。 |
| `idea-postfix-completion` | Postfix Completion / 后缀补全模块，覆盖 `PostfixTemplateProvider`、可编辑模板和模板描述资源。 |
| `intellij-action-system` | Action System 模块，处理 `AnAction`、菜单、工具栏、快捷键、Action Group 和 `plugin.xml` 动作注册。 |
| `intellij-code-documentation` | 代码文档模块，覆盖 Quick Documentation、Hover 文档、外部文档和 `DocumentationTarget` / `DocumentationProvider`。 |
| `intellij-dialog-wrapper` | 对话框模块，聚焦 `DialogWrapper`、校验、按钮布局、首焦点组件和尺寸持久化。 |
| `intellij-editor-components` | 编辑器组件模块，覆盖 `EditorTextField`、`LanguageTextField`、补全输入框和嵌入式代码编辑体验。 |
| `intellij-embedded-terminal` | 嵌入式终端模块，覆盖 IntelliJ 终端 API、终端标签页、命令执行、输出读取与终端集成。 |
| `intellij-extension-points` | 扩展点模块，覆盖 `plugin.xml` 中的 `extensionPoints` / `extensions`、`ExtensionPointName`、bean 扩展点、动态扩展点和扩展实现约束。 |
| `intellij-file-and-class-choosers` | 文件/目录/类/包选择器模块，覆盖 `FileChooser`、`TreeClassChooserFactory`、`PackageChooserDialog` 等。 |
| `intellij-icons` | 图标模块，覆盖插件 SVG 图标、`IconLoader`、New UI 图标映射、Tool Window 图标和动态图标。 |
| `intellij-informing-users` | 用户提示与通知模块，覆盖 Hint、Editor Notification、Got It 提示、气泡通知和非模态反馈。 |
| `intellij-inlay-hints` | Inlay Hints 模块，覆盖参数提示、声明式 hints、Code Vision、预览文件和扩展点注册。 |
| `intellij-lists-and-trees` | 列表与树模块，覆盖 `JBList`、`Tree`、速度搜索、渲染器、`ToolbarDecorator` 和可排序结构。 |
| `intellij-misc-swing-components` | IntelliJ 杂项 Swing 组件模块，覆盖 `Messages`、`JBSplitter`、`JBTabs`、工具栏等常用 UI 基础件。 |
| `intellij-platform-fundamentals` | IntelliJ Platform 基础规则模块，覆盖 service 生命周期、`Disposable` / `Disposer`、读写动作、EDT/BGT 线程模型、modality 和 Message Bus。 |
| `intellij-plugin-internationalization` | 国际化模块，覆盖 `DynamicBundle`、NLS 注解、资源包、模板翻译和插件 UI 文本本地化。 |
| `intellij-popups` | Popup 模块，覆盖 `JBPopupFactory`、列表弹窗、动作弹窗、自定义组件弹窗和定位方式。 |
| `intellij-project-view` | Project View 模块，覆盖项目视图节点装饰、树过滤、文件嵌套、自定义 Pane 和刷新逻辑。 |
| `intellij-reference-contributor` | 引用与跳转模块，覆盖 `PsiReferenceContributor`、引用解析、重命名、补全和跨语言导航。 |
| `intellij-settings` | 设置页与持久化模块，覆盖 `Configurable`、`PersistentStateComponent`、应用/项目级设置和同步策略。 |
| `intellij-status-bar-widgets` | 状态栏组件模块，覆盖 `StatusBarWidgetFactory`、编辑器感知组件、弹出式状态栏组件和 LightEdit 兼容。 |
| `intellij-tool-windows` | Tool Window 模块，覆盖工具窗口注册、动态工具窗口、内容管理、标签页与停靠区配置。 |
| `intellij-ui-faq` | UI FAQ 模块，覆盖 `JBColor`、`JBUI`、Insets/Border、UI Inspector、图标叠加和主题适配细节。 |
| `intellij-virtual-file-system` | VFS 模块，覆盖 `VirtualFile`、刷新机制、VFS 监听、路径查找、文件系统扩展和元数据钩子。 |

## 关键子目录与子模块

这些目录本身是 skill 容器，内部还包含更细的模块：

| 子目录 | 模块功能 |
| --- | --- |
| `idea-jewel-skill/intellij-plugin-ui-components` | IntelliJ 插件 UI 组件总入口，覆盖工具窗口、对话框、Kotlin UI DSL、通知、文件选择器、编辑器组件、状态栏等。 |
| `idea-jewel-skill/jetbrains-jewel` | JetBrains Jewel 专项模块，面向 Compose for Desktop 与 IntelliJ `platform/jewel` 源码导航、示例和兼容性说明。 |
| `idea-jewel-skill/dist` | 预留输出目录，当前通过 `.gitkeep` 保留空目录结构。 |
| `idea-kotlin-dsl2-skill/intellij-kotlin-ui-dsl2` | Kotlin UI DSL v2 专项实现模块，适合插件表单、配置页和带绑定/校验的 UI。 |
| `idea-postfix-completion/docs-to-skill` | 文档转 skill 辅助模块，用于把在线文档、本地文档或 API 说明整理成新的 Codex skill。 |

## 使用建议

- 如果需求是“生成 UI / 做设置页 / 做工具窗口”，优先看 `idea-jewel-skill`、`idea-kotlin-dsl2-skill`、`intellij-tool-windows`、`intellij-dialog-wrapper`。
- 如果需求是“编辑器增强”，优先看 `idea-intention-preview`、`idea-live-templates`、`idea-postfix-completion`、`intellij-inlay-hints`、`intellij-reference-contributor`。
- 如果需求是“平台集成”，优先看 `intellij-platform-fundamentals`、`intellij-extension-points`、`intellij-action-system`、`intellij-project-view`、`intellij-settings`、`intellij-embedded-terminal`、`intellij-virtual-file-system`。
- 如果需求是“体验优化和界面细节”，优先看 `intellij-icons`、`intellij-informing-users`、`intellij-popups`、`intellij-ui-faq`、`intellij-status-bar-widgets`。
