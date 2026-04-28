# Official IntelliJ Translation Layouts

Canonical sources:
- `https://plugins.jetbrains.com/docs/intellij/providing-translations.html` (JetBrains IntelliJ Platform Plugin SDK, updated 2026-04-07)
- `https://plugins.jetbrains.com/docs/intellij/internationalization.html` (JetBrains IntelliJ Platform Plugin SDK, updated 2025-08-05)

Use this reference when the task includes translated resources beyond plain message lookups, or when the plugin's minimum IDE version matters.

## Delivery Model
- For third-party plugins that need their own translations, use bundled translations inside the plugin.
- Do not try to register `com.intellij.languageBundle`; JetBrains documents that extension point as internal and for JetBrains only.
- Separate language packs are for full-IDE localization, not normal third-party plugin translation work.

## Supported Bundled Translation Targets
- Message bundles (`*.properties`): supported since 2024.1
- Inspection descriptions (`/inspectionDescriptions/**/*.html`): supported since 2024.1
- Intention descriptions (`/intentionDescriptions/**/*.html`): supported since 2024.1
- File template descriptions (`/fileTemplates/**/*.html`): supported since 2024.2
- Postfix template descriptions (`/postfixTemplates/**/*.xml`): supported since 2024.2
- Tips of the day (`/tips/**/*.html`): supported since 2024.2

If the task targets an older platform version, verify whether that translated resource type is actually available before implementing it.

## Two Supported Resource Layouts

### 1. Localization Directory
Use a locale directory rooted under:

```text
/localization/<language>/<REGION>/
```

`<REGION>` is optional.

Examples:
- Base file:
  - `/fileTemplates/code/JavaDoc Class.java.html`
- Region-specific translation:
  - `/localization/zh/CN/fileTemplates/code/JavaDoc Class.java.html`
- Language-only translation:
  - `/localization/zh/fileTemplates/code/JavaDoc Class.java.html`

### 2. Localization Suffix in Filename
Keep the resource in its normal directory and add a locale suffix to the filename.

Examples:
- Base file:
  - `/intentionDescriptions/QuickEditAction/description.html`
- Region-specific translation:
  - `/intentionDescriptions/QuickEditAction/description_zh_CN.html`
- Language-only translation:
  - `/intentionDescriptions/QuickEditAction/description_zh.html`
- Bundle variants:
  - `messages/MyBundle_zh_CN.properties`
  - `messages/MyBundle_zh.properties`

JetBrains documents that directory layout or filename suffix is enough for runtime lookup; no extra extension-point registration is required for bundled translations.

## Lookup Order
For an IDE running in `zh_CN`, JetBrains documents this effective lookup order:
1. Matching translation from an installed language pack, if present.
2. Region-level translation from `/localization/zh/CN/...`
3. Region-level translation from filename suffix `_zh_CN`
4. Language-level translation from `/localization/zh/...`
5. Language-level translation from filename suffix `_zh`
6. Default resource with no locale suffix

Design implication:
- Prefer region-specific files only when wording truly differs by region.
- Always keep a complete default English/base resource because it is the final fallback.

## Programmatic Locale Checks
- Use `DynamicBundle.getLocale()` when code needs to inspect the current IDE UI locale.
- Avoid custom locale-selection systems unless the plugin has a separate, explicit requirement outside normal IDE localization.
