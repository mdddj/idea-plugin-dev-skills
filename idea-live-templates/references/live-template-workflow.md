# Live Template Workflow

Official sources checked on 2026-04-17:
- JetBrains SDK docs: https://plugins.jetbrains.com/docs/intellij/live-templates.html
- Configuration file reference: https://plugins.jetbrains.com/docs/intellij/live-templates-configuration-file.html
- SDK sample: https://github.com/JetBrains/intellij-sdk-docs/tree/main/code_samples/live_templates

## When To Read This File

Read this file when you need the full workflow for bundling editor templates into an IntelliJ Platform plugin.

## Default Workflow

1. Decide whether the task is declarative or code-backed.
   - If the template can be expressed with built-in contexts and built-in functions, stay in XML.
   - If applicability depends on custom file or PSI logic, add `TemplateContextType`.
   - If a variable needs custom computation, add `MacroBase`.
2. Author or export the template XML.
   - Store bundled templates in `src/main/resources/liveTemplates/<Group>.xml`.
   - Keep one `<templateSet>` per file.
   - Use `$END$` for the final caret location.
3. Register the XML file in `plugin.xml`.
   - Add `<defaultLiveTemplates file="/liveTemplates/<Group>.xml"/>` inside `<extensions defaultExtensionNs="com.intellij">`.
4. Register custom code only when needed.
   - Add `<liveTemplateContext implementation="..."/>` for custom contexts.
   - Add `<liveTemplateMacro implementation="..."/>` for custom functions.
5. Verify in a sandbox IDE.
   - Inspect `Settings | Editor | Live Templates`.
   - Expand the abbreviation in a real editor.
   - If the template is meant for `Surround With`, test it on a selection.

## File Layout

- `src/main/resources/liveTemplates/<Group>.xml`: bundled template definitions
- `src/main/resources/META-INF/plugin.xml`: extension registrations
- `src/main/resources/messages/<Bundle>.properties`: optional localized descriptions
- `src/main/kotlin/...`: default location for custom context or macro classes in Kotlin-based plugins
- `src/main/java/...`: fallback location for Java-based plugins

## Version Note

The JetBrains documentation page updated on 2025-04-29 states that IntelliJ Platform 2025.1 and later should register `liveTemplateContext` with a `contextId` attribute. Older constructor-based declarations still work, but treat `contextId` in `plugin.xml` as the preferred form for new work.

## Recommended Review Checklist

- Does the template belong in an existing group instead of creating a new one?
- Does the abbreviation collide with another template in the same group?
- Is the context narrow enough to avoid showing the template everywhere?
- Could a built-in function replace a custom macro?
- If the plugin localizes UI text, is the description using `key` plus `resource-bundle`?
