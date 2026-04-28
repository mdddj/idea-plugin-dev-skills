# Quality Checklist

Run this checklist before considering the skill done.

## Trigger quality

- `name` is hyphen-case.
- `description` clearly states both purpose and trigger conditions.
- The description includes concrete phrases, file types, domains, or scenarios.

## Content quality

- `SKILL.md` is concise and procedural.
- Long technical detail was moved into `references/`.
- Unused placeholder files were deleted.
- The skill does not contain README-style auxiliary docs.

## Source fidelity

- Important APIs and workflows match the source docs.
- Official docs URLs are preserved where useful.
- Time-sensitive guidance was checked against live docs when needed.
- Examples reflect the actual documented patterns, not guessed ones.

## Packaging quality

- `quick_validate.py` passes.
- `package_skill.py` succeeds.
- The packaged `.skill` contains only the files that should ship.

## Installation quality

- The install target under `~/.codex/skills` does not unintentionally overwrite another skill.
- If using a symlink, it points to the intended workspace directory.
- The user is told to restart Codex.
