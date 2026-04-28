# IntelliJ UI FAQ Official Docs

Verified against the live JetBrains docs on 2026-04-17.

## Canonical Sources

- User Interface FAQ: <https://plugins.jetbrains.com/docs/intellij/ui-faq.html>
  - Official FAQ page for common IntelliJ Platform plugin UI details.
  - Last updated in the live docs: 2025-05-08.
- Internal Actions - UI Inspector: <https://plugins.jetbrains.com/docs/intellij/internal-ui-inspector.html>
  - Use for runtime UI inspection, `added-at`, component-specific properties, and accessibility checks.
  - Last updated in the live docs: 2025-09-18.
- Exposing Theme Metadata: <https://plugins.jetbrains.com/docs/intellij/themes-metadata.html>
  - Use when the request involves `JBColor.namedColor()` keys or exposing plugin theme customization keys.
  - Last updated in the live docs: 2025-04-24.
- Internationalization: <https://plugins.jetbrains.com/docs/intellij/internationalization.html>
  - Use when the FAQ mentions `NlsMessages` or the task involves user-facing text, message bundles, or NLS annotations.
- Working with Icons: <https://plugins.jetbrains.com/docs/intellij/icons.html>
  - Use when the request grows from icon replacement into icon file layout, dark variants, New UI icon mapping, or icon loading conventions.
  - Last updated in the live docs: 2025-05-08.

## Request Routing

- "How do I inspect an existing IDE UI element or find what class built it?"
  - Start with UI Inspector.
  - Use `Ctrl/Cmd+Alt` plus left-click on the target UI element.
  - Use the `added-at` property when the component tree alone is not enough.
  - Mention `UiInspectorContextProvider` when the user asks how custom components expose extra inspector properties.

- "How should I handle plugin UI colors?"
  - Use `JBColor` instead of plain `java.awt.Color`.
  - Use `JBColor.namedColor()` for theme-defined keys with light and dark defaults.
  - Use `JBColor.lazy()` if the color is derived from another place and must stay fresh across theme or scheme changes.
  - Mention that the lambda passed to `JBColor.lazy()` must stay fast and safe because it can run during painting.
  - Reach for `UIUtil`, `JBUI`, and `ColorUtil` when the question is about generic UI colors or tuning an existing color.

- "How do I expose custom theme color keys?"
  - Register a `*.themeMetadata.json` file through `com.intellij.themeMetadataProvider`.
  - The metadata root contains `name`, optional `fixed`, and a `ui` list of keys.
  - Each key entry can include `key`, `description`, `deprecated`, `source`, and `since`.
  - Key naming scheme: `Object[.SubObject].[state][Part]Property`.

- "How should I format or localize UI strings?"
  - Use `NaturalComparator` for natural sorting.
  - Use `StringUtil` for pluralization, durations, file sizes, escaping line breaks, and ellipsis helpers.
  - Use `NlsMessages` for localized messages.
  - For real i18n work, route to the Internationalization doc:
    - Message bundles belong under `src/main/resources/messages/*.properties` in Gradle-based plugins.
    - Use `DynamicBundle`-based bundle classes rather than extending `DynamicBundle`.
    - Use `@Nls`, `@NonNls`, `@NlsSafe`, and context annotations where appropriate.
    - Avoid composing localized strings from smaller localized fragments.

- "How do I remember recently used values?"
  - Use `RecentsManager`.
  - This is the FAQ-endorsed storage for recently used entries such as filter values.

- "How do I detect whether the current theme is dark or light?"
  - Use `JBColor.isBright()`.

- "How should I create borders and insets?"
  - Use `JBUI.Borders` and `JBUI.Insets` so they are DPI-aware.
  - If those insets are only used inside `JBUI.Borders.empty()`, scaling updates happen automatically.
  - If the insets are stored and reused elsewhere, call `JBInsets.update()` from the component's `updateUI()`.

- "How do I replace or manipulate icons?"
  - For virtual files, implement `FileIconProvider` and register `com.intellij.fileIconProvider`.
  - For PSI elements, implement `IconProvider` and register `com.intellij.iconProvider`.
  - Use `LoadingDecorator` for a loading placeholder panel.
  - Use `IconUtil` to scale, colorize, darken, or desaturate existing icons.
  - Use `RowIcon` for horizontal composition.
  - Use `LayeredIcon` or `IconWithOverlay` for stacked or overlay icons.

## Cross-Doc Notes

- The FAQ page is intentionally narrow. When the user asks for file layout, icon naming, dark variants, HiDPI assets, `IconLoader.getIcon()`, or New UI icon mapping, switch to Working with Icons.
- The icons doc says plugin icon paths used with `IconLoader.getIcon()` must start with `/`.
- Kotlin icon holder fields should use `@JvmField`.
- Reuse platform icons whenever possible before creating custom icons.
- Working with Icons documents New UI tool window icon requirements:
  - 20x20 outlined icons for standard New UI tool windows.
  - 16x16 variants for Compact Mode.
  - `com.intellij.iconMapper` plus a `$PluginName$IconMappings.json` file for New UI icon mapping.

## Version-Sensitive Notes

- UI Inspector accessibility checks are available since 2025.2.
- UI Inspector shows additional Settings-related properties in 2023.1+ and the "Debugging Info in UI" setting path changed in 2024.2+.
- The FAQ and icons pages were both verified live and list 2025-05-08 as their current update date.
