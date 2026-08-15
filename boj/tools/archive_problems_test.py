#!/usr/bin/env python3
"""Regression tests for archive_problems.py.

The risky logic in the archiver is the HTML -> Markdown conversion: a bad
conversion does not crash, it silently produces a statement that reads fine but
states the wrong constraint. Every tag and entity asserted below was observed in
the BOJ catalog snapshot across the problems this repo has solved, so the cases
are the real inventory rather than a guess at what HTML might appear.

Usage: python3 boj/tools/archive_problems_test.py
"""

import unittest

from archive_problems import PROBLEM_SCHEMA_VERSION, build_problem_json, html_to_markdown, sample_name


class HtmlToMarkdownTest(unittest.TestCase):
    # --- Blocks: paragraphs are the overwhelming majority of BOJ statement markup. ---

    def test_single_paragraph_loses_its_tags(self):
        self.assertEqual(html_to_markdown("<p>Hello</p>"), "Hello")

    def test_consecutive_paragraphs_are_separated_by_a_blank_line(self):
        self.assertEqual(html_to_markdown("<p>One</p>\n\n<p>Two</p>"), "One\n\nTwo")

    def test_empty_html_becomes_an_empty_string(self):
        self.assertEqual(html_to_markdown(""), "")

    def test_missing_html_becomes_an_empty_string(self):
        # statement_html, input_html, output_html and hint_html are all nullable
        # columns; hint_html in particular is NULL for most problems.
        self.assertEqual(html_to_markdown(None), "")

    def test_bare_text_without_any_tag_is_kept(self):
        self.assertEqual(html_to_markdown("just text"), "just text")

    # --- Entities: &nbsp;, &lt; and &gt; are the only three the snapshot contains. ---

    def test_less_than_entity_is_unescaped(self):
        self.assertEqual(html_to_markdown("<p>A(&lt; 0)</p>"), "A(< 0)")

    def test_greater_than_entity_is_unescaped(self):
        self.assertEqual(html_to_markdown("<p>A(&gt; 0)</p>"), "A(> 0)")

    def test_non_breaking_space_becomes_an_ordinary_space(self):
        # A literal U+00A0 surviving into the Markdown is invisible in a diff and
        # breaks naive whitespace splitting downstream.
        self.assertEqual(html_to_markdown("<p>N&nbsp;=&nbsp;5</p>"), "N = 5")

    # --- Inline emphasis and code. ---

    def test_em_becomes_single_asterisks(self):
        self.assertEqual(html_to_markdown("<p>an <em>emphasis</em></p>"), "an *emphasis*")

    def test_strong_becomes_double_asterisks(self):
        self.assertEqual(html_to_markdown("<p>a <strong>bold</strong></p>"), "a **bold**")

    def test_code_becomes_backticks(self):
        self.assertEqual(html_to_markdown("<p>the <code>N</code> value</p>"), "the `N` value")

    # --- sub/sup carry the exponents and indices that constraints are stated in. ---

    def test_sup_becomes_a_caret_exponent(self):
        # 10<sup>9</sup> silently becoming "109" is the single most damaging
        # conversion bug possible here: it turns a bound into a different bound.
        self.assertEqual(html_to_markdown("<p>10<sup>9</sup></p>"), "10^9")

    def test_multi_character_sup_is_parenthesized(self):
        self.assertEqual(html_to_markdown("<p>2<sup>31</sup></p>"), "2^(31)")

    def test_sub_becomes_an_underscore_index(self):
        self.assertEqual(html_to_markdown("<p>A<sub>i</sub></p>"), "A_i")

    def test_multi_character_sub_is_parenthesized(self):
        self.assertEqual(html_to_markdown("<p>A<sub>ij</sub></p>"), "A_(ij)")

    def test_padded_sub_is_trimmed_before_its_width_is_judged(self):
        # BOJ 13423 ships "X<sub>b </sub>- X<sub>a</sub>". Counting the trailing
        # space as content renders "X_(b )- X_a", parenthesizing a single index
        # and making two occurrences of the same variable look different.
        self.assertEqual(html_to_markdown("<p>X<sub>b </sub>- X<sub>a</sub></p>"), "X_b - X_a")

    def test_padded_sup_is_trimmed_before_its_width_is_judged(self):
        self.assertEqual(html_to_markdown("<p>2<sup> 3 </sup></p>"), "2^3")

    def test_whitespace_only_sup_is_dropped_entirely(self):
        self.assertEqual(html_to_markdown("<p>N<sup> </sup></p>"), "N")

    # --- Lists. ---

    def test_unordered_list_items_become_dashes(self):
        self.assertEqual(html_to_markdown("<ul><li>first</li><li>second</li></ul>"), "- first\n- second")

    def test_ordered_list_items_are_numbered_from_one(self):
        self.assertEqual(html_to_markdown("<ol><li>first</li><li>second</li></ol>"), "1. first\n2. second")

    def test_list_item_keeps_its_inline_markup(self):
        self.assertEqual(html_to_markdown("<ul><li>A(&gt; 0)<sup>2</sup></li></ul>"), "- A(> 0)^2")

    def test_list_is_separated_from_a_following_paragraph(self):
        self.assertEqual(html_to_markdown("<ul><li>item</li></ul><p>after</p>"), "- item\n\nafter")

    # --- pre holds sample-shaped text whose whitespace is load-bearing. ---

    def test_pre_becomes_a_fenced_block_preserving_interior_whitespace(self):
        self.assertEqual(html_to_markdown("<pre>a  b\n  c</pre>"), "```\na  b\n  c\n```")

    def test_pre_does_not_collapse_blank_lines(self):
        self.assertEqual(html_to_markdown("<pre>a\n\nb</pre>"), "```\na\n\nb\n```")

    # --- Tables. ---

    def test_table_becomes_a_gfm_table_with_a_separator_row(self):
        html = "<table><thead><tr><th>i</th><th>A</th></tr></thead><tbody><tr><td>1</td><td>7</td></tr></tbody></table>"
        self.assertEqual(html_to_markdown(html), "| i | A |\n| --- | --- |\n| 1 | 7 |")

    def test_table_without_a_header_row_still_gets_a_separator(self):
        # GFM requires a separator; a headerless table would otherwise render as
        # a single run-on paragraph.
        html = "<table><tr><td>1</td><td>2</td></tr><tr><td>3</td><td>4</td></tr></table>"
        self.assertEqual(html_to_markdown(html), "| 1 | 2 |\n| --- | --- |\n| 3 | 4 |")

    def test_pipe_inside_a_cell_is_escaped(self):
        html = "<table><tr><td>a|b</td></tr></table>"
        self.assertEqual(html_to_markdown(html), "| a\\|b |\n| --- |")

    def test_paragraph_inside_a_cell_does_not_break_the_row(self):
        html = "<table><tr><td><p>one</p><p>two</p></td></tr></table>"
        self.assertEqual(html_to_markdown(html), "| one two |\n| --- |")

    # --- Remaining observed tags. ---

    def test_blockquote_is_prefixed_with_an_angle_bracket(self):
        self.assertEqual(html_to_markdown("<blockquote><p>quoted</p></blockquote>"), "> quoted")

    def test_br_becomes_a_hard_line_break_inside_a_paragraph(self):
        self.assertEqual(html_to_markdown("<p>one<br>two</p>"), "one\ntwo")

    def test_anchor_becomes_a_markdown_link(self):
        self.assertEqual(
            html_to_markdown('<p><a href="https://example.com">text</a></p>'),
            "[text](https://example.com)",
        )

    def test_span_is_transparent(self):
        self.assertEqual(html_to_markdown("<p><span>plain</span></p>"), "plain")

    def test_div_is_treated_as_a_block(self):
        self.assertEqual(html_to_markdown("<div>one</div><div>two</div>"), "one\n\ntwo")

    # --- Images: 7 of the 43 in the snapshot are site-relative and must be resolved. ---

    def test_absolute_image_url_is_preserved(self):
        self.assertEqual(
            html_to_markdown('<p><img src="https://baeksoon-files.pages.dev/a.png" alt="fig"></p>'),
            "![fig](https://baeksoon-files.pages.dev/a.png)",
        )

    def test_relative_image_src_is_resolved_against_acmicpc(self):
        self.assertEqual(
            html_to_markdown('<p><img src="/upload/images/path.png"></p>'),
            "![](https://www.acmicpc.net/upload/images/path.png)",
        )

    def test_image_without_alt_text_still_renders(self):
        self.assertEqual(
            html_to_markdown('<p><img src="https://example.com/a.png"></p>'),
            "![](https://example.com/a.png)",
        )

    # --- Whitespace hygiene across the whole document. ---

    def test_leading_and_trailing_whitespace_is_stripped(self):
        self.assertEqual(html_to_markdown("\n\n  <p>body</p>  \n\n"), "body")

    def test_runs_of_blank_lines_collapse_to_one(self):
        self.assertEqual(html_to_markdown("<p>a</p>\n\n\n\n<p>b</p>"), "a\n\nb")

    def test_internal_whitespace_in_a_paragraph_is_collapsed(self):
        self.assertEqual(html_to_markdown("<p>a\n   b</p>"), "a b")


class SampleNameTest(unittest.TestCase):
    def test_first_sample_is_zero_padded(self):
        self.assertEqual(sample_name(0), "sample-01")

    def test_tenth_sample_keeps_two_digits(self):
        self.assertEqual(sample_name(9), "sample-10")

    def test_hundredth_sample_widens_past_two_digits(self):
        self.assertEqual(sample_name(99), "sample-100")


class BuildProblemJsonTest(unittest.TestCase):
    def _row(self, **overrides):
        row = {
            "id": 2015,
            "title": "수들의 합 4",
            "boj_url": "https://www.acmicpc.net/problem/2015",
            "level": "Gold IV",
            "tags_json": "[]",
            "time_limit_ms": 2000,
            "memory_limit_mb": 128,
            "samples_json": '[{"input":"4 0\\n2 -2 2 -2\\n","output":"4\\n"}]',
            "provenance_json": '{"sourceUrl":"https://www.acmicpc.net/problem/2015","contentHash":"fnv1a-8fb2d269"}',
            "coverage": "samples-only",
            "archived_at": "2026-04-22T13:36:01.607Z",
        }
        row.update(overrides)
        return row

    def test_identity_fields_come_straight_from_the_catalog(self):
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="2026-04-24T14:00:23.134Z")
        self.assertEqual(doc["id"], 2015)
        self.assertEqual(doc["title"], "수들의 합 4")
        self.assertEqual(doc["source_url"], "https://www.acmicpc.net/problem/2015")
        self.assertEqual(doc["level"], "Gold IV")

    def test_limits_are_split_into_time_and_memory(self):
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["limits"], {"time_ms": 2000, "memory_mb": 128})

    def test_absent_limits_are_recorded_as_null_rather_than_guessed(self):
        row = self._row(time_limit_ms=None, memory_limit_mb=None)
        doc = build_problem_json(row, sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["limits"], {"time_ms": None, "memory_mb": None})

    def test_tags_json_is_parsed_into_a_list(self):
        row = self._row(tags_json='[{"key":"dp","nameKo":"다이나믹 프로그래밍"}]')
        doc = build_problem_json(row, sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["tags"], [{"key": "dp", "nameKo": "다이나믹 프로그래밍"}])

    def test_sample_entries_point_at_the_files_on_disk(self):
        doc = build_problem_json(self._row(), sample_count=2, catalog_updated_at="x")
        self.assertEqual(
            doc["samples"],
            [
                {"name": "sample-01", "input": "samples/sample-01.in", "output": "samples/sample-01.out"},
                {"name": "sample-02", "input": "samples/sample-02.in", "output": "samples/sample-02.out"},
            ],
        )

    def test_provenance_is_carried_through_verbatim(self):
        # The content hash and source URL are what let a future reader tell a
        # verified constraint from one triangulated off a mirror.
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["provenance"]["contentHash"], "fnv1a-8fb2d269")

    def test_retrieval_records_which_catalog_snapshot_was_read(self):
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="2026-04-24T14:00:23.134Z")
        self.assertEqual(doc["retrieval"]["catalog_updated_at"], "2026-04-24T14:00:23.134Z")
        self.assertEqual(doc["retrieval"]["coverage"], "samples-only")

    def test_schema_version_is_stamped(self):
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["schema_version"], PROBLEM_SCHEMA_VERSION)

    def test_content_points_at_the_sibling_files(self):
        doc = build_problem_json(self._row(), sample_count=1, catalog_updated_at="x")
        self.assertEqual(doc["content"], {"raw_html": "source.html", "statement_markdown": "statement.md"})


if __name__ == "__main__":
    unittest.main(verbosity=2)
