# Message Bus and Messaging Infrastructure

Official source: https://plugins.jetbrains.com/docs/intellij/messaging-infrastructure.html

## Concepts

- IntelliJ messaging is a publisher-subscriber system with bus hierarchy broadcasting and defined nested-event ordering.
- `Topic<T>` is the endpoint for a listener interface.
- `MessageBus` creates connections for subscribers and publishers.
- `MessageBusConnection` owns subscriptions for one client in one bus.
- A connection can be explicitly disconnected, but disposable-managed connections are preferred for plugin code.

## Defining a Topic

- Define a listener/business interface with methods representing events.
- Define a `Topic<ListenerType>` bound to that interface.
- Annotate the topic declaration with `@Topic.AppLevel`, `@Topic.ProjectLevel`, or both as appropriate.
- Topic properties include display name, broadcast direction, and listener class.

Example shape:

```java
public interface ChangeActionNotifier {
  @Topic.ProjectLevel
  Topic<ChangeActionNotifier> CHANGE_ACTION_TOPIC =
      Topic.create("custom name", ChangeActionNotifier.class);

  void beforeAction(Context context);

  void afterAction(Context context);
}
```

## Subscribing

- Use declarative listener registration whenever possible.
- For manual subscriptions, get the application/project message bus, connect with a parent disposable, then subscribe.
- A connection allows at most one handler per topic within that connection.

```java
project.getMessageBus()
    .connect(parentDisposable)
    .subscribe(ChangeActionNotifier.CHANGE_ACTION_TOPIC, new ChangeActionNotifier() {
      @Override
      public void beforeAction(Context context) {
        // keep handler short
      }

      @Override
      public void afterAction(Context context) {
        // keep handler short
      }
    });
```

## Publishing

- Use `syncPublisher(topic)` on the relevant bus.
- Call methods on the returned listener interface; the bus dispatches the same method and arguments to subscribers.

```java
ChangeActionNotifier publisher =
    project.getMessageBus().syncPublisher(ChangeActionNotifier.CHANGE_ACTION_TOPIC);
publisher.beforeAction(context);
try {
  // perform action
} finally {
  publisher.afterAction(context);
}
```

## Broadcasting

Message buses form a hierarchy, commonly application bus to project bus to module bus.

Broadcast directions:

- `TO_CHILDREN` is the default.
- `TO_DIRECT_CHILDREN` broadcasts only to direct child buses.
- `NONE` disables hierarchical broadcasting.
- `TO_PARENT` broadcasts upward.

Use broadcast direction intentionally. Subscribing project-specific code on a project bus to application-level events can avoid hard references from application-level publishers to project-level subscribers.

## Nested Messages

- A nested message is sent during processing of another message.
- For a particular topic, messages are delivered in send order.
- Do not assume a handler can safely modify shared mutable data while another subscriber is processing a related event.

## Handler Guidance

- Keep subscribers lightweight. Prefer cache invalidation or enqueueing work.
- Re-check PSI/VFS/project validity before background work acts on an event captured earlier.
- Avoid modifying shared documents or models from subscribers in ways that can conflict with nested delivery.
- If handlers need document or model writes, apply the threading and modality rules from [threading-read-write-actions.md](threading-read-write-actions.md).
