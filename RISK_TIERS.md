# Risk tiers

Every pull request gets exactly one tier: the **highest** tier of any file it touches.

| Tier | Meaning | Review required |
|---|---|---|
| `low` | Cannot change what ships or how it is built. | Any reviewer. The author may merge once CI is green. |
| `owned` | Changes app behaviour or the verification harness. | One maintainer of that area. CI green plus `./verify` proof in the PR body. |
| `blast-radius` | Changes the build, CI, dependencies, networking, data layer, or the rules agents follow. | A code owner from `.github/CODEOWNERS`. CI green. No auto-merge. |

## Paths

### `blast-radius`

- `build.gradle.kts`, `app/build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
- `gradle/**`, `gradlew`, `gradlew.bat` (version catalog and wrapper)
- `.github/**` (workflows, Dependabot, `CODEOWNERS`)
- `config/detekt/**`
- `app/proguard-rules.pro`, `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/msomu/androidkt/di/**`
- `app/src/main/java/com/msomu/androidkt/network/**`
- `app/src/main/java/com/msomu/androidkt/repository/**`
- `AGENTS.md`, `CLAUDE.md`, `RISK_TIERS.md`

### `owned`

- Everything else under `app/src/main/**` (presentation, view models, model, resources)
- `.cursor/skills/**`, `verify` (verification harness)
- `evals/**`

### `low`

- Documentation: `README.md`, `LICENSE`, other `*.md` not listed above
- Tests only: `app/src/test/**`, `app/src/androidTest/**`
- Editor and agent memory: `.idea/**`, `.junie/**`, `.gitignore`

A pull request that touches a test **and** the code under test takes the tier of the code.

## Enforcement

`.github/CODEOWNERS` lists the `blast-radius` paths, so GitHub requests the owner automatically.
Requiring that review before merge is a branch-protection setting on `main`; it is **not** enabled on this repository.

`evals/` holds a golden set that checks an AI reviewer assigns these tiers correctly. See `AGENTS.md`.
