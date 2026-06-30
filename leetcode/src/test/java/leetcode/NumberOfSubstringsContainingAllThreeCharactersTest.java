package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOfSubstringsContainingAllThreeCharactersTest {
  NumberOfSubstringsContainingAllThreeCharacters sut =
      new NumberOfSubstringsContainingAllThreeCharacters();

  // Step 1: LeetCode Example 3 — the shortest possible input; the whole string is the only
  // substring and it already holds one of each, so the count is exactly 1
  @Test
  void wholeStringIsTheOnlyValidSubstring() {
    assertThat(sut.numberOfSubstrings("abc")).isEqualTo(1);
  }

  // Step 2: the same three characters in a different order — "at least one occurrence" cares about
  // presence, not arrangement, so a solution must not assume an a-then-b-then-c layout
  @Test
  void characterOrderDoesNotMatter() {
    assertThat(sut.numberOfSubstrings("bca")).isEqualTo(1);
  }

  // Step 3: a string lacking two of the three letters — with no b or c no substring can qualify,
  // pinning that all three are required, not just any one
  @Test
  void missingCharactersYieldNone() {
    assertThat(sut.numberOfSubstrings("aaa")).isEqualTo(0);
  }

  // Step 4: exactly two distinct letters, still short one — "abab" never gathers a c, so even its
  // longer substrings fail; guards against counting "has at least two kinds" by mistake
  @Test
  void onlyTwoDistinctCharactersYieldNone() {
    assertThat(sut.numberOfSubstrings("abab")).isEqualTo(0);
  }

  // Step 5: LeetCode Example 2 — every valid substring ends at the b, and each leading a opens a
  // new
  // valid start (aaacb, aacb, acb); a solution must credit all three left edges, giving 3
  @Test
  void leadingRepeatsEachExtendTheWindowLeftward() {
    assertThat(sut.numberOfSubstrings("aaacb")).isEqualTo(3);
  }

  // Step 6: once "abc" is complete, appending more characters keeps it complete — "abc" and "abcc"
  // both count, so trailing duplicates each add one more substring (answer 2)
  @Test
  void trailingCharactersEachAddOneSubstring() {
    assertThat(sut.numberOfSubstrings("abcc")).isEqualTo(2);
  }

  // Step 7: when the a repeats at index 3, the earliest valid start moves from 0 to 1 — substrings
  // abc, abca and bca count (3). This separates "most recent occurrence" from "first occurrence":
  // keying off the first a would wrongly report 4
  @Test
  void repeatedCharacterAdvancesTheWindowStart() {
    assertThat(sut.numberOfSubstrings("abca")).isEqualTo(3);
  }

  // Step 8: LeetCode Example 1 — the canonical mix of repeats on both ends; ten substrings qualify,
  // combining the leftward and rightward extension behaviors from steps 5 and 6
  @Test
  void mixedRepeatsCountAcrossExample() {
    assertThat(sut.numberOfSubstrings("abcabc")).isEqualTo(10);
  }

  // Step 9: in a perfect abc-cycle every position i >= 2 contributes i-1, so the total is the
  // triangular number (n-2)(n-1)/2; at n = 9 that is 28, a hand-checkable medium case
  @Test
  void repeatedPatternFollowsTheTriangularFormula() {
    assertThat(sut.numberOfSubstrings("abcabcabc")).isEqualTo(28);
  }

  // Step 10: the constraint ceiling n = 5e4 as an abc-cycle. The exact answer (50000-2)(50000-1)/2
  // = 1,249,925,001 stays just under Integer.MAX_VALUE, so the running total must accumulate as it
  // goes without overflowing; it is also the near-maximal count, and an O(n^2) scan would crawl
  // here
  @Test
  void maxLengthInputStaysWithinIntAndLinearTime() {
    assertThat(sut.numberOfSubstrings(abcCycle(50_000))).isEqualTo(1_249_925_001);
  }

  /** The string a,b,c,a,b,c,... truncated to length n. */
  private static String abcCycle(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) ('a' + i % 3));
    }
    return sb.toString();
  }
}
