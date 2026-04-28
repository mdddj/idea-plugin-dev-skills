# IntelliJ DialogWrapper Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html` (updated 2025-10-30)

Related official pages worth routing to when the request is adjacent rather than truly dialog-specific:

- `https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html`
- `https://plugins.jetbrains.com/docs/intellij/popups.html`
- `https://plugins.jetbrains.com/docs/intellij/tool-windows.html`
- `https://plugins.jetbrains.com/docs/intellij/settings.html`

## Scope

The DialogWrapper page is the primary reference for IntelliJ Platform modal and semi-modal dialogs. JetBrains positions `DialogWrapper` as the standard wrapper that supplies consistent buttons, keyboard handling, validation plumbing, help integration, and size persistence so plugin authors can focus on the center panel.

## When To Use It

Route to `DialogWrapper` when the request sounds like:

- "Make a modal plugin dialog."
- "Collect a few inputs and confirm or cancel."
- "I need OK or Cancel plus inline validation."
- "I need a custom IntelliJ dialog instead of raw Swing `JDialog`."

Do not force the request into `DialogWrapper` when it is actually:

- A popup or quick chooser: use `JBPopupFactory`.
- A persistent sidebar or results surface: use a tool window.
- A settings page under Preferences or Settings: use `Configurable`.

## Required Lifecycle

Use this order unless the existing codebase already has a narrowly scoped variant:

1. Subclass `DialogWrapper`.
2. Call the parent constructor with the owning `Project`, parent component, or parent-policy variant that matches the surrounding code.
3. Set dialog metadata such as title.
4. Call `init()`.
5. Implement `createCenterPanel()` and return the main panel.
6. Show the dialog with `showAndGet()` or `show()`.

The docs explicitly treat `init()` as part of the standard constructor flow. Missing it is a real implementation bug, not a style preference.

## Content Options

The docs show three viable content paths:

- Kotlin UI DSL
  Preferred for new Kotlin dialogs. Return the generated `DialogPanel` from `createCenterPanel()`.
- GUI Designer bound form
  Useful in older Java-heavy plugins that already use form classes and generated panels.
- Hand-written Swing panel
  Fine when the dialog content is small or already exists.

The wrapper owns the dialog shell. Put layout, fields, and bindings in the returned panel rather than reworking the window frame.

## Result Handling

- `showAndGet()`
  Best when the caller only needs a boolean success or cancellation result.
- `show()` plus `getExitCode()`
  Better when the dialog has more than one meaningful exit path.
- `doOKAction()` and `doCancelAction()`
  Use when close-time work belongs inside the dialog class.

Prefer the narrowest result API that matches the flow. If there are multiple terminal actions, `getExitCode()` is clearer than homemade flags.

## Validation

The JetBrains page documents inline validation as the normal path:

- Call `initValidation()` when inputs can become invalid while the dialog remains open.
- Override `doValidate()`.
- Return `ValidationInfo` pointing at the component that needs attention.
- Return `null` when the dialog state is valid.

This keeps errors in the dialog itself. Avoid following OK with a second error dialog unless the failure comes from a later operation outside the editable form.

## Buttons And Actions

The default button model already covers common dialogs:

- OK
- Cancel
- optional Help

Only customize when the default set is insufficient.

- Override `createActions()` for a custom right-side action list.
- Override `createLeftActions()` for extra left-side actions.
- Use `DialogWrapperExitAction` when a custom action should close the dialog with a defined exit code.
- Mark preferred buttons with `DialogWrapper.DEFAULT_ACTION` and `DialogWrapper.FOCUSED_ACTION`.

The docs also mention button-text customization methods such as `setOKButtonText()` when the standard label is not clear enough.

## Optional Integrations

Use these only when the UX calls for them:

- `getPreferredFocusedComponent()`
  Set initial focus.
- `getDimensionServiceKey()`
  Persist size and location across openings.
- `getHelpId()`
  Wire the Help action to IntelliJ help content.
- Do-not-ask-again support
  Available when the dialog really represents a repeatable confirmation flow.

These are good refinements, but they are not the first thing to add in a minimal dialog implementation.

## Minimal Kotlin Skeleton

```kotlin
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class SampleDialog(project: Project) : DialogWrapper(project) {
    private val ui = panel {
        row("Name:") {
            textField()
        }
    }

    init {
        title = "Sample Dialog"
        init()
        initValidation()
    }

    override fun createCenterPanel(): JComponent = ui

    override fun getPreferredFocusedComponent(): JComponent? =
        ui.preferredFocusedComponent

    override fun doValidate(): ValidationInfo? {
        return null
    }
}
```

Typical call site:

```kotlin
if (SampleDialog(project).showAndGet()) {
    // read dialog state and continue
}
```

## Practical Routing Rules

- If the user asks for a small data-entry form with OK and Cancel, start with `DialogWrapper`.
- If they already have a `DialogPanel`, host it inside `createCenterPanel()` instead of rebuilding the dialog class around raw Swing widgets.
- If they ask for validation, steer toward `initValidation()` plus `doValidate()`.
- If they ask for unusual buttons or multiple exit paths, inspect `createActions()`, `createLeftActions()`, and `getExitCode()` before inventing a custom window pattern.
- If they ask for one-click transient choices instead of a form, stop and route to popup guidance.
