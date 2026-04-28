# IntelliJ Status Bar Widgets Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/status-bar-widgets.html` (page last modified 2025-04-16)
- `https://plugins.jetbrains.com/docs/intellij/popups.html` for popup creation used by `EditorBasedStatusBarPopup.createPopup()`

Source files checked on 2026-04-17 from JetBrains `intellij-community`:

- `StatusBarWidgetFactory`
- `StatusBarEditorBasedWidgetFactory`
- `StatusBarWidget`
- `CustomStatusBarWidget`
- `EditorBasedWidget`
- `EditorBasedStatusBarPopup`
- `LightEditCompatible`

## Scope

Use this guidance when the user asks to add, debug, review, or register an IntelliJ status bar widget in a plugin. The docs frame status bar widgets as small, prominent UI elements for information or settings relevant to the current file, project, or IDE. Because space is limited and visibility is high, the bar is not a good dumping ground for low-value status text.

## Registration

The documented entry point is `StatusBarWidgetFactory`, registered in the `com.intellij.statusBarWidgetFactory` extension point.

Important rule from both docs and source:

- The XML `id` attribute is required.
- The XML `id` must match `StatusBarWidgetFactory.getId()`.
- That identifier is also used for persisted visibility settings.

Minimal registration pattern:

```xml
<extensions defaultExtensionNs="com.intellij">
  <statusBarWidgetFactory
      id="myWidget"
      implementation="com.example.MyStatusBarWidgetFactory"/>
</extensions>
```

Placement can be controlled with the usual `order` attribute when the widget must appear before or after known widgets.

Example from JetBrains sources:

```xml
<statusBarWidgetFactory id="hg"
                        implementation="org.zmlx.hg4idea.status.ui.HgStatusWidget$Factory"
                        order="after CodeStyleStatusBarWidget,before ReadOnlyAttribute"/>
```

## Factory API

`StatusBarWidgetFactory` is more than `createWidget()`:

- `getId()`
  Stable widget identifier. Must match XML `id`.
- `getDisplayName()`
  Human-facing label used in enable or disable UI.
- `isAvailable(project)`
  Controls whether the IDE should create or keep the widget for that project.
- `createWidget(project, scope)`
  Current overload. The legacy `createWidget(project)` still exists as a fallback.
- `disposeWidget(widget)`
  Defaults to `Disposer.dispose(widget)`.
- `canBeEnabledOn(statusBar)`
  Controls whether the widget can be enabled for a specific status bar instance.
- `isEnabledByDefault()`
  Defaults to `true`.
- `isConfigurable()`
  Controls whether the user can enable or disable it from the UI.

Source-backed lifecycle caveat:

- The platform does not automatically recreate a widget just because availability or visibility changed later.
- When those conditions change, explicitly call the status bar widgets manager update path.

## Choosing The Base Implementation

### Plain `StatusBarWidget`

Use this when one of the stock presentations is enough:

- `StatusBarWidget.IconPresentation`
  Icon only.
- `StatusBarWidget.TextPresentation`
  Text only.
- `StatusBarWidget.MultipleTextValuesPresentation`
  Text plus popup.

JetBrains docs explicitly note that these presentations do not combine. If the user wants icon plus text or more complex layout, do not try to stack stock presentation interfaces.

Examples linked from the docs:

- `PowerSaveStatusWidgetFactory` for icon-only presentation
- `DvcsStatusWidget` for text plus popup

### `CustomStatusBarWidget`

Use this only when the stock presentations are too limited. Implement `getComponent()` and return the custom Swing component to display.

Important source caveat:

- Widgets can be instantiated on a background thread.
- Do not build Swing components in constructors or eager field initializers.
- Lazy-create the component in `getComponent()` or a lazy property.

Example linked from the docs:

- `MemoryUsagePanel`

### `StatusBarEditorBasedWidgetFactory`

Use this factory base when the widget makes sense only with a text editor in the frame attached to the status bar. Its `canBeEnabledOn(statusBar)` implementation already checks for an editor, so it is a good fit for file-oriented widgets.

### `EditorBasedWidget`

Use this for editor-aware widgets that do not need the higher-level popup helper. It gives access to:

- focused editor lookup
- selected file lookup
- status bar install lifecycle
- message bus connection via `myConnection`

Register listeners after initialization through `registerCustomListeners(connection)` rather than relying on the deprecated no-arg variant.

### `EditorBasedStatusBarPopup`

Use this when the widget should show a popup with a list of actions, such as encoding or line-ending selectors.

Key methods and behavior:

- `createComponent()`
  Creates the visible Swing component. Default is a text panel with arrows.
- `getWidgetState(file)`
  Computes the current state from the selected file.
- `updateComponent(state)`
  Applies the state to the component.
- `createPopup(context)`
  Returns the popup shown on click.
- `createInstance(project)`
  Returns a fresh widget instance for another frame.
- `update()`
  Schedules a debounced refresh.
- `registerCustomListeners(connection)`
  Subscribe to project events that should trigger `update()`.

Useful built-in states:

- `WidgetState.HIDDEN`
  Hide the widget.
- `WidgetState.NO_CHANGE`
  Leave presentation unchanged.
- `WidgetState.NO_CHANGE_MAKE_VISIBLE`
  Show the widget without changing its content.
- `WidgetState.getDumbModeState(...)`
  Standard disabled state for indexing or dumb mode.

If the widget can return `HIDDEN`, keep that logic aligned with `canBeEnabledOn(statusBar)` so the enable toggle does not expose a widget that still renders nothing.

## Threading And Update Rules

Important source-derived constraints:

- `StatusBarWidget` instances may be created on a background thread.
- Avoid Swing work in constructors.
- Use lazy component creation.
- `EditorBasedStatusBarPopup.update()` is debounced internally, so it is the normal refresh entry point for that base class.
- Simpler widgets often update by calling `statusBar.updateWidget(ID())`.

Practical rule:

- Use factory methods for creation and availability.
- Use widget methods for presentation refresh.
- Use manager-level refresh when availability or persisted visibility changed and the widget may need recreation.

## LightEdit

By default, widgets do not appear in LightEdit mode. To allow them there, implement `LightEditCompatible` on the factory. It is only a marker interface, so do not search for extra methods on it.

## Programmatic Access

The docs show the standard lookup pattern:

```kotlin
val widget = WindowManager.getInstance().getStatusBar(project)
    .getWidget(MyWidget.ID) as MyWidget
```

This is useful when an action, listener, or service needs to poke an already-installed widget instance.

## Kotlin Skeletons

Minimal factory shell:

```kotlin
class MyWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "myWidget"

    override fun getDisplayName(): String = "My Widget"

    override fun createWidget(project: Project, scope: CoroutineScope): StatusBarWidget {
        return MyWidget(project)
    }
}
```

Minimal custom widget shell:

```kotlin
class MyWidget(private val project: Project) : CustomStatusBarWidget {
    private val component by lazy {
        JLabel("Ready")
    }

    override fun ID(): String = "myWidget"

    override fun getPresentation(): StatusBarWidget.WidgetPresentation? = null

    override fun getComponent(): JComponent = component
}
```

For popup-based editor widgets, prefer subclassing `EditorBasedStatusBarPopup` instead of reimplementing click handling and state management yourself.
