# IntelliJ Code Documentation Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/documentation.html`
- `https://plugins.jetbrains.com/docs/intellij/code-documentation.html`
- `https://plugins.jetbrains.com/docs/intellij/documentation-provider.html`

## API Selection

Use this matrix before writing code:

- Plugin baseline `2023.1+` and new work: use the Documentation Target API.
- Plugin baseline `< 2023.1`: use `DocumentationProvider`.
- Existing legacy provider that the user wants kept stable: stay on `DocumentationProvider` unless migration is explicitly requested.
- Cross-language legacy resolution, such as a custom language reference inside Java PSI: use global `com.intellij.documentationProvider` plus `getCustomDocumentationElement()`.

## Documentation Target API

Recommended by JetBrains for 2023.1 and later.

### Extension points

- `com.intellij.platform.backend.documentation.targetProvider`
  Use with `DocumentationTargetProvider.documentationTargets()` when the request starts from an editor offset in a `PsiFile`.
- `com.intellij.platform.backend.documentation.psiTargetProvider`
  Use with `PsiDocumentationTargetProvider.documentationTarget()` when PSI elements are already available.
- `com.intellij.platform.backend.documentation.symbolTargetProvider`
  Use with `SymbolDocumentationTargetProvider.documentationTarget()` when the symbol layer is the stable source of truth.

### `DocumentationTarget` responsibilities

- `computeDocumentation()`
  Replaces the old `generateDoc()` responsibility and produces the main rendered documentation.
- `computeDocumentationHint()`
  Replaces the old `getQuickNavigateInfo()` role for hover and quick-navigation text.
- `createPointer()`
  Must restore the target safely across read actions. Return `null` when the target cannot be restored because invalid PSI cannot be rehydrated.

### Rendering notes

- Return `DocumentationResult`.
- `DocumentationResult` can wrap HTML, images, and external URLs.
- Use asynchronous result creation if generating docs could block the IDE.

## Documentation Provider API

Legacy path. Deprecated as of 2023.1, but still relevant for older baselines and existing code.

### Registration

- `com.intellij.lang.documentationProvider`
  Default choice for legacy documentation tied to a specific language. Requires the `language` attribute in `plugin.xml`.
- `com.intellij.documentationProvider`
  Use only when the provider must also be invoked for PSI outside the language, for example resolving a custom-language symbol from a Java string literal.

Provider precedence matters: if multiple providers apply, the first one that returns non-`null` wins. Use ordering only when there is a concrete interoperability reason.

### Common override points

- `generateDoc(PsiElement element, PsiElement originalElement)`
  Main popup content.
- `getCustomDocumentationElement(Editor editor, PsiFile file, PsiElement context, int targetOffset)`
  Override when IntelliJ chooses the wrong PSI element.
- `getQuickNavigateInfo(PsiElement element, PsiElement originalElement)`
  Hover with `Ctrl`/`Cmd` or quick navigation text.
- `generateHoverDoc(PsiElement element, PsiElement originalElement)`
  Hover-only content when it should differ from the full popup.
- `getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element)`
  Completion-popup documentation.
- `getUrlFor(PsiElement element, PsiElement originalElement)` plus `ExternalDocumentationProvider`
  Online or browser-backed documentation.

### Tutorial pattern from the official Simple language example

- Start with a minimal provider extending `AbstractDocumentationProvider`.
- Register it in `plugin.xml`.
- Verify the PSI element being passed into `generateDoc()` before rendering anything.
- Extract nearby documentation comments with PSI traversal helpers.
- Render sections with `DocumentationMarkup`.
- Add hover and quick-navigate behavior only after the main popup works.
- Switch from `lang.documentationProvider` to the global provider only if documentation must resolve from foreign-language PSI.

## Rendering Helpers and Related APIs

- `DocumentationMarkup`
  Preferred helper for consistent HTML structure in legacy providers and reusable render helpers.
- `HtmlSyntaxInfoUtil`
  Use for lexer-based highlighted code samples inside documentation.
- `DocumentationActionProvider`
  Register `com.intellij.documentationActionProvider` when the request includes custom actions in documentation popups or inlays.

## Practical Decision Rules

- If the user says "new plugin" and the baseline is modern, do not start from `DocumentationProvider`.
- If the user says "fix tutorial sample" and the code already subclasses `AbstractDocumentationProvider`, preserve that approach unless they ask for migration.
- If documentation fails outside the exact token under the caret, inspect target resolution first, not HTML generation.
- If the same content appears in both hover and full popup, share a renderer helper but keep separate entry points because the APIs differ.
