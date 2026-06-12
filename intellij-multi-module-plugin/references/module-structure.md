# Module Structure Reference

Source sample: `https://github.com/novotnyr/multi-module-plugin`, commit `2b6659cabf57c38685d8b2798132f9a07e755efa` as cloned on 2026-06-12.

## Shape

Use one root plugin project and one Gradle subproject per plugin content module:

```text
root/
  settings.gradle.kts
  build.gradle.kts
  src/main/resources/META-INF/plugin.xml
  shared/
    src/main/kotlin/...
    src/main/resources/<plugin-prefix>.shared.xml
  css/
    build.gradle.kts
    src/main/kotlin/...
    src/main/resources/<plugin-prefix>.css.xml
```

The root descriptor owns plugin identity and content composition. Content-module descriptors own actions, extensions, services, and dependencies for that module.

## Naming

Derive content module names from `rootProject.name` plus a module suffix:

| Gradle directory | Gradle path | Content module name | Descriptor path |
| --- | --- | --- | --- |
| `shared` | `:shared` | `mincssrel.shared` | `shared/src/main/resources/mincssrel.shared.xml` |
| `css` | `:css` | `mincssrel.css` | `css/src/main/resources/mincssrel.css.xml` |

Keep descriptor filenames exactly aligned with content module names, with `.xml` appended. Place content-module descriptors directly under that subproject's `src/main/resources`.

## Root Descriptor

Keep `src/main/resources/META-INF/plugin.xml` focused:

```xml
<idea-plugin>
    <id>com.example.myplugin</id>
    <name>My Plugin</name>
    <vendor>Example</vendor>
    <description><![CDATA[
        Plugin assembled from required and optional content modules.
    ]]></description>
    <content>
        <module name="myplugin.shared" loading="required" />
        <module name="myplugin.css" loading="optional" />
    </content>
</idea-plugin>
```

Use `loading="required"` for functionality that must always load. Use `loading="optional"` for integrations that depend on IDE modules, paid features, or product-specific APIs that may be unavailable.

## Required Shared Module

Put shared services and universally available actions in a required content module:

```xml
<idea-plugin>
    <actions>
        <action id="com.example.myplugin.shared.SharedAction"
                class="com.example.myplugin.shared.SharedAction"
                text="Invoke Shared Action" />
    </actions>
</idea-plugin>
```

The sample uses a project service:

```kotlin
@Service(Service.Level.PROJECT)
class SharedMessageService(private val project: Project) {
    fun getMessage(): String = "Shared plugin content module in project '${project.name}'."
}
```

Actions in other modules can depend on this module via Gradle and XML module dependencies.

## Optional Feature Module

Put optional API usage in its own module descriptor and subproject. The sample CSS module declares both the shared module dependency and bundled CSS module dependency:

```xml
<idea-plugin>
    <dependencies>
        <module name="mincssrel.shared" />
        <module name="intellij.css" />
    </dependencies>

    <actions>
        <action id="com.example.myplugin.css.CssAction"
                class="com.example.myplugin.css.CssAction"
                text="Invoke CSS Action" />
    </actions>
</idea-plugin>
```

Keep all imports from optional APIs, such as `com.intellij.psi.css.CssFile`, inside the optional module. If the dependency is unavailable, the optional content module can be skipped without blocking required plugin content.

## Kotlin Pattern Notes

- Keep `DumbAwareAction` implementations stateless.
- Get the current `Project` from `AnActionEvent` and return early when absent.
- Access project-level reusable behavior through services, not action fields.
- Use `readAction { ... }` for PSI reads and switch back to `Dispatchers.EDT` for UI notifications when using coroutines.
- If code requires a read lock, make that explicit with platform annotations where useful.

## Common Mistakes

- Registering optional feature actions in the root descriptor, which can force classloading of APIs that are not present.
- Naming the module descriptor `plugin.xml`; content modules should use their content module name as the filename.
- Declaring XML dependency on `intellij.css` while forgetting the Gradle `bundledModule("intellij.css")` dependency.
- Putting shared services in an optional module and then calling them from required functionality.
