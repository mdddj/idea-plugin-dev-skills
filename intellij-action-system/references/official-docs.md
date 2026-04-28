# Official Docs

- Canonical source: [Action System](https://plugins.jetbrains.com/docs/intellij/action-system.html)
- Checked against the live JetBrains documentation on 2026-04-17.
- The page footer showed an update date of 2026-03-16 when this skill was created.

## What This Skill Should Remember

- Actions are UI contributions backed by code plus registration.
- The core implementation type is `AnAction`.
- Dumb-mode-safe actions should subclass `DumbAwareAction`; do not emulate this by overriding `isDumbAware()`.
- Action instances are long-lived. Do not store short-lived context in fields.
- `update()` controls availability through `Presentation`.
- `getActionUpdateThread()` decides whether `update()` runs on BGT or EDT.
- `actionPerformed()` is where the real work happens.

## Threading Rules

- Prefer `ActionUpdateThread.BGT` for `update()` when PSI, VFS, or project models are needed.
- BGT `update()` code must not inspect Swing hierarchy directly.
- EDT `update()` code may inspect UI state but must not read PSI, VFS, or project data directly.
- If a BGT update needs a small UI-only computation, use `AnActionEvent.getUpdateSession().compute()` to run that fragment on EDT.

## Context And Presentation

- Use `AnActionEvent.getData()` with keys such as `CommonDataKeys.PROJECT`, `EDITOR`, and `PSI_FILE` for lightweight context reads.
- Each place where an action appears gets its own `Presentation`, copied from the template presentation.
- `Presentation.setEnabled()` and `Presentation.setVisible()` drive availability and visibility.
- Toolbar actions may need `ActivityTracker.getInstance().inc()` when state changes without focus or user activity.

## Registration Surface

- Static registration normally belongs in `plugin.xml`.
- Runtime registration requires both:
  - `ActionManager.registerAction()` to bind an ID to an action instance.
  - Adding the action to one or more groups, usually by fetching a `DefaultActionGroup` via `ActionManager.getAction()`.
- For runtime-created actions that should still appear in Settings | Keymap, reserve the ID with `EmptyAction` in `plugin.xml`.

## `plugin.xml` Features Worth Remembering

- `<actions>` may declare a dedicated `resource-bundle`.
- `<action>` supports `id`, `class`, `text`, `description`, `icon`, child `<override-text>`, `<synonym>`, `<add-to-group>`, `<keyboard-shortcut>`, and `<mouse-shortcut>`.
- `<group>` supports grouping, nested actions, `popup`, `compact`, `searchable`, `<separator>`, `<reference>`, and `<add-to-group>`.
- Use `relative-to-action` plus `anchor` when placing actions next to existing platform actions.
- Find valid places through `ActionPlaces`, `PlatformActions.xml`, code insight, or UI Inspector.

## Localization Keys

- Declare the bundle with `<resource-bundle>messages.MyBundle</resource-bundle>` in `plugin.xml`, or set `resource-bundle` on `<actions>`.
- When localized, do not hard-code `text` and `description` in `plugin.xml`.
- Action keys:
  - `action.<action-id>.text`
  - `action.<action-id>.description`
  - `action.<action-id>.<place>.text` for `<override-text>`
- Group keys:
  - `group.<group-id>.text`
  - `group.<group-id>.description`
  - `group.<group-id>.<place>.text` for `<override-text>`

## Toolbars And Popups

- Build plugin-owned UI action surfaces with `ActionManager.createActionToolbar()` and `createActionPopupMenu()`.
- Use `ActionToolbar.setTargetComponent()` when the toolbar belongs to a specific panel or tool window.
- To expose a group under Settings | Appearance & Behavior | Menus and Toolbars, implement `CustomizableActionGroupProvider` and register `com.intellij.customizableActionGroupProvider`.

## Useful Base Classes And Utilities

- `ToggleAction` / `DumbAwareToggleAction` for checked or pressed state.
- `BackAction` / `ForwardAction` for history navigation based on `History.KEY`.
- `ActionUtils.invokeAction()` only when programmatic execution is unavoidable and the action is outside your control.

## Action ID Code Insight

- Built-in Action ID references already work in places such as:
  - `ActionManager.getAction()`
  - `CodeInsightTestFixture.performEditorAction()`
  - string literal fields named `ACTION_ID`
  - constants in `IdeActions`
- For custom code paths, annotate string sites with `@Language("devkit-action-id")`.
- For other file types or config surfaces, use IntelliLang's Action ID reference injection.
