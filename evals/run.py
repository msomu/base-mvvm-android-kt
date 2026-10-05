#!/usr/bin/env python3
"""Golden-set eval: does an AI reviewer assign the RISK_TIERS.md tier to a pull request?

Replay (default; what CI runs, no model key):
    python3 evals/run.py
    python3 evals/run.py --replay evals/recorded/<file>.jsonl

Live (calls a model through any shell command that reads the prompt on stdin
and prints the answer; records every output so it can be replayed):
    EVAL_MODEL_CMD='cursor-agent -p --model <model>' \
        python3 evals/run.py --live --record evals/recorded/<date>-<model>.jsonl

Replay scores outputs recorded earlier. It proves the scoring and the CI wiring,
not the quality of the model today.
"""

import argparse
import datetime
import json
import os
import pathlib
import subprocess
import sys

EVALS = pathlib.Path(__file__).resolve().parent
POLICY = EVALS.parent / "RISK_TIERS.md"
GOLDEN = EVALS / "golden.jsonl"
DEFAULT_RECORDING = EVALS / "recorded" / "2026-10-05-cursor-agent.jsonl"
TIERS = ("low", "owned", "blast-radius")
MODEL_TIMEOUT_SECONDS = 300


def load_jsonl(path):
    return [json.loads(line) for line in pathlib.Path(path).read_text().splitlines() if line.strip()]


def build_prompt(case, policy):
    files = "\n".join(f"- {name}" for name in case["files"])
    return (
        "You review pull requests for an Android repository. Its risk policy is below.\n\n"
        f"{policy}\n\n"
        f"Pull request title: {case['title']}\n"
        f"Files changed:\n{files}\n\n"
        "Answer with exactly one word on the last line: low, owned, or blast-radius."
    )


def parse_tier(text):
    lines = [line.strip() for line in text.strip().splitlines() if line.strip()]
    if not lines:
        return None
    answer = lines[-1].strip("`*\"'. ").lower()
    return answer if answer in TIERS else None


def score(golden, predictions):
    return [
        {"id": case["id"], "expected": case["expected"], "predicted": predictions.get(case["id"]),
         "pass": predictions.get(case["id"]) == case["expected"]}
        for case in golden
    ]


def run_live(golden, command, record_path):
    policy = POLICY.read_text()
    meta = {"_meta": {"recorded_at": datetime.datetime.now(datetime.timezone.utc).isoformat(timespec="seconds"),
                      "command": command, "policy": "RISK_TIERS.md", "golden": "golden.jsonl"}}
    records = []
    for case in golden:
        result = subprocess.run(command, shell=True, input=build_prompt(case, policy), capture_output=True,
                                text=True, timeout=MODEL_TIMEOUT_SECONDS)
        records.append({"id": case["id"], "raw": result.stdout.strip(), "predicted": parse_tier(result.stdout),
                        "exit": result.returncode, "stderr": result.stderr.strip()[-500:]})
        print(f"recorded {case['id']}: {records[-1]['predicted']}", file=sys.stderr)
    if record_path:
        pathlib.Path(record_path).write_text("".join(json.dumps(row) + "\n" for row in [meta, *records]))
    return records


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--live", action="store_true", help="call EVAL_MODEL_CMD instead of replaying")
    parser.add_argument("--record", help="with --live, write model outputs to this JSONL file")
    parser.add_argument("--replay", default=str(DEFAULT_RECORDING), help="recorded outputs to score")
    parser.add_argument("--threshold", type=float, default=0.9, help="minimum accuracy to pass (0-1)")
    args = parser.parse_args(argv)

    golden = load_jsonl(GOLDEN)
    if args.live:
        command = os.environ.get("EVAL_MODEL_CMD")
        if not command:
            parser.error("--live needs EVAL_MODEL_CMD")
        records, source = run_live(golden, command, args.record), f"live: {command}"
    else:
        records, source = load_jsonl(args.replay), f"replay: {pathlib.Path(args.replay).name}"

    rows = score(golden, {row["id"]: row["predicted"] for row in records if "id" in row})
    for row in rows:
        mark = "PASS" if row["pass"] else "FAIL"
        print(f"{mark}  {row['id']:<28} expected={row['expected']:<13} predicted={row['predicted']}")
    passed = sum(row["pass"] for row in rows)
    accuracy = passed / len(rows)
    print(f"\n{passed}/{len(rows)} correct ({accuracy:.0%}), threshold {args.threshold:.0%}, {source}")
    return 0 if accuracy >= args.threshold else 1


if __name__ == "__main__":
    sys.exit(main())
