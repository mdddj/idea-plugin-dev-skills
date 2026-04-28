# Context And Macro Extensions

Official sources:
- https://plugins.jetbrains.com/docs/intellij/live-templates.html
- https://github.com/JetBrains/intellij-sdk-docs/tree/main/code_samples/live_templates

## When To Read This File

Read this file when built-in contexts or built-in functions are not enough and the plugin must add Kotlin or Java code. Default to Kotlin unless the plugin module is already Java-based.

## Custom Context Workflow

1. Reuse a built-in context if one already matches the target language or file type.
2. If not, implement a `TemplateContextType` subclass.
3. Keep `isInContext(...)` narrow and cheap.
4. Register the class in `plugin.xml` with `liveTemplateContext`.
5. Reference the same context ID in the XML `<context>` block.

### Registration Pattern

```xml
<extensions defaultExtensionNs="com.intellij">
  <defaultLiveTemplates file="/liveTemplates/Markdown.xml"/>
  <liveTemplateContext
      implementation="com.example.livetemplates.MarkdownContext"
      contextId="MARKDOWN"/>
</extensions>
```

### Context Class Pattern

Use the Kotlin example in `assets/examples/MarkdownContext.kt` as the default starting point. Use `assets/examples/MarkdownContext.java` only when the plugin is maintained in Java.

## Custom Macro Workflow

1. Check the built-in functions list first.
2. If a custom function is still needed, subclass `MacroBase`.
3. Give the macro a stable invocation name such as `titleCase`.
4. Implement `calculateResult(...)`.
5. Narrow `isAcceptableInContext(...)` if the macro only makes sense in specific contexts.
6. Register the class with `liveTemplateMacro`.

### Registration Pattern

```xml
<extensions defaultExtensionNs="com.intellij">
  <liveTemplateMacro implementation="com.example.livetemplates.TitleCaseMacro"/>
</extensions>
```

### XML Usage Pattern

```xml
<variable name="TITLE"
          expression="titleCase(SELECTION)"
          defaultValue="the quick brown fox"
          alwaysStopAt="true"/>
```

## Design Guidance

- Put simple file-name or language checks in the context type.
- Put value transformation logic in the macro.
- Do not read heavy PSI or indexes unless the template truly requires it.
- Favor deterministic output over clever behavior.
- If the request only needs a new abbreviation or new variable order, stay in XML and do not add code.

## Kotlin Example Set

Use this Kotlin-first bundle together:

- `assets/examples/Markdown.xml`
- `assets/examples/plugin.xml.fragment.xml`
- `assets/examples/MarkdownContext.kt`
- `assets/examples/TitleCaseMacro.kt`

This set mirrors the JetBrains sample but keeps the plugin-side code in idiomatic Kotlin.
