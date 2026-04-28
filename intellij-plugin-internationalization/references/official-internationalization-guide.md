# Official IntelliJ Internationalization Guide

Canonical source:
- `https://plugins.jetbrains.com/docs/intellij/internationalization.html` (JetBrains IntelliJ Platform Plugin SDK, updated 2025-08-05)

Use this reference when you need the actual JetBrains rules for classifying strings, wiring bundles, or writing translatable messages.

## String Categories
- `@Nls`: user-visible text that must be localized.
- `@NonNls`: internal strings such as IDs, file-format tokens, cache keys, and config attributes.
- `@NlsSafe`: values that stay untranslated but can appear in the UI, such as filenames, URLs, or user-authored names.

Do not reuse the same string value/API for both localized and non-localized roles. If a concept needs both, expose separate methods such as `getId()` and `getDisplayName()`.

## NLS Context Annotations
- Prefer semantic annotations where available because they improve inspections and generated bundle key names.
- Common built-in contexts include:
  - `NlsContexts.*`
  - `NlsActions.*`
  - `@InspectionMessage`
  - `@IntentionName`
  - `@IntentionFamilyName`
  - `@GutterName`
  - `@TooltipTitle`
- If existing contexts are insufficient, define a custom annotation that itself carries `@Nls`.

Useful effect from JetBrains inspections:
- `@NlsContext` can define suggested key prefixes/suffixes for the "I18nize hardcoded string literal" quick fix.
- `@Nls(capitalization = ...)` lets inspections verify capitalization for the UI context.

## Bundle Layout
- Standard bundle path in Gradle-based plugins:
  - `src/main/resources/messages/*.properties`
- Conventional filename:
  - `<Name>Bundle.properties`
- In multi-module plugins that merge resources into one JAR, ensure bundle names or paths stay unique. Otherwise later-packed bundles can overwrite earlier ones.

## Bundle Access Pattern
- Use a thin helper around `DynamicBundle`.
- Do not subclass `DynamicBundle`.
- Keep the bundle name in a `@NonNls` constant.
- Annotate bundle keys with `@PropertyKey(resourceBundle = BUNDLE)` so unresolved keys are caught by the IDE.

Minimal Kotlin pattern:

```kotlin
@NonNls
private const val BUNDLE = "messages.MyPluginBundle"

object MyPluginBundle {
    private val bundle = DynamicBundle(MyPluginBundle::class.java, BUNDLE)

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg args: Any): @Nls String =
        bundle.getMessage(key, *args)
}
```

## Moving Hardcoded Strings
- Bundle files should use UTF-8 properties encoding.
- Use IntelliJ inspections to migrate literals instead of hand-editing everything:
  - `Editor | Inspections | Java | Internationalization | Hardcoded strings`
  - `Plugin DevKit | Plugin descriptor | Plugin.xml i18n verification`
- Batch migration path from the docs:
  1. Run inspection by name for `Hardcoded strings`.
  2. Select the scope.
  3. In the Problems tool window, choose the literals to internationalize.
  4. Apply the `I18nize hardcoded string literal` quick fix.
  5. Review generated keys and bundle targets.

## Message Key Design
- Use context-rich keys. Short ambiguous keys like `set=Set` are hard to translate and easy to misuse.
- Prefer keys that encode purpose, such as `dialog.title.add.library` or `action.myAction.text`.
- Let NLS context annotations guide generated prefixes when possible.

## Properties File Rules
- `&` marks mnemonics. To display a literal ampersand, escape it as `\\&`.
- For long property values, continue lines with a trailing backslash.
- Use `\n` for real line breaks; a trailing backslash only joins lines.

## Message Composition Rules
- Do not localize by programmatic capitalization, pluralization, grammatical case conversion, or gender transformation after lookup.
- Do not compose full UI text from multiple localized fragments unless there is no better design.
- If variable data is involved, use `MessageFormat` placeholders from the start, even when the dynamic piece is only at one end of the sentence.
- In `MessageFormat` patterns, escape a literal apostrophe as `''`.

Example:

```properties
title.delete.class=Delete Class {0}
error.message.file.does.not.exist=File {0} doesn''t exist
```

## ChoiceFormat and Numbers
- Use `ChoiceFormat` when the wording depends on numeric ranges.
- Remember that each `n#...` clause starts a range, not just an exact match.
- If a nested choice branch itself contains a quoted pattern, apostrophes may need additional escaping.
- Ordinal rendering is also supported in number formatting patterns, for example `{0,number,ordinal}`.

## Locale-Aware Formatting APIs
- Dates and times:
  - `DateFormatUtil`
  - `NlsMessages.formatDateLong()`
- Durations:
  - `NlsMessages.formatDuration()`
- File sizes:
  - `Formats.formatFileSize()`

Notes:
- JetBrains documents IDE-locale support for `DateFormatUtil` since 2024.1.
- JetBrains also notes that `Formats.formatFileSize()` does not localize units and uses the default JVM locale rather than the IDE locale.
