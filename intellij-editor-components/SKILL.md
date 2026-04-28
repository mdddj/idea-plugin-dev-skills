---
name: intellij-editor-components
description: Build or review IntelliJ Platform plugin editor-backed text inputs and embedded editors. Use when requests mention `EditorTextField`, `LanguageTextField`, `TextFieldWithCompletion`, `TextCompletionProvider`, `EditorCustomization`, `JavaCodeFragmentFactory`, `PsiDocumentManager`, or embedding syntax-aware editor UI inside dialogs, settings, tool windows, or popups. Prefer Kotlin for plugin-side code.
---

# IntelliJ Editor Components

## Overview

Build, review, or debug IntelliJ Platform plugin UI that embeds editor infrastructure inside Swing surfaces. Default to Kotlin for plugin-side code and treat these fields as editor integrations, not as styled `JTextField` replacements.

## Workflow

1. Classify the field before writing code.
   - General editor-backed input or viewer: start with `EditorTextField`.
   - Dialog input that should be language-aware with a friendlier API: start with `LanguageTextField`.
   - Editor-backed input with custom text completion variants: use `TextFieldWithCompletion` with a `TextCompletionProvider`.
   - Java class, package, or expression entry: build a Java code fragment first, then wrap its `Document` in `EditorTextField`.
   - Existing editor-backed field that needs scrollbars, spellcheck, stripes, or similar tuning: check `EditorCustomization` and the documented customization helpers.

2. Implement the narrowest platform surface that matches the request.
   - Prefer the platform editor/document model over hand-rolled Swing behavior when syntax highlighting, completion, or PSI-backed parsing is required.
   - For Java-backed entry, create the fragment with `JavaCodeFragmentFactory`, obtain its `Document` via `PsiDocumentManager.getDocument()`, and pass that document to `EditorTextField` or `setDocument()`.
   - For text completion, prefer `TextFieldWithCompletion` and the documented provider types before inventing custom popup logic.
   - If the request only needs a plain text box, do not force an editor-backed field into the design.

3. Verify the component in its host surface.
   - Check focus and keyboard behavior inside dialogs, settings panels, tool windows, and popups.
   - Check the file type or language is intentional, because parsing and highlighting depend on it.
   - Check editable vs read-only and single-line vs multiline behavior explicitly.
   - Check each editor-backed field owns its own backing document when multiple fields are created.

## Guardrails

- Prefer `LanguageTextField` over manual setup when the request is simply "language-aware input in a dialog."
- Prefer `TextFieldWithCompletion` for controlled string completion. Do not oversell it as full IDE code completion unless the request truly needs the broader completion stack.
- For Java input, use PSI/code fragments instead of raw strings so parsing and highlighting stay aligned with the platform.
- Do not rely on `setText()` for fragment-backed Java fields. Recreate the fragment/document when the text source changes.
- Read `references/official-docs.md` before subclassing `createEditor()` or choosing a completion provider variant.

## References

- Read `references/official-docs.md` for the official component-selection rules, source links, Java fragment workflow, and documented caveats.
