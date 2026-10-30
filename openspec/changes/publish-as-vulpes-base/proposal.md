# Proposal

**Conventional Commits:** `build!: publish as net.onelitefeather.vulpes:vulpes-base`

## Why

The library is published as `net.onelitefeather:vulpes` (from `rootProject.name`), while the README calls it `net.onelitefeather:vulpes-base`. With the move to content releases, the Vulpes tools share the group `net.onelitefeather.vulpes` (`vulpes-gradle-plugin`, `vulpes-format`), and content artifacts use `net.onelitefeather.vulpes.content`. The Gradle plugin adds `vulpes-base` to games automatically and needs one stable coordinate for it.

## What Changes

- **BREAKING** Publish the library as `net.onelitefeather.vulpes:vulpes-base`.
- The README shows the new coordinates.

## Capabilities

### New Capabilities
- `library-publishing`: Maven coordinates of the library.

### Modified Capabilities
<!-- None. -->

## Non-goals

- No relocation POM for the old coordinates; no consumer resolves them from Reposilite today besides the generated clone, which goes away.
- No change to the publishing repositories or credentials.

## Impact

- Consumers change the dependency coordinates once; the Gradle plugin uses the new ones.
