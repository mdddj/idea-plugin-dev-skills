# IntelliJ Inlay Hints Reference

Canonical docs checked live on 2026-04-23:

- `https://plugins.jetbrains.com/docs/intellij/inlay-hints.html`

JetBrains marks the page itself as updated on 2025-04-16.

## API Selection

Use this matrix before writing code:

- Simple parameter-name strings before call arguments: use `InlayParameterHintsProvider`.
- Inline textual hints with clickable items and limited presentation control: use declarative `InlayHintsProvider`.
- Block hints for declarations, metrics, usages, inheritors, or authorship: use code vision.
- Custom inline or block rendering, advanced behavior, or legacy compatibility: use `InlayHintsProvider` from `com.intellij.codeInsight.inlayProvider`.

## Inlay Parameter Hints

- Implement `InlayParameterHintsProvider`.
- Register `com.intellij.codeInsight.parameterNameHints`.
- Use `ParameterNameHintsSuppressor` with `com.intellij.codeInsight.parameterNameHintsSuppressor` to suppress hints in specific places.
- Constraint: parameter hints are simple string inline inlays. The docs explicitly call out that advanced presentation and behavior are not available here.
- Official examples named in the docs: `GroovyInlayParameterHintsProvider`, `KotlinInlayParameterHintsProvider`.

## Declarative Inline Hints

- Implement declarative `InlayHintsProvider`.
- Register `com.intellij.codeInsight.declarativeInlayProvider`.
- Optional custom settings UI: `InlayHintsCustomSettingsProvider` with `com.intellij.codeInsight.declarativeInlayProviderCustomSettingsProvider`.
- The API is intentionally UI-independent, so presentation customization is limited compared with Swing-specific providers.
- Preview resources must live at `inlayProviders/<providerId>/preview.<ext>`.
- `<providerId>` must match the `providerId` attribute of `com.intellij.codeInsight.declarativeInlayProvider`.
- Preview hints use the documented markup:
  - `/*<# Displayed Hint #>*/`
  - `<# Displayed Hint #>`
- Official examples named in the docs: `JavaImplicitTypeDeclarativeInlayHintsProvider`, `JavaMethodChainsDeclarativeInlayProvider`.
- For one hint concept shared across languages, use declarative `InlayHintsProviderFactory` on `2023.1+` rather than duplicating provider implementations.

## Code Vision

- JetBrains labels this API experimental and warns that backward compatibility is not guaranteed.
- Use `DaemonBoundCodeVisionProvider` with `com.intellij.codeInsight.daemonBoundCodeVisionProvider` when entries depend on PSI and should be recomputed after PSI changes.
- Use `CodeVisionProvider` with `com.intellij.codeInsight.codeVisionProvider` when the presented information does not depend on PSI.
- Register `CodeVisionGroupSettingProvider` with `com.intellij.config.codeVisionGroupSettingProvider` so the provider has a settings name and description.
- `groupId` in the settings provider must match the provider group id. If the provider does not specify `groupId`, it falls back to `id`.
- Code vision hints can render above the element or at end of line; users control the position in Settings | Editor | Inlay Hints | Code vision.
- Preview resources must live at `codeVisionProviders/<modelId>/preview.<ext>`.
- Register `com.intellij.codeInsight.codeVisionSettingsPreviewLanguage` with the matching `language` and `modelId`.
- Official examples named in the docs: `JavaInheritorsCodeVisionProvider`, `JavaReferencesCodeVisionProvider`, `VcsCodeVisionProvider`.

## Legacy or Fully Custom Inlay Providers

- Implement `InlayHintsProvider`.
- Register `com.intellij.codeInsight.inlayProvider`.
- This API supports both inline and block inlays with custom presentation and behavior.
- For new work, JetBrains recommends declarative inline hints on `2023.1+` and code vision for block hints on `2022.1+`.
- Use the legacy or custom API only when you genuinely need the extra presentation control or you are maintaining existing provider code.
- Official examples named in the docs: `GroovyLocalVariableTypeHintsInlayProvider`, `MarkdownTableInlayProvider`, `KotlinLambdasHintsProvider`.

## Preview and Settings Failure Modes

Check these before debugging provider logic:

- Declarative preview directory name does not match `providerId`.
- Code vision preview directory name does not match `modelId`.
- `CodeVisionGroupSettingProvider.groupId` does not match the provider group id.
- Missing `com.intellij.codeInsight.codeVisionSettingsPreviewLanguage` registration.
- Preview file extension does not match a supported default file extension for the intended language.

## Testing and Inspection

- Use UI Inspector and Settings | Editor | Inlay Hints to inspect existing inlays and discover platform conventions before inventing a new presentation.
- For tests, start with `InlayHintsProviderTestCase` or `InlayParameterHintsTest`.
- If one provider should work across several languages, revisit `InlayHintsProviderFactory` before duplicating tests and providers per language.

## Practical Decision Rules

- If the request is "parameter names before arguments", do not overbuild with declarative or legacy providers.
- If the request is inline text on modern IDE baselines, start with declarative hints unless the user explicitly needs custom rendering or behavior.
- If a PSI-backed code vision hint fails to refresh after edits, check whether the implementation mistakenly used plain `CodeVisionProvider`.
- If previews show nothing in settings, debug ids and resource paths before debugging PSI traversal or rendering logic.
