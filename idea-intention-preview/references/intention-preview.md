# Intention Preview Reference

## Official docs

- Intention Action Preview: https://plugins.jetbrains.com/docs/intellij/code-intentions-preview.html
- Intentions: https://plugins.jetbrains.com/docs/intellij/code-intentions.html

These notes were extracted from the official JetBrains docs on 2026-04-17.

## Execution model

- `generatePreview()` exists on both `IntentionAction` and `LocalQuickFix`.
- The default implementation runs `invoke()` or `applyFix()` on a non-physical copy of the current file inside a headless preview editor.
- If the copy changes successfully, the framework returns `IntentionPreviewInfo.DIFF`.
- If the diff cannot be produced, the framework falls back to `IntentionPreviewInfo.EMPTY`.
- `IntentionPreviewUtils` contains helper methods and generic preview builders.

## Choose the preview type

Use the default diff preview when:

- the action changes only the current file
- the action needs no additional UI or user input
- the action can run on copied PSI and a mock editor
- the resulting diff is the correct user-facing preview

Use `IntentionPreviewInfo.Html` when:

- the action is better explained than diffed
- the preview should summarize choices or consequences
- the action is logically previewable but not through a file diff

Constraints for HTML preview:

- keep it concise because the pane is roughly 300px wide
- do not rely on links or buttons being clicked
- prefer `HtmlChunk` helpers over hand-built string concatenation
- when a generic preview fits, prefer helpers such as `rename()`, `navigate()`, or `movePsi()`

Use `IntentionPreviewInfo.CustomDiff` when:

- the meaningful change is not bound to the currently opened file
- only part of the overall result should be shown
- the default diff would be noisy or misleading

Custom diff guidance:

- `originalText` may be empty when only the destination snippet matters
- provide a crafted `originalText` when you need precise highlighting or multiple isolated hunks
- specify a file name when possible

Use `IntentionPreviewInfo.EMPTY` when:

- no honest preview can be produced
- any preview would imply behavior the action does not guarantee

## Default diff requirements

The documented safety checks for the inherited preview are:

1. `startsInWriteAction()` returns `true`.
2. `getElementToMakeWritable()` returns its argument.
3. Instance fields are preview-safe:
   fields are annotated with `@SafeFieldForPreview`, or
   their types are annotated with `@SafeTypeForPreview`, or
   their values are already known-safe types such as primitives, strings, enums, classes, or arrays of those.

If these checks pass, the platform may call the original `invoke()` or `applyFix()` on the copied file in a background thread.

## Common failure modes

1. A field is safe but missing `@SafeFieldForPreview`.
2. A field stores physical PSI that is later mutated.
3. `invoke()` or `applyFix()` starts its own write action even though `startsInWriteAction()` is already `true`.
4. The code asserts EDT or write-action execution, which is false during preview.
5. The code uses `PsiDocumentManager.getDocument(psiFile)` on a non-physical file.
6. The code calls `PsiDocumentManager.commitAllDocuments()` when only the current document matters.
7. The code uses `FileEditorManager.openTextEditor()` or `getSelectedEditor()` instead of `getSelectedTextEditor()`.
8. The code calls `Application.invokeLater()`, which is not allowed in preview.
9. The code depends on unsupported mock-editor features such as folding operations.
10. The action has side effects outside the current file, such as touching other PSI, changing settings, or launching external processes.
11. The code branches on `PsiElement.isPhysical()` and accidentally treats preview elements like unrelated non-physical PSI.
12. The action mutates results from `ReferencesSearch.search(...)`, which still point at the original physical file.

## Fix patterns for failing previews

For PSI captured in fields:

- prefer recomputing the target PSI from the descriptor or caret position
- or override `getFileModifierForPreview(target)` and remap PSI with `PsiTreeUtil.findSameElementInCopy()`
- declare the action class `final` when remapping behavior should not be inherited loosely

For thread and write-action problems:

- remove redundant write actions when `startsInWriteAction()` is already `true`
- gate assertions or post-processing with `IntentionPreviewUtils.isIntentionPreviewActive()`

For document and editor access:

- use `psiFile.getViewProvider().getDocument()`
- prefer `FileEditorManager.getSelectedTextEditor()`
- skip editor-only cosmetics during preview because they do not affect the rendered result

For searches and side effects:

- restrict expensive searches when preview is active
- traverse the copied file directly when you only need in-file updates
- if real behavior spans multiple files, prefer `CustomDiff` or a concise HTML preview

If `startsInWriteAction()` returns `false`, the docs recommend overriding `generatePreview()` instead of relying on the inherited diff.

## Testing notes

For `CodeInsightTestFixture`:

- `checkPreviewAndLaunchAction()` verifies preview and then executes the action
- `checkIntentionPreviewHtml()` verifies HTML preview content
- `getIntentionPreviewText()` returns the preview text for custom assertions

For `LightQuickFixTestCase` and `LightQuickFixParameterizedTestCase`:

- append `-preview` in the quoted enablement flag to turn on preview verification
- add a sibling `previewXyz` file when the expected preview is custom HTML or differs from the final applied file

`IntentionPreviewPopupUpdateProcessor` also exposes useful helpers when custom frameworks need to inspect or reproduce preview behavior.

## Related intention description resources

The preview docs are separate from Settings descriptions, but they interact in practice:

- Intention descriptions live under `resources/intentionDescriptions/<DirectoryName>/`
- the default directory name is the intention class name, unless `<descriptionDirectoryName>` overrides it in `plugin.xml`
- before/after example files use `before.<ext>.template` and `after.<ext>.template`
- if static before/after examples are not appropriate for the Settings page, `<skipBeforeAfter>true</skipBeforeAfter>` can be set in the `com.intellij.intentionAction` registration

Keep these resources unique in multi-module plugin builds so later-packed files do not overwrite earlier ones.
