---
name: intellij-settings
description: Build or review IntelliJ Platform plugin settings pages and persisted state. Use when requests mention `applicationConfigurable`, `projectConfigurable`, `Configurable`, `ConfigurableProvider`, `SearchableConfigurable`, `PersistentStateComponent`, `SimplePersistentStateComponent`, `SerializablePersistentStateComponent`, `@State`, `@Storage`, `PropertiesComponent`, Settings or Preferences pages, `parentId`, `groupWeight`, Backup and Sync, or split plugin settings sync. Prefer Kotlin for plugin-side code.
---

# IntelliJ Settings

## Overview

Build, review, or refactor IntelliJ Platform plugin settings pages and their persisted state. Separate three concerns: the settings UI, the persistent service, and the `plugin.xml` registration.

## Workflow

1. Classify the request before writing code.
   - IDE-wide settings page: `com.intellij.applicationConfigurable`.
   - Project-scoped settings page: `com.intellij.projectConfigurable`, usually with `nonDefaultProject="true"`.
   - Runtime-conditional page: `ConfigurableProvider`.
   - Multi-level tree: declare parent/child relationships in XML instead of computing children dynamically.

2. Keep persistence out of the configurable.
   - `Configurable` implementations are extensions and should not own persisted state.
   - Put persistent data in an application- or project-level service.
   - If the request is only about a few temporary, non-roamable primitives, use `PropertiesComponent`.
   - If the request includes secrets, tokens, or passwords, use `PasswordSafe`, not `PersistentStateComponent`.

3. Choose the persistence model deliberately.
   - Prefer `SerializablePersistentStateComponent` for immutable Kotlin state and copy-on-write updates.
   - Use `SimplePersistentStateComponent<BaseState>` for small mutable state with delegated properties.
   - Use manual `PersistentStateComponent<T>` only when the built-in base classes do not fit.
   - Split shareable and machine-specific data into separate components when roaming rules differ.

4. Register the settings page correctly.
   - Declare `id`, `parentId`, and display name in `plugin.xml`.
   - Prefer localized `key` and `bundle` over hardcoded `displayName` when localization matters.
   - Application configurables need a no-arg constructor.
   - Project configurables need a single `Project` constructor.
   - Do not use constructor injection other than `Project`.

5. Implement UI around the platform lifecycle.
   - Do not create Swing UI in the configurable constructor.
   - Expect `reset()` to run immediately after `createComponent()`.
   - Implement `isModified()`, `apply()`, `reset()`, and `disposeUIResources()` against the backing service.
   - Prefer Kotlin UI DSL for non-trivial forms even if the official tutorial uses a simple Swing panel.

6. Place the page intentionally.
   - Default third-party plugin settings to `parentId="tools"` unless a built-in group is materially better.
   - Never rely on the deprecated `other` or `project` groups.
   - Use Internal UI Inspector in Internal Mode to confirm existing page `id`, `groupWeight`, and nesting before attaching under built-in settings.

7. Add sync behavior only when the request needs it.
   - For Backup and Sync, pick a custom XML storage file, choose the right `roamingType`, and set a non-`OTHER` `SettingsCategory`.
   - For split plugins, add `RemoteSettingInfoProvider` metadata plus `applicationSettings` or `projectSettings` XML declarations so initial sync runs before the first manual change.
   - If `SimplePersistentStateComponent` is used in split mode, override `noStateLoaded()` to restore defaults when the remote side sends empty state.

## Guardrails

- Do not persist plugin state directly inside a `Configurable` or other extension instance.
- Do not omit `parentId`; the fallback lands in the undesirable default group.
- Do not store secrets in XML settings files.
- Do not make user-path or machine-local values roam unless the request explicitly needs that behavior.
- Do not build settings hierarchies dynamically when static XML can express them.

## References

- Read `references/settings-pages.md` for extension points, `parentId` values, hierarchy rules, lifecycle, and UI Inspector usage.
- Read `references/persistence.md` for `PersistentStateComponent`, storage, roaming, Backup and Sync, `PropertiesComponent`, and `PasswordSafe`.
- Read `references/split-mode.md` only when the plugin targets split frontend/backend mode or Remote Development.
