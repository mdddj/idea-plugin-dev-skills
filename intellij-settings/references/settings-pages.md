# IntelliJ Settings Pages

## Sources

- `https://plugins.jetbrains.com/docs/intellij/settings.html`
- `https://plugins.jetbrains.com/docs/intellij/settings-guide.html`
- `https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html`
- `https://plugins.jetbrains.com/docs/intellij/settings-groups.html`
- `https://plugins.jetbrains.com/docs/intellij/internal-ui-inspector.html`

## Scope And Extension Point Choice

- Application-wide page: register `com.intellij.applicationConfigurable`.
- Project-wide page: register `com.intellij.projectConfigurable`.
- Conditionally visible page: register a `ConfigurableProvider` instead of a direct `Configurable`.
- If a settings tree has children, prefer XML-declared parent/child relationships over dynamic child computation.

## Important `plugin.xml` Attributes

- `instance`: FQN of the `Configurable` implementation.
- `provider`: FQN of the `ConfigurableProvider` implementation.
- `id`: unique FQN-like identifier. Base it on the plugin ID.
- `parentId`: required placement hook for the settings tree.
- `displayName`: non-localized page title.
- `key` and `bundle`: localized alternative to `displayName`.
- `groupWeight`: sibling ordering. If any sibling uses non-zero weight, ordering is by descending weight and then ascending display name.
- `nonDefaultProject`: project settings only. `true` means hide on the default project.
- `dynamic`: supported for `Configurable.Composite`, but the docs recommend avoiding it because it forces extra class loading.
- `childrenEPName`: declares which EP supplies children for a configurable.

Use either `instance` or `provider`, not both. Use either `displayName` or `key` plus `bundle`.

## Constructor And Lifecycle Rules

- Application configurables must have a no-argument constructor.
- Project configurables must have a constructor that accepts exactly one `Project`.
- Constructor injection other than `Project` is not allowed.
- The platform may instantiate a configurable on a background thread. Do not create Swing UI in the constructor.
- `reset()` is invoked immediately after `createComponent()`.
- The configurable instance survives page switches inside the Settings dialog.
- `disposeUIResources()` is called when the dialog closes.

## Minimal Registration Patterns

Application settings:

```xml
<extensions defaultExtensionNs="com.intellij">
  <applicationConfigurable
      id="com.example.myPlugin.settings"
      parentId="tools"
      instance="com.example.settings.MySettingsConfigurable"
      key="settings.display.name"
      bundle="messages.MyBundle"/>
</extensions>
```

Project settings:

```xml
<extensions defaultExtensionNs="com.intellij">
  <projectConfigurable
      id="com.example.myPlugin.projectSettings"
      parentId="tools"
      instance="com.example.settings.MyProjectSettingsConfigurable"
      nonDefaultProject="true"
      key="settings.project.display.name"
      bundle="messages.MyBundle"/>
</extensions>
```

Conditional settings:

```xml
<extensions defaultExtensionNs="com.intellij">
  <applicationConfigurable
      id="com.example.myPlugin.conditionalSettings"
      parentId="tools"
      provider="com.example.settings.MySettingsConfigurableProvider"
      key="settings.display.name"
      bundle="messages.MyBundle"/>
</extensions>
```

## `parentId` Values Worth Using

- `appearance`: Appearance and Behavior.
- `build`: Build, Execution, Deployment.
- `build.tools`: build-tool subgroup under `build`.
- `editor`: Editor.
- `language`: Languages and Frameworks.
- `tools`: third-party integrations and the safest default for most plugin settings.

Avoid these:

- `root`: internal super-parent. Do not place plugin settings here.
- default fallback with no `parentId`: lands in the old `other` bucket.
- `other`: deprecated. Use `tools` instead.
- `project`: deprecated.

## Parent-Child Settings Trees

Two supported patterns:

1. Separate declarations:

```xml
<projectConfigurable
    id="com.example.tasks"
    parentId="tools"
    instance="com.example.TasksConfigurable"
    displayName="Tasks"
    nonDefaultProject="true"/>

<projectConfigurable
    id="com.example.tasks.servers"
    parentId="com.example.tasks"
    instance="com.example.TaskServersConfigurable"
    displayName="Servers"
    nonDefaultProject="true"/>
```

2. Nested child declaration:

```xml
<projectConfigurable
    id="com.example.tasks"
    parentId="tools"
    instance="com.example.TasksConfigurable"
    displayName="Tasks"
    nonDefaultProject="true">
  <configurable
      id="com.example.tasks.servers"
      instance="com.example.TaskServersConfigurable"
      displayName="Servers"
      nonDefaultProject="true"/>
</projectConfigurable>
```

Use XML relationships unless runtime-only conditions make that impossible.

## Useful `Configurable` Variants

- `SearchableConfigurable`: common subtype when the page should integrate with settings search.
- `Configurable.NoScroll`: suppress outer scrolling when the page already owns scroll panes.
- `Configurable.NoMargin`: suppress the default empty border.
- `Configurable.Beta`: adds a Beta label in the Settings tree.

If the user needs to open a page programmatically, use `ShowSettingsUtil`.

## Tutorial Architecture Pattern

The official tutorial separates responsibilities into three parts:

- `AppSettingsConfigurable`: controller for lifecycle methods like `isModified()`, `apply()`, and `reset()`.
- `AppSettings`: persistent state holder.
- `AppSettingsComponent`: Swing panel or form wrapper.

That split is not mandatory, but it is a good default when the page has enough UI state to justify a dedicated form class.

## Inspecting Existing Settings

Use Internal UI Inspector when you need an existing page's identifiers before choosing `parentId` or nesting under another configurable.

- Enable Internal Mode if `Tools | Internal Actions` is missing.
- In 2024.2+, enable `Internal Actions | UI | Debugging Info in UI`.
- In older IDEs, enable `View | Appearance | Details in Tree View`.
- Open the Settings dialog before invoking UI Inspector.

The inspector can expose:

- configurable class
- configurable `id`
- configurable `groupWeight`
- additional page-specific properties for inspections, file types, intentions, plugins, and other built-in nodes
