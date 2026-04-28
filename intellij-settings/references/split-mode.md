# IntelliJ Settings In Split Mode

## Sources

- `https://plugins.jetbrains.com/docs/intellij/persistent-state-in-split-mode.html`
- `https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html`
- `https://plugins.jetbrains.com/docs/intellij/plugin-management-in-split-mode.html`

## When To Read This

Read this file only when the plugin targets split frontend/backend mode or Remote Development. Ordinary local-only plugins can ignore it.

## Required Pieces

The official split-mode guidance requires four pieces:

1. a normal `PersistentStateComponent`
2. a `RemoteSettingInfoProvider`
3. `applicationSettings` or `projectSettings` XML declarations
4. an explicit sync direction

Without the XML declaration, initial synchronization does not start until the user changes the setting manually for the first time.

## Persistent State Component Notes

Start with a regular settings service.

If using `SimplePersistentStateComponent`, override `noStateLoaded()` so an empty state from the remote side resets cleanly to defaults:

```kotlin
@State(
  name = "com.example.myPlugin.MySettings",
  storages = [Storage("MyPluginSettings.xml")]
)
class MySettings : SimplePersistentStateComponent<MySettings.State>(State()) {

  class State : BaseState() {
    var enabled by property(true)
  }

  override fun noStateLoaded() {
    loadState(State())
  }
}
```

## Register `RemoteSettingInfoProvider`

Implement the provider in shared code and register it on both sides that need the sync metadata.

Sketch:

```kotlin
class MySettingsRemoteInfoProvider : RemoteSettingInfoProvider {
  override fun getRemoteSettingsInfo() = mapOf(
    "com.example.myPlugin.MySettings" to
      RemoteSettingInfo(direction = Direction.InitialFromFrontend)
  )
}
```

Registration:

```xml
<extensions defaultExtensionNs="com.intellij">
  <rdct.remoteSettingProvider
      implementation="com.example.settings.MySettingsRemoteInfoProvider"/>
</extensions>
```

## Declare The Settings Service For Initial Sync

Application-level settings:

```xml
<applicationSettings service="com.example.settings.MySettings"/>
```

Project-level settings:

```xml
<projectSettings service="com.example.settings.MySettings"/>
```

These declarations are separate from `applicationConfigurable` and `projectConfigurable`. They exist so the synchronization layer sees the state component immediately.

## Choosing Direction

The split-mode guide gives this default mapping:

- `InitialFromFrontend`: default for application-level settings
- `InitialFromBackend`: default for project-level settings
- `OnlyFromFrontend`: frontend exclusively owns the setting
- `OnlyFromBackend`: backend exclusively owns the setting

Choose the direction by ownership semantics, not by where the configurable UI lives.

## Installation Limitation To Remember

The current plugin synchronization flow in split mode works only for plugins that can be resolved through JetBrains Marketplace metadata. A plugin installed only from a local ZIP or JAR does not participate in that cross-side plugin sync flow automatically.

That limitation is separate from settings synchronization, but it matters during testing because both plugin deployment and setting sync are easy to conflate.

## Split-Mode Review Checklist

- The settings service still works as a normal persistent component without split mode.
- `RemoteSettingInfoProvider` is registered where the platform can load it.
- `applicationSettings` or `projectSettings` is declared for initial sync.
- Sync direction matches application-level vs project-level ownership.
- Empty remote state resets safely instead of leaving stale in-memory values.
