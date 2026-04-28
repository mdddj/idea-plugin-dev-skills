# Chooser API Map

## Canonical Sources

- JetBrains Plugin SDK: https://plugins.jetbrains.com/docs/intellij/file-and-class-choosers.html
- JetBrains Plugin SDK dependencies guide: https://plugins.jetbrains.com/docs/intellij/plugin-dependencies.html

## Selection Matrix

| Need | API | Returns | Notes |
| --- | --- | --- | --- |
| Choose files or folders from the filesystem | `FileChooser` with `FileChooserDescriptor` | `VirtualFile` or `VirtualFile[]` | Use for plain filesystem selection, including directories. |
| Attach a chooser to a form field | `TextFieldWithBrowseButton` | Text plus chooser callback | Good for settings/configuration UIs. |
| Choose a project file by PSI | `TreeFileChooserFactory` | `PsiFile` | Use when the user wants project-aware file selection. |
| Choose a Java class | `TreeClassChooserFactory` | `PsiClass` | Java-specific. Supports scope and inheritance-oriented chooser variants. |
| Choose a Java package | `PackageChooserDialog` | Package selection | Java-specific. Use for package-oriented UI. |

## File Chooser Pattern

Use `FileChooser.chooseFile(...)` for a single item and `FileChooser.chooseFiles(...)` for multiple items. Back the chooser with a `FileChooserDescriptor` that defines whether files, directories, archives, jars, or multiple items are allowed.

Use `FileChooserDescriptorFactory` when a standard descriptor already matches the request. Customize the descriptor only for the constraints the user actually asked for, such as directories-only selection or multi-select behavior.

Minimal pattern:

```kotlin
val descriptor = FileChooserDescriptor(true, false, false, false, false, false)

FileChooser.chooseFile(descriptor, project, null) { file ->
    // Persist a stable value such as file.path
}
```

## Browse Field Pattern

`TextFieldWithBrowseButton` is the documented way to add chooser behavior to a text field in plugin forms. Prefer its browse listener helpers over a custom button and manual dialog plumbing.

Minimal pattern:

```kotlin
val field = TextFieldWithBrowseButton()
field.addBrowseFolderListener("Select Path", null, project, descriptor)
```

## Java-Specific Choosers

`TreeClassChooserFactory` and `PackageChooserDialog` depend on the Java plugin. Before using them:

- Add `bundledPlugin("com.intellij.java")` in the Gradle IntelliJ Platform dependency block.
- Add `<depends>com.intellij.java</depends>` to the plugin descriptor.

Dependency snippet:

```kotlin
dependencies {
    intellijPlatform {
        bundledPlugin("com.intellij.java")
    }
}
```

```xml
<depends>com.intellij.java</depends>
```

Do not use Java-specific chooser APIs when the plugin must work without the Java plugin or when the target IDE may not ship it.

## Practical Rules

- Choose `FileChooser` for filesystem paths, not PSI elements.
- Choose `TreeFileChooserFactory` when the result should be a project file represented by PSI.
- Choose `TreeClassChooserFactory` only for Java class selection; for other languages, use another symbol/search surface.
- Store paths, qualified names, or package names after selection instead of holding chooser UI state.
