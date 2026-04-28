---
name: intellij-icons
description: Build or review IntelliJ Platform plugin icon integrations, SVG icon assets, `IconLoader.getIcon()`, `*Icons` holder classes, `plugin.xml` icon paths, New UI `expui` mappings, `com.intellij.iconMapper`, `AnimatedIcon`, and icon tooltip bundles. Use when requests mention IntelliJ or IDEA plugin icons, SVG icons, `AllIcons`, `MyIcons`, tool window icons, file type icons, gutter icons, or Chinese requests such as 图标, SVG 图标, ToolWindow 图标, and New UI 图标适配. Prefer Kotlin for plugin-side code.
---

# Intellij Icons

## Overview

Build IntelliJ Platform plugin icons with the documented resource layout and runtime wiring instead of guessing path rules or New UI mappings. Use this skill to choose between platform and custom icons, organize SVG assets, reference them from Kotlin/Java and `plugin.xml`, and add New UI, animation, or tooltip support only when the request needs it.

## Workflow

1. Reuse a platform icon before drawing a new one.
   Check the JetBrains icons list and existing `AllIcons` entries first.
   Only create a custom icon when the platform set does not cover the use case.

2. Choose the resource layout before wiring code.
   In Gradle-based plugins, store icons under the resources directory, typically `src/main/resources/icons`.
   In DevKit-based plugins, place them in a source root marked as Resources Root.
   If an icon is referenced repeatedly from code or XML, create a dedicated `*Icons` holder class.

3. Reference icons with the correct API and path rules.
   Use `IconLoader.getIcon()` for code references, and make sure the resource path starts with `/`.
   In Kotlin icon holder objects, annotate fields with `@JvmField`.
   In `plugin.xml` and `@Presentation`, use either a resource path such as `/icons/foo.svg` or an icon holder constant such as `MyIcons.Foo`.
   If the holder class lives in the top-level `icons` package, omit the package prefix in XML references. Otherwise use the fully qualified class name.

4. Generate the correct asset variants.
   Use SVG by default.
   Add a dark variant when the light asset does not work well in dark theme.
   Add `@2x` assets only when the icon needs a more detailed HiDPI drawing.
   Match the documented usage sizes instead of scaling one icon blindly across tool windows, gutter icons, and file types.

5. Add New UI support deliberately.
   When the plugin must support both Classic UI and New UI, create an `expui/` subtree under the icon root, add New UI-specific assets there, create `$PluginName$IconMappings.json`, and register it through `com.intellij.iconMapper`.
   Map every New UI icon back to its Classic UI counterpart, and use the dedicated outlined tool window icon filenames and New UI color values from the reference.

6. Add optional runtime behavior only when requested.
   Use `AnimatedIcon`, `AnimatedIcon.Frame`, `AnimatedIcon.Default`, or `AsyncProcessIcon` for long-running activity indicators.
   Use `com.intellij.iconDescriptionBundle` plus `icon.<path>.tooltip` keys when the request needs automatic tooltips in `SimpleColoredComponent` renderers.

## Guardrails

- Treat plugin runtime icons and Marketplace plugin logos as different problems. This skill focuses on in-product icons, not plugin branding assets.
- Do not invent custom naming or directory conventions when the docs already define the path and filename scheme.
- For New UI migrations, do not stop at copying new assets; the mapping file and EP registration are part of the feature.
- Keep style guidance in the reference files. Do not bloat `SKILL.md` with palette dumps or long SVG checklists.

## References

- Read [references/usage-and-mappings.md](references/usage-and-mappings.md) for canonical docs URLs, resource layout, code/XML wiring, size and filename rules, New UI mappings, animated icons, and tooltip bundles.
- Read [references/svg-style-and-colors.md](references/svg-style-and-colors.md) when creating or reviewing custom SVG artwork, modifiers, stroke alignment, or JetBrains color usage.
