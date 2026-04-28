# IntelliJ VFS API Map

## Canonical Docs

- Virtual File System: <https://plugins.jetbrains.com/docs/intellij/virtual-file-system.html>
- Virtual Files: <https://plugins.jetbrains.com/docs/intellij/virtual-file.html>

## What VFS Is

- VFS is the IntelliJ Platform layer for file access independent of real storage location, such as local disk, archives, or other pluggable file systems.
- All reads go through an application-level snapshot shared across projects.
- The snapshot stores only data that has been requested through the VFS API. File metadata may be present even when directory children or file contents have not been loaded yet.
- VFS works with binary content. Encoding and line-separator concerns are handled above VFS, typically by `Document` or PSI layers.

## Pick The Right Abstraction

Use VFS when the task is about:

- locating files
- traversing filesystem structure
- reacting to external file changes
- refresh behavior and snapshot consistency
- custom file system implementations
- persistent file metadata

Prefer another layer when the task is about:

- plain editable text: `Document`
- language-aware structure or syntax trees: PSI

## Getting A `VirtualFile`

Context | API | Notes
--- | --- | ---
Action | `AnActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE)` / `VIRTUAL_FILE_ARRAY` | Best default for selected files in actions.
Document | `FileDocumentManager.getFile()` | Use when work begins from editor text.
PSI file | `PsiFile.getVirtualFile()` | May return `null` for in-memory PSI.
File name | `FilenameIndex.getVirtualFilesByName()` | Narrow results after lookup.
Local path | `LocalFileSystem.findFileByIoFile()` | Useful if the caller already has `java.io.File`.
Local path | `VirtualFileManager.findFileByNioPath()` / `refreshAndFindFileByNioPath()` | Preferred for `Path`-based lookup and optional refresh.

## Refresh And Snapshot Rules

- Snapshot state can lag behind disk contents. Deleted files may remain visible until a refresh catches up.
- The platform refreshes project contents asynchronously on startup and usually when the IDE regains focus, but plugins should still request refreshes when external tools mutate files.
- All write operations made through VFS are synchronous to disk.
- Refreshes are based on file timestamps. If file contents change without a timestamp change, VFS may not detect the update.
- There is no general eviction mechanism for files already loaded into the snapshot. Deleted files disappear only after disk deletion is observed and a parent directory is refreshed.
- VFS does not automatically honor ignored files or excluded folders. Higher-level code must skip them when appropriate.

## Synchronous vs Asynchronous Refresh

- Prefer asynchronous refresh in nearly all cases.
- Both sync and async refreshes may be initiated from any thread.
- If a background thread initiates a refresh, it must not hold a read lock, or deadlock can occur.
- Use `postRunnable` with:
  - `RefreshQueue.createSession()`
  - `VirtualFile.refresh()`
- Relevant APIs:
  - `VirtualFileManager.asyncRefresh()`
  - `VirtualFileManager.syncRefresh()`
  - `VirtualFile.refresh()`
  - `VirtualFileManager.refreshAndFindFileByNioPath()`
  - `LocalFileSystem.refreshAndFindFileByPath()`

## Events

- VFS events are fired on EDT and inside a write action.
- Standard listener: subscribe a `BulkFileListener` to `VirtualFileManager.VFS_CHANGES`.
- Non-blocking alternative: `AsyncFileListener`.
- Listeners are application-level, not project-level. Filter events to relevant project content, for example with `ProjectFileIndex.isInContent()`.
- Before and after events are available. For refresh-driven deletion events, the file may already be gone from disk while still accessible from the VFS snapshot.
- Refresh emits events only for data already loaded into the snapshot.
  - If a directory `VirtualFile` was loaded but `getChildren()` was never called, created-file events under that directory may be missed.
  - If only one child was loaded with `findChild()`, notifications for unrelated siblings may be missed.

## Common Operations And Utilities

- Traverse or analyze files with `VfsUtil` and `VfsUtilCore`.
- Use `VfsUtilCore.iterateChildrenRecursively()` for recursive walking so recursive symlinks do not cause endless loops.
- Use `VfsUtilCore.createCompactVirtualFileSet()` when storing many files.
- Use `ProjectLocator` to find projects containing a `VirtualFile`.
- Usually create files via PSI or `java.io.File`; if VFS creation is necessary, use:
  - `VirtualFile.createChildData()`
  - `VirtualFile.setBinaryContent()`

## Lifetime And Identity

- Equal `VirtualFile` instances can represent the same disk file during the IDE process lifetime.
- Equal instances share `UserDataHolder` data.
- Deleted files become invalid; `isValid()` returns `false`, and operations on invalid files can fail.

## Extending VFS

- Custom filesystem: implement `VirtualFileSystem` and usually `VirtualFile`, then register via the `com.intellij.virtualFileSystem` extension point.
- Custom local filesystem operation hooks: implement `LocalFileOperationsHandler` and register through `LocalFileSystem.registerAuxiliaryFileOperationsHandler()`.

## File Metadata Hooks

- `FilePropertyPusher`
- `FileAttribute`

Use these only when the task is specifically about storing extra persistent metadata for files.
