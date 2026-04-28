# Live Template XML

Official reference:
- https://plugins.jetbrains.com/docs/intellij/live-templates-configuration-file.html

## When To Read This File

Read this file when editing bundled template XML or reviewing whether a template definition is valid and well-scoped.

## Required Shape

Every bundled file should follow this structure:

```xml
<templateSet group="Markdown">
  <template name="{"
            value="[$TEXT$]($LINK$)$END$"
            description="Insert Markdown link"
            toReformat="false"
            toShortenFQNames="false">
    <variable name="TEXT" expression="" defaultValue="" alwaysStopAt="true"/>
    <variable name="LINK" expression="complete()" defaultValue="" alwaysStopAt="true"/>
    <context>
      <option name="MARKDOWN" value="true"/>
    </context>
  </template>
</templateSet>
```

## Important Attributes

### `templateSet`

- `group`: the group name shown in the Live Templates settings UI
### `template`

- `name`: the abbreviation typed in the editor
- `value`: the inserted template body; escape literal dollar signs as `$$`
- `description`: user-facing description
- `key` and `resource-bundle`: localized alternative to `description`
- `shortcut`: usually `TAB`, `ENTER`, `SPACE`, or `NONE`
- `toReformat`: whether the inserted code is reformatted
- `toShortenFQNames`: whether fully qualified names are shortened
- `useStaticImport`: whether static imports may be added
- `deactivated`: keep a bundled template disabled by default

### `variable`

- `name`: variable placeholder name used inside `value`
- `expression`: built-in function call such as `complete()` or `camelCase(SELECTION)`
- `defaultValue`: fallback literal or expression
- `alwaysStopAt`: whether tab navigation should stop on this variable

### `context`

- Add one or more `<option name="..." value="true"/>` nodes.
- Use `value="false"` to exclude a child context that would otherwise be included by a broader parent context.

## Practical Notes

- Variable order controls the tab order in the editor.
- Keep XML file names stable; moving files requires updating `plugin.xml`.
- Use `key` plus `resource-bundle` if the plugin already has a message bundle.
- `description` is fine for internal or single-language plugins.
- Add `<context>` even for obvious cases. It makes review and debugging easier.

## Common Mistakes

- Forgetting to register the XML with `defaultLiveTemplates`
- Using a context option name that does not match any registered or built-in context ID
- Omitting `$END$`, which leaves the caret at an awkward location
- Writing a custom macro when a built-in function already exists
- Using a broad context and then wondering why the template appears in unrelated files
