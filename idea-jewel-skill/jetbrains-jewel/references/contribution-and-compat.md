# Contribution And Compatibility

## Overview

Use this file when the request is about supported versions, release targeting, Compose upgrades, or how to contribute changes to Jewel.

## Version parity

Read `platform/jewel/VERSIONS.md` before answering compatibility questions. Current mappings in that file:

| Jewel Version | IJP target(s) | Standalone Version |
| --- | --- | --- |
| 0.34 | 253.3, 261 Beta 1 | 0.34.0-253.31033.149 |
| 0.33 | 253.1, 261 EAP | 0.33.0-253.29795 |
| 0.32.1 | 253.0 | 0.32.1-253.28294.285 |
| 0.32 | 252.5, 253 beta 1 | 0.32.0-253.28294.205 |
| 0.31 | 252.4, 253 EAP | 0.31.0-253.28086.58, 0.31.0-252.27409 |
| 0.30 | 251.5, 252.2 | 0.30.0-252.26252, 0.30.0-251.28458 |
| 0.29 | 251.4.1, 252.1 | 0.29.0-252.24604, 0.29.0-251.27828 |

Do not answer version questions from memory when the mapping file is available.

## Branching strategy

Use the top-level README guidance:

- `main` tracks the latest major IntelliJ Platform version.
- `releases/xxx` tracks a specific major IntelliJ Platform line.
- Release tags come from `main`, and release-branch heads are tagged with a suffix for the corresponding IJP version.

## Contribution scope

The Jewel-specific contribution flow applies to changes in these public paths:

- `community/platform/jewel`
- `community/libraries/skiko`
- `community/libraries/compose-runtime-desktop`
- `community/libraries/compose-foundation-desktop`
- `community/libraries/compose-foundation-desktop-junit`
- `community/libraries/detekt-compose-rules`
- `community/build/src/org/jetbrains/intellij/build/JewelMavenArtifacts.kt`
- `community/build/src/JewelMavenArtifactsBuildTarget.kt`

If the requested patch is outside these areas, do not assume it uses the Jewel contribution flow.

## Public PR rules

When describing or preparing contributions:

- Use commit messages that start with `[JEWEL-xxx]`.
- Add release notes in the PR description when the change affects user-visible APIs or behavior.
- Expect dual approval: one external Jewel maintainer and one internal JetBrains reviewer.
- Expect GitHub checks to pass before merge can proceed.
- Expect a `Ready for merge` comment to trigger the internal merge flow.

## Merge flow

The public PR is not the final merge step.

Use this mental model:

1. Open PR in `JetBrains/intellij-community`.
2. Get required approvals.
3. Wait for GitHub checks.
4. Add `Ready for merge`.
5. Merge Robot mirrors the PR into the internal monorepo and runs Patronus CI.
6. If Patronus passes, the change is merged internally and synchronized back.

## Compose upgrade work

Use `platform/jewel/docs/upgrade-compose.md` when the task is to update Compose.

Important guidance from that file:

- The upgrade affects Gradle, JPS, and Bazel.
- The public repo can cover much of the work, but some steps require JetBrains internal access.
- Verify the Compose version choice deliberately; major upgrades can be expensive.
- Re-check Skiko compatibility and packaging, not just Compose artifacts.

## Answering strategy

For compatibility or contribution questions:

- Give the exact Jewel and IJP versions when the source file contains them.
- Separate public-repo steps from JetBrains-internal-only steps.
- Call out unsupported or risky cases, such as mixing arbitrary Compose versions or relying on unsupported packaging arrangements.
