# Page Map

Use this file first when the request is vague. It mirrors the official page set shown in the screenshot and points to the fastest starting API.

| Topic | Use for | Start with | Official page |
| --- | --- | --- | --- |
| Tool Windows | Persistent docked UI, result tabs, side panels | `com.intellij.toolWindow`, `ToolWindowFactory`, `ToolWindowManager.registerToolWindow()` | <https://plugins.jetbrains.com/docs/intellij/tool-windows.html> |
| Dialogs | Modal and semi-modal forms | `DialogWrapper`, `DialogBuilder` | <https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html> |
| Kotlin UI DSL | Bound forms, settings pages, validation-heavy dialogs | `panel {}`, `row {}`, `DialogPanel`, `bind*()` | <https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html> |
| Popups | Lightweight transient choices and contextual UI | `JBPopupFactory` | <https://plugins.jetbrains.com/docs/intellij/popups.html> |
| Informing Users | Non-modal feedback patterns | `HintManager`, `EditorNotificationProvider`, `GotItTooltip` | <https://plugins.jetbrains.com/docs/intellij/informing-users.html> |
| Notification Balloons | General IDE or project notifications | `notificationGroup`, `Notification`, `NotificationAction` | <https://plugins.jetbrains.com/docs/intellij/notification-balloons.html> |
| File and Class Choosers | Picking files, directories, classes, packages | `FileChooser`, `TextFieldWithBrowseButton`, `TreeClassChooserFactory` | <https://plugins.jetbrains.com/docs/intellij/file-and-class-choosers.html> |
| Editor Components | Syntax-aware text input and completion | `EditorTextField`, `LanguageTextField`, `TextFieldWithCompletion` | <https://plugins.jetbrains.com/docs/intellij/editor-components.html> |
| List and Tree Controls | Structured selection, renderers, reorderable CRUD UIs | `JBList`, `Tree`, `ToolbarDecorator` | <https://plugins.jetbrains.com/docs/intellij/lists-and-trees.html> |
| Status Bar Widgets | Compact always-visible indicators | `StatusBarWidgetFactory`, `StatusBarWidget` | <https://plugins.jetbrains.com/docs/intellij/status-bar-widgets.html> |
| Miscellaneous Swing Components | Simple dialogs, splitters, tabs, toolbars | `Messages`, `JBSplitter`, `JBTabs` | <https://plugins.jetbrains.com/docs/intellij/misc-swing-components.html> |
| Working with Icons | Plugin icons, New UI icon mapping, icon loaders | `IconLoader`, `*Icons`, `com.intellij.iconMapper` | <https://plugins.jetbrains.com/docs/intellij/icons.html> |
| User Interface FAQ | Theme-aware Swing helpers and small utilities | `JBColor`, `JBUI`, `IconUtil`, `RowIcon` | <https://plugins.jetbrains.com/docs/intellij/ui-faq.html> |
| Embedded Browser (JCEF) | HTML previews and web-based plugin surfaces | `JBCefApp`, `JBCefBrowser`, `JBCefJSQuery` | <https://plugins.jetbrains.com/docs/intellij/embedded-browser-jcef.html> |
| Color Scheme Management | Syntax and editor text attributes | `TextAttributesKey`, `DefaultLanguageHighlighterColors`, `com.intellij.additionalTextAttributes` | <https://plugins.jetbrains.com/docs/intellij/color-scheme-management.html> |
| Exposing Theme Metadata | Theme keys for custom plugin UI | `com.intellij.themeMetadataProvider`, `*.themeMetadata.json`, `JBColor.namedColor()` | <https://plugins.jetbrains.com/docs/intellij/themes-metadata.html> |
