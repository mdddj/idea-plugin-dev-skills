---
name: intellij-extension-points
description: Build or review IntelliJ Platform plugin extension points and extension registrations. Use when requests mention plugin.xml extensionPoints or extensions, ExtensionPointName, AbstractExtensionPointBean, @Attribute, @Tag, @RequiredElement, defaultExtensionNs, dynamic extension points, ExtensionPointListener, DumbService.getDumbAwareExtensions, or IntelliJ plugin APIs designed for other plugins to implement.
---

# IntelliJ Extension Points

Use this skill when a plugin defines an extension point for other plugins, contributes to an existing extension point, or reads extension instances at runtime.

## Workflow

1. Classify the task before editing code:
   - Defining a new extension point or plugin API: read [official-docs.md](references/official-docs.md), especially "Defining Extension Points".
   - Contributing to an existing extension point: read "Declaring Extensions" and verify the extension namespace and dependency.
   - Runtime lookup, caching, dumb mode, or dynamic plugin support: read "Runtime Access", "Dumb Mode", and "Dynamic Plugin Rules".
2. Prefer Kotlin for new plugin code, but follow the platform's Java-oriented API shapes exactly where required by extension beans and annotations.
3. Choose the extension point type deliberately:
   - Use an `interface` extension point for callbacks or behavior implemented by other plugins.
   - Use a `beanClass` extension point for declarative data read from `plugin.xml`.
4. Declare extension points in `plugin.xml` under `<extensionPoints>`.
   Use exactly one of `name` or `qualifiedName`, and exactly one of `interface` or `beanClass`.
   Avoid new project- or module-level extension points; keep the EP application-level and pass `Project` or `Module` into methods when needed.
5. When registering an extension under `<extensions>`, set `defaultExtensionNs` to `com.intellij` for platform EPs or to the provider plugin ID for third-party/plugin-owned EPs.
   If consuming another plugin's EP, add the corresponding `<depends>` entry.
6. Keep extension implementations stateless.
   Put mutable runtime state in services, avoid work in constructors, avoid static initialization, and do not register the same implementation as a service or component.
7. Read extension instances through a private `ExtensionPointName<T>` whose string matches the fully qualified EP name.
   Enumerate extensions when needed instead of storing extension instances long-term.
8. Treat `dynamic="true"` as a correctness claim.
   Mark an EP dynamic only after the code either enumerates extensions on every use or maintains derived state via an `ExtensionPointListener`.
9. For dumb-mode-filtered implementations, make the base type communicate `PossiblyDumbAware` and retrieve implementations with `DumbService.getDumbAwareExtensions()`.
10. Before finishing, check Plugin DevKit inspections relevant to the change: dynamic plugin verification, static initialization in EP implementations, extension registered as service/component, and `plugin.xml` code insight/validation.

## Guardrails

- Do not guess extension point names. Derive `name` from the provider plugin ID or use a documented `qualifiedName`.
- Do not cache extension implementation instances unless the extension point explicitly supports dynamic updates and the cache is updated through listeners.
- Do not use Kotlin `object` for extension implementations; use `class`.
- Avoid `companion object` inside extension implementation classes when top-level declarations or separate objects can hold constants/helpers.
- Use `PluginException` when reporting errors caused by another plugin's extension registration or implementation so IDE error reporting attributes the problem correctly.
- If the extension point is public API for other plugins, consider exposing API sources and annotating override-only contracts where appropriate.

## References

- Read `references/official-docs.md` for canonical JetBrains URLs, XML attributes, implementation rules, examples, dynamic plugin constraints, and discovery tools.
