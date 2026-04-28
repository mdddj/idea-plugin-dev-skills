# Kotlin Notes

## Official docs

- Configuring Kotlin Support: https://plugins.jetbrains.com/docs/intellij/using-kotlin.html

## Rules that matter for postfix plugins

- Implement plugin.xml extensions as Kotlin `class`, not `object`.
- Keep extension classes cheap to instantiate. Avoid heavy `companion object` initialization.
- Prefer top-level helpers or private functions for selector predicates and PSI checks.
- Use the IDE-bundled Kotlin libraries whenever possible.

## Versioning and dependencies

- Kotlin 2.x is recommended for plugins targeting IntelliJ Platform 2024.3 or later and required for 2025.1 or later.
- The platform already bundles `stdlib` in normal cases. Do not package another copy unless compatibility constraints require it.
- Plugins should use the bundled `kotlinx.coroutines` library and must not ship a competing version.

## Coding style for this skill

- Write provider and template implementations in Kotlin unless the user explicitly requests Java.
- Keep constructors small and declarative.
- Prefer immutable sets for `getTemplates()`.
- Put PSI-heavy logic in small testable helpers instead of inside `expand()`.

## Practical review checklist

- No extension class is declared as Kotlin `object`.
- No custom or transitively bundled `kotlinx.coroutines` sneaks into plugin distribution.
- No unnecessary `stdlib` packaging is introduced.
- Selector helpers are readable enough to explain why a template is or is not applicable.
