# Practical Plugin Tips

This file distills reusable plugin-development techniques from the blog post [Idea插件开发技巧](https://itbug.shop/blog/blog-552), published on 2025-12-16. Treat these notes as practice-driven guidance, not as official JetBrains API documentation.

## Syntax-Highlighted HTML

- Use `HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet()` when the feature needs IDE-highlighted code exported as HTML.
- Pass the target `Project`, `Language`, source text, and a `StringBuilder`; return the accumulated HTML string.
- Use this for copy-as-HTML, rich previews, or browser-rendered code snippets inside plugin UI.

## JBList and JTree Hardening

- On `JBList`, install keyboard scrolling with `ScrollingUtil.installActions(list)`.
- Use `ListUiUtil.Selection.installSelectionOnRightClick(list)` and `installSelectionOnFocus(list)` so context menus act on the expected row.
- Register popup menus with `PopupHandler.installPopupMenu(list, actionGroup, place)`.
- Use `TreeUIHelper.getInstance().installListSpeedSearch(list) { itemText }` for fast filtering and `SpeedSearchUtil.applySpeedSearchHighlighting(...)` inside custom renderers.
- On trees, install popup handling with `CustomizationUtil.installPopupHandler(tree, place, actionGroupId)` when working with action groups.
- Add `TreeUIHelper.getInstance().installTreeSpeedSearch(tree)` for quick lookup.
- Add `SmartExpander.installOn(tree)`, `TreeHoverListener.DEFAULT.addTo(tree)`, and `TreeUtil.installActions(tree)` for more native-feeling tree interactions.

## Clipboard and Editor Font Helpers

- Copy plain text through `CopyPasteManager.getInstance().setContents(StringSelection(text))`.
- Fetch the editor plain font from `EditorColorsManager.getInstance().globalScheme.getFont(EditorFontType.PLAIN)` when custom UI should align with editor typography.

## Intention Actions

- Register editor intentions under the `intentionAction` extension.
- Provide both `className` and `language` in plugin.xml.
- Implement `IntentionAction` directly, or extend `PsiElementBaseIntentionAction` when the action depends on PSI context.
- Keep `isAvailable()` narrowly scoped so the intention appears only on relevant elements.
- Use intentions for local code generation, refactoring helpers, or context-sensitive copy operations.

## Search Everywhere Contributor

- Register a custom tab with the `searchEverywhereContributor` extension.
- Implement a `SearchEverywhereContributorFactory` and return a contributor that supplies ID, group name, renderer, selection behavior, and item fetching.
- Keep element loading off the UI thread; prefer modern background execution over `GlobalScope`.
- Use `showInFindResults()` and `isShownInSeparateTab()` intentionally so the tab behaves as expected in the Search Everywhere UI.

## Progress, Processes, and Document Saving

- Wrap unavoidable blocking work with `ProgressManager.getInstance().runProcessWithProgressSynchronously(...)` when a synchronous progress dialog is acceptable.
- Launch external commands through `GeneralCommandLine`, `ProcessHandlerFactory.getInstance().createColoredProcessHandler(...)`, and `ProcessTerminatedListener.attach(...)`.
- Call `FileDocumentManager.getInstance().saveAllDocuments()` before operations that depend on the latest in-memory edits being flushed to disk.

## When to Reach for These Tips

- Use this reference when the user asks for real-world IntelliJ plugin ergonomics beyond the official UI component pages.
- Prefer the official JetBrains references first when the problem maps cleanly to tool windows, dialogs, popups, choosers, icons, or theme APIs.
- Use these practical notes to close the gap between the official component surface and the utility code a production plugin often needs.
