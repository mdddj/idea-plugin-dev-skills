# IntelliJ Reference Contributor Reference

Canonical docs checked live on 2026-04-23:

- `https://plugins.jetbrains.com/docs/intellij/reference-contributor.html`
- `https://plugins.jetbrains.com/docs/intellij/psi-references.html`
- `https://plugins.jetbrains.com/docs/intellij/references-and-resolve.html`
- `https://plugins.jetbrains.com/docs/intellij/rename-refactoring.html`
- `https://plugins.jetbrains.com/docs/intellij/code-completion.html`

Observed page footer dates during creation:

- `Reference Contributor`: 19 March 2025
- `PSI References`: 05 August 2025
- `References and Resolve`: 16 April 2025
- `Rename Refactoring`: 25 March 2026
- `Code Completion`: 19 May 2025

## What This Skill Should Remember

- Contributed references are how plugins add navigation, completion, rename, and usages support to host PSI that does not semantically own the reference itself, such as Java string literals, comments, XML, or JSON.
- `PsiReference.getElement()` returns the usage site; `resolve()` or `multiResolve()` returns the declaration target.
- It is normal for `resolve()` to return `null` in incomplete code.

## Declaration-Side Prerequisites

- Any PSI element that can be renamed or referenced should implement `PsiNamedElement`.
- Prefer `PsiNameIdentifierOwner` when the declaration has a distinct identifier PSI element.
- In the tutorial pattern, Grammar-Kit PSI uses:
  - a named-element interface extending `PsiNameIdentifierOwner`
  - a mixin implementation extending `ASTWrapperPsiElement`
  - helper methods in `*PsiImplUtil`
  - grammar updates that expose `getName`, `setName`, and `getNameIdentifier`
- For `setName()`, JetBrains recommends creating a dummy file or element through an element factory, then extracting the replacement node from parsed PSI rather than assembling AST manually.

## Reference Implementation Choices

- Use `PsiReferenceBase` for a single resolve target.
- Use `PsiPolyVariantReferenceBase` when one usage may resolve to multiple valid targets.
- `PsiReference.isSoft()` should return `true` when unresolved targets should not be highlighted as hard errors.
- If a host element can contain multiple independent references, implement `getReferences()` instead of forcing everything through one reference object.
- Consider `HintedReferenceHost` when contributed reference lookup is frequent and the host can provide filtering hints.

## Range and Host-Element Rules

- For embedded references, derive the referenced text from an explicit `TextRange` inside the host PSI element.
- The tutorial example contributes a reference to `PsiLiteralExpression` and computes a range that excludes the prefix and literal quotes.
- Incorrect `TextRange` values break both navigation and rename.

## Contributor Registration

- Contributed references are registered via `PsiReferenceContributor`.
- Register the contributor in `plugin.xml` with `com.intellij.psi.referenceContributor`.
- Set the `language` attribute to the host language ID where the contributor should run.
- Narrow actual matching locations with `PsiReferenceRegistrar.registerReferenceProvider()` and `PlatformPatterns.psiElement(...)`.
- Return `PsiReference.EMPTY_ARRAY` for non-matching elements instead of allocating unused references.

## Resolve, Completion, and Related APIs

- `resolve()` is enough for single-target references.
- `multiResolve()` returns `ResolveResult[]`; each result can indicate whether it is valid.
- `PsiReference.getVariants()` powers basic reference completion.
- `getVariants()` may return strings, `PsiElement`, or `LookupElement` objects.
- `LookupElementBuilder` is the standard way to build completion items with icon and type text.
- If the request needs richer completion behavior, all completion types, keyword completion, or advanced sorting/insertion logic, use a `CompletionContributor` in addition to or instead of reference completion.

## Rename and Refactoring

- Rename calls `PsiNamedElement.setName()` on declarations and `PsiReference.handleElementRename()` on usages.
- For references extending `PsiReferenceBase`, rename is delegated through `ElementManipulator.handleContentChange()`, so the manipulator and `TextRange` must agree.
- In-place rename is enabled explicitly through `RefactoringSupportProvider` registered in `com.intellij.lang.refactoringSupport`.
- Use `NamesValidator` for language-wide identifier validation.
- Use `RenameInputValidator` or `RenameInputValidatorEx` when validation depends on PSI context or needs a custom error message.

## Practical Decision Rules

- If the target declaration belongs to your language PSI, implement rename support on the declaration first; contributor-side usages become much easier afterward.
- If the host is a foreign language literal, keep the contributor narrow and content-aware instead of matching every literal indiscriminately.
- If the usage can legally point to multiple declarations, model that explicitly with `PsiPolyVariantReference` rather than arbitrarily picking one target.
- If the reference is visually hidden inside a host string or similar container, consider `HighlightedReference` so users can see the linked segment.
