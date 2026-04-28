---
name: intellij-embedded-terminal
description: Build or review IntelliJ Platform plugin integrations with the Embedded Terminal and Reworked 2025 terminal API. Use when requests mention embedded terminal, terminal tabs, `TerminalView`, `TerminalToolWindowTabsManager`, terminal output models, shell integration, terminal shortcuts, `org.jetbrains.plugins.terminal.allowedActionsProvider`, or IntelliJ/IDEA plugin work around sending commands, reading terminal output, or reacting to terminal command execution. Prefer Kotlin for plugin-side code.
---

# IntelliJ Embedded Terminal

## Overview

Build against the Reworked 2025 terminal API, not the legacy Classic or deprecated Experimental terminal. Default to Kotlin and verify the target IDE version before using any API from this skill.

## Workflow

1. Confirm the request matches the new terminal surface.
   - Use this skill only when the task targets the Reworked Terminal API.
   - If the task is about `ShellTerminalWidget`, `JBTerminalWidget`, or pre-2025.3 compatibility, check whether the user actually needs the older terminal path before reusing these patterns.

2. Add the terminal plugin dependency before writing code.
   - Declare the bundled plugin dependency `org.jetbrains.plugins.terminal` in Gradle.
   - Declare the same dependency in `plugin.xml`.
   - Treat `NoClassDefFoundError` around terminal classes as a dependency declaration problem first.

3. Choose the narrowest API surface that fits the task.
   - Need a terminal instance or terminal tabs: use `TerminalView` and `TerminalToolWindowTabsManager`.
   - Need to send text: use `TerminalView.sendText()` or the send-text builder when execution or bracketed paste behavior matters.
   - Need output/history inspection: use `TerminalView.outputModels`, `TerminalOutputModel`, and shell-integration-backed block metadata only after confirming the correct buffer.
   - Need terminal-only shortcuts: register the action normally and also whitelist it through `TerminalAllowedActionsProvider`.
   - Need command lifecycle information: wait for `shellIntegrationDeferred`, then use `TerminalShellIntegration`.

4. Respect terminal-specific constraints.
   - The Reworked Terminal is currently available only in the Terminal tool window.
   - Output is split between regular and alternative buffers.
   - Output models are trimmed over time, so offsets are absolute, not stable array indexes.
   - Shell integration is not guaranteed to initialize and is currently limited to Bash, Zsh, and PowerShell.
   - `await()` on shell integration can hang forever on unsupported or misconfigured shells; always add timeout or cancellation.

5. Verify in the actual terminal surface.
   - Check action availability inside terminal focus, not just from menus.
   - Check supported-shell and unsupported-shell behavior separately.
   - Check fullscreen terminal applications against the alternative buffer if output parsing is involved.
   - Re-read current source linked from the official docs when a method signature or builder API looks different from memory.

## References

- Read `references/official-docs.md` first for the version matrix, dependency declarations, API map, and documented caveats.
- Read `references/kotlin-recipes.md` for Kotlin-first snippets and implementation patterns.
