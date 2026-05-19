# Lifetimes, Disposers, and Services

Official sources:

- Fundamentals: https://plugins.jetbrains.com/docs/intellij/fundamentals.html
- Disposer and Disposable: https://plugins.jetbrains.com/docs/intellij/disposers.html
- Services: https://plugins.jetbrains.com/docs/intellij/plugin-services.html
- Components (Deprecated): https://plugins.jetbrains.com/docs/intellij/plugin-components.html

## Disposer Model

- `Disposer` is the singleton that owns a parent-child tree of `Disposable` instances.
- A child `Disposable` must never outlive its parent. Children are disposed before their parent.
- Register child lifetimes with `Disposer.register(parentDisposable, childDisposable)`.
- End a manually controlled disposable lifecycle with `Disposer.dispose(disposable)`, not by calling `Disposable.dispose()` directly.
- Use `Disposer.isDisposed(disposable)` before async callbacks touch an object that may have ended its lifetime.
- Once disposed, an object should be inactive and should not be retained by still-live objects.

## Choosing a Parent Disposable

- For resources needed for the plugin's full application/project lifetime, use an existing or dedicated application/project service as parent.
- For resources needed only while a dialog is visible, use `DialogWrapper.getDisposable()`.
- For resources needed while a tool window tab is shown, pass a disposable to `Content.setDisposer()`.
- For shorter operation lifetimes, create a child with `Disposer.newDisposable()` and dispose it manually. Still register it under a longer-lived parent so exceptions do not leak it.
- Do not use `Application` or `Project` as parent disposables in plugin code. They outlive dynamic plugin unload and can leak plugin objects.
- Prefer the shortest lifetime that safely covers the resource. A UI component registered under a project service can stay alive long after the UI operation is gone.

## Listener Cleanup

- Prefer listener registration overloads that accept `parentDisposable`.
- For message bus subscriptions, prefer `MessageBus.connect(parentDisposable)` with the shortest suitable parent.
- Avoid manually removing listeners from `dispose()` when an API has a disposable-aware registration overload.

## Services

- Services are loaded on demand through `ComponentManager.getService()`.
- Service scopes are application, project, and module. Avoid module-level services for new code unless there is a strong reason because they increase memory use in multi-module projects.
- A service that needs cleanup can implement `Disposable`; the platform disposes application services on IDE close or plugin unload and project services on project close or plugin unload.
- Keep service constructors light. Avoid heavy initialization and avoid acquiring other services in constructors.
- Dependency-service constructor injection is deprecated and not supported for light services. Retrieve dependencies only where needed.
- Getting a service can happen on any thread and does not need a read action, but initialization may block other threads requesting the same service.
- Do not acquire service instances prematurely or store them in static fields or immutable properties.

## Light Services

- Use `@Service` for services that are not overridden or exposed as an API to other plugins.
- Light service classes must be `final`.
- Light services cannot use registration-only attributes such as `id`, `os`, `client`, `overrides`, `configurationSchemaKey`, or internal `preload`.
- Light services do not use separate headless/test implementation registrations.
- Application-level light services implementing `PersistentStateComponent` must disable roaming with `roamingType = RoamingType.DISABLED`.

## Non-Light Service Registration

Register non-light services in `plugin.xml` under:

```xml
<extensions defaultExtensionNs="com.intellij">
  <applicationService serviceImplementation="com.example.MyAppService"/>
  <projectService serviceImplementation="com.example.MyProjectService"/>
</extensions>
```

Use `serviceInterface` only when exposing a separate API type.

## Deprecated Components

- Do not create new application/project/module Components.
- Existing Components should be migrated to services, extensions, or listeners.
- Component-based plugins do not support dynamic loading.
- Component declarations use legacy `<application-components>`, `<project-components>`, and `<module-components>` sections.
- For application/project close handling, move cleanup into a service implementing `Disposable`, or register children under a service parent.

## Diagnostics

- In tests, internal mode, and debug mode with `idea.disposer.debug=on`, `Disposer.register()` records allocation traces.
- For "memory leak detected" shutdown errors, inspect the `Caused by: java.lang.Throwable` allocation stack, not only the top-level shutdown stack.
- Relevant inspections include incorrect `parentDisposable`, light service restrictions, static service retrieval, incorrect service retrieving, and simplifiable service retrieving.
