package leetcode.p3401_3500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ReverseDegreeOfAStringTest {
  ReverseDegreeOfAString sut = new ReverseDegreeOfAString();

  // ===========================================================================================
  // Alphabet ranks and original string positions (Steps 1-7).
  // ===========================================================================================

  // Step 1: the smallest legal input still contributes 26. Ordinary alphabet ranks or a
  //         zero-based string position give the wrong value even for one character.
  @Test
  void singleAHasReverseRank26() {
    assertThat(sut.reverseDegree("a")).isEqualTo(26);
  }

  // Step 2: the other alphabet endpoint has rank 1, never 0. This catches a missing +1 when
  //         measuring a character's distance from 'z'.
  @Test
  void singleZHasReverseRank1() {
    assertThat(sut.reverseDegree("z")).isEqualTo(1);
  }

  // Step 3: positions belong to the original string: 26 * 1 + 1 * 2 = 28. Reversing the string
  //         along with the alphabet changes which letter receives the larger multiplier.
  @Test
  void azWeightsZByItsSecondPosition() {
    assertThat(sut.reverseDegree("az")).isEqualTo(28);
  }

  // Step 4: mirror Step 3: 1 * 1 + 26 * 2 = 53. Sorting or counting letters without preserving
  //         their positions incorrectly treats these two strings as equivalent.
  @Test
  void zaWeightsAByItsSecondPosition() {
    assertThat(sut.reverseDegree("za")).isEqualTo(53);
  }

  // Step 5: every occurrence contributes separately: 26 * (1 + 2 + 3) = 156. Deduplicating
  //         letters or multiplying their rank by frequency loses the positional weights.
  @Test
  void repeatedLettersKeepEveryPositionWeight() {
    assertThat(sut.reverseDegree("aaa")).isEqualTo(156);
  }

  // Step 6: cover every lowercase letter, including the interior of any lookup table. Checking
  //         only 'a' and 'z' would miss a bad rank for a middle letter.
  @Test
  void everyLetterUsesItsReversedAlphabetRank() {
    assertThat(sut.reverseDegree("abcdefghijklmnopqrstuvwxyz")).isEqualTo(3276);
  }

  // Step 7: the separated copies of 'e' contribute at positions 2, 3, and 8. Reusing a letter's
  //         first position is wrong: the products are 15, 44, 66, 28, 120, 72, 161, and 176.
  @Test
  void separatedRepeatsUseTheirOwnStringPositions() {
    assertThat(sut.reverseDegree("leetcode")).isEqualTo(682);
  }

  // ===========================================================================================
  // Official examples (Steps 8-9).
  // ===========================================================================================

  // Step 8: LeetCode Example 1 sums all products, 26 + 50 + 72 = 148. Returning just the final
  //         product or summing the unweighted alphabet ranks misses the stated operation.
  @Test
  void leetCodeExample1() {
    assertThat(sut.reverseDegree("abc")).isEqualTo(148);
  }

  // Step 9: LeetCode Example 2 keeps both copies of each endpoint at their original positions:
  //         1 + 52 + 3 + 104 = 160. Sorting the letters or collapsing duplicates changes the sum.
  @Test
  void leetCodeExample2() {
    assertThat(sut.reverseDegree("zaza")).isEqualTo(160);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 10-12). At n = 1000, both O(n) and O(n^2) scans fit comfortably.
  // Five-second timeouts catch runaway work, but cannot establish linear complexity here.
  // ===========================================================================================

  // Step 10: the largest possible result is 26 * 1000 * 1001 / 2 = 13,013,000. A char or short
  //          accumulator truncates it, and the final position must still contribute.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllAsProducesLargestDegree() {
    assertThat(sut.reverseDegree("a".repeat(1000))).isEqualTo(13_013_000);
  }

  // Step 11: minimum letter rank at maximum length still gives 1000 * 1001 / 2 = 500,500.
  //          Treating 'z' as zero discards every position, while summing ranks drops the weights.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllZsRetainsEveryPosition() {
    assertThat(sut.reverseDegree("z".repeat(1000))).isEqualTo(500_500);
  }

  // Step 12: 38 complete alphabets plus "abcdefghijkl" reach exactly 1000 characters. A
  //          position counter must continue across alphabet wraps and include the partial tail.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlphabetCyclesKeepGlobalPositions() {
    String s = "abcdefghijklmnopqrstuvwxyz".repeat(38) + "abcdefghijkl";

    assertThat(sut.reverseDegree(s)).isEqualTo(6_784_570);
  }

  // ===========================================================================================
  // Repeated calls (Step 13). String inputs are immutable, so no array-mutation check applies.
  // ===========================================================================================

  // Step 13: one instance handles different contents and sizes, with the largest input in the
  //          middle. Cached totals, cached results by length, and retained positions must reset.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceResetsBetweenCallsOfDifferentSizesAndContents() {
    assertThat(sut.reverseDegree("az")).isEqualTo(28);
    assertThat(sut.reverseDegree("a".repeat(1000))).isEqualTo(13_013_000);
    assertThat(sut.reverseDegree("za")).isEqualTo(53);
    assertThat(sut.reverseDegree("b")).isEqualTo(25);
    assertThat(sut.reverseDegree("az")).isEqualTo(28);
  }
}
