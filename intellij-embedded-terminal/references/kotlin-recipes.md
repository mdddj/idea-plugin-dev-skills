# Kotlin Recipes

Use these snippets as starting points. Re-check current source for signatures that are only named, but not fully documented, on the SDK page.

## Declare the Terminal Plugin Dependency

`build.gradle.kts` with IntelliJ Platform Gradle Plugin 2.x:

```kotlin
dependencies {
  intellijPlatform {
    bundledPlugin("org.jetbrains.plugins.terminal")
  }
}
```

`plugin.xml`:

```xml
<idea-plugin>
  <depends>org.jetbrains.plugins.terminal</depends>
</idea-plugin>
```

## Read the Active Terminal in an Action

```kotlin
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.terminal.frontend.view.TerminalView

fun activeTerminal(e: AnActionEvent): TerminalView? {
  return e.getData(TerminalView.DATA_KEY)
}
```

Use this only in contexts where terminal focus is expected. Outside terminal-backed contexts the data key will be absent.

## Access the Tabs Manager

```kotlin
import com.intellij.openapi.project.Project
import com.intellij.terminal.frontend.toolwindow.TerminalToolWindowTabsManager

fun tabsManager(project: Project): TerminalToolWindowTabsManager {
  return TerminalToolWindowTabsManager.getInstance(project)
}
```

## Allow a Shortcut to Work Inside the Terminal

Based on the official docs pattern:

```kotlin
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.terminal.frontend.view.TerminalAllowedActionsProvider
import com.intellij.terminal.frontend.view.TerminalView

class MyTerminalAction : DumbAwareAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val terminal = e.getData(TerminalView.DATA_KEY) ?: return
    // Act on the terminal here.
  }
}

class MyTerminalAllowedActionsProvider : TerminalAllowedActionsProvider {
  override fun getActionIds(): List<String> {
    return listOf("MyPlugin.MyTerminalAction")
  }
}
```

```xml
<actions>
  <action id="MyPlugin.MyTerminalAction" class="com.example.MyTerminalAction">
    <keyboard-shortcut first-keystroke="shift ENTER" keymap="$default"/>
  </action>
</actions>

<extensions defaultExtensionNs="org.jetbrains.plugins.terminal">
  <allowedActionsProvider implementation="com.example.MyTerminalAllowedActionsProvider"/>
</extensions>
```

## Send Text Safely

Minimal direct send:

```kotlin
import com.intellij.terminal.frontend.view.TerminalView

fun sendLiteralText(terminal: TerminalView, text: String) {
  terminal.sendText(text)
}
```

Execute text with the documented builder API:

```kotlin
import com.intellij.terminal.frontend.view.TerminalView

fun executeCommand(terminal: TerminalView, command: String) {
  terminal.createSendTextBuilder()
    .useBracketedPasteMode()
    .shouldExecute()
    .send(command)
}
```

## Wait for Shell Integration Without Hanging Forever

```kotlin
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.withTimeoutOrNull
import com.intellij.terminal.frontend.view.TerminalView

suspend fun awaitShellIntegration(terminal: TerminalView) =
  withTimeoutOrNull(2.seconds) {
    terminal.shellIntegrationDeferred.await()
  }
```

Treat a `null` result as normal. Unsupported shells and custom shell startup configuration can prevent initialization.

## Inspect Output Carefully

Start from:

```kotlin
val regularModel = terminal.outputModels.regular
val alternativeModel = terminal.outputModels.alternative
val activeModel = terminal.outputModels.active.value
```

Then decide which buffer matters:

- regular buffer for prompts, commands, and typical output
- alternative buffer for fullscreen apps

Because trimming occurs, avoid caching raw offsets as if they were local indexes into a permanently growing string.
