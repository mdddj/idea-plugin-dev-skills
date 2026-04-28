# Official Docs

## Scope

- Primary doc: https://plugins.jetbrains.com/docs/intellij/misc-swing-components.html
- Toolbar companion doc: https://plugins.jetbrains.com/docs/intellij/action-system.html
- The JetBrains page was updated on March 16, 2026.

## Routing Matrix

- Need a blocking confirmation, short info, error, or text input dialog: use `Messages`.
- Need two panes with a draggable divider: use `JBSplitter`.
- Need tabs inside one plugin UI surface: use `JBTabs`, `TabInfo`, and `JBTabsPresentation`.
- Need action buttons around a `JList`, `JTree`, `JTable`, or `TableView`: use `ToolbarDecorator`.
- Need a plain horizontal or vertical action strip: use `ActionToolbar`.

## Messages

- The official docs call `Messages` a utility for standard dialogs and explicitly say to use it sparingly because the dialogs are modal.
- Documented use cases include info, error, question, yes-no, input, and choose dialogs.
- Source-backed entry points include:
  - `showInfoMessage(...)`
  - `showMessageDialog(...)`
  - `showYesNoDialog(...)`
  - `showYesNoCancelDialog(...)`
  - `showInputDialog(...)`
  - `showMultilineInputDialog(...)`
  - `showPasswordDialog(...)`
  - `showInputDialogWithCheckBox(...)`
  - `showTextAreaDialog(...)`
- Non-obvious source notes:
  - The source comments recommend the `MessageDialogBuilder` yes-no and yes-no-cancel builders when possible, because they follow native OS behavior.
  - `showChooseDialog(...)` is deprecated in source and should not be used as a modern default.
  - HTML text can be rendered in message panes, and hyperlink handling is wired through `BrowserHyperlinkListener`.

## JBSplitter

- The official docs recommend `JBSplitter` for IntelliJ split panes.
- Constructors let you choose orientation and initial proportion.
- The non-obvious part from source is persistence:
  - `setSplitterProportionKey(...)` assigns the persistence key.
  - `setAndLoadSplitterProportionKey(...)` assigns the key and immediately reloads the stored value.
  - `addNotify()` reloads the saved proportion.
  - `setProportion(...)` writes the current value back to `PropertiesComponent`.
- Use a stable non-empty key when the divider position should survive IDE restarts.

## JBTabs And TabInfo

- The docs point to `JBTabs` and `TabInfo` for tabbed panes.
- `JBTabs` is the interface. A concrete JetBrains implementation available in source is `JBTabsImpl(project)` or `JBTabsImpl(project, parentDisposable)`.
- Core `JBTabs` flow:
  - `addTab(...)`
  - `removeTab(...)`
  - `select(...)`
  - `getSelectedInfo()`
  - `getPresentation()`
  - `getComponent()`
  - `setNavigationActionBinding(...)`
  - `setPopupGroup(...)`
- `TabInfo` is the per-tab model. Source-backed mutators include:
  - `setText(...)`
  - `setIcon(...)`
  - `setTooltipText(HtmlChunk)`
  - `setActions(...)`
  - `setActionsContextComponent(...)`
  - `setTabLabelActions(...)`
  - `setTabPaneActions(...)`
  - `setPreferredFocusableComponent(...)`
  - `setTabColor(...)`
- `JBTabsPresentation` is where you tune behavior:
  - `setSingleRow(...)`
  - `setTabsPosition(...)`
  - `setTabDraggingEnabled(...)`
  - `setSupportsCompression(...)`
  - `setSideComponentVertical(...)`
  - `setSideComponentOnTabs(...)`
  - `setToDrawBorderIfTabsHidden(...)`
  - `setEmptyText(...)`

## Toolbars

- The misc components page routes toolbar work to the Action System docs.
- `ActionToolbar` is the low-level choice when you already have an `ActionGroup`.
- Source-backed pattern:
  - `ActionManager.getInstance().createActionToolbar(place, group, horizontal)`
  - set `toolbar.targetComponent`
  - add `toolbar.component` to your panel
- `ToolbarDecorator` is the high-level choice for data components with common add, remove, edit, and move actions.
- Supported factory entry points in source include:
  - `createDecorator(JList)`
  - `createDecorator(JTree)`
  - `createDecorator(JTable)`
  - `createDecorator(TableView, ElementProducer?)`
  - generic `createDecorator(JComponent)` only when the component is one of the supported families
- Non-obvious source notes:
  - `createPanel()` wraps supported list, tree, and table components in a `JBScrollPane` automatically.
  - `createDecorator(JComponent)` throws for unsupported component types instead of silently degrading.
  - Useful customization points include `setToolbarPosition(...)`, `disableAddAction()`, `setAddAction(...)`, `setRemoveAction(...)`, `addExtraAction(...)`, and `setPreferredSize(...)`.

## Source URLs

- https://plugins.jetbrains.com/docs/intellij/misc-swing-components.html
- https://plugins.jetbrains.com/docs/intellij/action-system.html
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/openapi/ui/Messages.java
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/JBSplitter.java
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/tabs/JBTabs.java
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/tabs/JBTabsPresentation.kt
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/tabs/TabInfo.kt
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/tabs/impl/JBTabsImpl.kt
- https://github.com/JetBrains/intellij-community/blob/712142e0a4218b139639f9a86d2304bc7651b08b/platform/platform-api/src/com/intellij/ui/ToolbarDecorator.java
