# Selection, Editor, and IDE Widgets

## File and Class Choosers

- Use `FileChooser.chooseFiles()` for file or directory selection.
- Prefer the callback-based overload because it is the only one that can show a native open dialog on macOS.
- Configure allowed selections with `FileChooserDescriptor` or `FileChooserDescriptorFactory`.
- Use `TextFieldWithBrowseButton.addBrowseFolderListener()` for the common path field plus ellipsis button pattern.
- Use `TreeFileChooserFactory` when typing a filename or choosing from project structure is the better UX than a raw file dialog.
- Use `TreeClassChooserFactory` for Java class selection and `PackageChooserDialog` for package selection.
- Add an explicit dependency on the Java plugin before recommending Java-specific choosers.
- Official page: <https://plugins.jetbrains.com/docs/intellij/file-and-class-choosers.html>

## Editor Components

- Use `EditorTextField` when the input should behave like an IDE editor rather than a plain `JTextArea`.
- Configure file type, readonly state, and single-line or multiline behavior up front.
- Override `createEditor()` and apply `EditorCustomization` when the default editor behavior is not enough.
- Use `LanguageTextField` for dialog-style language-aware input because the API is simpler than manual editor setup.
- Use `TextFieldWithCompletion` with a `TextCompletionProvider` or `TextFieldCompletionProvider` for lightweight completion.
- When building Java reference entry fields, create a code fragment via `JavaCodeFragmentFactory`, obtain a `Document`, and pass it into `EditorTextField`.
- Use a separate `Document` per field; do not share a single fragment-backed document across multiple fields.
- Official page: <https://plugins.jetbrains.com/docs/intellij/editor-components.html>

## List and Tree Controls

- Prefer `JBList` over `JList` and `Tree` over `JTree`.
- Use `JBList` or `Tree` for built-in empty text, busy painting, wide selection, and drag-aware behavior.
- Use `ColoredListCellRenderer` or `ColoredTreeCellRenderer` for mixed-style text fragments and icons.
- Install `ListSpeedSearch` or `TreeSpeedSearch` for keyboard filtering and quick navigation.
- For production lists, add `ScrollingUtil.installActions(list)` and right-click or focus selection helpers so popup actions target the selected row reliably.
- When building custom list renderers with speed search, pair `TreeUIHelper.getInstance().installListSpeedSearch(...)` with `SpeedSearchUtil.applySpeedSearchHighlighting(...)`.
- Use `ToolbarDecorator` for common add, remove, edit, and reorder flows around a list or tree.
- Back reorderable lists with an `EditableModel`; `CollectionListModel` is a common default.
- For production trees, add popup handling, `SmartExpander.installOn(tree)`, `TreeHoverListener.DEFAULT.addTo(tree)`, and `TreeUtil.installActions(tree)` to match typical IntelliJ behavior.
- Official page: <https://plugins.jetbrains.com/docs/intellij/lists-and-trees.html>

## Status Bar Widgets

- Use a status bar widget only for high-value information or settings that are relevant enough to stay visible all the time.
- Register a `StatusBarWidgetFactory` in `com.intellij.statusBarWidgetFactory`; keep its `id` aligned with `getId()`.
- Extend `StatusBarEditorBasedWidgetFactory` when the widget depends on the current editor file.
- Implement `StatusBarWidget` directly or extend `EditorBasedWidget` or `EditorBasedStatusBarPopup`.
- Use `IconPresentation`, `TextPresentation`, or `MultipleTextValuesPresentation` when a standard presentation fits.
- Implement `CustomStatusBarWidget` only when a custom component is necessary.
- Use editor-based helper `update()` methods or `StatusBarWidgetsManager.updateWidget()` to refresh widget state, and implement `LightEditCompatible` when the widget must appear in LightEdit mode.
- Official page: <https://plugins.jetbrains.com/docs/intellij/status-bar-widgets.html>

## Miscellaneous Swing Components

- Use `Messages` for truly simple message, input, or chooser dialogs, but prefer non-modal notifications whenever possible.
- Use `showCheckboxMessageDialog()` for a built-in "Do not show again" pattern.
- Prefer `JBSplitter` over `JSplitPane` for a platform-consistent splitter and persist its ratio with `setSplitterProportionKey()`.
- Consider `JBTabs` only when the platform tab look is desired; the docs note that standard Swing tabs may look more native on macOS in some cases.
- Build IntelliJ-style toolbars from `AnAction`-based actions rather than ad hoc button rows.
- Official page: <https://plugins.jetbrains.com/docs/intellij/misc-swing-components.html>
