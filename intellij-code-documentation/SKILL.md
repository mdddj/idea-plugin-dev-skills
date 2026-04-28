---
name: intellij-code-documentation
description: Build or review IntelliJ Platform plugin documentation features for Quick Documentation, hover docs, and external documentation. Use when requests mention `DocumentationTargetProvider`, `PsiDocumentationTargetProvider`, `SymbolDocumentationTargetProvider`, `DocumentationTarget`, `DocumentationProvider`, `AbstractDocumentationProvider`, `DocumentationMarkup`, `ExternalDocumentationProvider`, `generateDoc()`, `computeDocumentation()`, `getQuickNavigateInfo()`, `getCustomDocumentationElement()`, or `com.intellij.*documentation*` extension points. Prefer Kotlin for plugin-side code.
---

# Intellij Code Documentation

## Overview

Build IntelliJ Platform plugin documentation support with the current API by default, and fall back to the legacy provider API only when the plugin baseline or existing code requires it. Use this skill to choose the right documentation extension point, map cursor context to the correct target, render consistent HTML, and avoid provider conflicts.

## Workflow

1. Choose the API before writing code.
   Use the Documentation Target API for plugins targeting IntelliJ Platform 2023.1 or later.
   Use `DocumentationProvider` only for pre-2023.1 baselines or when extending an existing legacy implementation that the user does not want migrated yet.

2. Pick the narrowest extension point that matches the request.
   Use `DocumentationTargetProvider` when documentation starts from an editor offset in the current `PsiFile`.
   Use `PsiDocumentationTargetProvider` when the caller already has PSI elements.
   Use `SymbolDocumentationTargetProvider` when the plugin's symbol model is the stable abstraction.
   Use `com.intellij.lang.documentationProvider` for language-scoped legacy providers.
   Use `com.intellij.documentationProvider` only when legacy documentation must also work for PSI from other languages, such as references embedded in Java strings.

3. Implement target resolution first.
   In the new API, provider classes should only locate the entity and return `DocumentationTarget` instances.
   In the legacy API, start with `generateDoc()` and confirm IntelliJ is handing you the correct PSI element before adding rendering logic.
   If the wrong PSI element is selected in the legacy API, override `getCustomDocumentationElement()` instead of compensating with fragile HTML-generation code.

4. Put rendering in the documentation object, not in the provider selection layer.
   For `DocumentationTarget`, implement `computeDocumentation()` for the main popup and tool window content, `computeDocumentationHint()` for hover/quick navigate text, and `createPointer()` so targets can be restored safely across read actions.
   For `DocumentationProvider`, implement `generateDoc()` first, then add `generateHoverDoc()`, `getQuickNavigateInfo()`, `getDocumentationElementForLookupItem()`, `getUrlFor()`, or `ExternalDocumentationProvider` only when the request actually needs those surfaces.

5. Render documentation with platform helpers.
   Use `DocumentationMarkup` for consistent sections, definitions, and content blocks.
   Use `HtmlSyntaxInfoUtil` when the popup should show syntax-highlighted code samples.
   In the new API, return `DocumentationResult`; use its asynchronous path when generating documentation could block the IDE, and include images or external URLs only when the request needs them.

6. Verify every surface the user cares about.
   Check Quick Documentation from both definitions and references.
   Check hover behavior separately because hover text may intentionally differ from the full popup.
   Check completion-popup documentation if lookup items are involved.
   Check cross-language calls if the provider is expected to resolve references outside the custom language file itself.

## Guardrails

- Prefer the new Documentation Target API for new work on 2023.1+ instead of copying old `DocumentationProvider` tutorials.
- Prefer language-scoped registration over global registration because the first provider that returns non-`null` wins.
- Avoid the `order` attribute unless you are deliberately composing with another provider and can explain why ordering is required.
- In `createPointer()`, return `null` when the target cannot be restored; the new API does not use an `isValid()` check.
- Keep provider classes thin. Selection logic should identify the target; rendering logic should live in the target or a dedicated renderer helper.
- When legacy documentation must work from other-language PSI, combine the global legacy EP with `getCustomDocumentationElement()` instead of assuming `lang.documentationProvider` will see foreign PSI.

## References

- Read `references/official-docs.md` when you need the API-selection matrix, extension point names, migration notes, supported rendering helpers, or the canonical JetBrains documentation URLs.
