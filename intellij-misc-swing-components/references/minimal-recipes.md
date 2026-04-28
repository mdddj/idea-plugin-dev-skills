# Minimal Recipes

## Messages

Use `Messages` only when a blocking dialog is justified.

```kotlin
import com.intellij.openapi.ui.Messages

val confirmed = Messages.showYesNoDialog(
  project,
  "Delete the generated preview?",
  "My Plugin",
  Messages.getYesButton(),
  Messages.getNoButton(),
  Messages.getQuestionIcon(),
) == Messages.YES
```

## JBSplitter

Use a stable key when the user is expected to resize the split.

```kotlin
import com.intellij.ui.JBSplitter

val splitter = JBSplitter(false, "my.plugin.preview.splitter", 0.35f)
splitter.setFirstComponent(leftPanel)
splitter.setSecondComponent(rightPanel)
```

## JBTabs

Use `TabInfo` as the tab model and configure behavior through the presentation object.

```kotlin
import com.intellij.ui.tabs.JBTabsPosition
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.impl.JBTabsImpl

val tabs = JBTabsImpl(project, parentDisposable)
tabs.presentation
  .setSingleRow(true)
  .setTabsPosition(JBTabsPosition.top)
  .setTabDraggingEnabled(true)

tabs.addTab(
  TabInfo(previewPanel)
    .setText("Preview")
    .setPreferredFocusableComponent(previewPanel)
)
tabs.addTab(
  TabInfo(sourcePanel)
    .setText("Source")
    .setPreferredFocusableComponent(sourcePanel)
)
```

## ActionToolbar

Use this when you already have actions and just need a toolbar surface.

```kotlin
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup

val group = DefaultActionGroup(refreshAction, clearAction)
val toolbar = ActionManager.getInstance()
  .createActionToolbar("MyPlugin.Toolbar", group, true)
toolbar.targetComponent = contentPanel

container.add(toolbar.component)
```

## ToolbarDecorator

Use this for list, tree, and table CRUD controls instead of hand-building button rows.

```kotlin
import com.intellij.ui.ToolbarDecorator

val panel = ToolbarDecorator.createDecorator(myList)
  .setAddAction { addItem() }
  .setRemoveAction { removeSelectedItems() }
  .disableUpDownActions()
  .createPanel()
```
