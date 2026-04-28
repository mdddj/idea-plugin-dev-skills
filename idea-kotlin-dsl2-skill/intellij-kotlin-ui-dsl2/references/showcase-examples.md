# Showcase Examples

## Scope

这些示例都整理自 JetBrains `uiDslShowcase`，原始源码链接都来自 Kotlin UI DSL v2 官方页面。

分支版本：

- `idea/261.22158.277`

使用方式：

- 先按问题类型找最近的示例
- 再把示例结构改造成你的业务模型
- 保留 JetBrains 的布局语义，不要为了“看起来像”而丢掉 label、comment、binding 等语义

## 1. 基础行与父 grid 对齐

来源：

- `DemoBasics.kt`

用途：

- 学会默认 `row("Label:")` 与 `RowLayout.PARENT_GRID` 的差异

示例：

```kotlin
panel {
  row("Name:") {
    textField()
    label("Inline note")
  }

  row("Advanced:") {
    textField()
    label("Aligned with the next row")
  }.layout(RowLayout.PARENT_GRID)

  row("More:") {
    textField()
    label("Uses the same parent grid")
  }.layout(RowLayout.PARENT_GRID)
}
```

记住：

- 有外部 label 的 `row("...")` 默认是 `LABEL_ALIGNED`
- 需要多行共享同一父 grid 时，再切到 `PARENT_GRID`

## 2. 水平/垂直间距

来源：

- `DemoGaps.kt`

用途：

- 正确处理 checkbox 后字段、字段后单位标签、两列布局和垂直分组间距

示例：

```kotlin
panel {
  row {
    val mail = checkBox("Use mail:")
      .gap(RightGap.SMALL)
    textField().enabledIf(mail.selected)
  }

  row("Width:") {
    textField().gap(RightGap.SMALL)
    label("px")
  }

  row {
    checkBox("Column 1").gap(RightGap.COLUMNS)
    checkBox("Column 2")
  }.layout(RowLayout.PARENT_GRID)
}
```

记住：

- `RightGap.SMALL` 只给紧密相关组件
- 两列模式优先用 `twoColumnsRow` 或 `RightGap.COLUMNS`
- 垂直间距附着在“相关行”上，用 `topGap` / `bottomGap`

## 3. 分组、子 panel 与折叠组

来源：

- `DemoGroups.kt`

用途：

- 判断该用独立 grid 还是继续沿用父 grid

示例：

```kotlin
panel {
  group("Credentials") {
    row("User:") { textField() }
    row("Token:") { textField() }
  }

  groupRowsRange("Flags") {
    row { checkBox("Enable feature A") }
    row { checkBox("Enable feature B") }
  }

  collapsibleGroup("Advanced") {
    row("Path:") { textField() }
  }
}
```

记住：

- `group` 有自己的 grid 和组间距
- `groupRowsRange` 保留父 grid 对齐
- `panel {}` 是整行宽度子 panel

## 4. RowLayout 选择

来源：

- `DemoRowLayout.kt`

用途：

- 修复“为什么这行没和上一行对齐”“为什么 label/field 落位不一致”

示例：

```kotlin
panel {
  row("Label aligned:") {
    textField()
    textField()
  }

  row {
    label("Independent:")
    textField()
    textField()
  }

  row("Parent grid:") {
    textField()
    textField()
  }.layout(RowLayout.PARENT_GRID)
}
```

选择规则：

- 有外部标签且只需要正常表单对齐：`LABEL_ALIGNED`
- 没有外部标签：`INDEPENDENT`
- 想把 label 和组件一起并入父 grid：`PARENT_GRID`

## 5. 注释、行说明与上下文帮助

来源：

- `DemoComments.kt`

用途：

- 给字段补说明，而不破坏布局和无障碍语义

示例：

```kotlin
panel {
  row("Path:") {
    textField().comment("Stored relative to the project root")
  }

  row {
    textField().commentRight("Optional")
    textField().contextHelp("Used only for advanced import mode")
  }

  row("Mode:") {
    textField()
  }.rowComment("The change takes effect after restart")
}
```

记住：

- 和某个组件绑定的说明，优先放 `comment` / `commentRight`
- 和整行相关的说明，放 `rowComment`
- 点击式帮助优先用 `contextHelp`

## 6. 可见性与可用性联动

来源：

- `DemoAvailability.kt`

用途：

- 让 checkbox 控制一组子项的 enable/visible 状态

示例：

```kotlin
panel {
  lateinit var master: Cell<JBCheckBox>

  row {
    master = checkBox("Show advanced options")
  }

  indent {
    row { checkBox("Option 1") }
    row { checkBox("Option 2") }
  }.visibleIf(master.selected)
}
```

记住：

- 对简单依赖关系，优先用 `enabledIf` / `visibleIf`
- 把被控制的多行包进 `indent`, `rowsRange` 或 `group*`，再整体联动

## 7. 标签与无障碍语义

来源：

- `DemoComponentLabels.kt`

用途：

- 给可编辑组件建立正确标签、mnemonic 和辅助功能上下文

示例：

```kotlin
panel {
  row("&Name:") {
    textField()
    textField().label("Extra &value:")
  }

  row {
    textField().label("Description:", LabelPosition.TOP)
  }
}
```

记住：

- 可编辑组件的标签优先走 `row("...")` 或 `Cell.label(...)`
- `LabelPosition.TOP` 适合窄列或纵向密度更高的区域

## 8. 绑定、apply/reset/isModified

来源：

- `DemoBinding.kt`

用途：

- 把 UI 和模型连起来，并使用 `DialogPanel` 自带生命周期

示例：

```kotlin
data class Model(var enabled: Boolean = false, var name: String = "")

val model = Model()
val panel = panel {
  row {
    checkBox("Enabled").bindSelected(model::enabled)
  }
  row("Name:") {
    textField().bindText(model::name)
  }
}

if (panel.validateAll().isEmpty()) {
  panel.apply()
}
```

记住：

- `bindSelected`, `bindText`, `bindItem`, `bindValue`, `bindIntValue` 是一线工具
- `DialogPanel.reset()` 和 `isModified()` 会自动尊重这些绑定

## 9. 常用技巧

来源：

- `DemoTips.kt`

用途：

- 处理剩余宽度、统一宽度、默认 radio、输入列宽等细节

示例：

```kotlin
panel {
  row("Filter:") {
    textField()
      .columns(COLUMNS_MEDIUM)
      .resizableColumn()
    link("Reset") {}
  }

  row {
    textField().widthGroup("fields")
    button("Browse") {}.widthGroup("fields")
  }
}
```

记住：

- `columns(...)` 调整常见输入宽度
- `resizableColumn()` 吃掉剩余水平空间
- `widthGroup(...)` 让不同组件对齐到统一宽度
- 需要“最后一个组件不要自动扩张”时，可用空 `cell()` 占位

## 10. 读示例时的优先级

按这个顺序找最近模式：

1. 宿主界面问题：回到 `official-pages.md`
2. 行/组/布局问题：看 Basics, Groups, Row Layout, Gaps
3. 说明和标签问题：看 Comments, Components Labels
4. 状态和模型问题：看 Availability, Binding
5. 组件细节问题：看 Tips
