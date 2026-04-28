---
name: idea-live-templates
description: Build, review, and debug IntelliJ Platform plugin Live Templates with Kotlin-first examples. Use when requests mention live templates, template XML under resources/liveTemplates, defaultLiveTemplates, liveTemplateContext, TemplateContextType, liveTemplateMacro, MacroBase, custom template functions, Surround With templates, Kotlin-based IntelliJ plugins, or Chinese requests such as 实时代码模板, 模板上下文, 模板函数, 导出模板到插件.
---

# IDEA Live Templates

## Overview

Implement IntelliJ Platform plugin live templates by editing XML under `src/main/resources/liveTemplates/`, registering the XML in `plugin.xml`, and adding `TemplateContextType` or `MacroBase` classes only when built-in contexts or functions are not enough. Default to Kotlin sources under `src/main/kotlin/`; use Java only when the host plugin is already Java-based.

## Workflow Decision Tree

1. Determine the scope:
   - **Bundled templates only**: Add or edit the XML file and register `defaultLiveTemplates`.
   - **Custom applicability rule**: Implement `TemplateContextType` and register `liveTemplateContext`.
   - **Custom function in `expression=`**: Implement `MacroBase` and register `liveTemplateMacro`.
2. Prefer built-in contexts and built-in functions before adding code.
3. Keep one XML file per logical template group.
4. Verify the template in a sandbox IDE before finalizing.

## Core Workflow

1. Start with the XML definition:
   - Create or update `src/main/resources/liveTemplates/<Group>.xml`.
   - Add one `<templateSet group="...">` root per file.
   - Put the cursor landing spot in `$END$`.
2. Register the XML in `plugin.xml`:
   - Add `<defaultLiveTemplates file="/liveTemplates/<Group>.xml"/>` under `<extensions defaultExtensionNs="com.intellij">`.
3. Add context or macro code only if the XML cannot express the requirement:
   - Read [references/context-and-macro.md](references/context-and-macro.md).
4. Verify in the IDE:
   - Open `Settings | Editor | Live Templates`.
   - Confirm the template group, description, variables, and context visibility.
   - Test expansion by abbreviation and, if relevant, as a `Surround With` template.

## Implementation Rules

- Prefer exporting a working template from the IDE for the first iteration when the XML is complex.
- Keep template abbreviations short but unique inside the group.
- Always define `<context>` explicitly. Do not rely on wide defaults.
- Prefer `key` plus `resource-bundle` when the plugin already localizes UI strings.
- For Kotlin-based plugins, place code examples under `src/main/kotlin/` and keep the implementation idiomatic instead of transliterating Java syntax.
- Use custom context types only for applicability checks that built-in contexts cannot model.
- Use custom macros only when built-in functions cannot produce the required value.
- For IntelliJ Platform 2025.1 and later, prefer the `contextId` attribute on `liveTemplateContext` registration.

## Resources

- Read [references/live-template-workflow.md](references/live-template-workflow.md) for the end-to-end workflow and version notes.
- Read [references/live-template-xml.md](references/live-template-xml.md) when editing template XML attributes, variables, and contexts.
- Read [references/context-and-macro.md](references/context-and-macro.md) when implementing `TemplateContextType` or `MacroBase`.
- Copy and adapt the Kotlin files in `assets/examples/` first; fall back to the Java files only when the plugin module is Java-based.
- Run `scripts/scaffold_live_template.py` when you want a fast XML skeleton without hand-writing boilerplate.
