---
name: intellij-virtual-file-system
description: Build or review IntelliJ Platform plugin integrations with the Virtual File System (VFS). Use when requests mention `VirtualFile`, `VirtualFileManager`, `VirtualFileSystem`, `LocalFileSystem`, `VfsUtil`, `VfsUtilCore`, `BulkFileListener`, `AsyncFileListener`, VFS refresh, VFS snapshot behavior, file watchers, `com.intellij.virtualFileSystem`, `LocalFileOperationsHandler`, `FilePropertyPusher`, or `FileAttribute`. Prefer Kotlin for plugin-side code.
---

# Intellij Virtual File System

## Overview

Implement or debug IntelliJ Platform plugin code that reads files through VFS, refreshes snapshot state, reacts to filesystem changes, or extends VFS behavior. Keep this file focused on API selection and VFS-specific guardrails; load [references/api_reference.md](references/api_reference.md) for the exact entry points, snapshot caveats, listener behavior, and canonical docs URLs.

## Workflow

1. Decide whether the task really belongs at the VFS layer.
   Use VFS when the request is about file identity, file locations, refresh behavior, external file changes, filesystem traversal, or non-PSI file operations.
   If the request is about editor text, compare against `Document`.
   If the request is language-aware or PSI-tree-based, prefer PSI and convert to `VirtualFile` only at boundaries.

2. Choose the narrowest entry point.
   For selected files in actions, start from `PlatformDataKeys.VIRTUAL_FILE` or `VIRTUAL_FILE_ARRAY`.
   For a `Document`, use `FileDocumentManager.getFile()`.
   For PSI, use `PsiFile.getVirtualFile()` and handle `null` for in-memory PSI.
   For local paths, use `VirtualFileManager.findFileByNioPath()` or `refreshAndFindFileByNioPath()`.
   For file-name lookups, use `FilenameIndex.getVirtualFilesByName()` and narrow the results to the relevant scope.

3. Treat VFS as a snapshot, not the disk.
   Assume reads come from the IDE snapshot and may lag behind on-disk state.
   If an external tool just created or changed files, plan an explicit refresh.
   Prefer asynchronous refreshes unless the caller truly must block for the result.

4. Pick the right refresh strategy.
   Use `VirtualFile.refresh()` when you already have the file or directory instance.
   Use `VirtualFileManager.asyncRefresh()` or `syncRefresh()` for broader refresh needs.
   Use `refreshAndFindFileByNioPath()` when discovery and refresh must happen together.
   If a refresh is initiated from a background thread, do not hold a read lock.

5. Choose event delivery carefully.
   Use `BulkFileListener` on `VirtualFileManager.VFS_CHANGES` for standard VFS change observation.
   Use `AsyncFileListener` when the handler should stay non-blocking.
   Filter application-level events to relevant project content instead of assuming project scope.
   Remember that created or deleted notifications depend on what has already been loaded into the snapshot.

6. Extend the right layer.
   Implement `VirtualFileSystem` only for a real custom filesystem.
   Use `LocalFileOperationsHandler` for custom local rename, move, or delete behavior.
   Use `FilePropertyPusher` or `FileAttribute` only when the request is specifically about persistent file metadata.

## Guardrails

- Prefer Kotlin for plugin-side code unless the existing module is Java.
- Do not promise immediate disk-to-IDE consistency; explain snapshot lag and refresh scope.
- Avoid recursive directory walking without `VfsUtilCore.iterateChildrenRecursively()`, because recursive symlinks can loop indefinitely.
- Do not assume reference identity for files; multiple equal `VirtualFile` instances may represent the same disk file during the IDE process lifetime.
- Check `isValid()` before acting on files that may have been deleted or invalidated by refreshes.

## References

- Read [references/api_reference.md](references/api_reference.md) before implementing refresh logic, VFS listeners, extension points, or path-to-`VirtualFile` conversion code.
