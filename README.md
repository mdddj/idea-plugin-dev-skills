# IDEA Plugin Dev Skills

[中文说明](README.zh-CN.md)

A curated collection of Codex skills for IDEA and IntelliJ Platform plugin development. The repository groups common JetBrains plugin development topics so they are easier to browse, reuse, and extend.

## Repository Overview

- Contains `28` top-level skill directories.

## Top-Level Skills

| Directory | Purpose |
| --- | --- |
| `idea-file-and-code-templates` | File and Code Templates, template variables, template groups, and custom `File | New` template actions. |
| `idea-intention-preview` | Intention and Quick Fix previews, including `generatePreview()`, `IntentionPreviewInfo`, and preview tests. |
| `idea-jewel-skill` | JetBrains Jewel and Compose-style UI skills, including IntelliJ plugin UI component guidance and `platform/jewel` source navigation. |
| `idea-kotlin-dsl2-skill` | Kotlin UI DSL v2 for plugin settings pages, forms, and `DialogWrapper` UI. |
| `idea-live-templates` | Live Templates, template XML, contexts, macros, Surround With templates, and plugin exports. |
| `idea-postfix-completion` | Postfix Completion, including `PostfixTemplateProvider`, editable templates, and template description resources. |
| `intellij-action-system` | `AnAction`, menus, toolbars, shortcuts, Action Groups, and `plugin.xml` action registration. |
| `intellij-code-documentation` | Quick Documentation, hover documentation, external documentation, and `DocumentationTarget` / `DocumentationProvider`. |
| `intellij-dialog-wrapper` | `DialogWrapper`, validation, button layout, preferred focus, and persistent dialog sizing. |
| `intellij-editor-components` | `EditorTextField`, `LanguageTextField`, completion input fields, and embedded code editing experiences. |
| `intellij-embedded-terminal` | IntelliJ terminal APIs, terminal tabs, command execution, output reading, and terminal integration. |
| `intellij-extension-points` | `plugin.xml` `extensionPoints` / `extensions`, `ExtensionPointName`, bean extension points, dynamic extension points, and extension implementation constraints. |
| `intellij-file-and-class-choosers` | `FileChooser`, `TreeClassChooserFactory`, `PackageChooserDialog`, and related file/class/package pickers. |
| `intellij-icons` | Plugin SVG icons, `IconLoader`, New UI icon mapping, Tool Window icons, and dynamic icons. |
| `intellij-informing-users` | Hints, Editor Notifications, Got It tooltips, balloon notifications, and non-modal feedback. |
| `intellij-inlay-hints` | Parameter hints, declarative hints, Code Vision, preview files, and extension point registration. |
| `intellij-lists-and-trees` | `JBList`, `Tree`, speed search, renderers, `ToolbarDecorator`, and sortable structures. |
| `intellij-misc-swing-components` | Common IntelliJ Swing components such as `Messages`, `JBSplitter`, `JBTabs`, and toolbars. |
| `intellij-platform-fundamentals` | Service lifetimes, `Disposable` / `Disposer`, read/write actions, EDT/BGT threading, modality, and Message Bus. |
| `intellij-plugin-internationalization` | `DynamicBundle`, NLS annotations, resource bundles, template translation, and plugin UI text localization. |
| `intellij-popups` | `JBPopupFactory`, list popups, action popups, custom component popups, and popup placement. |
| `intellij-project-view` | Project View node decoration, tree filtering, file nesting, custom panes, and refresh logic. |
| `intellij-reference-contributor` | `PsiReferenceContributor`, reference resolution, rename support, completion, and cross-language navigation. |
| `intellij-settings` | `Configurable`, `PersistentStateComponent`, application/project settings, and sync strategies. |
| `intellij-status-bar-widgets` | `StatusBarWidgetFactory`, editor-aware widgets, popup status bar widgets, and LightEdit compatibility. |
| `intellij-tool-windows` | Tool Window registration, dynamic tool windows, content management, tabs, and anchor configuration. |
| `intellij-ui-faq` | `JBColor`, `JBUI`, insets/borders, UI Inspector, icon overlays, and theme adaptation details. |
| `intellij-virtual-file-system` | `VirtualFile`, refresh behavior, VFS listeners, path lookup, file system extensions, and metadata hooks. |

## Nested Skill Groups

Some directories are containers with more focused sub-skills:

| Path | Purpose |
| --- | --- |
| `idea-jewel-skill/intellij-plugin-ui-components` | General IntelliJ plugin UI components, including tool windows, dialogs, Kotlin UI DSL, notifications, file choosers, editor components, and status bars. |
| `idea-jewel-skill/jetbrains-jewel` | JetBrains Jewel guidance for Compose for Desktop and IntelliJ `platform/jewel` source navigation, examples, and compatibility notes. |
| `idea-jewel-skill/dist` | Reserved output directory currently kept with `.gitkeep`. |
| `idea-kotlin-dsl2-skill/intellij-kotlin-ui-dsl2` | Kotlin UI DSL v2 implementation guidance for plugin forms, configuration pages, binding, and validation. |
| `idea-postfix-completion/docs-to-skill` | A helper skill for turning online docs, local docs, and API references into new Codex skills. |

## Usage Guide

- For UI, settings pages, or tool windows, start with `idea-jewel-skill`, `idea-kotlin-dsl2-skill`, `intellij-tool-windows`, and `intellij-dialog-wrapper`.
- For editor features, start with `idea-intention-preview`, `idea-live-templates`, `idea-postfix-completion`, `intellij-inlay-hints`, and `intellij-reference-contributor`.
- For platform integration, start with `intellij-platform-fundamentals`, `intellij-extension-points`, `intellij-action-system`, `intellij-project-view`, `intellij-settings`, `intellij-embedded-terminal`, and `intellij-virtual-file-system`.
- For UX details and interface polish, start with `intellij-icons`, `intellij-informing-users`, `intellij-popups`, `intellij-ui-faq`, and `intellij-status-bar-widgets`.
