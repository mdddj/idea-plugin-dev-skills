# IntelliJ Tool Windows Official Docs

## Canonical sources

- Tool windows implementation guide: <https://plugins.jetbrains.com/docs/intellij/tool-windows.html>
- Tool window UI guidelines: <https://plugins.jetbrains.com/docs/intellij/tool-window.html>
- `ToolWindowFactory` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/platform-api/src/com/intellij/openapi/wm/ToolWindowFactory.kt>
- `ToolWindowManager` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/platform-api/src/com/intellij/openapi/wm/ToolWindowManager.kt>
- `RegisterToolWindowTask` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/platform-api/src/com/intellij/openapi/wm/RegisterToolWindowTask.kt>
- JetBrains sample plugin: <https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/tool_window>
- Sample `plugin.xml`: <https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/tool_window/src/main/resources/META-INF/plugin.xml>
- Sample factory: <https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/tool_window/src/main/java/org/intellij/sdk/toolWindow/CalendarToolWindowFactory.java>

## Fast routing

Use this API set when the request is about:

- A persistent docked side or bottom panel in the IDE.
- A plugin-owned panel that can host one or more tabs.
- Lazy initialization of plugin UI only after the user opens it.
- Dynamically opening a results panel for a specific action.
- Stripe title localization, tool window availability, or closable tabs.

Do not route here when the request is really about:

- Short-lived prompts or confirmations: use `DialogWrapper` or a popup.
- Project View customization: use the Project View APIs instead.
- Compose/Jewel specifics inside a tool window: combine this skill with the relevant Compose/Jewel guidance.

## Declarative registration

Register stable tool windows in `plugin.xml`:

```xml
<extensions defaultExtensionNs="com.intellij">
  <toolWindow
    id="My Results"
    factoryClass="com.example.MyToolWindowFactory"
    anchor="right"
    secondary="false"
    icon="/icons/myToolWindow.svg"
    canCloseContents="true" />
</extensions>
```

Attributes that matter:

- `id`: required. Also used by `ToolWindowManager.getToolWindow(id)`.
- `factoryClass`: required. Must implement `ToolWindowFactory`.
- `icon`: stripe button icon.
- `anchor`: `left` default, `right`, or `bottom`.
- `secondary`: chooses the secondary stripe group on that side.
- `canCloseContents`: enables user-closing of tabs. Default is `false`.

Localization detail from the current `ToolWindowFactory` source:

- Add `toolwindow.stripe.<id-with-spaces-replaced-by-underscores>` to the plugin resource bundle when localizing the stripe title.

## `ToolWindowFactory` API map

Current platform source exposes these methods and properties:

- `suspend fun isApplicableAsync(project: Project): Boolean`
  - Preferred Kotlin entry point for one-time applicability checks during project load.
- `fun isApplicable(project: Project): Boolean`
  - Deprecated in favor of `isApplicableAsync`, but still used from Java.
- `fun createToolWindowContent(project: Project, toolWindow: ToolWindow)`
  - Build the UI lazily after the user opens the tool window.
- `fun init(toolWindow: ToolWindow)`
  - Optional extra initialization hook.
- `suspend fun manage(toolWindow: ToolWindow, toolWindowManager: ToolWindowManager)`
  - Experimental lifecycle hook in current source.
- `fun shouldBeAvailable(project: Project): Boolean`
  - Initial visibility only. For later changes, call `toolWindow.setAvailable(...)`.
- `val anchor: ToolWindowAnchor?`
  - Experimental override of the configured anchor.
- `val icon: Icon?`
  - Experimental override of the configured icon.

Important lifecycle rules:

- Applicability is evaluated once when the project loads.
- Startup remains cheap because plugin code is not loaded until the user opens the tool window.
- If the window should appear or disappear while the project is already open, use dynamic registration or `ToolWindow.setAvailable(...)` instead of expecting `isApplicable*()` to rerun.

Minimal Kotlin shape:

```kotlin
class MyToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout()).apply {
            add(JLabel("Hello from tool window"), BorderLayout.CENTER)
        }

        val content = ContentFactory.getInstance().createContent(panel, "", false)
        content.setPreferredFocusableComponent(panel)
        toolWindow.contentManager.addContent(content)
    }
}
```

## Content and tabs

Per the docs:

- Use `toolWindow.contentManager` to manage tabs.
- Create a tab with `ContentManager.getFactory().createContent(...)` or the equivalent `ContentFactory` helper used in JetBrains samples.
- Call `Content.setPreferredFocusableComponent(...)` for focus behavior.
- Call `Content.setDisposer(...)` to bind cleanup to tab disposal.
- `SimpleToolWindowPanel` is a convenient base when you need a toolbar plus content area.

Close behavior rules:

- `canCloseContents` defaults to `false`.
- `Content.setCloseable(true)` is ignored unless closeable contents are enabled for the tool window.
- Once global closing is enabled, you can selectively keep individual tabs fixed with `Content.setCloseable(false)`.

## Programmatic registration

Use programmatic registration only for tool windows created by a specific operation, such as search results or a one-off analysis run.

Current APIs from `ToolWindowManager` and `RegisterToolWindowTask`:

- `ToolWindowManager.registerToolWindow(task: RegisterToolWindowTask)`
- `ToolWindowManager.registerToolWindow(id: String) { ... }`
- `RegisterToolWindowTask.lazyAndClosable(...)`
- `RegisterToolWindowTask.closable(...)`
- `RegisterToolWindowTask.notClosable(...)`

Builder fields that matter:

- `anchor`
- `stripeTitle`
- `icon`
- `shouldBeAvailable`
- `canCloseContent`
- `hideOnEmptyContent`
- `sideTool`
- `contentFactory`

Minimal Kotlin example:

```kotlin
val toolWindow = ToolWindowManager.getInstance(project).registerToolWindow("My Results") {
    anchor = ToolWindowAnchor.BOTTOM
    icon = MyIcons.ToolWindow
    stripeTitle = Supplier { "My Results" }
    canCloseContent = true
    contentFactory = MyToolWindowFactory()
}
```

Threading rule from the docs:

- Use `ToolWindowManager.invokeLater()` instead of plain `Application.invokeLater()` for EDT work related to tool windows.

## Visibility and indexing

- Tool windows are disabled during indexing by default when they require indexes.
- Mark the factory dumb-aware only if its content and actions are safe in dumb mode.
- `shouldBeAvailable(project)` controls initial availability.
- `toolWindow.setAvailable(...)` controls later state changes while the project is already open.
- `toolWindow.hide(null)` is the standard way to hide an open window from its own UI.

## Notifications and events

- Access a registered tool window with `ToolWindowManager.getToolWindow(id)`.
- Show balloon notifications with `ToolWindowManager.notifyByBalloon(...)` or a notification registered for a specific tool window.
- Listen for registration and show/hide changes through the project-level `ToolWindowManagerListener` topic.

## UI guidance that affects implementation

From the official UI guidelines:

- Tool window names should be short, descriptive, and preferably no more than two words.
- Use title case.
- Provide an abbreviation when it improves stripe readability.
- Supply monochrome grey icons in 20x20 and 16x16 sizes.
- Use tabs for similar parallel entities such as results, sessions, or history views.
- Prefer vertical orientation for trees and horizontal orientation for tables or wide master-detail content.
- If the window may start empty, design an empty state instead of a blank panel.
- Hide the stripe button by default if the tool window is irrelevant to the current project setup.

## Sample plugin notes

The official sample shows the expected baseline:

- `plugin.xml` registers `com.intellij.toolWindow` with `id`, `secondary`, `icon`, `anchor`, and `factoryClass`.
- The factory builds a simple panel lazily in `createToolWindowContent(...)`.
- A `Hide` button calls `toolWindow.hide(null)`.

Use the sample as a structure reference, not as a UI quality benchmark.

## Testing checklist

- The registered `id` is stable and matches any later `getToolWindow(id)` lookup.
- The factory is not doing heavy work before `createToolWindowContent(...)`.
- Applicability logic is safe to run only once.
- The tool window is dumb-aware only when safe.
- `canCloseContents` and per-tab closability behave as intended.
- Dynamic registration is used only for action-scoped windows.
- Tool-window-related EDT scheduling uses `ToolWindowManager.invokeLater()`.
- The stripe title localization key exists when a localized label is required.
