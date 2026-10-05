import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parent))
import run  # noqa: E402


class ParseTierTest(unittest.TestCase):
    def test_reads_last_line(self):
        self.assertEqual(run.parse_tier("Touches gradle/.\n\nblast-radius"), "blast-radius")

    def test_strips_markdown(self):
        self.assertEqual(run.parse_tier("**Owned**."), "owned")

    def test_rejects_unknown_answer(self):
        self.assertIsNone(run.parse_tier("medium"))
        self.assertIsNone(run.parse_tier(""))


class ScoreTest(unittest.TestCase):
    def test_missing_prediction_fails(self):
        golden = [{"id": "a", "expected": "low"}, {"id": "b", "expected": "owned"}]
        rows = run.score(golden, {"a": "low"})
        self.assertEqual([row["pass"] for row in rows], [True, False])


class GoldenSetTest(unittest.TestCase):
    def test_labels_are_valid_tiers(self):
        for case in run.load_jsonl(run.GOLDEN):
            self.assertIn(case["expected"], run.TIERS, case["id"])

    def test_recording_covers_every_case(self):
        recorded = {row["id"] for row in run.load_jsonl(run.DEFAULT_RECORDING) if "id" in row}
        golden = {case["id"] for case in run.load_jsonl(run.GOLDEN)}
        self.assertEqual(recorded, golden)

    def test_threshold_gates_exit_code(self):
        self.assertEqual(run.main(["--threshold", "0"]), 0)
        self.assertEqual(run.main(["--threshold", "1.01"]), 1)


if __name__ == "__main__":
    unittest.main()
