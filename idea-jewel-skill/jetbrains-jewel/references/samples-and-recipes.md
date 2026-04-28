# Samples And Recipes

## Overview

Use this file when the user wants concrete Jewel examples, asks which sample to copy from, or needs a Markdown renderer example instead of a conceptual explanation.

If the local examples in [local-code-examples.md](local-code-examples.md) are sufficient, prefer them before going back to the upstream sample tree.
Use upstream sources mainly for gap-filling and API parity checks, not as the default first hop.

## Contents

- Best sample entry points
- Component showcase map
- How to read the component samples
- Representative component recipe
- Markdown sample entry points
- Plugin bridge entry points
- Markdown recipes
- Answering strategy

## Best sample entry points

Start here before opening individual component files:

- `platform/jewel/samples/standalone/src/main/kotlin/org/jetbrains/jewel/samples/standalone/Main.kt`
- `platform/jewel/samples/showcase/src/main/kotlin/org/jetbrains/jewel/samples/showcase/views/ComponentsView.kt`
- `platform/jewel/samples/showcase/src/main/kotlin/org/jetbrains/jewel/samples/showcase/views/ComponentsViewModel.kt`

What these give you:

- `Main.kt` shows how the standalone app wires `IntUiTheme`, `DecoratedWindow`, and `ProvideMarkdownStyling`.
- `ComponentsView.kt` shows the shell for a browsable component gallery with a toolbar and a current demo pane.
- `ComponentsViewModel.kt` is the index of component demos. Use it as the fastest way to discover which sample file demonstrates which component.

## Component showcase map

The showcase currently registers these component demo groups through `ComponentsViewModel.kt`:

- Buttons
- Radio Buttons
- Checkboxes
- Combo Boxes
- Menus
- Chips and trees
- ProgressBar
- Icons
- Links
- Borders
- Segmented Controls
- Sliders
- Tabs
- Tooltips
- TextAreas
- TextFields
- Scrollbars
- SplitLayout
- Banners
- Typography
- Brushes

The corresponding source files live under:

- `platform/jewel/samples/showcase/src/main/kotlin/org/jetbrains/jewel/samples/showcase/components`

Use the filename as the component category:

- `Buttons.kt`
- `Checkboxes.kt`
- `ComboBoxes.kt`
- `Menus.kt`
- `Tabs.kt`
- `TextFields.kt`
- `TextAreas.kt`
- `Tooltips.kt`
- `Scrollbars.kt`
- `SplitLayouts.kt`
- `Banners.kt`

## How to read the component samples

Use this reading order:

1. Open `ComponentsViewModel.kt` to find the demo name and file.
2. Open the matching component file under `components/`.
3. If layout or navigation behavior matters, open `ComponentsView.kt`.

Common patterns in the showcase:

- A top-level demo composable named after the file, such as `Buttons()`.
- `GroupHeader` sections to separate variants.
- `FlowRow` or `Column` layouts to show multiple states side by side.
- Local `remember` state to demonstrate selected, toggleable, or disabled modes.
- `AllIconsKeys` and `PainterHint`s for icon-based controls.

## Representative component recipe: buttons and icon buttons

If the user asks how to structure a Jewel component demo, start with `Buttons.kt`.

That file is useful because it shows several patterns in one place:

- ordinary outline and default buttons
- slim buttons
- split buttons with menu content and popup content
- icon buttons
- selectable and toggleable icon buttons
- action buttons with optional tooltips

Use this as the mental template:

```kotlin
@Composable
fun Demo() {
    Column {
        GroupHeader("Buttons")
        FlowRow {
            OutlinedButton(onClick = {}) { Text("Outlined") }
            DefaultButton(onClick = {}) { Text("Default") }
        }

        GroupHeader("Icon buttons")
        FlowRow {
            IconButton(onClick = {}) {
                Icon(key = AllIconsKeys.Actions.AddFile, contentDescription = "Add")
            }
        }
    }
}
```

When the user wants a production component instead of a gallery demo, strip out the headers and comparison variants, but keep the state and icon patterns.

## Markdown sample entry points

The standalone Markdown sample lives under:

- `platform/jewel/samples/standalone/src/main/kotlin/org/jetbrains/jewel/samples/standalone/markdown`

Most useful files:

- `MarkdownPreview.kt`
- `MarkdownEditor.kt`
- `MarkdownCatalog.kt`
- `JewelReadme.kt`

Use them like this:

- `MarkdownPreview.kt` for the actual renderer pipeline
- `MarkdownEditor.kt` for a simple editor-plus-preview setup
- `MarkdownCatalog.kt` for a large fixture covering alerts, tables, code blocks, links, images, and edge cases
- `JewelReadme.kt` for a real-world document sample

## Plugin bridge entry points

The public sample tree is mostly standalone-focused. For IntelliJ plugin hosting patterns, jump to these bridge files:

- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/ToolWindowExtensions.kt`
- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/JewelComposePanelWrapper.kt`
- `platform/jewel/markdown/ide-laf-bridge-styling/src/main/kotlin/org/jetbrains/jewel/intui/markdown/bridge/BridgeProvideMarkdownStyling.kt`

Use them like this:

- `ToolWindowExtensions.kt` to confirm what `ToolWindow.addComposeTab(...)` already does for you.
- `JewelComposePanelWrapper.kt` to confirm that `JewelComposePanel` already provides `SwingBridgeTheme` and JBPopup-backed popup rendering.
- `BridgeProvideMarkdownStyling.kt` to wire plugin-side Markdown rendering and syntax highlighting when a `Project` is available.

Local companion examples in this skill:

- `references/example-configurable-panel.kt`
- `references/example-project-settings-panel.kt`
- `references/example-bridge-popup-panel.kt`
- `references/example-dialog-wrapper.kt`
- `references/example-bridge-markdown-preview.kt`
- `references/example-toolwindow-compose-tab.kt`

Do not build public plugin examples directly on top of `ide-laf-bridge/actionSystem/*` internals unless the user is
explicitly doing source-level bridge work.

There is no dedicated public Jewel sample for `DialogWrapper` hosting in `platform/jewel/samples`, so prefer the local
`example-dialog-wrapper.kt` pattern for modal plugin UI.

## Markdown recipe: processed blocks plus lazy rendering

The sample uses a two-stage flow:

1. Build a `MarkdownProcessor` with the extensions you need.
2. Convert raw Markdown into `MarkdownBlock`s.
3. Build a `MarkdownBlockRenderer` with matching renderer extensions and styling.
4. Wrap the UI with `ProvideMarkdownStyling`.
5. Render with `LazyMarkdown`.

Use this minimal pattern:

```kotlin
val processor = MarkdownProcessor(
    listOf(
        AutolinkProcessorExtension,
        GitHubAlertProcessorExtension,
        GitHubStrikethroughProcessorExtension(),
        GitHubTableProcessorExtension,
    )
)

val blocks = processor.processMarkdownDocument(rawMarkdown)

ProvideMarkdownStyling(markdownStyling, blockRenderer, NoOpCodeHighlighter) {
    LazyMarkdown(blocks = blocks)
}
```

For IntelliJ plugins, switch to the bridge styling module and prefer the `ProvideMarkdownStyling(project = ...)`
overload when a `Project` is available. See `references/example-bridge-markdown-preview.kt`.

Important sample behaviors from `MarkdownPreview.kt`:

- processing is done off the main thread before updating UI state
- renderer extensions are paired with processor extensions
- dark and light styling are selected from the current Jewel theme
- `LazyMarkdown` is preferred for longer documents
- URL clicks are handled explicitly

## Markdown recipe: editor plus preview

If the user wants a split editor/preview experience, combine the sample ideas like this:

- use `TextFieldState` as the editable Markdown source
- render the editor with Jewel `TextArea`
- feed the current text into the preview processor
- keep editor and preview separate, instead of processing inside the text area composable

`MarkdownEditor.kt` is the simplest reference for:

- loading `.md` files
- swapping between built-in sample documents
- using `JewelTheme.editorTextStyle` in a `TextArea`

## Answering strategy

When the user asks for examples:

- Point them to the exact sample file first.
- Then extract the smallest reusable pattern from that file.
- If multiple components are involved, start from `ComponentsViewModel.kt` to map demo names to files.
- If the request is Markdown-specific, start from `MarkdownPreview.kt` instead of the top-level Markdown README.
