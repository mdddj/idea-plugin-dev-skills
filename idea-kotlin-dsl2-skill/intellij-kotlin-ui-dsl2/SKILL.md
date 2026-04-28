---
name: intellij-kotlin-ui-dsl2
description: Build and review IntelliJ Platform form UIs with Kotlin UI DSL Version 2 using JetBrains official docs plus inspected showcase source examples. Use when requests mention IntelliJ or IDEA plugin settings pages, DialogWrapper forms, Configurable panels, Kotlin UI DSL v2, `panel {}` / `row {}` builders, `DialogPanel`, `Panel`, `Row`, `Cell`, bindings, validation, `enabledIf`, `visibleIf`, labels, comments, spacing, accessibility, or 将 Swing 表单/插件设置页改写为 Kotlin UI DSL 2.
---

# IntelliJ Kotlin UI DSL 2

## Overview

使用这个 skill 为 IntelliJ Platform 插件实现或评审表单式 UI，并且优先遵循 JetBrains 官方 Kotlin UI DSL v2 指南与 JetBrains 自己的 `uiDslShowcase` 示例。

先决定宿主界面，再决定 DSL 结构，最后对照示例收口细节。

## 快速路由

- 先读 [references/official-pages.md](references/official-pages.md)：
  当需求还停留在“设置页 / 对话框 / 工具窗口该放哪”这一层，先用它选宿主界面，并确认原始文档里有哪些链接已经检查过。
- 再读 [references/api-surface.md](references/api-surface.md)：
  当你要写或评审 `panel {}`, `row {}`, `DialogPanel.apply()`, `bindXXX`, `validationOnApply()` 这类 DSL/API 细节时用它。
- 需要读 [references/persisting-state.md](references/persisting-state.md)：
  当需求涉及设置保存、`PersistentStateComponent`、`@State`、`@Storage`、`PropertiesComponent`、跨 IDE 同步、split mode，或者“这个表单的数据应该存哪里”时用它。
- 最后读 [references/showcase-examples.md](references/showcase-examples.md)：
  当你需要可复用写法时，按“布局 / 间距 / 分组 / 注释 / 标签 / 可见性 / 绑定”查最近的 JetBrains 示例。

## 工作流

1. 先判断 Kotlin UI DSL v2 是否合适。
   - 设置页、配置页、带校验的表单、模态对话框内容：优先用 Kotlin UI DSL v2。
   - 工具窗口里的一小块表单区域：可以用 Kotlin UI DSL v2。
   - 工具窗口整体骨架、通知、一次性消息框、非表单型复杂可视化：不要默认上 Kotlin UI DSL v2。

2. 再选择宿主界面。
   - Settings / Configurable：让 DSL 产出 `DialogPanel`，交给设置页宿主。
   - `DialogWrapper`：把 DSL 产出的 `DialogPanel` 作为中心面板。
   - Tool Window：只把 DSL 当成内部表单内容，不把它当通用 chrome/layout 框架。

3. 再把需求翻译成行和组。
   - 每个用户决策或字段，优先映射成一行。
   - 先用 `row("Label:")` 或 `Cell.label(...)` 建立正确标签关系。
   - 再用 `group`, `groupRowsRange`, `collapsibleGroup`, `panel`, `indent`, `rowsRange` 组织层次。

4. 再补状态、绑定和校验。
   - 用 `bindXXX` 把组件值绑定到模型。
   - 用 `enabledIf` / `visibleIf` 驱动联动状态。
   - 对提交型表单，优先在提交前执行 `DialogPanel.validateAll()`，无错误后再 `apply()`。
   - 如果这些值需要跨重启保存，继续读 `persisting-state.md`，把 UI 绑定到单独的持久化 service，而不是让扩展点实例自己持久化。

5. 最后对齐 JetBrains 习惯。
   - 间距优先用标准 gap / group spacing，不要先手写魔法数。
   - 帮助文本优先用 `comment`, `commentRight`, `rowComment`, `contextHelp`。
   - 可编辑组件优先使用带标签语义的写法，保证 mnemonic、`labelFor` 与无障碍信息一起成立。

## 决策规则

- 优先使用 `Row` 已提供的组件工厂方法，而不是手工塞原始 Swing 组件。
- 优先使用 `row("Label:")` 或 `Cell.label(...)`，不要用普通 `label()` 冒充输入组件标签。
- 只有在紧密相关的组件之间才使用 `RightGap.SMALL`，例如 checkbox 后跟 text field，或 text field 后跟单位标签。
- 当一组行必须与父 grid 保持对齐时，优先使用 `rowsRange` 或 `groupRowsRange`。
- 当一组行需要自己的标题和独立 grid 时，优先使用 `group` 或 `collapsibleGroup`。
- 对简单联动，优先使用 `enabledIf` / `visibleIf`，不要先写手工 listener。
- 需要统一宽度时用 `widthGroup`；需要让某列吃掉剩余宽度时用 `resizableColumn`。
- 需要让帮助文案参与无障碍语义时，把文案挂到对应 cell comment 或 context help，而不是另起一行普通标签。
- 不要让 extension 实例自己实现 `PersistentStateComponent`；需要持久化时，拆出单独的 application/project service。
- 敏感数据不要进 `PersistentStateComponent` 或 `PropertiesComponent`；改用 `PasswordSafe`。
- 不要把 Kotlin UI DSL v2 当成通用工具窗口布局系统。
- 不要在没确认 `RowLayout`、标准 gap、group 间距之前手写自定义 spacing。

## 常见请求到参考文件的映射

- “给插件设置页写 UI”：
  先读 `official-pages.md` 的 Settings / DialogWrapper 段，再读 `showcase-examples.md` 里的绑定、标签、注释示例。
- “这个 row 为什么没对齐 / 为什么列宽怪异”：
  先读 `api-surface.md` 的 `RowLayout` 与 `Panel.row`，再读 `showcase-examples.md` 的 Basics、Row Layout、Gaps。
- “让 checkbox 控制一组配置项启用或显示”：
  直接读 `showcase-examples.md` 的 Enabled/Visible 示例。
- “把旧 Swing 表单改成 Kotlin UI DSL v2”：
  保留原宿主界面，按 `row/group/bind/comment` 重新映射，不要先改外层框架。
- “如何做可点击说明、上下文帮助、行注释”：
  直接读 `showcase-examples.md` 的 Comments 与 Components Labels。
- “设置页的数据应该存哪、怎么保存、能不能同步”：
  直接读 `persisting-state.md`。
- “这个 token / password 要不要跟普通设置一起存”：
  直接读 `persisting-state.md` 里的敏感数据规则。

## References

- [references/official-pages.md](references/official-pages.md)
- [references/api-surface.md](references/api-surface.md)
- [references/persisting-state.md](references/persisting-state.md)
- [references/showcase-examples.md](references/showcase-examples.md)
