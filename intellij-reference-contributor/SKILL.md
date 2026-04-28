---
name: intellij-reference-contributor
description: Build or review IntelliJ Platform plugin reference contributors, PSI references, and cross-language navigation/completion. Use when requests mention `PsiReferenceContributor`, `PsiReferenceProvider`, `PsiReferenceBase`, `PsiPolyVariantReference`, `PsiReferenceRegistrar`, `com.intellij.psi.referenceContributor`, string-literal references, `resolve()`, `getVariants()`, `handleElementRename()`, or rename/navigation/completion for IntelliJ or IDEA plugins. Prefer Kotlin for plugin-side code.
---

# IntelliJ Reference Contributor

## Overview

Build IntelliJ Platform references that connect usages to declarations, especially when the usage lives in another language or a host element like a string literal. Use this skill to model named PSI correctly, choose the right reference base class, register narrow reference providers, and keep navigation, completion, and rename behavior consistent.

## Workflow

1. Decide which PSI elements are declarations and which are usages.
   Declaration PSI that introduces a name should implement `PsiNamedElement`, and prefer `PsiNameIdentifierOwner` when the identifier element is available.
   Usage PSI should not implement `PsiNamedElement`; it only exposes `getReference()` or contributed references.

2. Make declaration PSI rename-safe before adding contributed usages.
   Implement `getName()`, `setName()`, and `getNameIdentifier()` on the declaration side.
   For Grammar-Kit generated PSI, add the methods through the mixin and `*ImplUtil` path, then regenerate parser code.
   Build replacement nodes through a dummy file or element factory instead of hand-assembling AST fragments.

3. Pick the reference shape deliberately.
   Use `PsiReferenceBase` when the usage resolves to one target and you want standard rename handling through `ElementManipulator`.
   Use `PsiPolyVariantReferenceBase` when the usage can resolve to multiple valid targets.
   Mark the reference soft only when unresolved targets should not be highlighted as errors.

4. Define the reference range from the host text, not from guesswork.
   For references embedded in literals or prefixed strings, calculate `TextRange` so it excludes quotes, prefixes, separators, or other syntax that should not rename.
   If the host can contain multiple references, return them from `getReferences()` and consider `HintedReferenceHost` if performance matters.

5. Implement resolution and completion together.
   In single-target cases, implement `resolve()`; in multi-target cases, implement `multiResolve()` and return `ResolveResult[]`.
   Implement `getVariants()` for basic reference completion and return `LookupElementBuilder`, `PsiElement`, or strings.
   If the request needs keyword completion, non-basic completion types, or richer completion control, add a separate `CompletionContributor` instead of overloading the reference.

6. Register the contributor on the host language with a narrow pattern.
   Subclass `PsiReferenceContributor` and register providers in `registerReferenceProviders(PsiReferenceRegistrar registrar)`.
   Register `com.intellij.psi.referenceContributor` in `plugin.xml` with the host language ID, then narrow actual matches with `PlatformPatterns.psiElement(...)` and parent constraints where needed.
   Return `PsiReference.EMPTY_ARRAY` when the element or literal content does not match the reference syntax.

7. Wire rename and in-place refactoring explicitly.
   Ensure rename from usages works through `handleElementRename()` semantics provided by the chosen base class and a correct element manipulator/range.
   If in-place rename is expected on the declaration side, add `RefactoringSupportProvider` and register `com.intellij.lang.refactoringSupport`.
   Add `NamesValidator` or `RenameInputValidator` only when default name validation is insufficient.

8. Verify the full feature surface.
   Check `Ctrl/Cmd+B` navigation from the usage site.
   Check completion in the host element.
   Check rename from both declaration and usage.
   Check that unresolved targets produce the intended severity.
   If the reference is hidden inside a literal or other non-obvious text, consider `HighlightedReference`.

## Guardrails

- Do not make usage PSI implement `PsiNamedElement`; only declarations own names.
- Do not register a broad `psiElement()` pattern without also constraining host language and literal shape.
- Do not let `TextRange` include quotes, prefixes, or separators that should survive rename.
- Do not use reference completion when the task really requires a full `CompletionContributor`.
- Do not assume `resolve()` always succeeds; null or empty results are normal in incomplete code.
- Do not scan the whole project repeatedly in `resolve()` or `getVariants()` when the result can be cached or narrowed.

## References

- Read `references/official-docs.md` for the canonical JetBrains URLs, API-selection notes, extension points, and rename/completion details.
