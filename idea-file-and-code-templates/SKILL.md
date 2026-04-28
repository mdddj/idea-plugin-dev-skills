---
name: intellij-file-and-code-templates
description: Build or modify IntelliJ Platform and IDEA plugin support for File and Code Templates. Use when requests mention bundled `.ft` templates, matching `.html` descriptions, Apache Velocity variables, `resources/fileTemplates`, custom File | New actions, `CreateFileFromTemplateAction`, `FileTemplateGroupDescriptorFactory`, `DefaultTemplatePropertiesProvider`, `CreateFromTemplateHandler`, `SaveFileAsTemplateHandler`, `com.intellij.internalFileTemplate`, `com.intellij.fileTemplateGroup`, or programmatic template rendering with `FileTemplateManager` and `FileTemplateUtil`. Prefer Kotlin for plugin-side code.
---

# IntelliJ File and Code Templates

## Overview

Build, review, or refactor IntelliJ Platform plugin support around File and Code Templates. Default to Kotlin for plugin-side code and Apache Velocity syntax inside `.ft` files.

## Workflow

1. Classify the request before writing code.
   - Bundled user-editable template: place templates under `resources/fileTemplates`.
   - Hidden template behind a custom action: use `resources/fileTemplates/internal` and usually register `com.intellij.internalFileTemplate`.
   - Template group in the `Other` tab: use `resources/fileTemplates/j2ee` plus `com.intellij.fileTemplateGroup`.
   - Extra template variables: implement `DefaultTemplatePropertiesProvider`.
   - Custom creation logic, validation, or post-processing: implement `CreateFromTemplateHandler`.
   - Better `Save File as Template...`: implement `SaveFileAsTemplateHandler`.
   - Programmatic rendering or PSI file creation: use `FileTemplateManager` and `FileTemplateUtil`.

2. Choose the correct template category.
   - `fileTemplates/`: default file templates exposed in `File | New` and Settings.
   - `fileTemplates/includes/`: reusable Velocity fragments for `#parse(...)`.
   - `fileTemplates/code/`: code-generation snippets.
   - `fileTemplates/internal/`: plugin-owned templates not meant for general editing.
   - `fileTemplates/j2ee/`: `Other` templates grouped by `FileTemplateGroupDescriptorFactory`.

3. Implement in this order.
   - Create the `.ft` template first.
   - Add a same-basename `.html` description when the template is user-facing.
   - Register extension points only when the request actually needs discoverability or custom behavior.
   - Reuse starter files from `assets/starter/` when the request is close to the provided patterns.

4. Verify the result in the intended surface.
   - Check the folder category, template name, and extension.
   - Check all Velocity variables are supplied.
   - Check `File | New`, Settings, or the invoking action exposes the template in the expected place.
   - Check Kotlin code calls platform APIs instead of hand-building file text when `FileTemplateUtil` already fits.

## Guardrails

- Prefer Kotlin examples unless the user explicitly wants Java.
- Prefer `CreateFileFromTemplateAction` for custom `File | New` flows instead of ad hoc dialogs.
- Do not put niche templates into the default `Files` category unless the user explicitly wants them there.
- Do not invent unsupported multi-file bundled templates for third-party plugins.
- Escape embedded live template syntax inside file templates with `#[[` and `]]#` when needed.

## References and Assets

- Read `references/official-docs.md` when you need the category-to-path map, extension point names, or API surface.
- Read `references/kotlin-recipes.md` when you need Kotlin implementations or want to adapt official Java examples.
- Reuse `assets/starter/` for copy-ready `plugin.xml`, Kotlin classes, and sample `.ft` / `.html` template pairs.
