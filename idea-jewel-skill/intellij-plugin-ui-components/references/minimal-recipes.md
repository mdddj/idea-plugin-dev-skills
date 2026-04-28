# Minimal Recipes

Use these snippets as the default starting point when the user asks for implementation. Keep them minimal and adapt package names, IDs, and disposal ownership.

Use `scripts/scaffold_ui_recipe.py` when the user wants actual files on disk. Use this reference when inline snippets are enough.

## Contents

- Tool Window
- Dialog With Kotlin UI DSL
- Intention Action
- Popup Chooser
- Notification Balloon
- File Chooser With Path Field
- EditorTextField
- List With ToolbarDecorator
- Search Everywhere Contributor
- Status Bar Widget Factory
- JCEF Browser
- Theme Metadata

## Tool Window

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <toolWindow
      id="Sample"
      anchor="right"
      factoryClass="com.example.SampleToolWindowFactory"
      icon="/icons/toolWindow.svg" />
  </extensions>
</idea-plugin>
```

`SampleToolWindowFactory.kt`

```kotlin
package com.example

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JLabel
import javax.swing.JPanel

class SampleToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel().apply {
            add(JLabel("Hello from tool window"))
        }
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        content.setPreferredFocusableComponent(panel)
        toolWindow.contentManager.addContent(content)
    }
}
```

## Dialog With Kotlin UI DSL

```kotlin
package com.example

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class SampleDialog(project: Project) : DialogWrapper(project) {
    private var name: String = ""

    private val ui = panel {
        row("Name:") {
            textField()
                .bindText(::name)
                .focused()
        }
    }

    init {
        title = "Sample Dialog"
        init()
        initValidation()
    }

    override fun createCenterPanel(): JComponent = ui

    override fun doValidate(): ValidationInfo? {
        return if (name.isBlank()) ValidationInfo("Name is required") else null
    }
}
```

Usage:

```kotlin
if (SampleDialog(project).showAndGet()) {
    // OK pressed
}
```

## Intention Action

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <intentionAction>
      <className>com.example.SampleIntentionAction</className>
      <language>JAVA</language>
    </intentionAction>
  </extensions>
</idea-plugin>
```

`SampleIntentionAction.kt`

```kotlin
package com.example

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException

class SampleIntentionAction : PsiElementBaseIntentionAction() {
    override fun getFamilyName(): String = "Sample Intention"

    override fun getText(): String = familyName

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        return true
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        // TODO implement PSI-aware logic
    }
}
```

## Popup Chooser

```kotlin
val popup = JBPopupFactory.getInstance()
    .createPopupChooserBuilder(listOf("Debug", "Run", "Test"))
    .setTitle("Choose Action")
    .setItemChosenCallback { selected ->
        println("Selected: $selected")
    }
    .createPopup()

popup.showInBestPositionFor(dataContext)
```

## Notification Balloon

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <notificationGroup id="Sample Notifications" displayType="BALLOON" />
  </extensions>
</idea-plugin>
```

`notify(project)`

```kotlin
Notification(
    "Sample Notifications",
    "Index finished successfully",
    NotificationType.INFORMATION
).notify(project)
```

## File Chooser With Path Field

```kotlin
val field = TextFieldWithBrowseButton()
field.addBrowseFolderListener(
    "Choose Directory",
    "Select the working directory",
    project,
    FileChooserDescriptorFactory.createSingleFolderDescriptor()
)
```

## EditorTextField

```kotlin
val field = EditorTextField(
    "println(\"hello\")",
    project,
    PlainTextFileType.INSTANCE,
    false,
    false
)
field.setOneLineMode(false)
```

## List With ToolbarDecorator

```kotlin
val model = CollectionListModel("One", "Two")
val list = JBList(model)

val panel = ToolbarDecorator.createDecorator(list)
    .setAddAction {
        model.add("New Item")
    }
    .setRemoveAction {
        val index = list.selectedIndex
        if (index >= 0) model.remove(index)
    }
    .createPanel()
```

## Search Everywhere Contributor

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <searchEverywhereContributor implementation="com.example.SampleSearchEverywhereContributor$Factory" />
  </extensions>
</idea-plugin>
```

`SampleSearchEverywhereContributor.kt`

```kotlin
package com.example

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.Processor
import javax.swing.JList
import javax.swing.ListCellRenderer

class SampleSearchEverywhereContributor(
    private val project: Project
) : SearchEverywhereContributor<String>, DumbAware {
    private val items = listOf("One", "Two", "Three")

    override fun getSearchProviderId(): String = "com.example.SampleSearchEverywhereContributor"

    override fun getGroupName(): String = "Sample"

    override fun getSortWeight(): Int = 1000

    override fun showInFindResults(): Boolean = false

    override fun isShownInSeparateTab(): Boolean = true

    override fun fetchElements(pattern: String, progressIndicator: ProgressIndicator, consumer: Processor<in String>) {
        items.filter { it.contains(pattern, ignoreCase = true) || pattern.isBlank() }
            .forEach {
                progressIndicator.checkCanceled()
                if (!consumer.process(it)) return
            }
    }

    override fun processSelectedItem(selected: String, modifiers: Int, searchText: String): Boolean = true

    override fun getElementsRenderer(): ListCellRenderer<in String> {
        return object : ColoredListCellRenderer<String>() {
            override fun customizeCellRenderer(
                list: JList<out String>,
                value: String?,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean
            ) {
                append(value ?: "", SimpleTextAttributes.REGULAR_ATTRIBUTES)
            }
        }
    }

    class Factory : SearchEverywhereContributorFactory<String> {
        override fun createContributor(initEvent: AnActionEvent): SearchEverywhereContributor<String> {
            val project = initEvent.getRequiredData(CommonDataKeys.PROJECT)
            return SampleSearchEverywhereContributor(project)
        }

        override fun isAvailable(project: Project): Boolean = true
    }
}
```

## Status Bar Widget Factory

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <statusBarWidgetFactory
      id="SampleStatus"
      implementation="com.example.SampleStatusBarWidgetFactory" />
  </extensions>
</idea-plugin>
```

`SampleStatusBarWidgetFactory.kt`

```kotlin
package com.example

import com.intellij.openapi.util.Disposer
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.StatusBar

class SampleStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "SampleStatus"

    override fun getDisplayName(): String = "Sample Status"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return SampleStatusBarWidget()
    }

    override fun disposeWidget(widget: StatusBarWidget) = Disposer.dispose(widget)

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
```

`SampleStatusBarWidget.kt`

```kotlin
package com.example

import com.intellij.openapi.Disposable
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.ui.components.JBLabel
import javax.swing.JComponent

class SampleStatusBarWidget : CustomStatusBarWidget, Disposable {
    private val label = JBLabel("Ready")

    override fun ID(): String = "SampleStatus"

    override fun install(statusBar: StatusBar) = Unit

    override fun getComponent(): JComponent = label

    override fun dispose() = Unit
}
```

## JCEF Browser

```kotlin
if (!JBCefApp.isSupported()) return

val browser = JBCefBrowser("https://plugins.jetbrains.com")
val component = browser.component
```

Dispose the browser with the owning `Disposable`.

## Theme Metadata

`plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <themeMetadataProvider path="/META-INF/MyPlugin.themeMetadata.json" />
  </extensions>
</idea-plugin>
```

`META-INF/MyPlugin.themeMetadata.json`

```json
{
  "name": "My Plugin",
  "fixed": false,
  "ui": [
    {
      "key": "MyPlugin.Panel.background",
      "description": "Panel background for the main plugin view",
      "source": "com.example.MyPanel",
      "since": "2026.1"
    }
  ]
}
```

`MyPanel.kt`

```kotlin
val background = JBColor.namedColor(
    "MyPlugin.Panel.background",
    JBColor.PanelBackground
)
```

## Source Notes

- Tool windows: <https://plugins.jetbrains.com/docs/intellij/tool-windows.html>
- Dialogs: <https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html>
- Kotlin UI DSL: <https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html>
- Popups: <https://plugins.jetbrains.com/docs/intellij/popups.html>
- Notification balloons: <https://plugins.jetbrains.com/docs/intellij/notification-balloons.html>
- PsiElementBaseIntentionAction source: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/codeInsight/intention/PsiElementBaseIntentionAction.java>
- Search Everywhere contributor interface: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/ide/actions/searcheverywhere/SearchEverywhereContributor.java>
- Search Everywhere contributor factory interface: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/ide/actions/searcheverywhere/SearchEverywhereContributorFactory.java>
- File choosers: <https://plugins.jetbrains.com/docs/intellij/file-and-class-choosers.html>
- Editor components: <https://plugins.jetbrains.com/docs/intellij/editor-components.html>
- Status bar widgets: <https://plugins.jetbrains.com/docs/intellij/status-bar-widgets.html>
- JCEF: <https://plugins.jetbrains.com/docs/intellij/embedded-browser-jcef.html>
- Theme metadata: <https://plugins.jetbrains.com/docs/intellij/themes-metadata.html>
