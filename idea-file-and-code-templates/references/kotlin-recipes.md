# Kotlin Recipes

Use these patterns when the request needs Kotlin plugin code around file templates. Full starter files live in `assets/starter/`.

## Register a template-backed action

Use `CreateFileFromTemplateAction` for a focused `File | New` flow that points at an internal template:

```kotlin
class CreateMyPluginServiceAction : CreateFileFromTemplateAction(
    "My Plugin Service",
    "Create a plugin service from a bundled template",
    AllIcons.Nodes.Class
) {
    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder,
    ) {
        builder
            .setTitle("New Plugin Service")
            .addKind("My Plugin Service", AllIcons.Nodes.Class, TEMPLATE_NAME)
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
        "Create My Plugin Service $newName"

    companion object {
        const val TEMPLATE_NAME = "MyPluginService.kt"
    }
}
```

## Provide extra Velocity variables

Use `DefaultTemplatePropertiesProvider` when bundled templates need stable custom variables:

```kotlin
class MyTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {
    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val packageName = JavaDirectoryService.getInstance().getPackage(directory)?.qualifiedName.orEmpty()
        props.setProperty("PACKAGE_NAME", packageName)
        props.setProperty("PLUGIN_ID", "com.example.myplugin")
    }
}
```

## Create a grouped `Other` template section

Use `FileTemplateGroupDescriptorFactory` only for `fileTemplates/j2ee` resources:

```kotlin
class MyTemplateGroupFactory : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("My Plugin", AllIcons.Nodes.Class)
        group.addTemplate(FileTemplateDescriptor("MyPluginInspection.kt", AllIcons.Nodes.Class))
        return group
    }
}
```

## Render or create files programmatically

Use platform APIs instead of manual string concatenation:

```kotlin
val manager = FileTemplateManager.getInstance(project)
val template = manager.getInternalTemplate("MyPluginService.kt")

val props = manager.defaultProperties.apply {
    put("NAME", "MyFeatureService")
    put("PACKAGE_NAME", "com.example.myplugin")
}

val element = FileTemplateUtil.createFromTemplate(template, "MyFeatureService", props, directory)
```

Use `FileTemplateUtil.mergeTemplate(...)` instead when the request only needs rendered text.

## Customize creation flow

Implement `CreateFromTemplateHandler` when the default PSI creation is not enough:

- Reject templates that do not fit the target directory.
- Precompute extra properties in `prepareProperties`.
- Return a specific PSI element from `createFromTemplate` if the caller needs more than the containing file.

Keep the default path unless the request explicitly requires custom validation or post-processing.
