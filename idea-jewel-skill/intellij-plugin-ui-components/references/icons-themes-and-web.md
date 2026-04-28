# Icons, Themes, and Web Surfaces

## Working with Icons

- Reuse platform icons whenever possible before designing custom ones.
- Browse built-in icons through the official icon list and `AllIcons`.
- Load plugin icons with `IconLoader.getIcon("/icons/foo.svg", MyIcons.class)` and keep the leading slash.
- Store icons in resources and collect repeated references in a `*Icons` holder class.
- Reference icons from `plugin.xml` either by path or by holder class constant.
- Provide dark and optional HiDPI variants using the documented naming scheme such as `icon.svg`, `icon_dark.svg`, `icon@2x.svg`, and `icon@2x_dark.svg`.
- Remember the size rules: common node or action icons are `16x16`; classic tool-window icons are `13x13`; New UI tool-window icons require `20x20` plus `16x16` compact variants.
- For New UI overrides, add an `expui` icon directory, create a `$PluginName$IconMappings.json` file, and register it via `com.intellij.iconMapper`.
- Official page: <https://plugins.jetbrains.com/docs/intellij/icons.html>

## User Interface FAQ

- Use `JBColor` instead of raw `java.awt.Color` for theme-aware colors.
- Use `JBColor.namedColor()` for custom named theme colors and `JBColor.lazy()` when the color must be resolved repeatedly and react to theme changes.
- Use `UIUtil`, `JBUI`, and `ColorUtil` for shared UI colors, DPI-aware borders, insets, and derived colors.
- Use `IconUtil` to scale or recolor existing icons.
- Use `RowIcon`, `LayeredIcon`, or `IconWithOverlay` instead of hand-compositing icons.
- Use `LoadingDecorator` for a standard loading placeholder.
- Use `FileIconProvider` or `IconProvider` extension points when overriding existing file or PSI icons.
- Official page: <https://plugins.jetbrains.com/docs/intellij/ui-faq.html>

## Embedded Browser (JCEF)

- Use JCEF only when Swing is insufficient or when the feature genuinely needs HTML or browser-like rendering.
- Check `JBCefApp.isSupported()` before recommending or instantiating browser code.
- Start with `JBCefBrowser` or `JBCefBrowserBuilder`; add the browser to UI through `browser.getComponent()`.
- Use `JBCefClient` for event handlers and explicitly dispose custom clients.
- Use `JBCefJSQuery` for browser-to-plugin callbacks.
- Load plugin-bundled web resources through request handlers when the browser must access local HTML, CSS, or JavaScript.
- Dispose `JBCefBrowser`, `JBCefClient`, and `JBCefJSQuery` according to the IntelliJ disposal model.
- Mention DevTools support when debugging web UI matters.
- Official page: <https://plugins.jetbrains.com/docs/intellij/embedded-browser-jcef.html>

## Color Scheme Management

- Use `TextAttributesKey.createTextAttributesKey()` with a fallback key from `DefaultLanguageHighlighterColors` instead of fixed default attributes.
- Chain text attribute keys when a plugin-specific key should inherit from another plugin-defined key.
- Use `com.intellij.additionalTextAttributes` only when a plugin truly needs scheme-specific explicit attribute files.
- Keep additional text attribute files uniquely named under resources to avoid collisions.
- Avoid fixed colors unless there is no good inherited default; the official guidance strongly discourages fixed default attributes.
- Official page: <https://plugins.jetbrains.com/docs/intellij/color-scheme-management.html>

## Exposing Theme Metadata

- Register a `*.themeMetadata.json` file with `com.intellij.themeMetadataProvider`.
- Define top-level `name`, optional `fixed`, and a `ui` array of exposed customization keys.
- For each key, provide `key`, `description`, and where relevant `deprecated`, `source`, and `since`.
- Prefer lowerCamelCase property naming in custom theme keys.
- Do not remove existing exposed keys; deprecate them and explain the replacement in `description`.
- Pair theme metadata with `JBColor.namedColor()` defaults in code so plugin UI stays usable outside custom themes.
- Treat the metadata file as the contract for theme authors editing `*.theme.json` files.
- Official page: <https://plugins.jetbrains.com/docs/intellij/themes-metadata.html>
