---
name: idea-intention-preview
description: Kotlin-first guide for implementing and testing IntelliJ Platform intention and quick-fix previews. Use when requests mention IntentionAction.generatePreview(), LocalQuickFix previews, IntentionPreviewInfo, IntentionPreviewUtils, CodeInsightTestFixture preview assertions, or IntelliJ plugin issues where an intention preview is empty, wrong, or unsafe.
---

# Idea Intention Preview

## Overview

Build preview-safe IntelliJ Platform intentions and quick fixes. Use this skill to choose between the default diff preview, HTML preview, custom diff preview, or no preview, and to diagnose why an existing preview fails on copied PSI or the mock preview editor.

## Workflow

1. Classify the preview path before changing code.
   Determine whether the target is an `IntentionAction`, a `LocalQuickFix`, or both.
   If the action changes only the current file, needs no extra user input, and can run on copied PSI, prefer the inherited diff preview instead of overriding `generatePreview()`.
   If the action changes other files, settings, or external state, plan a custom preview immediately.

2. Prefer the default diff preview when it is semantically correct.
   Keep `startsInWriteAction()` truthful. The default preview expects `true`, `getElementToMakeWritable()` to return its argument, and preview-safe instance fields.
   Reuse the existing `invoke()` or `applyFix()` when it can run on a non-physical file and a mock editor without extra UI.
   If the action stores PSI in fields, either remove that state or override `getFileModifierForPreview(target)` and remap those elements onto the copy.

3. Make the action preview-safe instead of preview-special when possible.
   Replace unsupported document access with `psiFile.getViewProvider().getDocument()`.
   Avoid nested write actions, EDT-only assertions, `Application.invokeLater()`, and editor APIs that do not work on `IntentionPreviewEditor`.
   Use `IntentionPreviewUtils.isIntentionPreviewActive()` to skip side effects or expensive follow-up work, and use `IntentionPreviewUtils.isPreviewElement()` instead of branching on `PsiElement.isPhysical()`.

4. Override `generatePreview()` only when the default diff is the wrong product.
   Return `IntentionPreviewInfo.DIFF` after mutating the copied file when a hand-written preview is unnecessary.
   Return `IntentionPreviewInfo.Html` for concise explanatory previews that do not depend on direct interaction.
   Return `IntentionPreviewInfo.CustomDiff` for cross-file changes, partial snippets, or cases where a synthetic diff communicates the change better than the current-file copy.
   Return `IntentionPreviewInfo.EMPTY` only when no meaningful preview can be shown.

5. Verify preview behavior explicitly.
   For fixture-based tests, prefer `checkPreviewAndLaunchAction()` instead of `launchAction()` when the preview should match the real change.
   Use `checkIntentionPreviewHtml()` for HTML previews and `getIntentionPreviewText()` when you need the raw preview text.
   For `LightQuickFixTestCase` or `LightQuickFixParameterizedTestCase`, use the `true-preview` suffix in the comment line and add `previewXyz` files when the expected preview differs from the final applied result.

## References

- Read `references/intention-preview.md` for the API map, default diff requirements, failure checklist, preview selection rules, and test-specific notes from the JetBrains docs.
