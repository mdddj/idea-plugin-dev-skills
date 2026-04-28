---
name: idea-postfix-completion
description: Kotlin-first guide for building and reviewing IntelliJ Platform postfix completion plugins. Use when implementing or debugging custom postfix templates, PostfixTemplateProvider, StringBasedPostfixTemplate, EditablePostfixTemplate, postfix template description resources under postfixTemplates/, or IDEA/IntelliJ plugin requests mentioning postfix completion, postfix templates, 后缀补全, 后缀模板, or PostfixTemplateProvider.
---

# Idea Postfix Completion

## Overview

Build IntelliJ Platform postfix completion features with Kotlin as the implementation language. Use this skill to choose the right postfix template base class, register a provider, add required resource descriptions, and avoid common IntelliJ plugin mistakes around extension lifecycle and bundled Kotlin libraries.

## Workflow

1. Identify the target language and scope.
   If the request is about a custom language plugin, confirm the plugin already has PSI elements for expressions. Postfix completion is expression-driven; without reliable PSI, the template logic will be brittle.
   If the request is about an existing language, reuse its language ID and study that language's built-in postfix templates before inventing new abstractions.

2. Choose the template family before writing code.
   Use `StringBasedPostfixTemplate` when expansion can be expressed as live-template text with `$expr$` and `$END$`.
   Use `PostfixTemplateWithExpressionSelector` when the main problem is selecting the correct expression.
   Use `EditablePostfixTemplate` or `EditablePostfixTemplateWithMultipleExpressions` when users must edit template content or conditions in Settings.
   Use `SurroundPostfixTemplateBase` when an existing Surround With implementation already expresses the behavior.

3. Implement the provider first.
   Register a provider in the `com.intellij.codeInsight.template.postfixTemplateProvider` extension point.
   The provider should return the built-in template set, define terminal symbols such as `.` when appropriate, and keep `preExpand()`, `afterExpand()`, and `preCheck()` minimal unless the language truly needs file mutations for availability checks.
   Keep the provider `id` stable because settings are stored against it.

4. Implement the template with the narrowest correct applicability.
   Favor small, explicit selectors over permissive ones.
   When using `StringBasedPostfixTemplate`, make `example` match the intended expanded shape and override `getElementToRemove()` if the default removal range is wrong for the language PSI.
   Only drop to fully custom `expand()` logic when live-template syntax cannot express the behavior.

5. Add required description resources immediately after creating the template class.
   Every postfix template needs a description directory under `postfixTemplates/<SimpleTemplateClassName>/`.
   Add `description.html`, plus language-specific `before.*.template` and `after.*.template` files. For Kotlin-targeted examples use `.kt`.
   Use `<spot>` to highlight the expression or caret position in previews, and use `$key` when the preview should show the actual template key.

6. Verify behavior in three places.
   Check code completion availability in positive and negative PSI contexts.
   Check expansion output and caret placement.
   Check the Settings preview so missing `postfixTemplates/` resources are caught early.

## Kotlin Rules

Use Kotlin `class` for plugin.xml-registered extensions. Do not implement a `PostfixTemplateProvider` or other extension class as `object`.

Keep `companion object` usage trivial in extension classes. Constants and logger are fine; expensive initialization is not.

Prefer top-level helper functions or small private predicates over large utility singletons. This keeps provider and template classes cheap to instantiate.

Use the IDE-bundled Kotlin libraries. Do not ship your own `stdlib` or `kotlinx.coroutines` unless compatibility requirements force it and you understand the tradeoff.

## References

- Read `references/postfix-templates.md` for the official API map, extension point name, description resource layout, and a minimal provider/template skeleton.
- Read `references/kotlin-notes.md` when the request touches Gradle, Kotlin versioning, coroutines, extension lifecycle, or Kotlin-specific plugin hygiene.
