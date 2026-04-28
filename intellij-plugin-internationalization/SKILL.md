---
name: intellij-plugin-internationalization
description: Internationalize JetBrains/IntelliJ Platform plugins with message bundles, `DynamicBundle`, NLS annotations, translated descriptions, and locale-aware formatting. Use when requests mention IntelliJ plugin i18n, `pluginBundle.properties`, `DynamicBundle`, `@PropertyKey`, `@Nls`/`@NonNls`, localized inspection or intention descriptions, translated file/postfix templates, tips, or hardcoded UI strings in plugins.
---

# IntelliJ Plugin Internationalization

## Overview
Use this skill when building, reviewing, or refactoring internationalization in an IntelliJ Platform plugin. Inspect the plugin's bundle files, hardcoded UI strings, translated HTML/XML resources, and version targets, then follow the JetBrains-documented patterns instead of inventing custom localization logic.

## Workflow
1. Inspect the existing plugin layout before editing.
   Look for `plugin.xml`, `src/main/resources/messages/*.properties`, translated `*_xx.properties` files, `inspectionDescriptions*`, `intentionDescriptions*`, `fileTemplates/`, `postfixTemplates/`, `tips/`, and obvious hardcoded UI strings.
2. Classify each string before moving it.
   Use NLS annotations intentionally: user-visible strings are `@Nls`, internal identifiers are `@NonNls`, and non-localized but display-safe values are `@NlsSafe`. Prefer more specific context annotations when the API supports them.
3. Centralize user-visible text in a bundle.
   Use a `messages/<Name>Bundle.properties` file and a small helper around `DynamicBundle`. Replace hardcoded literals with bundle keys and keep `@PropertyKey` on bundle accessors.
4. Use the documented translation layouts.
   When the task involves localized inspection descriptions, intention descriptions, file template descriptions, postfix template descriptions, or tips, follow the supported filename or directory layouts instead of custom resource lookup logic.
5. Validate the result.
   Run IntelliJ inspections for hardcoded strings and plugin descriptor i18n, verify keys and placeholders, and confirm the requested translated resource type is supported by the plugin's minimum IDE version.

## Reference Selection
- Read [official-internationalization-guide.md](references/official-internationalization-guide.md) for the annotation map, `DynamicBundle` pattern, message-key design, `MessageFormat`, `ChoiceFormat`, and locale-aware formatting APIs.
- Read [official-translation-layouts.md](references/official-translation-layouts.md) when the task includes translated HTML/XML/resources, bundled translations, locale lookup order, or version-sensitive resource support.

## Notes
- Re-open the canonical JetBrains docs from the reference files if the task depends on a specific IntelliJ Platform version.
- Prefer Kotlin for new plugin-side bundle helpers unless the repository already uses Java conventions.
