---
name: docs-to-skill
description: Turn online documentation, local docs, API guides, SDK references, or product manuals into a Codex skill. Use when creating a new skill from one or more docs URLs, markdown files, PDFs, or existing reference material, especially when the user asks to "根据文档生成 skill", "把文档做成 skill", "create a skill from docs", "turn docs into a skill", or wants the result validated, packaged, and installed into Codex.
---

# Docs To Skill

## Overview

Create a reusable Codex skill from documentation instead of hand-writing everything from scratch. Use this skill to extract only the non-obvious parts of the docs, split long material into `references/`, and finish with validation, packaging, and optional installation into `~/.codex/skills`.

## Inputs

Collect these inputs before writing:

- Skill name in hyphen-case.
- Source docs: URLs, local files, or both.
- Expected trigger phrases users might say.
- Preferred implementation language or ecosystem bias, if relevant.
- Whether the user wants packaging, installation, or both.

If the user omits some inputs, make pragmatic assumptions and state them briefly.

## Workflow

1. Read the docs before designing the skill.
   If the source is online or time-sensitive, browse it directly.
   Prefer official documentation and primary sources for technical material.
   Extract the actual APIs, workflows, constraints, resource paths, file layouts, edge cases, and version-sensitive notes. Ignore introductory filler.

2. Decide what belongs in the skill.
   Put trigger conditions, workflow, and selection guidance in `SKILL.md`.
   Put long API notes, conventions, examples, and version-specific details in `references/`.
   Add `scripts/` only when the same deterministic helper would otherwise be rewritten repeatedly.
   Add `assets/` only when the skill needs templates or files to be copied into output.

3. Initialize the skill.
   Run `init_skill.py` from the `skill-creator` bundle.
   Create the new skill in the current writable workspace first. Do not write directly into `~/.codex/skills`.

4. Write the frontmatter carefully.
   `name` must be hyphen-case.
   `description` is the trigger surface. Include what the skill does and when to use it, with concrete phrases, file types, or domains.
   Do not add extra frontmatter keys unless the validator allows them.

5. Write a small `SKILL.md`.
   Use imperative language.
   Keep the body focused on execution: how to read the docs, how to map docs to reusable skill content, how to structure the result, and how to validate it.
   When multiple variants exist, keep only the selection logic in `SKILL.md` and link to the relevant `references/` file.

6. Remove template clutter.
   Delete generated placeholders that are not needed.
   A good skill is lean. Do not keep example scripts or fake assets just because the initializer created them.

7. Validate and package.
   Run `quick_validate.py`.
   If valid, run `package_skill.py`.
   Fix frontmatter or structure issues before continuing.

8. Install if requested.
   Prefer a symlink from `~/.codex/skills/<skill-name>` to the workspace skill directory so later edits stay live.
   If a skill with the same name already exists, inspect it first and avoid overwriting it blindly.
   Tell the user to restart Codex after installation.

## Output Standard

The resulting skill should usually contain:

- `SKILL.md`
- zero or more `references/*.md`
- optional `scripts/`
- optional `assets/`

Do not add README-style project docs, changelogs, or setup essays.

## References

- Read `references/source-extraction.md` when the source material is large, unstable, or split across multiple pages.
- Read `references/quality-checklist.md` before validation, packaging, or installation.
