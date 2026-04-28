# API Surface

## Scope

这份摘要只覆盖主文档里显式链接到的 Kotlin UI DSL v2 核心入口：

- `DialogPanel.kt`
- `Panel.kt`
- `Row.kt`
- `Cell.kt`

这些源码来自 JetBrains `intellij-community` 分支 `idea/261.22158.277`。

## 先记住的对象模型

- `panel { ... }` 产出 `DialogPanel`
- `Panel` 负责组织行和组
- `Row` 负责一行里的布局和组件工厂
- `Cell<T>` 负责单个组件的标签、注释、对齐、绑定、校验与监听

如果需求说的是“对话框 / 设置页的一块表单”，通常就是在这个四层模型里找位置。

## DialogPanel

`DialogPanel` 是 DSL 构建结果，也是提交、重置、修改检测和校验的中心。

优先记住这些行为：

- `validateAll()`：
  只跑 apply 阶段校验；提交按钮逻辑里先调它。
- `apply()`：
  把绑定值写回模型，只对已修改的组件执行对应回调。
- `reset()`：
  用绑定源回填组件。
- `isModified()`：
  检测任意绑定组件或集成面板是否被改动。
- `registerValidators(...)`：
  把 input 级别校验接进生命周期。

最常见的提交模式：

```kotlin
val panel = panel {
  row("Name:") {
    textField().bindText(model::name)
  }
}

if (panel.validateAll().isEmpty()) {
  panel.apply()
}
```

## Panel

`Panel` 负责“放行、放组、放子 panel”。

优先使用这些入口：

- `row("Label:") { ... }`：
  最常见。自带 `LABEL_ALIGNED` 布局，并把标签关联到第一可编辑组件。
- `row { ... }`：
  没有外侧标签时使用，默认走 `INDEPENDENT`。
- `panel { ... }`：
  占整行宽度，并在内部开启自己的 grid。
- `rowsRange { ... }`：
  保持和父 grid 对齐，但把多行当成一个可统一管理的范围。
- `group(...) { ... }`：
  独立 grid + 标题 + 组间垂直间距。
- `groupRowsRange(...) { ... }`：
  有组标题，但仍与父 grid 对齐。
- `collapsibleGroup(...) { ... }`：
  需要折叠组时使用。
- `buttonsGroup(...) { ... }`：
  给一组 radio button 或带标题的 checkbox 组建语义分组。
- `separator()`：
  纯分隔线；如果还要标题，改用 `group*`。

## Row

`Row` 决定这一行怎么排、放什么组件、和上下文怎么交互。

### 布局

- `layout(RowLayout.INDEPENDENT)`：
  整行有自己的 grid。
- `layout(RowLayout.LABEL_ALIGNED)`：
  有外部标签时的默认行为。
- `layout(RowLayout.PARENT_GRID)`：
  标签和组件都进入父 grid；跨多行严格对齐时常用。

### 间距与结构

- `topGap(...)` / `bottomGap(...)`：
  给相关行附加垂直间距，避免隐藏/显示时打乱布局。
- `resizableRow()`：
  需要吃掉剩余垂直空间时使用。
- `rowComment(...)`：
  给整行补说明文案。
- `panel { ... }`：
  在当前行某个 cell 里再嵌一块子 panel。

### 常见工厂方法

优先用 DSL 自带工厂，而不是自己 new Swing：

- `textField()`
- `intTextField(...)`
- `checkBox(...)`
- `radioButton(...)`
- `comboBox(...)`
- `slider(...)`
- `spinner(...)`
- `button(...)`
- `text(...)`
- `comment(...)`
- `link(...)`
- `browserLink(...)`
- `contextHelp(...)`

## Cell<T>

`Cell<T>` 负责把单个组件“做完整”。

### 标签与帮助

- `label("...", LabelPosition.LEFT | TOP)`：
  给组件添加语义标签；可编辑组件的优先方案。
- `comment(...)`：
  在组件下方挂注释，能进入无障碍描述。
- `commentRight(...)`：
  在右侧放不换行的小注释。
- `contextHelp(...)`：
  右侧问号帮助，自动处理 gap、可见性、可用性和无障碍语义。

### 布局与尺寸

- `align(...)`
- `resizableColumn()`
- `gap(RightGap.SMALL | COLUMNS | ...)`
- `widthGroup("name")`
- `applyIfEnabled()`

### 绑定与校验

- `bind(...)`：
  通用绑定入口；实际更常用 `bindText`, `bindSelected`, `bindItem`, `bindValue`, `bindIntValue` 这类扩展。
- `validationOnInput(...)`
- `validationOnApply(...)`
- `errorOnApply(...)`
- `onApply { ... }`
- `onReset { ... }`
- `onIsModified { ... }`
- `onChanged { ... }`

## 快速启发式

- 你在决定“多行是否和父布局对齐”时，优先想 `RowLayout`。
- 你在决定“这几行是一组，但要不要自己的 grid”时，优先想 `group` vs `groupRowsRange`。
- 你在决定“说明文案该放哪”时，优先想 `comment` / `commentRight` / `rowComment` / `contextHelp`。
- 你在决定“表单怎么保存”时，优先想 `bindXXX + DialogPanel.apply/reset/isModified/validateAll`。
