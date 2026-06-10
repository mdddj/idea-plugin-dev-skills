# Official Docs

- Canonical source: [Extension Points](https://plugins.jetbrains.com/docs/intellij/plugin-extension-points.html)
- Related source: [Extensions](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html)
- Related source: [Plugin Configuration File](https://plugins.jetbrains.com/docs/intellij/plugin-configuration-file.html)
- Checked against the live JetBrains documentation on 2026-06-10.
- The checked pages showed last-modified dates of 2025-03-25 for Extension Points, 2025-10-31 for Extensions, and 2025-10-16 for Plugin Configuration File.

## Core Concepts

- Extension points let a plugin expose an API that other plugins can extend.
- Interface extension points expose behavior. The provider declares an interface, and consumers contribute implementation classes.
- Bean extension points expose data. The provider declares a bean class, and consumers contribute XML attributes/tags that are deserialized into bean instances.
- For public callback interfaces implemented by consumers, consider `@ApiStatus.OverrideOnly` to communicate that callers should implement, not call, the API.

## Defining Extension Points

Declare extension points in `META-INF/plugin.xml` under `<extensionPoints>`:

```xml
<idea-plugin>
  <id>com.example.provider</id>

  <extensionPoints>
    <extensionPoint
        name="formatter"
        interface="com.example.api.FormatterExtension"/>

    <extensionPoint
        name="toolBinding"
        beanClass="com.example.api.ToolBindingBean"/>
  </extensionPoints>
</idea-plugin>
```

Important `<extensionPoint>` attributes:

- `name`: short name unique inside the provider plugin. The runtime fully qualified name is the plugin ID plus `.` plus this name, such as `com.example.provider.formatter`.
- `qualifiedName`: fully qualified EP name. Use this instead of `name` only when the provider must control the complete name. Include the plugin ID for uniqueness.
- `interface`: fully qualified interface consumers implement. Mutually exclusive with `beanClass`.
- `beanClass`: fully qualified extension bean class. Mutually exclusive with `interface`.
- `dynamic`: optional boolean, default `false`. Set to `true` only after satisfying dynamic plugin rules.
- `area`: optional scope. `IDEA_APPLICATION` is the default. `IDEA_PROJECT` exists, and `IDEA_MODULE` is deprecated. New project/module-level EPs are strongly discouraged; prefer application-level EPs that accept `Project` or `Module` parameters.

`name` and `qualifiedName` are mutually exclusive. `interface` and `beanClass` are mutually exclusive.

## Bean Extension Points

Bean classes can extend `AbstractExtensionPointBean` when they need the platform helper behavior commonly used by plugin XML beans.

Use XML serialization annotations for properties:

```kotlin
class ToolBindingBean : AbstractExtensionPointBean() {
    @Attribute("id")
    lateinit var id: String

    @Attribute("implementationClass")
    lateinit var implementationClass: String
}
```

Use `@RequiredElement` for required bean properties. Set `allowEmpty = true` only when an explicit empty value is valid.

Implement `PluginAware` when the provider needs to know which plugin contributed a particular bean. This is useful for diagnostics and for validating registrations from third-party plugins.

Use child `<with>` elements to restrict class-name attributes or tags to a parent type:

```xml
<extensionPoint
    name="psiBinding"
    beanClass="com.example.api.PsiBindingBean">
  <with
      attribute="psiElementClass"
      implements="com.intellij.psi.PsiElement"/>
</extensionPoint>
```

`<with>` requires exactly one of `attribute` or `tag`, plus an `implements` parent type.

## Declaring Extensions

Consumers register implementations under `<extensions>`.

For IntelliJ Platform extension points, use:

```xml
<extensions defaultExtensionNs="com.intellij">
  <toolWindow
      id="Example"
      factoryClass="com.example.ui.ExampleToolWindowFactory"/>
</extensions>
```

For extension points defined by another plugin:

```xml
<idea-plugin>
  <id>com.example.consumer</id>
  <depends>com.example.provider</depends>

  <extensions defaultExtensionNs="com.example.provider">
    <formatter implementation="com.example.impl.MarkdownFormatter"/>
    <toolBinding
        id="markdown"
        implementationClass="com.example.impl.MarkdownTool"/>
  </extensions>
</idea-plugin>
```

Rules:

- `defaultExtensionNs` is `com.intellij` for platform core EPs or the provider plugin ID for another plugin's EPs.
- The child element name under `<extensions>` matches the EP short name.
- Interface EPs use the `implementation` attribute for the implementation class.
- Bean EPs use the bean properties annotated with `@Attribute` and `@Tag`.
- Extensions can use common attributes such as `id`, `order`, and `os`.
- `order` supports `first`, `last`, `before extension_id`, `after extension_id`, and comma-separated combinations.
- `os` can restrict an extension to `freebsd`, `linux`, `mac`, `unix`, or `windows`.

## Extension Implementation Rules

- Implementations must be stateless. Keep runtime data in services.
- Avoid constructor work; extension instances can be created by platform infrastructure at times you do not control.
- Avoid static initialization. Plugin DevKit has an inspection for static initialization in extension point implementations.
- Do not additionally register an extension implementation as a service or component. Plugin DevKit has an inspection for this.
- An implementation that is not applicable in the current runtime context may throw `ExtensionNotApplicableException` in its constructor.
- In Kotlin, use `class`, not `object`, for extension implementations.
- Avoid `companion object` in extension implementation classes when top-level declarations or separate objects can hold constants/helpers without forcing classloading.

## Runtime Access

Provider code that consumes all registered extensions should keep the `ExtensionPointName` private and use the fully qualified EP name:

```kotlin
private val FORMATTER_EP =
    ExtensionPointName.create<FormatterExtension>("com.example.provider.formatter")

fun formatters(): List<FormatterExtension> = FORMATTER_EP.extensionList
```

Expose a public query method only when consumers need a stable API that hides EP lookup details.

IDEA provides navigation/code insight for matching `ExtensionPointName` string literals and `plugin.xml` EP declarations in supported IDE versions.

## Dumb Mode

If extension implementations must be filtered according to dumb mode:

- Mark the base class or interface with `PossiblyDumbAware` so the contract is visible.
- Use `DumbService.getDumbAwareExtensions()` when retrieving only dumb-aware implementations.

For keyed extension lookups, check whether these platform helper bases match the model before building custom indexing:

- `LanguageExtension`
- `FileTypeExtension`
- `ClassExtension`
- `KeyedExtensionCollector`

## Dynamic Plugin Rules

An extension point can be dynamic only if provider code handles plugin unload/reload correctly.

Acceptable patterns:

- Enumerate extensions on every use and do not store extension instances.
- Maintain derived data through `ExtensionPointName.addExtensionPointListener()` and update it when extensions are added or removed.

After those rules hold, declare:

```xml
<extensionPoints>
  <extensionPoint
      name="toolBinding"
      beanClass="com.example.api.ToolBindingBean"
      dynamic="true"/>
</extensionPoints>
```

Plugin DevKit's `Plugin.xml dynamic plugin verification` inspection highlights non-dynamic extension points.

## Error Handling

Use `PluginException` for errors caused by an extension implementation or registration so IDE error reporting can attribute the error to the contributing plugin.

Use `PluginException.reportDeprecatedUsage()` when an extension uses a deprecated provider API and the provider needs to report that usage.

## Discovery And Code Insight

Use these sources when selecting an existing extension point:

- IntelliJ Platform Extension Point and Listener List.
- Bundled plugin and OSS plugin extension point lists in the official docs.
- IntelliJ Platform Explorer for real usages in open-source plugins.
- `plugin.xml` completion under `<extensions defaultExtensionNs="...">`.
- Quick Documentation in completion lookup to inspect EP attributes and implementation details.

Bean property code insight notes:

- Properties named `implementation`, `className`, ending in `Class`, or named `serviceInterface`/`serviceImplementation` resolve as fully qualified class names.
- Properties named `language` or ending in `Language` resolve to language IDs.
- `action` and `actionId` resolve to registered action IDs in supported IDE versions.
- Enum attributes use lower-camel-cased notation; enum classes should not override `toString()`.
- `@Nls` validates UI string capitalization based on the annotated capitalization value.

## Public API Sources

When a plugin exposes extension points for other plugins, consider bundling plugin API sources so consumers can read the extension API and get better IDE navigation/documentation.
