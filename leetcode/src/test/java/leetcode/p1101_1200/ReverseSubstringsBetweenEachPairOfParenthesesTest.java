package leetcode.p1101_1200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ReverseSubstringsBetweenEachPairOfParenthesesTest {
  ReverseSubstringsBetweenEachPairOfParentheses sut =
      new ReverseSubstringsBetweenEachPairOfParentheses();

  // ===========================================================================================
  // The floor (Steps 1-3).
  // ===========================================================================================

  // Step 1: smallest input the constraints allow (1 <= s.length) is a single letter. With no
  //         brackets there is nothing to reverse, so the letter comes back as is
  @Test
  void singleLetterIsReturnedAsIs() {
    assertThat(sut.reverseParentheses("a")).isEqualTo("a");
  }

  // Step 2: several letters and no brackets are left in their original order. A solution that
  //         reverses the whole string unconditionally answers "cba"
  @Test
  void lettersWithoutParenthesesAreUntouched() {
    assertThat(sut.reverseParentheses("abc")).isEqualTo("abc");
  }

  // Step 3: the smallest pair is an empty one, and the answer may be the empty string. The
  //         brackets themselves never survive into the result
  @Test
  void emptyPairVanishes() {
    assertThat(sut.reverseParentheses("()")).isEmpty();
  }

  // ===========================================================================================
  // One pair (Steps 4-8).
  // ===========================================================================================

  // Step 4: the core rule. The letters between one matching pair come out reversed
  @Test
  void onePairReversesItsContent() {
    assertThat(sut.reverseParentheses("(ab)")).isEqualTo("ba");
  }

  // Step 5: reversing one letter is a no-op, but its brackets must still be dropped. Skipping a
  //         pair whose reversal changes nothing leaves the brackets behind as "(a)"
  @Test
  void singleLetterPairLosesOnlyItsBrackets() {
    assertThat(sut.reverseParentheses("(a)")).isEqualTo("a");
  }

  // Step 6: letters outside every pair stay where they are, on both sides. Only the bracketed
  //         "ab" flips. Reversing the whole bracket-free string answers "ybax"
  @Test
  void lettersAroundAPairKeepTheirPlace() {
    assertThat(sut.reverseParentheses("x(ab)y")).isEqualTo("xbay");
  }

  // Step 7: a pair flush against the start, followed by an unbracketed suffix
  @Test
  void pairAtTheStartLeavesTheSuffixAlone() {
    assertThat(sut.reverseParentheses("(ab)cd")).isEqualTo("bacd");
  }

  // Step 8: the mirror of Step 7. A pair flush against the end, after an unbracketed prefix
  @Test
  void pairAtTheEndLeavesThePrefixAlone() {
    assertThat(sut.reverseParentheses("ab(cd)")).isEqualTo("abdc");
  }

  // ===========================================================================================
  // Sibling pairs (Steps 9-10).
  // ===========================================================================================

  // Step 9: two side-by-side pairs reverse independently, each in its own slot. Treating the
  //         first '(' and the last ')' as one pair answers "dcba", and a solution that only
  //         handles the first pair it finds answers "bacd"
  @Test
  void siblingPairsReverseIndependently() {
    assertThat(sut.reverseParentheses("(ab)(cd)")).isEqualTo("badc");
  }

  // Step 10: empty pairs, alone or nested, disappear wherever they sit and never shift the
  //          letters around them
  @Test
  void emptyPairsAnywhereVanish() {
    assertThat(sut.reverseParentheses("a()b(())c")).isEqualTo("abc");
  }

  // ===========================================================================================
  // Nesting (Steps 11-17).
  // ===========================================================================================

  // Step 11: every enclosing pair reverses again, so two reversals of the same letters cancel.
  //          Any solution that reverses a letter at most once, however deep it sits, answers "ba"
  @Test
  void doubleNestingCancelsOut() {
    assertThat(sut.reverseParentheses("((ab))")).isEqualTo("ab");
  }

  // Step 12: three reversals leave one net reversal. What counts is the parity of the depth
  @Test
  void tripleNestingReversesOnce() {
    assertThat(sut.reverseParentheses("(((ab)))")).isEqualTo("ba");
  }

  // Step 13: the inner pair reverses first, then the outer one reverses the combined result.
  //          "bc" -> "cb", then "acbd" -> "dbca". Reversing only the innermost pair answers
  //          "acbd", and reversing each letter once answers "dcba"
  @Test
  void nestedPairReversesBeforeItsParent() {
    assertThat(sut.reverseParentheses("(a(bc)d)")).isEqualTo("dbca");
  }

  // Step 14: an inner pair flush against the left edge of its parent. "a" -> "a", then "ab" ->
  //          "ba". Reversing only the innermost pair answers "ab"
  @Test
  void nestedPairAtTheLeftEdge() {
    assertThat(sut.reverseParentheses("((a)b)")).isEqualTo("ba");
  }

  // Step 15: the mirror of Step 14, with the inner pair flush against the right edge. Scanning
  //          code that treats the two edges differently fails one of these two steps
  @Test
  void nestedPairAtTheRightEdge() {
    assertThat(sut.reverseParentheses("(a(b))")).isEqualTo("ba");
  }

  // Step 16: two sibling pairs inside one parent. "bc" -> "cb" and "de" -> "ed" give "acbedf",
  //          and the parent then flips that to "fdebca". The siblings also swap places. Reversing
  //          only the innermost pairs answers "acbedf", and reversing each letter once answers
  //          "fedcba"
  @Test
  void nestedSiblingsReverseThenSwapInsideTheirParent() {
    assertThat(sut.reverseParentheses("(a(bc)(de)f)")).isEqualTo("fdebca");
  }

  // Step 17: a nested group embedded in unbracketed text. Only the middle moves: "ef" is reversed
  //          twice and reads forward, while "cd" and "gh" are reversed once and swap sides. So
  //          "ab" + "hg" + "ef" + "dc" + "ij". Reversing each letter once answers "abhgfedcij"
  @Test
  void nestedGroupInsideUnbracketedText() {
    assertThat(sut.reverseParentheses("ab(cd(ef)gh)ij")).isEqualTo("abhgefdcij");
  }

  // ===========================================================================================
  // LeetCode examples (Steps 18-20).
  // ===========================================================================================

  // Step 18: LeetCode Example 1. One pair around the whole string
  @Test
  void leetCodeExample1() {
    assertThat(sut.reverseParentheses("(abcd)")).isEqualTo("dcba");
  }

  // Step 19: LeetCode Example 2. "love" is reversed first, then the whole string, so "love"
  //          reads forward and "u" and "i" swap ends. Reversing each letter once answers
  //          "ievolu", and reversing only the innermost pair answers "uevoli"
  @Test
  void leetCodeExample2() {
    assertThat(sut.reverseParentheses("(u(love)i)")).isEqualTo("iloveu");
  }

  // Step 20: LeetCode Example 3. Three levels: "oc" -> "co", then "etco" -> "octe", then the
  //          whole "edocteel" -> "leetcode". Reversing each letter once answers "lecotede", and
  //          reversing only the innermost pair answers "edetcoel"
  @Test
  void leetCodeExample3() {
    assertThat(sut.reverseParentheses("(ed(et(oc))el)")).isEqualTo("leetcode");
  }

  // ===========================================================================================
  // Constraint bounds (Steps 21-25). s.length <= 2000 is small: O(n^2) splicing, about 4 * 10^6
  // character copies, finishes in milliseconds, and the intended matched-pair jump is O(n). The
  // timeouts only reject cubic or worse work, about 8 * 10^9 operations at this size. Nesting
  // reaches depth 1000, so a recursive solution has to survive a thousand frames.
  // ===========================================================================================

  // Step 21: maximum length with no brackets at all. The string comes back unchanged
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthWithoutParenthesesIsUnchanged() {
    String s = ramp(2000);

    assertThat(sut.reverseParentheses(s)).isEqualTo(s);
  }

  // Step 22: maximum length as 999 nested pairs around "ab". Odd depth means one net reversal
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxOddDepthAroundTwoLettersReversesThem() {
    String s = "(".repeat(999) + "ab" + ")".repeat(999);

    assertThat(sut.reverseParentheses(s)).isEqualTo("ba");
  }

  // Step 23: maximum length as 1000 nested empty pairs. Depth 1000 with nothing inside leaves
  //          the empty string
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxDepthOfEmptyPairsLeavesNothing() {
    String s = "(".repeat(1000) + ")".repeat(1000);

    assertThat(sut.reverseParentheses(s)).isEmpty();
  }

  // Step 24: maximum length as 500 sibling pairs "(ab)". Every pair flips in its own slot
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxSiblingPairsEachReverseInPlace() {
    String s = "(ab)".repeat(500);

    assertThat(sut.reverseParentheses(s)).isEqualTo("ba".repeat(500)).hasSize(1000);
  }

  // Step 25: 666 nested levels with one letter just inside each opening bracket, so
  //          "(a(b(c(d))))" in miniature, which answers "bdca". Level i holds 'a' + i % 26. In
  //          general, the odd levels come first in ascending order and the even levels follow in
  //          descending order. With every level holding a letter, every reversal moves something
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void deepCombWithALetterAtEveryLevel() {
    int levels = 666;
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < levels; i++) {
      s.append('(').append(letter(i));
    }
    s.append(")".repeat(levels));
    StringBuilder expected = new StringBuilder();
    for (int i = 1; i < levels; i += 2) {
      expected.append(letter(i));
    }
    for (int i = levels - 2; i >= 0; i -= 2) {
      expected.append(letter(i));
    }

    assertThat(sut.reverseParentheses(s.toString()))
        .isEqualTo(expected.toString())
        .hasSize(666);
  }

  // ===========================================================================================
  // Hygiene (Step 26).
  // ===========================================================================================

  // Step 26: one instance answers several inputs of different sizes, deliberately out of order
  //          with the largest in the middle. A stack or buffer kept on the instance instead of
  //          reset per call leaks letters or brackets from one answer into the next
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.reverseParentheses("(u(love)i)")).isEqualTo("iloveu");
    assertThat(sut.reverseParentheses("(ab)".repeat(500))).isEqualTo("ba".repeat(500));
    assertThat(sut.reverseParentheses("()")).isEmpty();
    assertThat(sut.reverseParentheses("((ab))")).isEqualTo("ab");
    assertThat(sut.reverseParentheses("a")).isEqualTo("a");
  }

  private static String ramp(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append(letter(i));
    }
    return sb.toString();
  }

  private static char letter(int i) {
    return (char) ('a' + i % 26);
  }
}
