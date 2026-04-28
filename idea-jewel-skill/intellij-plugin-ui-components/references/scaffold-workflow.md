# Scaffold Workflow

Use the script in `scripts/scaffold_ui_recipe.py` when the user wants starter files instead of inline snippets.

## Command Pattern

```bash
python3 scripts/scaffold_ui_recipe.py <recipe> \
  --package com.example.plugin.ui \
  --class-prefix Sample \
  --display-name "Sample" \
  --output-dir /tmp/sample-ui
```

List recipes first when the user does not know which scaffold to request:

```bash
python3 scripts/scaffold_ui_recipe.py --list
```

Preview generated paths without writing files:

```bash
python3 scripts/scaffold_ui_recipe.py tool-window \
  --package com.example.plugin.ui \
  --class-prefix Search \
  --display-name "Search" \
  --output-dir /tmp/search-ui \
  --dry-run
```

## Available Recipes

- `tool-window`: tool window factory plus `plugin.xml.fragment`
- `dialog-action`: Kotlin UI DSL dialog plus action plus `plugin.xml.fragment`
- `intention-action`: `PsiElementBaseIntentionAction` plus `plugin.xml.fragment`
- `notification-action`: notification group plus action plus `plugin.xml.fragment`
- `search-everywhere-contributor`: contributor plus factory plus `plugin.xml.fragment`
- `status-bar-widget`: widget factory plus widget plus `plugin.xml.fragment`
- `theme-metadata`: theme metadata JSON plus Kotlin named-color helper plus `plugin.xml.fragment`
- `jcef-tool-window`: browser-backed tool window plus `plugin.xml.fragment`

## Safety Rules

- The script refuses to overwrite files unless `--force` is provided.
- Generated XML files are intentionally `*.xml.fragment` files so they can be merged into an existing plugin descriptor.
- Point `--output-dir` at a temp directory when exploring, or at a plugin project root when you are ready to merge files.
- Use `jcef-tool-window` only when the request genuinely needs browser rendering.

## Parameter Notes

- `--package`: required Kotlin package for generated classes.
- `--class-prefix`: class stem used in generated filenames and symbols.
- `--display-name`: user-facing label for tool windows, actions, notifications, or metadata names.
- `--language-id`: only used by `intention-action`; required because the target language ID is plugin-specific.
- `--url`: only used by `jcef-tool-window`; defaults to the JetBrains Plugin SDK site.
- `--theme-key-prefix`: only used by `theme-metadata`; defaults to `class-prefix`.
