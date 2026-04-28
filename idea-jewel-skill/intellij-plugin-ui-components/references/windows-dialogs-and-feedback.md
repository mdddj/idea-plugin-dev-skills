# Windows, Dialogs, and Feedback

## Tool Windows

- Use a tool window for persistent functionality, docked panels, or result tabs that users revisit.
- Start declaratively with the `com.intellij.toolWindow` extension and a `ToolWindowFactory`.
- Implement `createToolWindowContent()` to build content lazily only when the user opens the tool window.
- Use `ToolWindowManager.registerToolWindow()` for dynamic, action-driven tool windows.
- Use `ToolWindowManager.invokeLater()` instead of generic `Application.invokeLater()` for tool-window EDT work.
- Manage tabs through `ToolWindow.getContentManager()`, `ContentManager.getFactory().createContent()`, and `ContentManager.addContent()`.
- Set focus and disposal explicitly with `Content.setPreferredFocusableComponent()` and `Content.setDisposer()`.
- Mark the factory dumb-aware when tab creation needs indexes during dumb mode.
- Official page: <https://plugins.jetbrains.com/docs/intellij/tool-windows.html>

## Dialogs

- Use `DialogWrapper` for modal and semi-modal dialogs that collect user input.
- In the constructor, call the base constructor, `setTitle()`, and then `init()`.
- Implement `createCenterPanel()` with the main content.
- Use `showAndGet()` for the common modal flow where only OK or Cancel matters.
- Use `initValidation()` plus `doValidate()` to surface errors inline instead of showing another modal dialog.
- Override `createActions()` or `createLeftActions()` only when the default OK or Cancel model is insufficient.
- Prefer Kotlin UI DSL for dialog content unless Java forms or existing Swing panels are a better fit.
- Official page: <https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html>

## Kotlin UI DSL

- Use Kotlin UI DSL for bound forms, settings pages, and dialog content driven by state objects.
- Start with `panel { row { ... } }`; the builder returns a `DialogPanel`.
- Use `group()`, `collapsibleGroup()`, `buttonsGroup()`, and nested `panel()` blocks to structure forms.
- Use bindings such as `bindSelected()`, `bindText()`, `bindIntText()`, `bindItem()`, and `bindValue()` instead of hand-written sync code.
- Use `visibleIf()` and `enabledIf()` for reactive form state.
- Use `comment()`, `rowComment()`, `label()`, `gap()`, `resizableColumn()`, and `align()` to match platform layout conventions.
- Register `onApply`, `onReset`, and `onIsModified` hooks when the form is not fully represented by direct bindings.
- Do not use Kotlin UI DSL as the default answer for general tool-window chrome; the docs explicitly frame it around dialogs and settings pages.
- Official page: <https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html>

## Popups

- Use popups for lightweight transient UI that should disappear on focus loss.
- Start from `JBPopupFactory`.
- Use `createComponentPopupBuilder()` for arbitrary Swing content.
- Use `createPopupChooserBuilder()` for selecting one or more items from a plain list.
- Use `createActionGroupPopup()` when the choices already exist as `AnAction`s.
- Use `createListPopup()` with `BaseListPopupStep` when the popup needs custom items or nested follow-up popups.
- Prefer sequential numbering for small fixed menus; prefer speed search for large or dynamic sets.
- Use `showInBestPositionFor()` when context positioning is sufficient; otherwise use explicit placement helpers such as `showUnderneathOf()` or `showInCenterOf()`.
- Official page: <https://plugins.jetbrains.com/docs/intellij/popups.html>

## Informing Users

- Prefer non-modal feedback over modal message boxes.
- In dialogs, validate inline through `DialogWrapper.doValidate()` instead of showing another dialog after OK.
- For editor-invoked actions that cannot proceed, use `HintManager.showErrorHint()` or related hint APIs.
- For important editor-top actions such as SDK or configuration prompts, implement `EditorNotificationProvider` and usually render with `EditorNotificationPanel`.
- Use `GotItTooltip` for feature discovery or guided explanation of new UI.
- Route generic non-modal messages to notification balloons rather than `Messages`.
- Official page: <https://plugins.jetbrains.com/docs/intellij/informing-users.html>

## Notification Balloons

- Register a `notificationGroup` in plugin.xml and set an appropriate display type such as `BALLOON`, `STICKY_BALLOON`, `TOOL_WINDOW`, or `NONE`.
- Create notifications with the `Notification` constructor and call `.notify(project)`.
- Use `NotificationType.INFORMATION`, `WARNING`, or `ERROR` to match severity.
- Add actions with `NotificationAction.createSimpleExpiring()` when the call to action is short-lived.
- Use `STICKY_BALLOON` plus `setSuggestionType(true)` when the notification is a suggestion that should stay visible until acted on.
- Use `TOOL_WINDOW` notifications only when the notification is bound to a specific tool window ID.
- Avoid older guidance around `NotificationGroupManager`, `Notifications.Bus`, or `NotificationsManager`; the official page marks those patterns obsolete.
- Official page: <https://plugins.jetbrains.com/docs/intellij/notification-balloons.html>
