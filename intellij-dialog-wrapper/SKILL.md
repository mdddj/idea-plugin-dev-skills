---
name: intellij-dialog-wrapper
description: Build or review IntelliJ Platform plugin dialogs with DialogWrapper. Use when requests mention IntelliJ or IDEA plugin dialogs, DialogWrapper, `createCenterPanel()`, `showAndGet()`, `getExitCode()`, `doValidate()`, `ValidationInfo`, `createActions()`, `createLeftActions()`, `getPreferredFocusedComponent()`, `getDimensionServiceKey()`, or modal and semi-modal plugin forms. Prefer Kotlin for plugin-side code.
---

# IntelliJ Dialog Wrapper

## Overview

Use this skill to route IntelliJ Platform dialog work through `DialogWrapper` only when that UI surface is correct, then implement the documented lifecycle without re-reading the whole JetBrains page. Keep answers focused on dialog selection, shell setup, content hosting, validation, button behavior, and result handling.

## Workflow

1. Confirm `DialogWrapper` is the right UI surface before writing code.
   Use it for modal or semi-modal dialogs that gather input and return a result.
   If the request is about a transient chooser or context menu, switch to popup guidance.
   If the request is about persistent docked UI, switch to tool window guidance.
   If the request is about application settings, route to `Configurable`; a dialog may host similar form content, but it is not the settings entry point.

2. Build the dialog shell in the documented order.
   Subclass `DialogWrapper`.
   Call the appropriate base constructor already used by the project, then set the title and call `init()` from the constructor or `init` block.
   Implement `createCenterPanel()` for the main content.
   Override `getPreferredFocusedComponent()`, `getDimensionServiceKey()`, or `getHelpId()` only when the UX explicitly needs initial focus, persisted size and position, or context help.

3. Choose the content host based on the existing codebase.
   Prefer Kotlin UI DSL for new Kotlin dialogs and return the resulting `DialogPanel` from `createCenterPanel()`.
   Use GUI Designer bindings or an existing Swing `JPanel` when the plugin is already built that way.
   Keep form layout inside the panel instead of trying to reimplement the dialog chrome.

4. Match the display flow to the result model.
   Use `showAndGet()` when the caller only needs OK versus Cancel.
   Use `show()` plus `getExitCode()` when multiple outcomes matter.
   Keep close-time side effects in `doOKAction()`, `doCancelAction()`, or explicit action handlers instead of scattering them around the caller.

5. Add validation and custom actions only when the default behavior is not enough.
   Call `initValidation()` when input can become invalid while the dialog is open.
   Implement `doValidate()` and return `ValidationInfo` for inline feedback; return `null` when valid.
   Override `createActions()` or `createLeftActions()` only when the stock button set is insufficient.
   When a custom action should close the dialog, base it on `DialogWrapperExitAction`.
   Mark custom default or focused buttons through `DialogWrapper.DEFAULT_ACTION` and `DialogWrapper.FOCUSED_ACTION`.

## Guardrails

- Do not use `DialogWrapper` for popups, notifications, or persistent tool windows.
- Do not forget `init()` after configuring the dialog shell.
- Do not replace inline `doValidate()` feedback with another modal error dialog unless the flow truly requires it.
- Do not customize button sets before checking whether the standard OK, Cancel, and Help behavior already fits.
- Do not encode rich dialog outcomes in ad hoc booleans when `getExitCode()` is the clearer API.

## References

- Read `references/official-docs.md` for the live-checked JetBrains documentation summary, API checklist, and a minimal Kotlin dialog skeleton.
