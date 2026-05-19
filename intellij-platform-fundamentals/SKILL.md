---
name: intellij-platform-fundamentals
description: Use when building, reviewing, or debugging IntelliJ Platform plugins that involve services, Disposable/Disposer lifetimes, read/write actions, EDT/BGT threading, modality, UI freezes, message bus topics, listeners, or migration away from deprecated components.
---

# IntelliJ Platform Fundamentals

Use this skill for low-level IntelliJ Platform plugin correctness: object lifetime, resource cleanup, thread access to PSI/VFS/project models, and message bus usage.

## Workflow

1. Identify which fundamental area is involved before changing code:
   - Services, component migration, cleanup, listeners, or leak symptoms: read [lifetimes-disposers-services.md](references/lifetimes-disposers-services.md).
   - PSI, VFS, project model access, write actions, indexing, UI freezes, or action update threading: read [threading-read-write-actions.md](references/threading-read-write-actions.md).
   - `Topic`, `MessageBus`, `MessageBusConnection`, listeners, subscriptions, nested events, or broadcasts: read [message-bus.md](references/message-bus.md).
2. Prefer the current documented platform APIs over legacy component APIs.
3. Keep lifetimes short and explicit. Use services, dialog/tool-window disposables, or dedicated child disposables as parents; avoid application/project objects as parent disposables in plugin code.
4. Keep lock scopes minimal. Move expensive PSI/VFS/index work off EDT, prepare data before write actions, and use non-blocking/coroutine read actions for long reads.
5. Connect message bus subscriptions with a parent disposable unless declarative registration is a better fit.
6. When reviewing existing code, check Plugin DevKit inspections and platform assertions mentioned in the relevant reference before proposing a workaround.

## Decision Rules

- New shared plugin logic belongs in a service when it needs application/project lifetime, state, cleanup, or reusable behavior.
- Short-lived UI resources should be owned by the UI lifetime: `DialogWrapper.getDisposable()`, `Content.setDisposer()`, or a manually disposed child created with `Disposer.newDisposable()`.
- Reads of PSI/VFS/project model from background threads need read actions. Writes must be in write actions and, for current platform versions, scheduled from a write-safe EDT context.
- Project-wide analysis that may touch indexes should wait for smart mode via `DumbService` or use the documented smart/non-blocking API.
- Message handlers and listeners should do little work directly. Clear caches, enqueue background work, and re-check validity before acting later.

## Source Scope

This skill is based on the IntelliJ Platform Plugin SDK Fundamentals section and the official linked pages for Disposer, Threading Model, Messaging Infrastructure, Services, and deprecated Components. Re-check the live docs when targeting a newly released platform version or APIs marked version-sensitive in the references.
