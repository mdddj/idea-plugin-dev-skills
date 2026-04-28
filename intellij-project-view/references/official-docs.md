# IntelliJ Project View Official Docs

## Canonical sources

- Project View overview: <https://plugins.jetbrains.com/docs/intellij/project-view.html>
- Modifying Project View Structure: <https://plugins.jetbrains.com/docs/intellij/tree-structure-view.html>
- `ProjectViewNodeDecorator` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/projectView/ProjectViewNodeDecorator.kt>
- `TreeStructureProvider` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/editor-ui-api/src/com/intellij/ide/projectView/TreeStructureProvider.java>
- `ProjectViewNestingRulesProvider` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-api/src/com/intellij/ide/projectView/ProjectViewNestingRulesProvider.java>
- `ProjectView` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/projectView/ProjectView.java>
- `AbstractProjectViewPane` source: <https://github.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/ide/projectView/impl/AbstractProjectViewPane.java>
- JetBrains sample plugin for `TreeStructureProvider`: <https://github.com/JetBrains/intellij-sdk-code-samples/tree/main/tree_structure_provider>

## Fast routing

Use this API when the request is about:

- `ProjectViewNodeDecorator`
  - Changing how an existing node looks.
  - Appending status text.
  - Adjusting icon, color, or presentation data.
  - Not changing which nodes exist.

- `TreeStructureProvider`
  - Hiding or filtering nodes.
  - Adding synthetic grouping nodes.
  - Reordering or replacing child collections.
  - Supplying UI data for the current selection.

- `ProjectViewNestingRulesProvider`
  - Grouping related files under a peer file by filename suffix.
  - Common example: source file plus generated or minified output.

- `AbstractProjectViewPane`
  - Creating a separate Project tool window pane or subview.
  - Supplying pane title, icon, weight, component, selection behavior, and refresh behavior.

## Extension point map

Register these in `plugin.xml`:

```xml
<extensions defaultExtensionNs="com.intellij">
  <projectViewNodeDecorator implementation="com.example.MyDecorator"/>
  <treeStructureProvider implementation="com.example.MyTreeStructureProvider"/>
  <projectViewNestingRulesProvider implementation="com.example.MyNestingRulesProvider"/>
  <projectViewPane implementation="com.example.MyProjectViewPane"/>
</extensions>
```

Use only the extension points that match the request. Most tasks need exactly one.

## API notes that matter

### `ProjectViewNodeDecorator`

Primary method from source:

```kotlin
fun decorate(node: ProjectViewNode<*>, data: PresentationData)
```

Practical guidance:

- Read the represented object with `node.value` or `node.virtualFile`.
- Change only `PresentationData`.
- Keep the work cheap because decoration happens during rendering.
- Ignore the deprecated package-dependencies overload. The current source marks it deprecated and states it is never called.

Minimal Kotlin shape:

```kotlin
class GeneratedFileDecorator : ProjectViewNodeDecorator {
  override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
    val file = node.virtualFile ?: return
    if (!file.isDirectory && file.name.endsWith(".generated.kt")) {
      data.addText(" generated", SimpleTextAttributes.GRAYED_ATTRIBUTES)
    }
  }
}
```

### `TreeStructureProvider`

Primary method from source:

```java
Collection<AbstractTreeNode<?>> modify(
  AbstractTreeNode<?> parent,
  Collection<AbstractTreeNode<?>> children,
  ViewSettings settings
)
```

Practical guidance:

- Use it for structural changes only.
- Return `children` unchanged when nothing should change.
- Implement `DumbAware` only if the logic does not require indexes.
- New code should prefer `uiDataSnapshot(...)`; the older `getData(...)` path is deprecated.

Minimal Kotlin shape:

```kotlin
class TextOnlyTreeStructureProvider : TreeStructureProvider, DumbAware {
  override fun modify(
    parent: AbstractTreeNode<*>,
    children: Collection<AbstractTreeNode<*>>,
    settings: ViewSettings,
  ): Collection<AbstractTreeNode<*>> {
    return children.filterNot { child ->
      val file = (child as? PsiFileNode)?.virtualFile ?: return@filterNot false
      !file.isDirectory && file.extension != "txt"
    }
  }
}
```

The official JetBrains sample uses this exact pattern to keep only plain-text files visible in Project View.

### `ProjectViewNestingRulesProvider`

Primary method from source:

```java
void addFileNestingRules(Consumer consumer)
```

Practical guidance:

- Rules are based on filename suffixes only.
- Provide the longest useful suffixes.
- Typical mapping: parent `.js`, child `.min.js`.

Minimal Kotlin shape:

```kotlin
class WebAssetNestingRules : ProjectViewNestingRulesProvider {
  override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
    consumer.addNestingRule(".js", ".min.js")
    consumer.addNestingRule(".css", ".min.css")
  }
}
```

### `ProjectView` refresh

From platform source:

- `ProjectView.getInstance(project).refresh()` refreshes the current pane asynchronously.
- `ProjectView.changeView(...)` switches panes or subviews.
- Pane-level refresh can be narrower with `AbstractProjectViewPane.updateFromRoot(...)` or `updateFrom(...)` when you already own the pane implementation.

Use refresh when the tree depends on mutable plugin state and that state changes outside normal VFS or PSI refresh paths.

### `AbstractProjectViewPane`

Key responsibilities from source:

- `getTitle()`
- `getIcon()`
- `getId()`
- `createComponent()`
- `getWeight()`
- `createSelectInTarget()`
- optional `getSubIds()` and `getPresentableSubIdName(...)`

Important source detail:

- `AbstractProjectViewPane` is itself an extension point host via `com.intellij.projectViewPane`.
- The base class listens for `TreeStructureProvider` and `ProjectViewNodeDecorator` extension changes and rebuilds the tree when those extensions are added or removed.

Choose a custom pane only when the user needs a separate tab or a meaningfully different navigation model. For ordinary filtering or visual annotation, stick with the narrower APIs above.

## Common decision rules

- Need a badge or extra text beside a file? Use `ProjectViewNodeDecorator`.
- Need to hide non-matching files or group nodes? Use `TreeStructureProvider`.
- Need parent/child file grouping by suffix? Use `ProjectViewNestingRulesProvider`.
- Need a new pane in the Project tool window? Use `AbstractProjectViewPane`.
- Need tree updates after plugin state changes? Call `ProjectView.refresh()` or pane update APIs.

## Validation checklist

- The implementation class matches the registered extension point.
- The plugin does not use a decorator to change structure.
- The provider returns the original children when no modification is needed.
- File nesting rules are suffix-based and ordered intentionally.
- Dynamic changes trigger refresh.
- Custom panes have a stable pane ID and a working `Select In` target.
