# IntelliJ Icons Usage and Mappings

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/icons.html`
- `https://plugins.jetbrains.com/docs/intellij/icons-style.html`

## Scope

The JetBrains `icons.html` page is about icons used inside IntelliJ Platform plugins: actions, tool windows, file types, gutter icons, renderers, and similar runtime UI. It explicitly separates these from plugin logos.

## Platform vs. Custom Icons

- Reuse platform icons whenever possible.
- Browse existing icons through the JetBrains icons list.
- Platform icons live in `AllIcons`.
- Icons shipped by other plugins typically live in their `<PluginName>Icons` holder classes.
- Only fall back to custom SVG artwork when the platform set does not express the intended meaning.

## Resource Layout

Choose the layout from the project type:

- Gradle-based plugin: put icon assets in the resources directory, commonly `src/main/resources/icons`.
- DevKit-based plugin: place icon assets in a dedicated source root marked as Resources Root.

When icons are referenced only from `plugin.xml` or `@Presentation`, raw resource paths are enough. When they are referenced repeatedly from Kotlin/Java and XML, introduce a `*Icons` holder class.

## Icon Holder Classes

Starting with IntelliJ Platform 2021.2, the `*Icons` class does not have to live in the top-level `icons` package. It can also live in the plugin package.

Classic top-level pattern:

```java
package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface MyIcons {
  Icon Action = IconLoader.getIcon("/icons/action.svg", MyIcons.class);
  Icon ToolWindow = IconLoader.getIcon("/icons/toolWindow.svg", MyIcons.class);
}
```

Kotlin pattern:

```kotlin
package icons

import com.intellij.openapi.util.IconLoader

object MyIcons {
    @JvmField
    val Action = IconLoader.getIcon("/icons/action.svg", javaClass)

    @JvmField
    val ToolWindow = IconLoader.getIcon("/icons/toolWindow.svg", javaClass)
}
```

Rules to keep straight:

- `IconLoader.getIcon()` paths must start with a leading `/`.
- If the icon holder class is in the top-level `icons` package, XML references omit the `icons.` prefix.
- If the icon holder class is elsewhere, XML references must use the fully qualified class name.

## Referencing Icons

### By resource path

Use resource paths directly in `plugin.xml` and similar metadata:

```xml
<actions>
  <action icon="/icons/myAction.svg" />
</actions>

<extensions defaultExtensionNs="com.intellij">
  <toolWindow icon="/icons/myToolWindow.svg" />
</extensions>
```

### By icon holder constant

Use a constant when the icon already lives in a holder class:

```xml
<actions>
  <action icon="MyIcons.MyAction" />
</actions>

<extensions defaultExtensionNs="com.intellij">
  <toolWindow icon="com.example.plugin.MyIcons.MyToolWindow" />
</extensions>
```

## Standard Sizes

Use the documented size for the UI surface instead of scaling a single drawing arbitrarily:

- Node, action, file type: `16x16`
- Tool window in Classic UI: `13x13`
- Tool window in New UI: `20x20` and `16x16` for Compact Mode
- Editor gutter in Classic UI: `12x12`
- Editor gutter in New UI: `14x14`

For SVG, set the base size with `width` and `height` attributes. If omitted, the base size defaults to `16x16`.

Minimal SVG example:

```xml
<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16">
  <rect width="100%" height="100%" fill="green"/>
</svg>
```

## Theme and HiDPI Variants

The documented filename scheme keeps all variants in the same directory:

- Light: `iconName.svg`
- Dark: `iconName_dark.svg`
- Light HiDPI: `iconName@2x.svg`
- Dark HiDPI: `iconName@2x_dark.svg`

Notes:

- Provide a dark variant when the light version does not work well in dark theme.
- Provide `@2x` only when the higher-density drawing needs more detail. Simple SVG graphics can often skip it.
- A HiDPI asset still represents the same logical base size.

## New UI Support

To support both Classic UI and New UI at the same time, the docs require both extra assets and a mapping file.

### Setup checklist

1. Create an `expui/` directory under the icon root directory.
2. Put all New UI-specific icons there.
3. Create an empty `$PluginName$IconMappings.json` in the resources directory.
4. Register that file with the `com.intellij.iconMapper` extension point in `plugin.xml`.

Registration pattern:

```xml
<extensions defaultExtensionNs="com.intellij">
  <iconMapper mappingFile="MyPluginIconMappings.json"/>
</extensions>
```

### Mapping format

Each directory becomes a nested object under `expui`. The left side is the New UI icon name. The right side is the Classic UI path or paths it replaces.

```json
{
  "icons": {
    "expui": {
      "dirName": {
        "icon1.svg": "icons/icon1.svg",
        "icon2.svg": "icons/icon2.svg"
      },
      "anotherDir": {
        "anotherIcon.svg": "images/anotherIcon.svg"
      }
    }
  }
}
```

If one New UI icon replaces several older icons, the value can be a JSON list:

```json
{
  "vcs.svg": [
    "toolwindows/toolWindowChanges.svg",
    "vcs/branch.svg"
  ]
}
```

### New UI tool window filenames

The New UI expects outlined tool window icons with this filename scheme:

- Light `20x20`: `toolWindowIcon@20x20.svg`
- Dark `20x20`: `toolWindowIcon@20x20_dark.svg`
- Light `16x16` Compact Mode: `toolWindowIcon.svg`
- Dark `16x16` Compact Mode: `toolWindowIcon_dark.svg`

### New UI icon colors

The docs prescribe icon content colors so the platform can switch active tool window buttons to white against highlighted backgrounds:

- Light: `#6C707E`
- Dark: `#CED0D6`

## Animated Icons

Use animated icons for long-running operations.

- `AnimatedIcon(delay, icons...)`: equal delay between frames
- `AnimatedIcon.Frame`: different delay per frame
- `AnimatedIcon.Default`: predefined loading indicator
- `AnimatedIcon.Big`: larger predefined loading indicator
- `AsyncProcessIcon`: alternative loader API

For list, table, and tree renderers, set `AnimatedIcon.ANIMATION_IN_RENDERER_ALLOWED` to `true` so repainting happens automatically.

## Tooltips for SVG Icons

To provide automatic tooltips for `SimpleColoredComponent` renderers:

1. Register a resource bundle through `com.intellij.iconDescriptionBundle`.
2. Add keys using the documented conversion rule:
   - Start from the icon path.
   - Keep the leading slash only conceptually while deriving the key.
   - Remove `.svg`.
   - Replace `/` with `.`.

Example:

- Icon path: `/nodes/class.svg`
- Bundle key: `icon.nodes.class.tooltip`

## Practical Review Checklist

Use this quick pass before finalizing a plugin icon change:

- The request really needs a custom icon and is not already covered by `AllIcons`.
- Asset location matches the build system.
- `IconLoader.getIcon()` paths start with `/`.
- Kotlin holder constants use `@JvmField`.
- XML references use either a raw path or the correct holder constant format.
- Size, dark, and HiDPI variants match the actual usage site.
- New UI work includes `expui/`, mapping JSON, and `iconMapper` registration.
- Tool window icons follow the special New UI filename scheme.
- Optional animation or tooltip support is added only when the request calls for it.
