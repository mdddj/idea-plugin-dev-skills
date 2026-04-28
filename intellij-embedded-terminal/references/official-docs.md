# Official Docs Notes

Canonical sources checked on 2026-04-17:

- Embedded Terminal: https://plugins.jetbrains.com/docs/intellij/embedded-terminal.html
- Plugin Dependencies: https://plugins.jetbrains.com/docs/intellij/plugin-dependencies.html

## Version and Scope

- IntelliJ Platform currently exposes three terminal implementations: Classic, Reworked 2025, and deprecated Experimental.
- Reworked Terminal is the default terminal since 2025.2.
- The public Reworked Terminal API is available since 2025.3 and is explicitly marked experimental and under development.
- Use this skill for Reworked Terminal work only. Do not assume Classic terminal APIs map one-to-one.

## Required Dependency Setup

Add the bundled Terminal plugin dependency:

- Plugin ID: `org.jetbrains.plugins.terminal`
- Gradle 2.x: use `bundledPlugin("org.jetbrains.plugins.terminal")`
- `plugin.xml`: add `org.jetbrains.plugins.terminal` as a required dependency

If terminal classes resolve at compile time but fail at runtime, first re-check the `plugin.xml` dependency declaration.

## Core API Map

The public surface is split across packages:

- `com.intellij.terminal.frontend.view.*` for the terminal view and terminal-focused frontend APIs
- `com.intellij.terminal.frontend.toolwindow.*` for tool-window tab management
- `org.jetbrains.plugins.terminal.view.*` for output models, send-text builder, and shell integration

### Terminal Instance and Tabs

- `TerminalView` is the terminal instance abstraction for the Reworked API.
- Reworked Terminal is currently exposed only inside the Terminal tool window.
- `TerminalToolWindowTabsManager.tabs` returns already-open terminal tabs.
- `TerminalToolWindowTabsManager.createTabBuilder()` starts creation of a new terminal tab.
- `TerminalToolWindowTabsManager.getInstance(project)` is the entry point for the service.
- `TerminalToolWindowTabsManager.closeTab(tab)` should be used to terminate a terminal tab instead of cancelling the terminal coroutine scope manually.
- `TerminalView.DATA_KEY` lets actions read the active terminal from `DataContext`.

### Output Models

- `TerminalView.outputModels` exposes the terminal output buffers.
- The terminal keeps two buffers:
  - regular: prompt, commands, and normal command output
  - alternative: fullscreen terminal apps such as `vim`, `nano`, or `mc`
- Both buffers are represented by `TerminalOutputModel` instances grouped in `TerminalOutputModelsSet`.
- `TerminalOutputModelsSet` exposes `regular`, `alternative`, and `active`.
- `TerminalOutputModel` is read-only and represents the visible screen plus retained history.
- Output history is trimmed over time, so navigation uses absolute offsets such as `TerminalOffset` and `TerminalLineIndex`.
- Use `textLength`, `startOffset`, and `endOffset` semantics instead of assuming a stable zero-based buffer.

### Sending Input

- `TerminalView.sendText()` sends bytes to the shell input stream as-is.
- `TerminalView.createSendTextBuilder()` exposes safer send options.
- `TerminalSendTextBuilder.send(text)` performs the actual asynchronous write.
- Prefer `shouldExecute()` instead of manually appending a newline when the intent is command execution.
- Use `useBracketedPasteMode()` when pasted text should not be interpreted as shell key bindings.

### Terminal Actions

- Registering an `AnAction` in `plugin.xml` is not enough for terminal shortcut handling.
- Terminal shortcut handling is filtered by the terminal's allowed-actions list because shell bindings take precedence in many cases.
- To enable an IDE action by shortcut inside terminal focus:
  - implement `TerminalAllowedActionsProvider`
  - register it in the `org.jetbrains.plugins.terminal.allowedActionsProvider` extension point
  - fetch the active terminal in the action via `TerminalView.DATA_KEY`

### Shell Integration

- Terminal injects shell startup scripts to learn prompt boundaries, command ranges, environment details, and shell events.
- APIs that depend on this mechanism live under `TerminalShellIntegration`.
- Access it via `TerminalView.shellIntegrationDeferred`.
- `shellIntegrationDeferred.await()` waits for initialization.
- Shell integration is currently documented only for Bash, Zsh, and PowerShell.
- Initialization is not guaranteed even on supported shells because user shell config and the Terminal settings option can block it.

### Command Structure and Lifecycle

- `TerminalBlocksModel` stores structured ranges for previous and current shell activity.
- The documented block type is `TerminalCommandBlock`.
- A command block spans text from one prompt start until the next prompt and may include prompt text, typed command, and output.
- Not every block necessarily represents a successfully executed shell command.
- To observe execution events, use `TerminalShellIntegration.addCommandExecutionListener()`.
- To observe current execution state, use `TerminalShellIntegration.outputStatus`.

## Guardrails

- Do not guess builder or model accessor signatures when the docs only name the type; inspect the linked source from the docs page first.
- Do not cancel `TerminalView.coroutineScope` manually when the goal is to close a terminal session.
- Do not parse only the regular buffer when the task can run inside fullscreen terminal apps.
- Do not block indefinitely on shell integration.
- Do not treat action registration and terminal shortcut availability as the same thing.
