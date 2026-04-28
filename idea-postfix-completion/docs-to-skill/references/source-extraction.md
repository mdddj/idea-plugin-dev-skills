# Source Extraction

## Goal

Read documentation like an engineer building a reusable skill, not like a user reading a tutorial.

## What to extract

Keep notes on:

- API names, class names, interfaces, extension points, commands, and file paths.
- Required project structure or resource layout.
- Mandatory setup steps, especially those easy to forget.
- Constraints and caveats.
- Version-sensitive notes.
- Short concrete examples that reveal the intended usage pattern.

Ignore:

- Marketing copy.
- Basic concepts the model already knows.
- Redundant walkthrough text.
- Huge example dumps unless they reveal structure or edge cases.

## How to split content

Put this in `SKILL.md`:

- When the skill should trigger.
- The high-level workflow.
- Decision rules.
- Links to reference files.

Put this in `references/`:

- Detailed API maps.
- Variant-specific guidance.
- Checklists.
- Examples that would bloat `SKILL.md`.

## Handling online docs

- Prefer official docs.
- If the docs may have changed recently, browse them directly rather than relying on memory.
- Record the canonical docs URL in a reference file when it will help future maintenance.
- If multiple pages are needed, summarize each one instead of copying long passages.

## Handling local docs

- Read only the relevant sections first.
- If the file is large, search for exact symbols, headings, or filenames before loading the full text.
- Preserve local project conventions if the docs describe an existing codebase or internal standard.
