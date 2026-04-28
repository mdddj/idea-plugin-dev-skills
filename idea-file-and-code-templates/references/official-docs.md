# Official Docs Map

Verified on 2026-04-17 against JetBrains docs and JetBrains `intellij-community` source.

## Sources

- File and code templates: <https://plugins.jetbrains.com/docs/intellij/file-and-code-templates.html>
- Providing bundled templates: <https://plugins.jetbrains.com/docs/intellij/providing-file-templates.html>
- Using templates programmatically: <https://plugins.jetbrains.com/docs/intellij/using-file-templates.html>
- `FileTemplateManager`: <https://github.com/JetBrains/intellij-community/blob/master/platform/ide-core-impl/src/com/intellij/ide/fileTemplates/FileTemplateManager.java>
- `FileTemplateUtil`: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/fileTemplates/FileTemplateUtil.java>
- `FileTemplateGroupDescriptorFactory`: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/ide/fileTemplates/FileTemplateGroupDescriptorFactory.java>
- `DefaultTemplatePropertiesProvider`: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/ide/fileTemplates/DefaultTemplatePropertiesProvider.java>
- `CreateFromTemplateHandler`: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/fileTemplates/CreateFromTemplateHandler.java>
- `SaveFileAsTemplateHandler`: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/actions/SaveFileAsTemplateHandler.java>
- Kotlin action example: <https://github.com/JetBrains/intellij-community/blob/master/plugins/kotlin/kotlin.ide/src/org/jetbrains/kotlin/idea/actions/NewKotlinFileAction.kt>

## Category to Path Map

| Category | Resource path under `src/main/resources` | Visible to users | Typical use |
| --- | --- | --- | --- |
| Files | `fileTemplates/` | Yes | Default file templates exposed in `File | New` and Settings |
| Includes | `fileTemplates/includes/` | Partially | Reusable Velocity fragments consumed with `#parse(...)` |
| Code | `fileTemplates/code/` | Yes | Code-generation snippets used by inspections, intentions, and actions |
| Internal | `fileTemplates/internal/` | No, unless surfaced by plugin code | Hidden templates behind custom actions or programmatic flows |
| Other | `fileTemplates/j2ee/` | Yes, in `Other` | Less-frequent templates grouped by a custom descriptor factory |

## Naming and File Rules

- Store bundled templates as `.ft` files.
- Add a same-basename `.html` description file for user-facing templates.
- Treat the basename as the template name. A file named `MyPluginService.kt.ft` is addressed as `MyPluginService.kt`.
- Keep names stable once actions or extension points refer to them.
- Avoid multi-file bundled templates for third-party plugins; the official docs call this unsupported for custom plugins.

## Velocity Rules

- Use Apache Velocity syntax inside `.ft` files.
- Supply reusable fragments through `includes/` and `#parse("TemplateName")`.
- Escape embedded live-template syntax with `#[[` and `]]#` if both systems need to coexist.
- Prefer platform-provided variables first, then add custom variables through `DefaultTemplatePropertiesProvider`.

## Extension Point Map

| Need | API or extension point | Notes |
| --- | --- | --- |
| Group `Other` templates | `com.intellij.fileTemplateGroup` | Implement `FileTemplateGroupDescriptorFactory` |
| Add default custom variables | `com.intellij.defaultTemplatePropertiesProvider` | Fill a `Properties` object per target directory |
| Customize creation | `com.intellij.createFromTemplateHandler` | Handle validation, naming, PSI creation, or property prep |
| Improve `Save File as Template...` | `com.intellij.saveFileAsTemplateHandler` | Clean generated text before template persistence |
| Register internal template metadata | `com.intellij.internalFileTemplate` | Use with internal templates when the plugin needs explicit registration |

## Programmatic API Map

- Use `FileTemplateManager.getInstance(project)` as the main entry point.
- Use `getTemplate(name)` for default templates.
- Use `getInternalTemplate(name)` or `findInternalTemplate(name)` for internal templates.
- Use `getCodeTemplate(name)` for code templates.
- Use `getJ2eeTemplate(name)` for `Other` templates.
- Use `getDefaultProperties()` for baseline variables.
- Use `FileTemplateUtil.createFromTemplate(...)` when you want PSI-backed file creation.
- Use `FileTemplateUtil.mergeTemplate(...)` when you only need rendered text.

## Practical Defaults

1. Start with the resource folder decision before touching Kotlin code.
2. Use `internal/` plus a custom action for focused plugin-specific creation flows.
3. Use `j2ee/` plus `FileTemplateGroupDescriptorFactory` when the user wants Settings discoverability in `Other`.
4. Copy the interaction shape from `NewKotlinFileAction` when building a Kotlin-first `File | New` flow.
