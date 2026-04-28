# IntelliJ Editor Components Reference

## Sources

- Canonical docs: https://plugins.jetbrains.com/docs/intellij/editor-components.html
- Page title: `Editor Components | IntelliJ Platform Plugin SDK`
- Page date shown by JetBrains: `29 April 2025`

## Source Links From The Page

- `EditorTextField`: `platform/platform-impl/src/com/intellij/ui/EditorTextField.java`
- `EditorCustomization`: `platform/platform-impl/src/com/intellij/ui/EditorCustomization.java`
- `LanguageTextField`: `platform/platform-impl/src/com/intellij/ui/LanguageTextField.java`
- `TextFieldWithCompletion`: `platform/platform-impl/src/com/intellij/util/textCompletion/TextFieldWithCompletion.java`
- `TextCompletionProvider`: `platform/platform-impl/src/com/intellij/util/textCompletion/TextCompletionProvider.java`
- `TextFieldCompletionProvider`: `platform/lang-impl/src/com/intellij/util/TextFieldCompletionProvider.java`
- `TextFieldCompletionProviderDumbAware`: `platform/lang-impl/src/com/intellij/util/TextFieldCompletionProviderDumbAware.java`
- `JavaCodeFragmentFactory`: `java/java-psi-api/src/com/intellij/psi/JavaCodeFragmentFactory.java`
- `PsiDocumentManager`: `platform/core-api/src/com/intellij/psi/PsiDocumentManager.java`

## What The Page Explicitly Documents

- `EditorTextField` is the embedded IntelliJ editor component for dialogs and tool windows.
- The documented first-order knobs are file type, read-only vs editable, and single-line vs multiline mode.
- Advanced tuning is done by subclassing and overriding `createEditor()` and then applying `EditorCustomization`.
- The page calls out these ready-made customizations:
  - `SpellCheckingEditorCustomization`
  - `HorizontalScrollBarEditorCustomization`
  - `ErrorStripeEditorCustomization`
- For dialog input, `LanguageTextField` is recommended as the more accessible API.
- For autocompletion, `TextFieldWithCompletion` takes a `TextCompletionProvider`.
- To build a custom provider, use `TextFieldCompletionProvider` and override `addCompletionVariants()`, then add variants with `CompletionResultSet.addElement()`.
- `TextFieldCompletionProviderDumbAware` exists for completion that should still work during indexing.

## Selection Rules

- Need an editor-backed field with IntelliJ editor behavior: choose `EditorTextField`.
- Need a dialog input field with language awareness and a friendlier entry point: choose `LanguageTextField`.
- Need text completion variants without building a separate popup stack: choose `TextFieldWithCompletion`.
- Need Java-aware class, package, or expression input: create a Java code fragment and then wrap its document in `EditorTextField`.
- Need to tweak editor chrome or behavior after the editor is created: use `EditorCustomization` and related helpers.

## Java Pattern From The Docs

The page describes class or package entry as:

1. Create a fragment with `JavaCodeFragmentFactory.createReferenceCodeFragment()`.
2. Convert the fragment to a document with `PsiDocumentManager.getDocument()`.
3. Pass that document to `EditorTextField` or call `setDocument()`.

The same page also includes a code sample that uses `createExpressionCodeFragment()` before constructing:

```java
PsiExpressionCodeFragment code = JavaCodeFragmentFactory.getInstance(project)
  .createExpressionCodeFragment("", element, null, true);
Document document = PsiDocumentManager.getInstance(project).getDocument(code);
EditorTextField editorTextField =
  new EditorTextField(document, project, JavaFileType.INSTANCE);
```

Inference from the docs:

- Pick the fragment factory method that matches the semantic input you need. The prose mentions reference fragments; the sample shows expression fragments.

## Practical Caveats Called Out By JetBrains

- When creating more than one field, use separate fragment instances so each field has its own document.
- `setText()` no longer works for the fragment-backed input field described on the page.
- To change the value, supply new text when creating the fragment and replace the document instead of mutating through `setText()`.
- In the GUI builder, replacing a `JTextField` with a custom editor-backed component requires using `Custom Create` so initialization code runs correctly.
