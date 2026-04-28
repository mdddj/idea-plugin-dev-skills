# Postfix Templates Reference

## Official docs

- Postfix Completion: https://plugins.jetbrains.com/docs/intellij/postfix-completion.html
- Postfix Templates: https://plugins.jetbrains.com/docs/intellij/postfix-templates.html
- Advanced Postfix Templates: https://plugins.jetbrains.com/docs/intellij/advanced-postfix-templates.html
- Extension point list: https://plugins.jetbrains.com/docs/intellij/intellij-platform-extension-point-list.html

## API map

Use `com.intellij.codeInsight.template.postfixTemplateProvider` to register a `PostfixTemplateProvider`.

Choose the base API by behavior:

- `PostfixTemplate`: fully custom applicability and expansion logic.
- `PostfixTemplateWithExpressionSelector`: choose among multiple candidate expressions.
- `StringBasedPostfixTemplate`: live-template-text expansion using `$expr$` and `$END$`.
- `EditablePostfixTemplate`: editable postfix templates stored in settings.
- `EditablePostfixTemplateWithMultipleExpressions`: editable templates with expression constraints.
- `SurroundPostfixTemplateBase`: wrap an existing `Surrounder`.

## Minimal provider skeleton

```kotlin
class ExamplePostfixTemplateProvider : PostfixTemplateProvider {
    override fun getTemplates(): Set<PostfixTemplate> = setOf(
        ExampleWrapPostfixTemplate(this),
    )

    override fun isTerminalSymbol(currentChar: Char): Boolean = currentChar == '.'

    override fun preExpand(file: PsiFile, editor: Editor) {}

    override fun afterExpand(file: PsiFile, editor: Editor) {}

    override fun preCheck(copyFile: PsiFile, realEditor: Editor, currentOffset: Int): PsiFile = copyFile

    override fun getId(): String = "com.example.postfix"
}
```

If the language needs no special file preparation, keep the three lifecycle methods no-op or identity. Do not mutate the real editor inside `preCheck()`.

## Minimal string-based template skeleton

```kotlin
class ExampleWrapPostfixTemplate(
    provider: PostfixTemplateProvider,
    selector: PostfixTemplateExpressionSelector,
) : StringBasedPostfixTemplate(
    "wrap",
    "wrap(expr)",
    selector,
    provider,
) {
    override fun getTemplateString(element: PsiElement): String =
        "wrap(\$expr\$)\$END\$"

    override fun getElementToRemove(expr: PsiElement): PsiElement = expr
}
```

Use a custom subclass of `PostfixTemplate` only when a live-template string is insufficient.

## Resource layout

For a template class `com.example.postfix.ExampleWrapPostfixTemplate`, place resources under:

```text
src/main/resources/postfixTemplates/ExampleWrapPostfixTemplate/
```

Required files:

- `description.html`
- `before.kt.template` and `after.kt.template` for Kotlin examples
- `before.java.template` and `after.java.template` for Java examples

Preview helpers:

- `<spot>` highlights the important expression or caret location.
- `$key` is replaced by the actual postfix key in Settings previews.

Example:

```text
before.kt.template
<spot>value</spot>$key

after.kt.template
wrap(value)<spot></spot>
```

If the plugin is multi-module and merges resources into one JAR, keep description paths unique so later-packed files do not overwrite earlier ones.

## Review checklist

- Provider is registered through `com.intellij.codeInsight.template.postfixTemplateProvider`.
- Provider `id` is stable.
- Template applicability is narrower than or equal to intended language semantics.
- `example` matches the resulting code shape.
- `description.html` and before/after templates exist under `postfixTemplates/`.
- Settings preview works and shows the template description.
