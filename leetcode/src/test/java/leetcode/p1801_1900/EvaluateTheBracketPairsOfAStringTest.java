package leetcode.p1801_1900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class EvaluateTheBracketPairsOfAStringTest {
  EvaluateTheBracketPairsOfAString sut = new EvaluateTheBracketPairsOfAString();

  // ===========================================================================================
  // The floor, and the two things a bracket pair can become (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= s.length, 0 <= knowledge.length). With no bracket pair
  //         there is nothing to evaluate, so the string comes back as it went in
  @Test
  void singleLetterWithoutBracketsIsReturnedUnchanged() {
    assertThat(sut.evaluate("a", List.of())).isEqualTo("a");
  }

  // Step 2: the shortest string that holds a pair is three characters, because the key inside is
  //         never empty. The value replaces the key and both brackets. Substituting the key alone
  //         answers "(b)"
  @Test
  void knownKeyIsReplacedByItsValueWithoutTheBrackets() {
    assertThat(sut.evaluate("(a)", List.of(List.of("a", "b")))).isEqualTo("b");
  }

  // Step 3: the mirror of Step 2. A key missing from knowledge still loses its brackets, and
  //         becomes "?" rather than surviving as "(a)" or "a" or vanishing
  @Test
  void unknownKeyIsReplacedByAQuestionMark() {
    assertThat(sut.evaluate("(a)", List.of())).isEqualTo("?");
  }

  // Step 4: one "?" per pair, however long the key and however close the next pair. Marking every
  //         key letter answers "???????", and a greedy match from the first "(" to the last ")"
  //         swallows both pairs as one unknown key and answers "?"
  @Test
  void eachUnknownPairBecomesExactlyOneQuestionMark() {
    assertThat(sut.evaluate("(name)(age)", List.of())).isEqualTo("??");
  }

  // ===========================================================================================
  // What a key lookup matches, and what it never touches (Steps 5-9).
  // ===========================================================================================

  // Step 5: only bracketed text is a key. The bare "name" on either side spells a known key but is
  //         left alone. Replacing every occurrence of each key answers "bob(bob)bob", and doing
  //         that and then stripping the brackets answers "bobbobbob"
  @Test
  void textOutsideBracketsIsNeverEvaluated() {
    assertThat(sut.evaluate("name(name)name", List.of(List.of("name", "bob"))))
        .isEqualTo("namebobname");
  }

  // Step 6: the whole bracketed key has to match. "ab" merely starts with the known key "a". A
  //         lookup that checks whether s continues with some known key after the "(" answers "x"
  @Test
  void bracketKeyThatOnlyStartsWithAKnownKeyIsUnknown() {
    assertThat(sut.evaluate("(ab)", List.of(List.of("a", "x")))).isEqualTo("?");
  }

  // Step 7: the mirror of Step 6. The known key "ab" merely starts with the bracketed "a". A lookup
  //         that accepts any knowledge key beginning with the bracketed key answers "x"
  @Test
  void knownKeyThatOnlyStartsWithTheBracketKeyDoesNotMatch() {
    assertThat(sut.evaluate("(a)", List.of(List.of("ab", "x")))).isEqualTo("?");
  }

  // Step 8: only keys are looked up, never values. "bob" is in knowledge, but as the value of
  //         "name". Scanning each entry for any element equal to the bracketed key answers "bob"
  @Test
  void valuesAreNeverLookedUpAsKeys() {
    assertThat(sut.evaluate("(bob)", List.of(List.of("name", "bob")))).isEqualTo("?");
  }

  // Step 9: each pair is evaluated exactly once. Here the two keys swap values, and each value is
  //         also the other key. Following a value on to its own value answers "ab". Replacing each
  //         key everywhere in turn and then stripping the brackets answers "aa"
  @Test
  void aSubstitutedValueIsNotEvaluatedAgain() {
    assertThat(sut.evaluate("(a)(b)", List.of(List.of("a", "b"), List.of("b", "a"))))
        .isEqualTo("ba");
  }

  // ===========================================================================================
  // Where one pair ends, and what a substitution does to the length (Steps 10-12).
  // ===========================================================================================

  // Step 10: a pair ends at the first ")" after its "(", even when the next "(" follows
  //          immediately. The known and the unknown key sit side by side and are settled
  //          separately. A greedy match from the first "(" to the last ")" reads the single key
  //          "a)(b" and answers "?"
  @Test
  void adjacentPairsAreEvaluatedIndependently() {
    assertThat(sut.evaluate("(a)(b)", List.of(List.of("a", "x")))).isEqualTo("x?");
  }

  // Step 11: a value can be longer than the pair it replaces. A three-character pair becomes ten
  //          letters, so the result outgrows s. A solution that rewrites s in place, or writes into
  //          a char[] of s.length(), runs out of room and clobbers or overflows before the trailing
  //          "bc"
  @Test
  void valueLongerThanItsPairGrowsTheString() {
    assertThat(sut.evaluate("(a)bc", List.of(List.of("a", "klmnopqrst"))))
        .isEqualTo("klmnopqrstbc");
  }

  // Step 12: the mirror of Step 11. The longest key knowledge can hold (10 letters) turns into one
  //          letter, so twelve characters become one. A solution that fills a char[] of s.length()
  //          and returns all of it answers "bz" followed by eleven NUL characters
  @Test
  void valueShorterThanItsPairShrinksTheString() {
    assertThat(sut.evaluate("(abcdefghij)z", List.of(List.of("abcdefghij", "b"))))
        .isEqualTo("bz");
  }

  // ===========================================================================================
  // The official examples (Steps 13-15).
  // ===========================================================================================

  // Step 13: LeetCode Example 1 — two known keys, text between them and after them. The greedy
  //          match that Steps 4 and 10 rule out reads "name)is(age" as one key and answers
  //          "?yearsold"
  @Test
  void leetCodeExample1() {
    assertThat(sut.evaluate(
            "(name)is(age)yearsold", List.of(List.of("name", "bob"), List.of("age", "two"))))
        .isEqualTo("bobistwoyearsold");
  }

  // Step 14: LeetCode Example 2 — the only known key "a" is a letter inside the unknown key
  //          "name". Plain substring replacement rewrites that letter and answers "hi(nbme)", and a
  //          "?" per key letter answers "hi????"
  @Test
  void leetCodeExample2() {
    assertThat(sut.evaluate("hi(name)", List.of(List.of("a", "b")))).isEqualTo("hi?");
  }

  // Step 15: LeetCode Example 3 — the same key three times over, then three bare copies of it. The
  //          statement's Notice sentence is Step 5: those trailing letters are not evaluated.
  //          Replacing the key everywhere and stripping the brackets answers "yes" six times
  @Test
  void leetCodeExample3() {
    assertThat(sut.evaluate("(a)(a)(a)aaa", List.of(List.of("a", "yes"))))
        .isEqualTo("yesyesyesaaa");
  }

  // ===========================================================================================
  // Upper end of the constraints (Steps 16-18).
  //
  // s runs to 10^5 characters and knowledge to 10^5 entries. Knowledge keys and values are capped
  // at 10 letters, but a key inside s is capped only by the length of s.
  //
  // Step 17 pins down the running time. The intended solution loads knowledge into a hash map once
  // and walks s once, which is linear in the input and finishes in milliseconds. Scanning the
  // knowledge list for every pair, or running one replace over s for every knowledge entry, is
  // |s| * knowledge.length, about 3.3 * 10^9 steps at these bounds, and measures 7 to 9 seconds.
  // That gap is a large constant factor rather than a different exponent, so these timeouts are 2
  // seconds instead of the usual 5: the intended solution keeps a hundredfold margin, and the
  // quadratic ones cannot come within reach of the limit.
  //
  // They run on a separate thread because JUnit's default SAME_THREAD mode only compares elapsed
  // time after the method returns: against a solution slow enough to matter, a same-thread timeout
  // hangs the build instead of reporting the failure it exists to report.
  // ===========================================================================================

  // Step 16: s at its maximum length, 33,333 copies of one known pair and a bare "a" at the end.
  //          The value is the longest allowed, so the result is 333,331 characters, more than three
  //          times the length of s. This is Step 11 at scale, and the bare "a" is Step 5 at scale
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfRepeatedPairsGrowsMoreThanThreefold() {
    String s = "(a)".repeat(33_333) + "a";

    assertThat(sut.evaluate(s, List.of(List.of("a", "bcdefghijk"))))
        .isEqualTo("bcdefghijk".repeat(33_333) + "a")
        .hasSize(333_331);
  }

  // Step 17: knowledge at its maximum of 10^5 entries, with the only one-letter key "a" placed
  // last,
  //          against 33,332 pairs that alternate between "a" and the unknown "b". Every lookup by
  //          linear scan runs to the end of knowledge, which is the complexity claim of this group.
  //          The trailing bare "abcd" is itself one of the filler keys, and stays as it is
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumKnowledgeIsNotScannedOncePerPair() {
    String s = "(a)(b)".repeat(16_666) + "abcd";

    assertThat(sut.evaluate(s, maximumKnowledgeEndingWith("a", "z")))
        .isEqualTo("z?".repeat(16_666) + "abcd");
  }

  // Step 18: one key that fills all of s, 99,998 letters long. No knowledge key can be that long,
  //          so it is always unknown. The longest legal knowledge key is a 10-letter prefix of it,
  //          and the prefix match of Step 6 answers "b". A solution that reads each key into a
  //          fixed 10-character buffer overflows
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthKeyIsLongerThanAnyKnowledgeKey() {
    String s = "(" + "a".repeat(99_998) + ")";

    assertThat(sut.evaluate(s, List.of(List.of("a".repeat(10), "b")))).isEqualTo("?");
  }

  // ===========================================================================================
  // Hygiene (Steps 19-20).
  // ===========================================================================================

  // Step 19: knowledge is the caller's. Sorting it by key to binary-search it is a tempting
  //          shortcut, and these entries are deliberately out of key order so that a sort shows
  @Test
  void knowledgeIsNotModified() {
    List<List<String>> knowledge =
        new ArrayList<>(List.of(List.of("name", "bob"), List.of("age", "two")));
    List<List<String>> original = List.copyOf(knowledge);

    sut.evaluate("(name)is(age)yearsold", knowledge);

    assertThat(knowledge).containsExactlyElementsOf(original);
  }

  // Step 20: several strings answered by one instance, the maximum knowledge in the middle. Each
  //          call may use its own knowledge and nothing else. A map kept on the instance and only
  //          ever added to still holds "a" -> "z" at the third call and answers "z", and still
  //          holds "name" -> "bob" at the fifth and answers "hibob"
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceForgetsEarlierKnowledge() {
    List<List<String>> maximum = maximumKnowledgeEndingWith("a", "z");

    assertThat(sut.evaluate("(a)", List.of(List.of("a", "b")))).isEqualTo("b");
    assertThat(sut.evaluate("(a)(b)".repeat(16_666) + "abcd", maximum))
        .isEqualTo("z?".repeat(16_666) + "abcd");
    assertThat(sut.evaluate("(a)", List.of())).isEqualTo("?");
    assertThat(sut.evaluate(
            "(name)is(age)yearsold", List.of(List.of("name", "bob"), List.of("age", "two"))))
        .isEqualTo("bobistwoyearsold");
    assertThat(sut.evaluate("hi(name)", List.of(List.of("a", "b")))).isEqualTo("hi?");
  }

  // 99,999 distinct four-letter filler keys counting up from "aaaa", each worth "x", then the given
  // entry last, for 10^5 unique keys in all
  private static List<List<String>> maximumKnowledgeEndingWith(String key, String value) {
    List<List<String>> knowledge = new ArrayList<>(100_000);
    for (int i = 0; i < 99_999; i++) {
      char[] filler = new char[4];
      int rest = i;
      for (int p = 3; p >= 0; p--) {
        filler[p] = (char) ('a' + rest % 26);
        rest /= 26;
      }
      knowledge.add(List.of(new String(filler), "x"));
    }
    knowledge.add(List.of(key, value));
    return knowledge;
  }
}
