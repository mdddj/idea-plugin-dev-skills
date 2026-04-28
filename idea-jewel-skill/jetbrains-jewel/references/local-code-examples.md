# Local Code Examples

## Overview

Use these local files first when the user asks for runnable or copyable Jewel code. They are compact examples written for this skill, so the agent does not need to fetch the upstream GitHub samples just to answer common usage questions.

## Quick picks by scenario

- Standalone app shell: `example-standalone-main.kt`
- Standalone component usage: `example-buttons.kt`, `example-menu.kt`, `example-tabs.kt`, `example-text-field.kt`
- IntelliJ plugin settings panel: `example-configurable-panel.kt`
- IntelliJ project workspace settings: `example-project-settings-panel.kt`
- IntelliJ plugin tool window: `example-toolwindow-compose-tab.kt`
- IntelliJ plugin popup or bridge host: `example-bridge-popup-panel.kt`, `example-swing-bridge-theme.kt`
- IntelliJ modal dialog: `example-dialog-wrapper.kt`
- Markdown preview: `example-markdown-preview.kt`
- Markdown split editor: `example-markdown-split-editor.kt`
- IntelliJ plugin Markdown: `example-bridge-markdown-preview.kt`

## Prefer these local examples first

- [references/example-standalone-main.kt](example-standalone-main.kt)
- [references/example-buttons.kt](example-buttons.kt)
- [references/example-radio-buttons.kt](example-radio-buttons.kt)
- [references/example-checkboxes.kt](example-checkboxes.kt)
- [references/example-combo-box.kt](example-combo-box.kt)
- [references/example-progress-bar.kt](example-progress-bar.kt)
- [references/example-links.kt](example-links.kt)
- [references/example-icons.kt](example-icons.kt)
- [references/example-menu.kt](example-menu.kt)
- [references/example-menu-advanced.kt](example-menu-advanced.kt)
- [references/example-split-layout.kt](example-split-layout.kt)
- [references/example-banners.kt](example-banners.kt)
- [references/example-scrollbars.kt](example-scrollbars.kt)
- [references/example-typography.kt](example-typography.kt)
- [references/example-borders.kt](example-borders.kt)
- [references/example-brushes.kt](example-brushes.kt)
- [references/example-chips.kt](example-chips.kt)
- [references/example-tree.kt](example-tree.kt)
- [references/example-segmented-controls.kt](example-segmented-controls.kt)
- [references/example-slider.kt](example-slider.kt)
- [references/example-text-field.kt](example-text-field.kt)
- [references/example-text-area.kt](example-text-area.kt)
- [references/example-tabs.kt](example-tabs.kt)
- [references/example-toolbar-actions.kt](example-toolbar-actions.kt)
- [references/example-tooltip.kt](example-tooltip.kt)
- [references/example-configurable-panel.kt](example-configurable-panel.kt)
- [references/example-project-settings-panel.kt](example-project-settings-panel.kt)
- [references/example-bridge-popup-panel.kt](example-bridge-popup-panel.kt)
- [references/example-dialog-wrapper.kt](example-dialog-wrapper.kt)
- [references/example-markdown-preview.kt](example-markdown-preview.kt)
- [references/example-markdown-advanced.kt](example-markdown-advanced.kt)
- [references/example-markdown-split-editor.kt](example-markdown-split-editor.kt)
- [references/example-bridge-markdown-preview.kt](example-bridge-markdown-preview.kt)
- [references/example-swing-bridge-theme.kt](example-swing-bridge-theme.kt)
- [references/example-toolwindow-compose-tab.kt](example-toolwindow-compose-tab.kt)

## When to use which file

- `example-standalone-main.kt`: standalone app shell with `IntUiTheme`, `DecoratedWindow`, and `ProvideMarkdownStyling`
- `example-buttons.kt`: ordinary buttons, icon buttons, and action buttons
- `example-radio-buttons.kt`: mutually exclusive options with outline variants
- `example-checkboxes.kt`: boolean and tri-state checkbox rows with outline variants
- `example-combo-box.kt`: `ListComboBox` plus `EditableListComboBox`
- `example-progress-bar.kt`: determinate, indeterminate, and circular progress indicators
- `example-links.kt`: `Link`, `ExternalLink`, and `DropdownLink`
- `example-icons.kt`: icon loading with hint-based badge and stroke examples
- `example-menu.kt`: `PopupMenu` with selectable items and a submenu
- `example-menu-advanced.kt`: submenu plus `adContent` popup menu pattern
- `example-split-layout.kt`: nested horizontal and vertical split layouts with `SplitLayoutState`
- `example-banners.kt`: default and inline banner usage
- `example-scrollbars.kt`: `VerticallyScrollableContainer` wrapped around a `LazyColumn`
- `example-typography.kt`: `JewelTheme.typography` text styles
- `example-borders.kt`: border alignment and stroke usage with Jewel border modifier
- `example-brushes.kt`: simplified `Brush.cssLinearGradient(...)` preview with sliders for angle, scale, and offset
- `example-chips.kt`: `Chip`, `ToggleableChip`, and `RadioButtonChip`
- `example-tree.kt`: minimal searchable tree using `buildTree` and `SpeedSearchableTree`
- `example-segmented-controls.kt`: `SegmentedControl` with selectable button data
- `example-slider.kt`: free and stepped slider variants
- `example-text-field.kt`: `TextField` with placeholder, search icon, and trailing clear button
- `example-text-area.kt`: multi-line `TextArea` with normal and error states
- `example-tabs.kt`: `TabStrip` with closable tabs and an add button
- `example-toolbar-actions.kt`: search field plus `ActionButton`, icon toggles, and scope dropdown for toolbar rows
- `example-tooltip.kt`: attaching a `Tooltip` to a Jewel component
- `example-configurable-panel.kt`: persisted `SearchableConfigurable` skeleton backed by `JewelComposePanel` and `SimplePersistentStateComponent`
- `example-project-settings-panel.kt`: project-level workspace settings panel backed by `StoragePathMacros.WORKSPACE_FILE`
- `example-bridge-popup-panel.kt`: plugin-hosted popup demo showing `PopupMenu` inside `JewelComposePanel`
- `example-dialog-wrapper.kt`: modal `DialogWrapper` host that embeds Jewel content through `JewelComposePanel`
- `example-markdown-preview.kt`: scalable Markdown rendering with `MarkdownProcessor`, `ProvideMarkdownStyling`, and `LazyMarkdown`
- `example-markdown-advanced.kt`: editor-preview Markdown pipeline with GitHub alerts, tables, strikethrough, image rendering, and URL handling
- `example-markdown-split-editor.kt`: editor plus preview layout using Jewel `TextArea`
- `example-bridge-markdown-preview.kt`: IntelliJ plugin Markdown preview using bridge `ProvideMarkdownStyling(project, ...)`
- `example-swing-bridge-theme.kt`: minimal IntelliJ plugin panel wrapped in `SwingBridgeTheme`
- `example-toolwindow-compose-tab.kt`: `ToolWindowFactory` snippet using `ToolWindow.addComposeTab(...)` without re-wrapping the bridge theme

## When to still inspect upstream samples

Read the upstream sample paths only when:

- the user asks for a specific component not covered by the local examples
- the user needs a large showcase layout rather than a minimal pattern
- the user needs the exact sample implementation from `platform/jewel/samples`

In those cases, use [samples-and-recipes.md](samples-and-recipes.md) to jump to the right upstream file.
