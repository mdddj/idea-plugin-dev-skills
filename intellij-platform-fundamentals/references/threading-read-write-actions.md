# Threading, Read/Write Actions, and UI Freezes

Official source: https://plugins.jetbrains.com/docs/intellij/threading-model.html

## Thread Categories

- EDT is the UI thread. UI event handling and current platform write operations run there. Keep EDT work fast.
- BGT is for long-running and costly work.
- Plugins targeting 2024.1+ should prefer coroutine dispatchers for switching between threads.

## Application-Wide RW Lock

PSI, VFS, and the project root model are not thread-safe. Access requires the IntelliJ Platform application-wide read/write lock through read and write action APIs.

Lock behavior:

- Read lock allows reading. Multiple read locks can coexist.
- Write intent lock allows reading and potential upgrade to write lock.
- Write lock allows reading and writing, excludes all other reads/writes, and is currently only acquired on EDT.
- Write lock acquisition is prioritized over read locks.
- Plugins must not acquire locks explicitly. Use read/write action APIs.

## Read Actions

- Use `ReadAction.compute()` or `ReadAction.run()` for Java/Kotlin blocking reads.
- Kotlin plugins targeting 2024.1+ should prefer suspending `readAction()`.
- `Application.runReadAction()` is low-level and should usually be avoided.
- Kotlin `runReadAction()` is obsolete since 2024.1.
- Reading data is allowed from any thread, but background reads must be wrapped in read actions.
- Reading on EDT may be covered by the implicit write-intent lock, but explicit read actions are still clearer outside write-safe UI code.
- Objects read in one read action may be invalid before the next read action. Re-check `isValid()` for `PsiElement`, `PsiFile`, `VirtualFile`, projects, and modules.

## Write Actions

- Use `WriteAction.run()` or `WriteAction.compute()`.
- Kotlin plugins targeting 2024.1+ should prefer suspending `writeAction()`.
- `Application.runWriteAction()` is low-level and should usually be avoided.
- Kotlin `runWriteAction()` is obsolete since 2024.1.
- Writes to PSI, VFS, or project model must be inside write actions.
- In current documented platform versions, writing data is only allowed on EDT from a write-safe context.
- Use `Application.invokeLater()` for EDT writes because it supports `ModalityState`. In 2023.3+, use it for data-writing operations.
- Do not modify PSI, VFS, or project model from UI renderers or arbitrary `SwingUtilities.invokeLater()` calls.

## Modality

- `Application.invokeLater(runnable, modalityState)` prevents writes from running at unsafe times while modal dialogs are active.
- `ModalityState.defaultModalityState()` is optimal in most cases.
- `ModalityState.current()` runs when the modality stack has not grown since scheduling.
- `ModalityState.stateForComponent(component)` ties execution to the dialog containing a component.
- `ModalityState.nonModal()` runs after modal dialogs close.
- `ModalityState.any()` behaves like Swing scheduling and is only suitable for pure UI operations. Do not use it for PSI, VFS, or project model writes.
- If EDT work needs indexes, use `DumbService.smartInvokeLater()` with the relevant modality state.

## Non-Blocking Reads

- Long BGT read actions can block EDT write actions and freeze the UI.
- Prefer APIs that cancel and restart reads when writes are pending:
  - 2024.1+: coroutine write-allowing read actions.
  - Older/current blocking API alternative: `ReadAction.nonBlocking()`.
  - Immediate cancellable computation: `ReadAction.computeCancellable()`.
- NBRA supports `expireWith()` and `expireWhen()`; use them to stop stale work.
- If the read touches indexes, add `.inSmartMode()` or use the corresponding coroutine/smart-mode API.
- Read actions must check cancellation often enough for cancellation to take effect.

## Avoiding UI Freezes

- Do not traverse VFS, parse PSI, resolve references, or query indexes on EDT.
- Move expensive preparation out of write actions, then run only the minimal mutation in the write action.
- Event listeners should do little direct work, ideally clearing caches or enqueueing background processing.
- For background event processing, assume newer events may arrive before queued work starts. Re-check validity and current state.
- Consider `MergingUpdateQueue` and non-blocking reads for coalesced background work.
- Massive VFS event batches can be preprocessed with `AsyncFileListener`.
- Slow operation assertions from `SlowOperations.assertSlowOperationsAreAllowed()` must be fixed by moving work to BGT, background tasks, pooled threads, or coroutines.

## Action Updates

- For `AnAction`, check `AnAction.getActionUpdateThread()` and the Action System docs before doing expensive work in `update()`.
- Avoid resolving or querying indexes on EDT from action updates.

## Quick Checks

- Current thread is EDT/UI thread: `Application.isDispatchThread()`.
- If code may already be on EDT and must run on EDT, `UIUtil.invokeLaterIfNeeded()` can execute immediately on EDT or schedule from BGT.
- Use the Thread Access Info plugin to visualize thread and read/write access in the debugger.
