# IntelliJ Settings Persistence

## Sources

- `https://plugins.jetbrains.com/docs/intellij/persistence.html`
- `https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html`
- `https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html`
- `https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html`

## Ownership Rule

- Persist settings in an application service or project service.
- Do not make an extension instance itself implement `PersistentStateComponent`.
- If a configurable needs persisted values, let it read and write through the service.

## Which Persistence API To Use

### `SerializablePersistentStateComponent`

Prefer this for modern Kotlin settings when immutable state works well.

- Recommended since 2022.2.
- Uses copy-on-write updates through `updateState()`.
- Tracks modifications automatically.
- Good fit for thread-safe, testable settings services.

Sketch:

```kotlin
@Service
@State(
  name = "com.example.myPlugin.MySettings",
  storages = [Storage("MyPluginSettings.xml")]
)
class MySettings :
  SerializablePersistentStateComponent<MySettings.State>(State()) {

  data class State(
    @JvmField val enabled: Boolean = true,
    @JvmField val endpoint: String = ""
  )

  var enabled: Boolean
    get() = state.enabled
    set(value) = updateState { it.copy(enabled = value) }

  var endpoint: String
    get() = state.endpoint
    set(value) = updateState { it.copy(endpoint = value) }
}
```

### `SimplePersistentStateComponent<BaseState>`

Use this for small mutable state with delegated properties.

- `BaseState` delegates track simple property changes.
- Incremental collection changes may require `incrementModificationCount()`.
- Override `noStateLoaded()` when split-mode sync needs a predictable default reset.

Sketch:

```kotlin
@Service
@State(
  name = "com.example.myPlugin.MySettings",
  storages = [Storage("MyPluginSettings.xml")]
)
class MySettings :
  SimplePersistentStateComponent<MySettings.State>(State()) {

  class State : BaseState() {
    var enabled by property(true)
    var endpoint by string("")
  }

  var enabled: Boolean
    get() = state.enabled
    set(value) { state.enabled = value }
}
```

### Manual `PersistentStateComponent<T>`

Use only when the built-in base classes do not fit. Prefer a separate state class over making the component itself be the state object.

### `PropertiesComponent`

Use only for a handful of simple, non-roamable values.

- Application values: `PropertiesComponent.getInstance()`
- Project values: `PropertiesComponent.getInstance(project)`
- Prefix keys with the plugin ID because the namespace is shared.

## State Class Rules

- The state class must have a default constructor that represents default values.
- Supported persisted types include numbers, booleans, strings, collections, maps, and enums.
- Use `@Transient` to exclude a field or bean property from serialization.
- If a custom type must be serialized, add a `Converter`.
- If `equals()` is not implemented, comparison falls back to field-based comparison.

## Storage Planning

The official docs call out these rules:

- Set `@State.name` explicitly. Use a FQN-style name when values may live in standard project or workspace files.
- Application-level settings should use a custom XML file instead of `other.xml`.
- Project-level plugin settings should prefer a custom project file over generic storage.
- Cached data belongs in `@Storage(StoragePathMacros.CACHE_FILE)`, not the same file as durable settings.
- `reloadable = false` on `@State` means external XML changes require a full reload.

Roaming is controlled by `@Storage.roamingType`:

- `RoamingType.DEFAULT`: share settings
- `RoamingType.PER_OS`: share, but keep values OS-specific
- `RoamingType.DISABLED`: do not share

If multiple components write to the same XML file, they must use the same `roamingType`.

## Backup And Sync / Export Implications

The official docs note that Settings Sync was renamed to Backup and Sync in 2024.3.

For a component to participate in Backup and Sync:

- `@Storage.roamingType` must not be `DISABLED`
- `@State.category` must use a non-`OTHER` `SettingsCategory`
- no component in the same XML file may use a different `roamingType`

Additional guidance:

- `other.xml` is non-roamable and disables roaming for the component
- do not roam user-specific paths or machine-local values
- if shareable and non-shareable data coexist, split them into separate components

## Sensitive Data

Never store secrets in `PersistentStateComponent` XML.

Use `PasswordSafe` with `CredentialAttributes`:

- `PasswordSafe.get()` and `PasswordSafe.set()` are blocking and should not run on the EDT
- since 2025.3, `PasswordSafe.getAsync()` is available for Remote Development contexts
- generate service names consistently with `generateServiceName(subsystem, key)`

Good candidates for `PasswordSafe`:

- API tokens
- passwords
- refresh tokens
- server credentials

Non-secret companion settings such as checkbox flags or base URLs can stay in the regular settings component.

## Migration And Serialization Edge Cases

- Use XML annotations like `@Tag`, `@Attribute`, `@Property`, `@XMap`, and `@XCollection` only when compatibility or a specific XML shape requires them.
- Use `ConverterProvider` and `ProjectConverter` when stored data must be migrated between formats.
- If path macros should not expand in persisted values, register `PathMacroFilter`.

## Practical Review Checklist

- Service scope matches settings scope.
- Settings UI reads from and writes to the service, not a duplicate cache.
- Storage file and roaming policy match the data semantics.
- Secrets are separated into `PasswordSafe`.
- Collections in `BaseState` are not mutated silently without modification tracking.
