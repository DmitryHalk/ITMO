#!/usr/bin/env python3
import os
import subprocess
import unittest

# путь к бинарнику считаем от места test.py, а не от текущей папки
BIN = os.path.join(os.path.dirname(os.path.abspath(__file__)), "dict")


def run(stdin: bytes):
    p = subprocess.run([BIN], input=stdin, capture_output=True, timeout=5)
    return p.returncode, p.stdout, p.stderr


class DictTests(unittest.TestCase):
    def check_found(self, key, value):
        code, out, err = run(key.encode() + b"\n")
        self.assertEqual(code, 0)
        self.assertEqual(out, value.encode() + b"\n")
        self.assertEqual(err, b"")

    def check_error(self, stdin):
        code, out, err = run(stdin)
        self.assertNotEqual(code, 0)
        self.assertEqual(out, b"")
        self.assertTrue(err.startswith(b"Error"))

    def test_first_added(self):
        self.check_found("third word", "third word explanation")

    def test_middle(self):
        self.check_found("second word", "second word explanation")

    def test_last_added(self):
        self.check_found("first word", "first word explanation")

    def test_no_trailing_newline(self):
        code, out, _ = run(b"first word")
        self.assertEqual(code, 0)
        self.assertEqual(out, b"first word explanation\n")

    def test_not_found(self):
        self.check_error(b"nothing\n")

    def test_prefix_of_key_not_found(self):
        self.check_error(b"first\n")

    def test_key_with_extra_suffix_not_found(self):
        self.check_error(b"first word!\n")

    def test_empty_input(self):
        self.check_error(b"")

    def test_empty_line(self):
        self.check_error(b"\n")

    def test_max_length_ok(self):
        self.check_error(b"a" * 255 + b"\n")  # влезает в буфер, но ключа нет

    def test_too_long(self):
        code, out, err = run(b"a" * 256 + b"\n")
        self.assertNotEqual(code, 0)
        self.assertEqual(out, b"")
        self.assertIn(b"longer", err)

    def test_way_too_long(self):
        self.check_error(b"a" * 5000 + b"\n")


if __name__ == "__main__":
    unittest.main(verbosity=2)