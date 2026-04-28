# IntelliJ Informing Users Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/informing-users.html` (updated 2026-03-16)
- `https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html` (updated 2025-10-30)
- `https://plugins.jetbrains.com/docs/intellij/got-it-tooltip.html` (updated 2026-03-16)
- `https://plugins.jetbrains.com/docs/intellij/notification-balloons.html` (updated 2026-03-23)

Related UI-guideline pages linked from the canonical docs:

- `https://plugins.jetbrains.com/docs/intellij/validation-errors.html`
- `https://plugins.jetbrains.com/docs/intellij/banner.html`
- `https://plugins.jetbrains.com/docs/intellij/notification-types.html`

## Fast Routing

Use this matrix before writing code:

| Need | Start from | Why |
| --- | --- | --- |
| Validate dialog input without another modal error | `DialogWrapper.initValidation()` + `doValidate()` | The docs explicitly say not to validate on `OK` with a separate modal dialog |
| Tell the user an editor action cannot run | `HintManager.showErrorHint()` | Best fit for refactorings, navigation, and code insight actions invoked from the editor |
| Ask for an important file-specific action at the top of the editor | `EditorNotificationProvider` + `EditorNotificationPanel` | The editor banner is for actionable setup or configuration issues |
| Explain a new or changed UI feature anchored to a specific control | `GotItTooltip` | Intended for discoverability of important new behavior |
| Show a general project/IDE notification or suggestion | `Notification` + `notificationGroup` | The current balloon API is based on notification groups plus `Notification` instances |

## Informing Users Page: What It Decides

The top-level JetBrains page says to avoid modal message boxes for errors and similar notification cases when a non-modal surface fits.

It routes requests into five main surfaces:

- Dialog validation via `DialogWrapper.doValidate()`
- Editor hints via `HintManager`
- Editor-top banners via `EditorNotificationProvider`
- Feature discovery via `GotItTooltip`
- General notifications via notification balloons

Keep that routing decision in `SKILL.md`; use the rest of this file for API specifics.

## Dialog Validation

Primary API:

- `DialogWrapper`
- `initValidation()`
- `doValidate(): ValidationInfo?`
- `ValidationInfo`

Required `DialogWrapper` setup from the official docs:

1. Call a base constructor with a `Project` or parent component.
2. Call `setTitle()`.
3. Call `init()` from the constructor.
4. Implement `createCenterPanel()`.

Validation-specific behavior:

- Call `initValidation()` in the constructor.
- Override `doValidate()`; the platform calls it automatically via a timer.
- Return `null` when valid.
- Return `ValidationInfo` when invalid.
- When `ValidationInfo` is associated with a component, IntelliJ shows an error icon beside it and focuses it when the user triggers `OK`.

Useful follow-up APIs:

- `show()` to display the dialog
- `showAndGet()` when a boolean result is enough
- `getExitCode()` for modal exit-code inspection
- `doOKAction()` / `doCancelAction()` for custom close-time behavior

Related source links exposed by the docs:

- `DialogWrapper.java`
- `ValidationInfo.java`
- `DialogBuilder.java`

## Editor Hints

Primary API:

- `HintManager`
- `HintManager.showErrorHint()`

Use editor hints when:

- The action started from the editor
- The action cannot be performed right now
- The message should disappear once the user continues editing or triggers another action

JetBrains explicitly calls this the best path for refactorings, navigation actions, and code insight features that need to explain why they cannot proceed.

## Editor Banner

Primary API:

- `EditorNotificationProvider`
- `EditorNotificationPanel`
- extension point `com.intellij.editorNotificationProvider`

Use editor banners when:

- The message belongs to the current file editor
- Ignoring it would impair the workflow
- The user likely needs to fix setup or project configuration, such as a missing SDK

Implementation notes from the docs:

- Register an `EditorNotificationProvider` in `plugin.xml`.
- If the provider does not require indexes, it can be `DumbAware`.
- `EditorNotificationPanel` is the common UI implementation for the banner content.

Minimal registration shape:

```xml
<extensions defaultExtensionNs="com.intellij">
  <editorNotificationProvider implementation="com.example.MyEditorNotificationProvider"/>
</extensions>
```

## Got It Tooltip

Primary API:

- `GotItTooltip`

Construction and display examples shown by JetBrains:

```kotlin
GotItTooltip(TOOLTIP_ID, GOT_IT_TEXT, parentDisposable)
  .show(targetComponent, GotItTooltip.TOP_MIDDLE)
```

Use it when:

- Highlighting an important new feature
- Explaining changed behavior
- Clarifying ambiguous behavior at a specific UI anchor

Do not use it when:

- There is nothing concrete to point at
- The UI already provides immediate feedback
- The change is just a new option in a frequently used list or tree, where JetBrains suggests a badge instead

Behavior and content rules from the docs:

- Show it only once per user by default.
- Show a sequence only when the user explicitly starts a tour.
- Always include body text.
- Add a title only when the body spans two or more lines.
- Keep body text to five lines or fewer.
- Default timeout is 5 seconds; use a custom timeout only when the content is short and easy to absorb.
- Do not tie it to the IDE version when it is triggered by first use of an action or plugin installation.
- The tooltip closes on `Esc`, outside click, or timeout.
- If multiple tooltips are queued, they appear one by one.

## Notification Balloons

Primary pieces:

- `notificationGroup` extension point: `com.intellij.notificationGroup`
- `Notification`
- `NotificationAction`
- `NotificationType`

Current pattern:

1. Declare a notification group in `plugin.xml`.
2. Create a `Notification(...)`.
3. Configure it with builder methods such as `addAction(...)` or `setSuggestionType(true)`.
4. Call `notify(...)` on the notification instance.

Minimal group declaration for a timed balloon:

```xml
<extensions defaultExtensionNs="com.intellij">
  <notificationGroup id="Bagel" displayType="BALLOON"/>
</extensions>
```

Suggestion-specific pattern:

- Use `displayType="STICKY_BALLOON"` on the group.
- Call `.setSuggestionType(true)` on the `Notification`.
- Suggestions stay until explicitly dismissed and show the primary action as a button.

Tool-window-specific pattern:

- Use `displayType="TOOL_WINDOW"`.
- Provide `toolWindowId`.
- The balloon appears next to the relevant tool window icon.

Other configuration notes:

- `BALLOON` is the default timed popup path and typically disappears after 10 seconds.
- `NONE` logs to the Notifications tool window without showing a balloon.
- `isLogByDefault="false"` hides entries from the Notifications tool window.
- The notification-group `id` is human-readable and user-visible in IDE settings, and it can be localized.
- Severity uses `NotificationType.INFORMATION`, `WARNING`, or `ERROR`.
- Notification bodies may contain HTML for presentation.

Minimal Kotlin examples reflected by the docs:

```kotlin
Notification("Bagel", "Bagel was eaten", NotificationType.INFORMATION)
  .addAction(NotificationAction.createSimpleExpiring("Track calories") {
    // handle action
  })
  .notify(project)

Notification("Bagel File", "Bagel file detected", NotificationType.INFORMATION)
  .setSuggestionType(true)
  .notify(project)
```

## Obsolete Notification APIs

The current docs explicitly say these older patterns are obsolete or unnecessarily complex for new code:

- `NotificationGroup` factory methods
- `NotificationGroupManager.getNotificationGroup(...)`
- `Notifications.Bus.notify(...)`
- `NotificationsManager`

Prefer the notification group identifier plus `Notification(...)` and instance-level `notify(...)`.
