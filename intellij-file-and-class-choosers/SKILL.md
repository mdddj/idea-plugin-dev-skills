---
name: intellij-file-and-class-choosers
description: Build or review IntelliJ Platform plugin file, directory, class, package, and browse-button choosers. Use when requests mention `FileChooser`, `FileChooserDescriptor`, `FileChooserDescriptorFactory`, `TreeFileChooserFactory`, `TreeClassChooserFactory`, `PackageChooserDialog`, `TextFieldWithBrowseButton`, chooser dialogs, file/class/package pickers, or IntelliJ plugin browse fields.
---

# IntelliJ File and Class Choosers

## Overview

Implement or debug IntelliJ Platform chooser UIs for filesystem paths, project files, Java classes, Java packages, and browse-button form fields. Keep this file focused on chooser selection and delivery flow; load [references/chooser-api-map.md](references/chooser-api-map.md) for API details, dependency snippets, and usage patterns.

## Choose The Chooser

- Use `FileChooser` when the user needs a filesystem file or directory chooser backed by `VirtualFile`.
- Use `TextFieldWithBrowseButton` when the chooser should be attached to a settings field or dialog row instead of a standalone action.
- Use `TreeFileChooserFactory` when the user needs to pick a file from the current project and work with `PsiFile`.
- Use `TreeClassChooserFactory` when the user needs a Java class picker with scope or inheritance filtering.
- Use `PackageChooserDialog` when the user needs a Java package picker.

## Workflow

1. Identify the target UI surface: action, popup, dialog, `Configurable`, or form field.
2. Pick the narrowest documented chooser API that matches the selection type instead of building a custom dialog.
3. For filesystem selection, start with a `FileChooserDescriptor` or `FileChooserDescriptorFactory` preset and then refine titles, roots, or selection flags.
4. For Java class or package choosers, add the Java plugin dependency before touching chooser code. Read [references/chooser-api-map.md](references/chooser-api-map.md) first.
5. Wire the result into the UI or model using the stable type returned by the chooser:
   - `VirtualFile` or `VirtualFile[]` for `FileChooser`
   - `PsiFile` for tree file choosers
   - `PsiClass` for class choosers
   - package name or `PsiPackage` for package choosers
6. Persist stable values such as paths, qualified class names, or package names instead of keeping chooser UI objects around.
7. If the request targets non-Java symbols, use a language-agnostic chooser or another IntelliJ search UI instead of forcing `TreeClassChooserFactory`.

## Implementation Rules

- Prefer documented helper APIs over handwritten Swing dialogs.
- When the chooser lives in a text field, prefer `TextFieldWithBrowseButton.addBrowseFolderListener(...)`.
- When filtering is requested, use descriptor/factory hooks first and avoid post-filtering arbitrary selections.
- Preserve existing plugin UI patterns unless the user explicitly wants a redesign.
- Treat cancellation as a normal path and handle empty results safely.

## Validation

- Verify Java-specific chooser code only appears when `com.intellij.java` is declared as a dependency.
- Check that imports come from IntelliJ Platform APIs, not ad hoc wrappers.
- Match single-select versus multi-select behavior to the descriptor and return type.
- Re-read [references/chooser-api-map.md](references/chooser-api-map.md) before adding uncommon chooser combinations or Java-only behavior.
