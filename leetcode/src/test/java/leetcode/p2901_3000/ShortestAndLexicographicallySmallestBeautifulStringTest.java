package leetcode.p2901_3000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ShortestAndLexicographicallySmallestBeautifulStringTest {
  ShortestAndLexicographicallySmallestBeautifulString sut =
      new ShortestAndLexicographicallySmallestBeautifulString();

  // ===========================================================================================
  // The floor and the counting rule (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest input the constraints permit — one character, k = 1. The single '1' is
  //         itself the shortest beautiful substring, so the answer is the whole string
  @Test
  void singleOneIsItsOwnBeautifulSubstring() {
    assertThat(sut.shortestBeautifulSubstring("1", 1)).isEqualTo("1");
  }

  // Step 2: the other one-character string — no '1' means no beautiful substring, and the
  //         contract is the empty string, not null
  @Test
  void singleZeroReturnsTheEmptyString() {
    assertThat(sut.shortestBeautifulSubstring("0", 1)).isEmpty();
  }

  // Step 3: ones exist but fewer than k of them — "1010" holds two ones against k = 3, so no
  //         window of any length can reach exactly k and the answer is again the empty string
  @Test
  void kLargerThanTotalOnesReturnsTheEmptyString() {
    assertThat(sut.shortestBeautifulSubstring("1010", 3)).isEmpty();
  }

  // Step 4: k equals the total number of ones — the only candidate spans from the first '1' to
  //         the last, and the zeros trapped between them stay in the answer
  @Test
  void kEqualToTotalOnesKeepsTheInteriorZeros() {
    assertThat(sut.shortestBeautifulSubstring("1001", 2)).isEqualTo("1001");
  }

  // ===========================================================================================
  // The shortest window is trimmed to its ones (Steps 5-7). Dropping an edge zero keeps the
  // count at k while shortening the window, so the answer starts and ends with '1'.
  // ===========================================================================================

  // Step 5: leading zeros are not part of the shortest window. A scan that returns the prefix
  //         reaching k ones answers "0011"
  @Test
  void leadingZerosAreTrimmed() {
    assertThat(sut.shortestBeautifulSubstring("0011", 2)).isEqualTo("11");
  }

  // Step 6: the mirror — trailing zeros are not part of the shortest window either
  @Test
  void trailingZerosAreTrimmed() {
    assertThat(sut.shortestBeautifulSubstring("1100", 2)).isEqualTo("11");
  }

  // Step 7: both trims at once, while the zeros between the ones survive — the answer is
  //         "1001", not the prefix "001001" and not a zero-free "11"
  @Test
  void bothEndsTrimToOnesButInteriorZerosSurvive() {
    assertThat(sut.shortestBeautifulSubstring("00100100", 2)).isEqualTo("1001");
  }

  // ===========================================================================================
  // The two-part ordering rule (Steps 8-10): shortest length is decided first, and only among
  // windows of that length does the lexicographic comparison run. This is where the problem's
  // real difficulty lives.
  // ===========================================================================================

  // Step 8: shortest beats lexicographically smaller. "0101" and "101" both hold two ones and
  //         both compare below "11" as strings, but the length-2 "11" at the tail wins. A
  //         solution that takes the string minimum over all beautiful substrings answers "0101"
  @Test
  void shorterLengthBeatsLexicographicallySmallerLongerWindow() {
    assertThat(sut.shortestBeautifulSubstring("101011", 2)).isEqualTo("11");
  }

  // Step 9: the lexicographic tie-break among equal-shortest windows. Three-one windows are
  //         "1101" at the left end and "1011" at the right end, both length 4, and "1011" is
  //         smaller. A scan that keeps only strictly shorter candidates answers "1101"
  @Test
  void lexicographicTieBreakAmongEqualShortestWindows() {
    assertThat(sut.shortestBeautifulSubstring("1101000010110", 3)).isEqualTo("1011");
  }

  // Step 10: a shorter window arriving late must replace the earlier longer one. Expanding
  //          right and returning as soon as the count first reaches k answers "10001",
  //          missing the "11" at the tail
  @Test
  void lateShorterWindowReplacesTheEarlierLongerOne() {
    assertThat(sut.shortestBeautifulSubstring("100011", 2)).isEqualTo("11");
  }

  // ===========================================================================================
  // The official examples (Steps 11-13).
  // ===========================================================================================

  // Step 11: LeetCode Example 1 — seven beautiful substrings, shortest length 5. The first
  //          window reaching three ones is the length-6 "100011", so a no-shrink scan fails
  //          here too; the answer is the later "11001"
  @Test
  void leetCodeExample1() {
    assertThat(sut.shortestBeautifulSubstring("100011001", 3)).isEqualTo("11001");
  }

  // Step 12: LeetCode Example 2 — three beautiful substrings, and the adjacent pair at the
  //          tail beats the "101" prefix on length
  @Test
  void leetCodeExample2() {
    assertThat(sut.shortestBeautifulSubstring("1011", 2)).isEqualTo("11");
  }

  // Step 13: LeetCode Example 3 — a string with no ones at all returns the empty string
  @Test
  void leetCodeExample3() {
    assertThat(sut.shortestBeautifulSubstring("000", 1)).isEmpty();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-15). n <= 100 is tiny — even a cubic enumeration of all
  // substrings is around 10^6 character scans — so these timeouts separate no complexity
  // classes; they exist to fail fast if a window loop stops advancing and spins forever.
  // ===========================================================================================

  // Step 14: maximum length. The ramp puts ones in adjacent pairs five zeros apart, so every
  //          three-one window has length 8 and there are two shapes: "11000001" from the
  //          first of a pair and "10000011" from the second. The tie-break must survive 100
  //          characters and pick "10000011"; keeping the first shortest answers "11000001"
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRampPicksTheSmallerOfTwoWindowShapes() {
    assertThat(sut.shortestBeautifulSubstring(ramp(100), 3)).isEqualTo("10000011");
  }

  // Step 15: the other corner of the constraints — k equals s.length on an all-ones string,
  //          so the single candidate is the entire 100-character input
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumKOnAllOnesReturnsTheWholeString() {
    assertThat(sut.shortestBeautifulSubstring("1".repeat(100), 100))
        .isEqualTo("1".repeat(100))
        .hasSize(100);
  }

  // ===========================================================================================
  // Hygiene (Step 16).
  // ===========================================================================================

  // Step 16: one instance answers several inputs of different sizes, largest in the middle —
  //          catches best-so-far state cached on the instance instead of reset per call
  @Test
  void oneInstanceAnswersManyInputsIndependently() {
    assertThat(sut.shortestBeautifulSubstring("1011", 2)).isEqualTo("11");
    assertThat(sut.shortestBeautifulSubstring(ramp(100), 3)).isEqualTo("10000011");
    assertThat(sut.shortestBeautifulSubstring("0", 1)).isEmpty();
    assertThat(sut.shortestBeautifulSubstring("100011001", 3)).isEqualTo("11001");
  }

  /** Deterministic maximum-size input: position i holds '1' when i modulo 7 is 0 or 1. */
  private static String ramp(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append(i % 7 < 2 ? '1' : '0');
    }
    return sb.toString();
  }
}
