# Gradle Patterns Reference

Source sample: `https://github.com/novotnyr/multi-module-plugin`, commit `2b6659cabf57c38685d8b2798132f9a07e755efa` as cloned on 2026-06-12.

## Settings File

Use IntelliJ Platform Gradle Plugin 2.x settings support and include one Gradle subproject per content module:

```kotlin
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

rootProject.name = "mincssrel"

pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.1.20"
        id("org.jetbrains.changelog") version "2.5.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("org.jetbrains.intellij.platform.settings") version "2.16.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}

include("shared")
include("css")
```

The `rootProject.name` becomes the natural prefix for plugin content module names.

## Root Build

The root project builds the composed plugin artifact. Depend on every content module project from the root:

```kotlin
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdea
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    intellijPlatform {
        intellijIdea("2025.3")
        testFramework(TestFrameworkType.Platform)
    }
    implementation(project(":shared"))
    implementation(project(":css"))
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.intellij.platform.module")
}

val runIde261 by intellijPlatformTesting.runIde.registering {
    type = IntellijIdea
    version = "2026.1"
}
```

The root `implementation(project(":shared"))` and `implementation(project(":css"))` dependencies are what package the content module artifacts into the resulting plugin ZIP under `lib/modules`.

## Content Module Builds

Content modules can often omit their own `plugins` block if the root `subprojects {}` convention applies `org.jetbrains.kotlin.jvm` and `org.jetbrains.intellij.platform.module`.

For a module that depends on shared code and CSS PSI APIs:

```kotlin
dependencies {
    implementation(project(":shared"))
    intellijPlatform {
        bundledModule("intellij.css")
    }
}
```

Match every optional platform module used by source code with both:

- a Gradle dependency, such as `bundledModule("intellij.css")`, for compilation
- a descriptor dependency, such as `<module name="intellij.css" />`, for runtime loading

## Gradle Properties

The sample uses:

```properties
group = com.github.novotnyr
version = 1.0.0-SNAPSHOT

pluginRepositoryUrl = https://github.com/novotnyr/multi-module-plugin

kotlin.stdlib.default.dependency = false

org.gradle.configuration-cache = true
org.gradle.caching = true
```

Keep `kotlin.stdlib.default.dependency = false` when following JetBrains template conventions and relying on platform-managed Kotlin dependencies.

## Validation Commands

Use these commands when the wrapper and network access are available:

```bash
./gradlew buildPlugin
./gradlew check
./gradlew verifyPlugin
```

Use a custom `runIde` task to test optional module behavior across IDE versions or product editions:

```bash
./gradlew runIde261
```

Expect optional content modules to be skipped when their required platform modules are unavailable. The required module should still load and its actions/extensions should remain usable.

## CI Shape

For GitHub Actions, the sample separates:

- `buildPlugin` to produce `build/distributions/*.zip`
- `check` to run tests
- `verifyPlugin` to run plugin verification
- release publishing through `publishPlugin` with Marketplace/signing secrets

For most project work, preserve the local Gradle tasks first; CI can mirror them after the project builds locally.
