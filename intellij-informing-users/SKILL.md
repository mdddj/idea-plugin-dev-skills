---
name: intellij-informing-users
description: Build or review IntelliJ Platform plugin user-notification UX using the official JetBrains docs. Use when requests mention informing users, `DialogWrapper.doValidate()`, validation errors, `HintManager`, `showErrorHint()`, `EditorNotificationProvider`, `EditorNotificationPanel`, `GotItTooltip`, notification balloons, notification groups, or non-modal IntelliJ/IDEA plugin notifications. Prefer Kotlin for plugin-side code.
---

# IntelliJ Informing Users

## Overview

Choose the narrowest IntelliJ Platform surface for notifying users without interrupting their flow. Use this skill to route requests between dialog validation, editor hints, editor banners, "Got It" tooltips, and notification balloons, then answer from the current JetBrains docs instead of outdated examples.

## Workflow

1. Classify the request before writing code.
   - Invalid data inside a dialog -> `DialogWrapper.initValidation()` plus `doValidate()` returning `ValidationInfo`
   - Editor action cannot run right now -> `HintManager.showErrorHint()`
   - Important file-specific action or setup issue at the top of the editor -> `EditorNotificationProvider`, usually with `EditorNotificationPanel`
   - Highlighting a new or changed UI behavior anchored to a specific control -> `GotItTooltip`
   - General project or IDE notification, suggestion, or tool-window-bound alert -> `Notification` plus a `notificationGroup`

2. Read [references/official-docs.md](references/official-docs.md) before giving API-level advice.
   It contains the current JetBrains URLs, routing notes, extension point names, and the notification-balloon API changes that replaced older patterns.

3. Prefer non-modal guidance by default.
   Use modal dialogs only when the task really is a dialog workflow. Do not replace inline validation or lightweight notifications with extra message boxes.

4. Apply the current platform APIs.
   For balloons, prefer `Notification(...)`, its builder methods, and `notify(...)`.
   For editor banners, register `com.intellij.editorNotificationProvider`.
   For dialog validation, call `initValidation()` and keep validation in `doValidate()` instead of showing a modal error after the user presses `OK`.

5. Verify the UX behavior matches the chosen surface.
   Check when it disappears, whether it is single-use or sticky, whether it must point to a concrete UI element, and whether it should appear only in the relevant editor or tool window.

## Guardrails

- Do not show a modal error dialog from an `OK` handler when `DialogWrapper.doValidate()` is the intended pattern.
- Do not use an editor banner for a transient "cannot perform action" case that should be an editor hint.
- Do not use `GotItTooltip` for routine feedback, ambiguous UI with no anchor, or repeated nagging.
- Do not use obsolete notification APIs such as `NotificationGroupManager`, `NotificationGroup` factory methods, `Notifications.Bus.notify`, or `NotificationsManager` for new code.
- Do not tie a `GotItTooltip` to the current IDE version when it is triggered by first use of an action or plugin feature.

## References

- [references/official-docs.md](references/official-docs.md)
