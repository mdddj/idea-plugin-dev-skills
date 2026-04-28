#!/usr/bin/env python3
"""
Generate starter IntelliJ Platform UI recipe files.

The script writes Kotlin and resource fragments into an output directory.
It is intentionally conservative: existing files are not overwritten unless
--force is provided.
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path


def humanize_camel(value: str) -> str:
    parts = re.findall(r"[A-Z]?[a-z0-9]+|[A-Z]+(?![a-z])", value)
    return " ".join(part for part in parts if part) or value


def kebab_case(value: str) -> str:
    parts = re.findall(r"[A-Z]?[a-z0-9]+|[A-Z]+(?![a-z])", value)
    if not parts:
        parts = [value]
    return "-".join(part.lower() for part in parts)


def validate_package(package_name: str) -> None:
    pattern = r"^[A-Za-z_]\w*(\.[A-Za-z_]\w*)*$"
    if not re.match(pattern, package_name):
        raise ValueError(
            f"Invalid package name '{package_name}'. "
            "Use dot-separated Java/Kotlin identifiers."
        )


def normalize_class_prefix(value: str) -> str:
    cleaned = re.sub(r"[^A-Za-z0-9]", "", value)
    if not cleaned:
        raise ValueError("Class prefix must contain at least one letter or digit.")
    if cleaned[0].isdigit():
        cleaned = f"Ui{cleaned}"
    return cleaned[0].upper() + cleaned[1:]


def escape_kotlin(value: str) -> str:
    return value.replace("\\", "\\\\").replace('"', '\\"')


def escape_json(value: str) -> str:
    return value.replace("\\", "\\\\").replace('"', '\\"')


def escape_xml(value: str) -> str:
    return (
        value.replace("&", "&amp;")
        .replace('"', "&quot;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
    )


RECIPES = {
    "tool-window": {
        "description": "Generate a basic tool window factory and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__ToolWindowFactory.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel

class __CLASS_PREFIX__ToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout()).apply {
            add(JLabel("__DISPLAY_NAME_KT__ content"), BorderLayout.CENTER)
        }
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        content.setPreferredFocusableComponent(panel)
        toolWindow.contentManager.addContent(content)
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-tool-window.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <toolWindow
      id="__TOOL_WINDOW_ID_XML__"
      anchor="right"
      factoryClass="__PACKAGE_NAME__.__CLASS_PREFIX__ToolWindowFactory" />
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
    "dialog-action": {
        "description": "Generate a Kotlin UI DSL dialog, action, and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__Dialog.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class __CLASS_PREFIX__Dialog(project: Project) : DialogWrapper(project) {
    private var name: String = ""

    private val ui = panel {
        row("Name:") {
            textField()
                .bindText(::name)
                .focused()
        }
    }

    init {
        title = "__DISPLAY_NAME_KT__"
        init()
        initValidation()
    }

    override fun createCenterPanel(): JComponent = ui

    override fun doValidate(): ValidationInfo? {
        return if (name.isBlank()) ValidationInfo("Name is required") else null
    }
}
""".strip(),
            "src/main/kotlin/__PACKAGE_PATH__/Show__CLASS_PREFIX__DialogAction.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class Show__CLASS_PREFIX__DialogAction : DumbAwareAction("Show __DISPLAY_NAME_KT__") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        __CLASS_PREFIX__Dialog(project).showAndGet()
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-dialog.xml.fragment": """
<idea-plugin>
  <actions>
    <action
      id="__PACKAGE_NAME__.__CLASS_PREFIX__DialogAction"
      class="__PACKAGE_NAME__.Show__CLASS_PREFIX__DialogAction"
      text="Show __DISPLAY_NAME_XML__" />
  </actions>
</idea-plugin>
""".strip(),
        },
    },
    "intention-action": {
        "description": "Generate a PsiElementBaseIntentionAction and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__IntentionAction.kt": """
package __PACKAGE_NAME__

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException

class __CLASS_PREFIX__IntentionAction : PsiElementBaseIntentionAction() {
    override fun getFamilyName(): String = "__DISPLAY_NAME_KT__"

    override fun getText(): String = familyName

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        return true
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        // TODO replace with PSI-aware logic for the target language
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-intention.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <intentionAction>
      <className>__PACKAGE_NAME__.__CLASS_PREFIX__IntentionAction</className>
      <language>__LANGUAGE_ID_XML__</language>
    </intentionAction>
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
    "notification-action": {
        "description": "Generate a notification group, action, and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/Show__CLASS_PREFIX__NotificationAction.kt": """
package __PACKAGE_NAME__

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class Show__CLASS_PREFIX__NotificationAction : DumbAwareAction("Show __DISPLAY_NAME_KT__ Notification") {
    override fun actionPerformed(e: AnActionEvent) {
        Notification(
            "__NOTIFICATION_GROUP_KT__",
            "__DISPLAY_NAME_KT__ finished successfully",
            NotificationType.INFORMATION
        ).notify(e.project)
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-notification.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <notificationGroup id="__NOTIFICATION_GROUP_XML__" displayType="BALLOON" />
  </extensions>
  <actions>
    <action
      id="__PACKAGE_NAME__.__CLASS_PREFIX__NotificationAction"
      class="__PACKAGE_NAME__.Show__CLASS_PREFIX__NotificationAction"
      text="Show __DISPLAY_NAME_XML__ Notification" />
  </actions>
</idea-plugin>
""".strip(),
        },
    },
    "search-everywhere-contributor": {
        "description": "Generate a Search Everywhere contributor, factory, and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__SearchEverywhereContributor.kt": """
package __PACKAGE_NAME__

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.Processor
import javax.swing.JList
import javax.swing.ListCellRenderer

class __CLASS_PREFIX__SearchEverywhereContributor(
    private val project: Project
) : SearchEverywhereContributor<String>, DumbAware {
    private val allItems = listOf(
        "__DISPLAY_NAME_KT__ Item 1",
        "__DISPLAY_NAME_KT__ Item 2",
        "__DISPLAY_NAME_KT__ Item 3"
    )

    override fun getSearchProviderId(): String = "__PACKAGE_NAME__.__CLASS_PREFIX__SearchEverywhereContributor"

    override fun getGroupName(): String = "__DISPLAY_NAME_KT__"

    override fun getSortWeight(): Int = 1000

    override fun showInFindResults(): Boolean = false

    override fun isShownInSeparateTab(): Boolean = true

    override fun fetchElements(
        pattern: String,
        progressIndicator: ProgressIndicator,
        consumer: Processor<in String>
    ) {
        val normalized = pattern.trim()
        val items = if (normalized.isEmpty()) {
            allItems
        } else {
            allItems.filter { it.contains(normalized, ignoreCase = true) }
        }
        for (item in items) {
            progressIndicator.checkCanceled()
            if (!consumer.process(item)) return
        }
    }

    override fun processSelectedItem(selected: String, modifiers: Int, searchText: String): Boolean {
        // TODO navigate, open, or otherwise handle the selected item
        return true
    }

    override fun getElementsRenderer(): ListCellRenderer<in String> {
        return object : ColoredListCellRenderer<String>() {
            override fun customizeCellRenderer(
                list: JList<out String>,
                value: String?,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean
            ) {
                append(value ?: "", SimpleTextAttributes.REGULAR_ATTRIBUTES)
            }
        }
    }

    class Factory : SearchEverywhereContributorFactory<String> {
        override fun createContributor(initEvent: AnActionEvent): SearchEverywhereContributor<String> {
            val project = initEvent.getRequiredData(CommonDataKeys.PROJECT)
            return __CLASS_PREFIX__SearchEverywhereContributor(project)
        }

        override fun isAvailable(project: Project): Boolean = true
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-search-everywhere.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <searchEverywhereContributor implementation="__PACKAGE_NAME__.__CLASS_PREFIX__SearchEverywhereContributor$Factory" />
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
    "status-bar-widget": {
        "description": "Generate a custom status bar widget, factory, and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__StatusBarWidgetFactory.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class __CLASS_PREFIX__StatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "__CLASS_PREFIX__Status"

    override fun getDisplayName(): String = "__DISPLAY_NAME_KT__"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return __CLASS_PREFIX__StatusBarWidget()
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
""".strip(),
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__StatusBarWidget.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.Disposable
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.ui.components.JBLabel
import javax.swing.JComponent

class __CLASS_PREFIX__StatusBarWidget : CustomStatusBarWidget, Disposable {
    private val label = JBLabel("__DISPLAY_NAME_KT__")

    override fun ID(): String = "__CLASS_PREFIX__Status"

    override fun install(statusBar: StatusBar) = Unit

    override fun getComponent(): JComponent = label

    override fun dispose() = Unit
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-status-bar.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <statusBarWidgetFactory
      id="__CLASS_PREFIX__Status"
      implementation="__PACKAGE_NAME__.__CLASS_PREFIX__StatusBarWidgetFactory" />
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
    "theme-metadata": {
        "description": "Generate theme metadata registration, metadata JSON, and named-color helper.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__ThemeColors.kt": """
package __PACKAGE_NAME__

import com.intellij.ui.JBColor

object __CLASS_PREFIX__ThemeColors {
    val panelBackground: JBColor = JBColor.namedColor(
        "__THEME_KEY_PREFIX__.Panel.background",
        JBColor(0xF7F7F7, 0x3C3F41)
    )
}
""".strip(),
            "src/main/resources/META-INF/__CLASS_PREFIX__.themeMetadata.json": """
{
  "name": "__DISPLAY_NAME_JSON__",
  "fixed": false,
  "ui": [
    {
      "key": "__THEME_KEY_PREFIX__.Panel.background",
      "description": "Panel background for __DISPLAY_NAME_JSON__ UI",
      "source": "__PACKAGE_NAME__.__CLASS_PREFIX__ThemeColors",
      "since": "2026.1"
    }
  ]
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-theme-metadata.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <themeMetadataProvider path="/META-INF/__CLASS_PREFIX__.themeMetadata.json" />
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
    "jcef-tool-window": {
        "description": "Generate a browser-backed tool window and plugin.xml fragment.",
        "files": {
            "src/main/kotlin/__PACKAGE_PATH__/__CLASS_PREFIX__ToolWindowFactory.kt": """
package __PACKAGE_NAME__

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel

class __CLASS_PREFIX__ToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = if (JBCefApp.isSupported()) {
            val browser = JBCefBrowser("__URL_KT__")
            val panel = JPanel(BorderLayout()).apply {
                add(browser.component, BorderLayout.CENTER)
            }
            ContentFactory.getInstance().createContent(panel, "", false).also {
                it.setPreferredFocusableComponent(browser.component)
                it.setDisposer(object : Disposable {
                    override fun dispose() {
                        browser.dispose()
                    }
                })
            }
        } else {
            val panel = JPanel(BorderLayout()).apply {
                add(JLabel("JCEF is not supported in this IDE runtime"), BorderLayout.CENTER)
            }
            ContentFactory.getInstance().createContent(panel, "", false)
        }

        toolWindow.contentManager.addContent(content)
    }
}
""".strip(),
            "src/main/resources/META-INF/__KEBAB_NAME__-jcef-tool-window.xml.fragment": """
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <toolWindow
      id="__TOOL_WINDOW_ID_XML__"
      anchor="right"
      factoryClass="__PACKAGE_NAME__.__CLASS_PREFIX__ToolWindowFactory" />
  </extensions>
</idea-plugin>
""".strip(),
        },
    },
}


def build_variables(args: argparse.Namespace) -> dict[str, str]:
    class_prefix = normalize_class_prefix(args.class_prefix)
    display_name = args.display_name or humanize_camel(class_prefix)
    theme_key_prefix = args.theme_key_prefix or class_prefix
    notification_group = args.notification_group or f"{display_name} Notifications"
    tool_window_id = args.tool_window_id or display_name

    return {
        "PACKAGE_NAME": args.package,
        "PACKAGE_PATH": args.package.replace(".", "/"),
        "CLASS_PREFIX": class_prefix,
        "DISPLAY_NAME_KT": escape_kotlin(display_name),
        "DISPLAY_NAME_XML": escape_xml(display_name),
        "DISPLAY_NAME_JSON": escape_json(display_name),
        "THEME_KEY_PREFIX": theme_key_prefix,
        "NOTIFICATION_GROUP_KT": escape_kotlin(notification_group),
        "NOTIFICATION_GROUP_XML": escape_xml(notification_group),
        "TOOL_WINDOW_ID_XML": escape_xml(tool_window_id),
        "LANGUAGE_ID_XML": escape_xml(args.language_id or ""),
        "URL_KT": escape_kotlin(args.url),
        "KEBAB_NAME": kebab_case(class_prefix),
    }


def render_text(template: str, variables: dict[str, str]) -> str:
    rendered = template
    for key, value in variables.items():
        rendered = rendered.replace(f"__{key}__", value)
    return rendered + "\n"


def iter_rendered_files(recipe_name: str, variables: dict[str, str]) -> list[tuple[Path, str]]:
    recipe = RECIPES[recipe_name]
    rendered = []
    for relative_path, template in recipe["files"].items():
        file_path = Path(render_text(relative_path, variables).strip())
        content = render_text(template, variables)
        rendered.append((file_path, content))
    return rendered


def write_recipe(
    recipe_name: str,
    output_dir: Path,
    variables: dict[str, str],
    force: bool,
    dry_run: bool,
) -> int:
    rendered_files = iter_rendered_files(recipe_name, variables)
    for relative_path, _ in rendered_files:
        destination = output_dir / relative_path
        if destination.exists() and not force:
            raise FileExistsError(
                f"Refusing to overwrite existing file: {destination}. "
                "Re-run with --force to overwrite."
            )

    for relative_path, content in rendered_files:
        destination = output_dir / relative_path
        if dry_run:
            print(destination)
            continue
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_text(content, encoding="utf-8")
        print(f"Wrote {destination}")

    return len(rendered_files)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Scaffold IntelliJ Platform UI recipe files.")
    parser.add_argument("recipe", nargs="?", choices=sorted(RECIPES.keys()))
    parser.add_argument("--package", help="Kotlin package for generated classes.")
    parser.add_argument("--class-prefix", default="Sample", help="Class stem for generated files.")
    parser.add_argument("--display-name", help="User-facing label.")
    parser.add_argument("--output-dir", type=Path, help="Destination directory.")
    parser.add_argument("--notification-group", help="Notification group ID for notification recipe.")
    parser.add_argument("--tool-window-id", help="Tool window ID for tool-window recipes.")
    parser.add_argument("--theme-key-prefix", help="Theme metadata key prefix.")
    parser.add_argument("--language-id", help="Language ID for intention-action recipe, for example JAVA or Rust.")
    parser.add_argument(
        "--url",
        default="https://plugins.jetbrains.com/docs/intellij/user-interface-components.html",
        help="Initial URL for the jcef-tool-window recipe.",
    )
    parser.add_argument("--list", action="store_true", help="List supported recipes.")
    parser.add_argument("--dry-run", action="store_true", help="Print paths without writing files.")
    parser.add_argument("--force", action="store_true", help="Overwrite existing generated files.")
    return parser


def main() -> int:
    parser = build_parser()
    args = parser.parse_args()

    if args.list:
        for name in sorted(RECIPES.keys()):
            print(f"{name}: {RECIPES[name]['description']}")
        return 0

    if not args.recipe:
        parser.error("recipe is required unless --list is used")
    if not args.package:
        parser.error("--package is required")
    if not args.output_dir:
        parser.error("--output-dir is required")
    if args.recipe == "intention-action" and not args.language_id:
        parser.error("--language-id is required for intention-action")

    try:
        validate_package(args.package)
        variables = build_variables(args)
        file_count = write_recipe(
            recipe_name=args.recipe,
            output_dir=args.output_dir,
            variables=variables,
            force=args.force,
            dry_run=args.dry_run,
        )
    except (ValueError, FileExistsError) as exc:
        print(f"Error: {exc}", file=sys.stderr)
        return 1

    if args.dry_run:
        print(f"Dry run complete. {file_count} file(s) would be written.")
    else:
        print(f"Scaffolded {file_count} file(s) for recipe '{args.recipe}'.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
