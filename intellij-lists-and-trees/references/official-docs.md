# IntelliJ Lists and Trees Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/lists-and-trees.html` (updated 2025-04-29)

## Scope

This JetBrains page covers embedded list and tree controls used inside plugin UI forms and dialogs:

- `JBList` as the JetBrains replacement for `JList`
- `Tree` as the JetBrains replacement for `JTree`
- `ColoredListCellRenderer` and `ColoredTreeCellRenderer` for rich row rendering
- `ListSpeedSearch` and `TreeSpeedSearch` for keyboard lookup
- `ToolbarDecorator` plus `EditableModel` and `CollectionListModel` for mutable list or tree UIs

It does not describe Project View extension points or popup chooser APIs.

## API Routing

Use this matrix before writing code:

| Need | Start from | Why |
| --- | --- | --- |
| IDE-style list component | `JBList` | Adds overflow tooltip, empty text, and busy-state painting on top of `JList` |
| IDE-style tree component | `Tree` | Adds `JBList` conveniences plus wide selection painting and drag-and-drop auto-scroll |
| Rich row rendering with multiple fragments or icon | `ColoredListCellRenderer` or `ColoredTreeCellRenderer` | Documented renderer path for `append()` fragments and `setIcon()` |
| Keyboard filtering or quick jump | `ListSpeedSearch` or `TreeSpeedSearch` | Official speed-search helpers for list and tree selection |
| Add/remove/reorder toolbar around mutable content | `ToolbarDecorator` | Handles toolbar actions and, with the right model, drag-and-drop reordering |

## Documented Behaviors

### `JBList`

The page explicitly documents these additions over plain `JList`:

- Item tooltip when row text does not fit the visible width
- Empty-state message configured via `getEmptyText().setText()`
- Busy indicator enabled via `setPaintBusy()`

### `Tree`

The page describes `Tree` as the standard replacement for `JTree` and calls out:

- The same convenience features listed for `JBList`
- Wide selection painting
- Auto-scroll during drag and drop

### `Colored*CellRenderer`

Use `ColoredListCellRenderer` or `ColoredTreeCellRenderer` when the row needs more than a simple string.

- Compose text from multiple fragments with `append()`
- Attach an icon with `setIcon()`
- Rely on the renderer for selected-state colors and platform-specific rendering details

### Speed Search

The page documents these direct entry points:

- `new ListSpeedSearch(list)`
- `new TreeSpeedSearch(tree)`

When the searchable text differs from the default string form:

- Override `getElementText()`, or
- Pass the text-mapping function supported by the constructor

The docs name that function parameter as `elementTextDelegate` for `ListSpeedSearch` and `toString` for `TreeSpeedSearch`.

### `ToolbarDecorator`

Use `ToolbarDecorator` when the user can add, remove, edit, or reorder items.

The documented flow is:

1. Ensure the list model implements `EditableModel` if remove and reorder behavior is needed.
2. Prefer `CollectionListModel` as the convenient built-in implementation.
3. Call `ToolbarDecorator.createDecorator(...)`.
4. Wire standard buttons with `setAddAction()` and `setRemoveAction()`.
5. Add nonstandard buttons with `addExtraAction()` or `setActionGroup()`.
6. Call `createPanel()` and add the returned component to the UI.

The docs also explicitly note that toolbar placement above or below the component depends on the platform.

## Kotlin-First Starter Snippets

### Mutable settings list with toolbar

```kotlin
val model = CollectionListModel(items)
val list = JBList(model)
list.emptyText.text = "No items configured"

val panel = ToolbarDecorator.createDecorator(list)
  .setAddAction { _ ->
    // add item to model
  }
  .setRemoveAction { _ ->
    // remove selected item from model
  }
  .createPanel()
```

### Rich row rendering plus speed search

```kotlin
val list = JBList(items)
list.cellRenderer = object : ColoredListCellRenderer<MyItem>() {
  override fun customizeCellRenderer(
    list: JList<out MyItem>,
    value: MyItem?,
    index: Int,
    selected: Boolean,
    hasFocus: Boolean
  ) {
    if (value == null) return
    append(value.name)
    append("  ${value.detail}", SimpleTextAttributes.GRAYED_ATTRIBUTES)
    icon = value.icon
  }
}

ListSpeedSearch(list) { item -> item.name }
```

## Decision Rules

- If the request says "just show a list of configurable items", start with `CollectionListModel` + `JBList` + `ToolbarDecorator`.
- If the request says each row needs primary text, secondary text, or an icon, use a `Colored*CellRenderer`.
- If the request mentions keyboard filtering, jumping to items, or fast selection in a long list, add speed search.
- If the request says "tree should feel like the IDE", use `Tree` rather than plain `JTree`.
- If the request is about tree structure providers, Project View panes, or node decorators, stop and switch to the Project View skill instead.
