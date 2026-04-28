# Request Routing

Use this file when the user describes a product need instead of naming the API. Route the request to one primary surface first, then refine.

| User phrasing | Choose first | Why | Start with |
| --- | --- | --- | --- |
| "做个侧边栏", "左边放个面板", "结果要常驻" | Tool window | Persistent IDE surface | `com.intellij.toolWindow`, `ToolWindowFactory` |
| "弹个表单", "做个配置框", "输入后点确定" | Dialog + Kotlin UI DSL | Structured data entry | `DialogWrapper`, `panel {}` |
| "点一下弹出选择", "临时小面板", "右键菜单后再选一步" | Popup | Lightweight transient choice | `JBPopupFactory` |
| "提示一下用户, 但别打断", "给个提醒", "编辑器上面给条提示" | Informing users or notification balloon | Non-modal feedback | `HintManager`, `EditorNotificationProvider`, `Notification` |
| "选文件", "选目录", "选类", "选包" | File or class chooser | Existing platform picker | `FileChooser`, `TreeClassChooserFactory`, `PackageChooserDialog` |
| "像代码框一样输入", "要语法高亮", "带补全输入框" | Editor component | IDE editor behavior in embedded field | `EditorTextField`, `LanguageTextField`, `TextFieldWithCompletion` |
| "Alt+Enter 出一个动作", "编辑器灯泡动作", "根据光标位置给个快捷修复" | Intention action | Context-sensitive editor action | `intentionAction`, `PsiElementBaseIntentionAction` |
| "做个列表增删改", "树形结构支持拖拽排序" | List or tree controls | Platform renderers and toolbar decorator | `JBList`, `Tree`, `ToolbarDecorator` |
| "状态栏显示状态", "右下角加个开关" | Status bar widget | Compact always-visible affordance | `StatusBarWidgetFactory` |
| "加一个 Search Everywhere 标签页", "全局搜索里搜我的内容" | Search Everywhere contributor | Custom global search tab | `searchEverywhereContributor`, `SearchEverywhereContributorFactory` |
| "内嵌网页", "HTML 预览", "浏览器面板" | JCEF | Browser rendering inside IDE | `JBCefBrowser`, `JBCefApp.isSupported()` |
| "图标适配新 UI", "主题切换颜色", "开放主题配置项" | Icons, theme metadata, UI FAQ | Theme-aware appearance | `IconLoader`, `JBColor.namedColor()`, `com.intellij.themeMetadataProvider` |

## Fast Rules

- If the UI should stay open and be revisited, choose a tool window over a dialog.
- If the interaction is a short decision or item pick, choose a popup over a dialog.
- If the message only informs the user, choose a hint, banner, or notification over `Messages`.
- If the UI is mostly form rows and validation, choose Kotlin UI DSL.
- If the request says "web", "HTML", or "preview", do not jump to JCEF until Swing is clearly insufficient.
- If the request is about editor context actions or Search Everywhere integration, read `practical-plugin-tips.md` before answering from memory.
- If the user explicitly asks for starter files for editor intentions or Search Everywhere, use `scripts/scaffold_ui_recipe.py` instead of writing the same boilerplate by hand.
