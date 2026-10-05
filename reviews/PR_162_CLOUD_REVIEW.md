# Independent cloud review: PR #162

| Field | Value |
|---|---|
| **Candidate PR** | [msomu/base-mvvm-android-kt#162](https://github.com/msomu/base-mvvm-android-kt/pull/162) — *Add AGENTS.md, risk tiers, CODEOWNERS and a replayable golden-set eval* |
| **Reviewed commit (pinned)** | `e480eb26b955bebba52beaf382940f8a4f391053` |
| **PR base branch** | `main` |
| **PR base commit** | `4666f10ad7920cd33f711f23ff27e0fe5ee0d207` |
| **Diff scope (base → reviewed SHA)** | 10 files, +334 lines (no app Kotlin changes) |
| **Cloud agent run** | [bc-87174986-7ae3-4698-9d18-f5d6039b2cd8](https://cursor.com/agents/bc-87174986-7ae3-4698-9d18-f5d6039b2cd8) (`cursor/pr-162-cloud-review-185c`) |
| **Review date (UTC)** | 2026-10-05 |
| **Artifact type** | Workshop review-evidence only — not merge authorization |

## Scope reviewed

This review inspected the **exact** merge-base diff `4666f10..e480eb2` fetched from GitHub (`git fetch origin pull/162/head`). Implementation was read in a **detached worktree** at `e480eb26b955bebba52beaf382940f8a4f391053`; no cherry-picks or edits were applied to the candidate tree.

| Path | Role |
|---|---|
| `AGENTS.md` | Agent operating instructions (`./verify`, risk tiers, eval commands) |
| `RISK_TIERS.md` | Path-based `low` / `owned` / `blast-radius` policy |
| `.github/CODEOWNERS` | Code-owner requests for blast-radius paths |
| `.github/workflows/eval.yml` | CI: unit-test scorer + replay default recording |
| `evals/golden.jsonl` | 12 golden PR scenarios (6 historical, 6 synthetic) |
| `evals/run.py` | Scorer, live record, replay |
| `evals/test_run.py` | Unit tests |
| `evals/recorded/*.jsonl` | Committed model outputs (passing + harness-error example) |
| `.gitignore` | Adds `__pycache__/` |

Repository instructions consulted: root `CLAUDE.md`, candidate `AGENTS.md`, `RISK_TIERS.md`, existing `.github/workflows/build_apk.yml` (unchanged by this PR but referenced by `AGENTS.md`).

## Executive summary

PR #162 adds **governance documentation**, **CODEOWNERS alignment**, and a **replay-based golden-set eval** for risk-tier labeling. The design **clearly separates** CI replay (scores frozen `predicted` fields) from optional `--live` inference via `EVAL_MODEL_CMD`. Documentation **does not overclaim** branch-protection enforcement; `RISK_TIERS.md` states CODEOWNERS requests review but merge gates are not enabled on `main`.

Python harness behavior at the pinned SHA **matches** PR claims (7/7 unit tests, 12/12 default replay, 10/12 on the harness-error fixture with exit code 1). **Android Gradle verification was not executed in this cloud VM** (no Android SDK / `local.properties`); secondary evidence from GitHub Actions at the same SHA shows `build` succeeded. No secrets were found in workflow definitions.

**Recommendation for `e480eb26b955bebba52beaf382940f8a4f391053`: SHIP** (as workshop governance + eval wiring — not human approval to merge).

---

## Findings

Severity legend: **Critical** (must fix before merge) · **High** · **Medium** · **Low** · **Info**

### Medium

#### M1 — Replay scores stored `predicted`, not a re-parse of `raw`

**Location:** `evals/run.py` lines 95–97

```python
records, source = load_jsonl(args.replay), f"replay: {pathlib.Path(args.replay).name}"
rows = score(golden, {row["id"]: row["predicted"] for row in records if "id" in row})
```

**Issue:** CI replay trusts the JSONL `predicted` column. A recording could be edited so `predicted` passes while `raw` would not parse to the same tier; `parse_tier()` is never applied on replay.

**Evidence:** Code path above; recordings include both fields (e.g. `evals/recorded/2026-10-05-cursor-agent.jsonl`).

**Impact:** Weakens integrity guarantees of “replay proves scorer + wiring”; still consistent with PR text that replay is **not** a fresh model eval. Mitigation: re-score with `parse_tier(row["raw"])` in replay mode and/or add a unit test that `predicted == parse_tier(raw)` for every committed line.

### Low

#### L1 — `AGENTS.md` overstates what `./verify doctor` validates

**Location:** `AGENTS.md` line 11 vs `.cursor/skills/verify-basemvvm/verify` lines 301–311 (present on base; cited because `AGENTS.md` is new in this PR)

**Issue:** `AGENTS.md` says doctor proves “Java, adb, Gradle wrapper **and Android SDK** are found.” `cmd_doctor` sets `ok` from `java`, `adb`, and `gradlew` only; `android_home` is reported but **not** required for `ok`.

**Evidence (cloud, pinned SHA):**

```bash
cd <detached worktree at e480eb26>
git rev-parse HEAD   # e480eb26b955bebba52beaf382940f8a4f391053
./verify doctor      # exit 1, "ok": false (no adb/SDK in this VM)
```

**Impact:** Doc drift for agents relying on doctor before `./verify test` / `assemble`.

#### L2 — Live eval uses `shell=True`

**Location:** `evals/run.py` lines 70–71

**Issue:** `subprocess.run(command, shell=True, ...)` with `EVAL_MODEL_CMD` enables shell metacharacter expansion if the env var is untrusted.

**Impact:** Acceptable for maintainer-only `--live` recording; document that `EVAL_MODEL_CMD` must be a trusted, fixed command string.

#### L3 — Harness-error recording is not exercised in CI

**Location:** `.github/workflows/eval.yml` lines 26–27

**Issue:** CI runs only `python3 evals/run.py` (default passing recording). The intentional 10/12 failure fixture (`2026-10-05-cursor-agent-harness-errors.jsonl`) is documented in `AGENTS.md` but not run in the workflow.

**Impact:** CI would not detect accidental corruption of the default recording toward failure; optional second job or documented manual check only.

### Info

#### I1 — Policy / CODEOWNERS alignment

**Location:** `RISK_TIERS.md` lines 13–23; `.github/CODEOWNERS` lines 1–18

Blast-radius path lists match between policy and CODEOWNERS (including `evals/**` correctly classified as `owned` in policy, while **this PR** is `blast-radius` because it touches `.github/**`, `AGENTS.md`, etc.).

#### I2 — Honest enforcement and eval claims

**Location:** `RISK_TIERS.md` lines 39–42; `AGENTS.md` lines 50–51; `evals/run.py` lines 13–14

Branch protection is **not** claimed as enabled. Replay vs live distinction is stated accurately in PR body, `AGENTS.md`, and module docstring.

#### I3 — Security / privacy

- `eval.yml`: `permissions: contents: read` only; no secrets.
- Golden set uses public PR titles and synthetic filenames only; no credentials in recordings inspected.

#### I4 — Error boundaries on model harness failure

**Location:** `evals/recorded/2026-10-05-cursor-agent-harness-errors.jsonl` lines 10–11; `evals/run.py` `parse_tier` / scoring

Model exit code 1 with empty `raw` yields `predicted: null` → case fails → aggregate 83% &lt; 90% threshold → exit 1. Behavior verified locally at pinned SHA (see checks below).

---

## Verification performed (this environment)

All commands run from detached worktree at **`e480eb26b955bebba52beaf382940f8a4f391053`** unless noted.

| Check | Command | Result |
|---|---|---|
| Pin SHA | `git rev-parse HEAD` | `e480eb26b955bebba52beaf382940f8a4f391053` |
| Eval unit tests | `python3 -m unittest discover -s evals -p 'test_*.py' -v` | **7 tests OK** |
| Default replay | `python3 evals/run.py` | **12/12**, exit **0** |
| Harness-error replay | `python3 evals/run.py --replay evals/recorded/2026-10-05-cursor-agent-harness-errors.jsonl` | **10/12**, exit **1** |
| Verify doctor | `./verify doctor` | `"ok": false` (no `adb` / Android SDK in VM), exit **1** |
| Gradle contract | `./gradlew detekt test assembleDebug` | **Not run** — no `sdk.dir` / Android SDK in cloud VM |

**GitHub Actions (same SHA, not re-run by this agent):** `build`, `eval (replay)`, and other checks reported **success** on commit `e480eb26` via GitHub API (`check-runs`).

---

## Limitations

1. **No local Android build** in this cloud environment; cannot independently reproduce PR body’s `./gradlew detekt test assembleDebug` at this SHA. Risk is mitigated because the diff contains **no** `app/src` production code changes; GitHub `build` check passed at the pinned commit.
2. **No live model inference** (`--live` / `EVAL_MODEL_CMD`) — intentionally out of scope; replay-only verification aligns with CI design.
3. **No post** to candidate PR #162 (per workshop boundaries).
4. This report is **not** code-owner approval, security sign-off, or authorization to merge.

---

## Recommendation

**SHIP** for commit `e480eb26b955bebba52beaf382940f8a4f391053` **as workshop governance and eval scaffolding**, subject to:

- Accepting replay integrity model (M1) or planning a follow-up to re-parse `raw` on replay.
- Treating Android proof as **CI-backed** for this SHA rather than re-verified in this cloud run.

**NO-SHIP** would be warranted if the team required independent Gradle reproduction in the review environment, or if policy docs were found to falsely claim enforced merge gates (they were not).

---

*Independent cloud review artifact. Does not modify candidate PR #162.*
