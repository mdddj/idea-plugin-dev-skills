---
name: intellij-inlay-hints
description: Build or review IntelliJ Platform plugin inlay hints. Use when requests mention `InlayParameterHintsProvider`, declarative `InlayHintsProvider`, `CodeVisionProvider`, `DaemonBoundCodeVisionProvider`, `CodeVisionGroupSettingProvider`, `InlayHintsCustomSettingsProvider`, `InlayHintsProviderFactory`, `com.intellij.codeInsight.*inlay*` extension points, preview files under `inlayProviders/` or `codeVisionProviders/`, or debugging editor inlays in JetBrains plugins. Prefer Kotlin for plugin-side code.
---

# IntelliJ Inlay Hints

## Overview

Build, review, or debug IntelliJ Platform plugin inlay hints. Choose the narrowest API that matches the hint style and target IDE baseline; prefer declarative inline hints on `2023.1+` and code vision for block hints on `2022.1+`.

## Workflow

1. Classify the requested hint before writing code.
   - Simple parameter-name strings before arguments: use `InlayParameterHintsProvider`.
   - Inline textual hints with clickable items and limited UI customization: use declarative `InlayHintsProvider`.
   - Block hints for classes, methods, fields, or metrics: use code vision.
   - Fully custom inline or block rendering, advanced behavior, or existing legacy code: use `InlayHintsProvider` from `com.intellij.codeInsight.inlayProvider`.

2. Choose the baseline-aware API.
   - New inline hints on `2023.1+`: start with declarative inlay hints.
   - New block hints on `2022.1+`: start with code vision.
   - Existing custom provider code or presentation needs that declarative or code vision cannot express: use the legacy inlay provider API.

3. Register the matching extension points and settings integration.
   - Parameter hints: `com.intellij.codeInsight.parameterNameHints`; add `ParameterNameHintsSuppressor` only when specific call sites must suppress hints.
   - Declarative hints: `com.intellij.codeInsight.declarativeInlayProvider`; add `InlayHintsCustomSettingsProvider` only when the settings UI needs custom controls.
   - Code vision: choose `DaemonBoundCodeVisionProvider` for PSI-backed values, `CodeVisionProvider` for non-PSI values, and pair them with `CodeVisionGroupSettingProvider` so the provider appears correctly in settings.
   - Legacy or fully custom inlays: `com.intellij.codeInsight.inlayProvider`.

4. Add preview resources before considering the provider complete.
   - Declarative previews live under `inlayProviders/<providerId>/preview.<ext>` and use inlay preview markup.
   - Code vision previews live under `codeVisionProviders/<modelId>/preview.<ext>` and require `com.intellij.codeInsight.codeVisionSettingsPreviewLanguage`.

5. Reuse provider logic across languages only when the hint concept is identical.
   Use `InlayHintsProviderFactory` instead of cloning nearly identical providers per language.

6. Test and inspect.
   - Inspect existing inlays and settings in the IDE before inventing a new interaction model.
   - Use `InlayHintsProviderTestCase` or `InlayParameterHintsTest` when the task is about provider tests or regressions.

## Guardrails

- Do not default to the legacy `InlayHintsProvider` for new inline hints on `2023.1+` unless custom presentation or behavior is a hard requirement.
- Treat code vision as experimental and baseline-sensitive.
- Keep `providerId`, `groupId`, `modelId`, preview directory names, and preview file extensions aligned; mismatches usually break settings previews before provider logic runs.
- Use `DaemonBoundCodeVisionProvider` when PSI changes should invalidate results. Use plain `CodeVisionProvider` when the data is not PSI-driven.
- Declarative hints are intentionally UI-independent and constrained. Do not promise arbitrary Swing rendering there.
- Parameter hints only support simple string inline hints plus suppression rules.

## References

- Read `references/official-docs.md` for the API selection matrix, extension point names, preview-resource rules, and testing hooks from the official JetBrains docs.
