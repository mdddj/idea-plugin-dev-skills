---
name: intellij-multi-module-plugin
description: Design, implement, or review IntelliJ Platform plugin projects that use multiple Gradle subprojects and Plugin Model v2 content modules. Use when requests mention multi-module IntelliJ plugins, required or optional plugin content modules, content module XML declarations with required or optional loading, module descriptor XML files outside META-INF/plugin.xml, org.jetbrains.intellij.platform.module, bundled module dependencies such as intellij.css, or adapting the novotnyr/multi-module-plugin sample.
---

# IntelliJ Multi-Module Plugin

## Overview

Use this skill to build IntelliJ Platform plugins where one root plugin artifact is assembled from multiple content modules. The model is based on Robert Novotny's `novotnyr/multi-module-plugin` sample, which demonstrates a required shared module and an optional CSS-dependent module.

## Workflow

1. Inspect the target project before editing.
   Read `settings.gradle.kts`, the root `build.gradle.kts`, `gradle.properties`, `src/main/resources/META-INF/plugin.xml`, and each subproject descriptor under `*/src/main/resources/*.xml`.

2. Decide the module split.
   Put cross-module services, shared actions, listeners, and utilities in a required shared content module. Put integration with optional IDE features or bundled modules in optional content modules.

3. Wire the root plugin descriptor.
   Keep root `META-INF/plugin.xml` minimal: plugin identity, vendor/description, and a `<content>` block listing required and optional content modules. Move actions, extensions, services, and feature-specific declarations into each module descriptor.

4. Wire Gradle composition.
   Include each subproject in `settings.gradle.kts`. In the root build, depend on every content-module project so its artifact is packaged. In `subprojects`, apply Kotlin and `org.jetbrains.intellij.platform.module` to content modules.

5. Add optional IDE dependencies only to the module that needs them.
   For example, a CSS feature module should declare `intellijPlatform { bundledModule("intellij.css") }` in that subproject, and the module descriptor should depend on both the shared module and `intellij.css`.

6. Validate with Gradle.
   Run at least `./gradlew buildPlugin` and `./gradlew verifyPlugin` when the wrapper and network dependencies are available. For behavior checks, use a `runIde` task against an IDE version that exposes or withholds the optional dependency.

## References

- Read `references/module-structure.md` when creating, modifying, or reviewing content module descriptors, naming, XML placement, or Kotlin module boundaries.
- Read `references/gradle-patterns.md` when creating or debugging `settings.gradle.kts`, root/subproject Gradle files, IntelliJ Platform Gradle Plugin 2.x dependencies, packaging, or verification tasks.

## Guardrails

- Do not put optional-platform API usage in the required module.
- Do not register feature-specific actions or extensions in the root plugin descriptor when they belong to a content module.
- Do not forget to add root `implementation(project(":module"))` dependencies; included Gradle projects are not automatically packaged as plugin content modules.
- Do not declare bundled module dependencies only in XML. Add the matching Gradle dependency so the module compiles against the API.
- Prefer Kotlin for plugin code and keep `AnAction` implementations stateless; put reusable behavior in services.
