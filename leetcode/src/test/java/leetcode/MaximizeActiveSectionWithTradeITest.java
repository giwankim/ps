package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximizeActiveSectionWithTradeITest {
  MaximizeActiveSectionWithTradeI sut = new MaximizeActiveSectionWithTradeI();

  // Step 1: smallest valid input (n = 1) — a lone inactive section has no one-block to
  //         sacrifice, so no trade exists and nothing activates
  @Test
  void singleInactiveSectionStaysInactive() {
    assertThat(sut.maxActiveSectionsAfterTrade("0")).isEqualTo(0);
  }

  // Step 2: a lone active section merges with the augmented '1's on both ends, so it is not
  //         surrounded by '0's and cannot be traded — the count is just the existing one
  @Test
  void singleActiveSectionStaysActive() {
    assertThat(sut.maxActiveSectionsAfterTrade("1")).isEqualTo(1);
  }

  // Step 3: an all-active string has no zeros to gain — the answer is the length itself
  @Test
  void allActiveStringHasNothingToTrade() {
    assertThat(sut.maxActiveSectionsAfterTrade("1111")).isEqualTo(4);
  }

  // Step 4: an all-inactive string has no one-block to sacrifice, so a trade can never start
  @Test
  void allInactiveStringHasNoBlockToSacrifice() {
    assertThat(sut.maxActiveSectionsAfterTrade("0000")).isEqualTo(0);
  }

  // Step 5: LeetCode Example 1 — "01": the '1' touches the right end, merges with the
  //         augmented '1', and is not surrounded by '0's; no valid trade is possible
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxActiveSectionsAfterTrade("01")).isEqualTo(1);
  }

  // Step 6: smallest tradeable string — sacrificing the lone interior '1' merges both
  //         zero-runs into one block surrounded by ones, and filling it activates everything
  @Test
  void smallestTradeableStringActivatesEverything() {
    assertThat(sut.maxActiveSectionsAfterTrade("010")).isEqualTo(3);
  }

  // Step 7: one-blocks touching either end of s fuse with the augmented sentinels, so
  //         neither the leading nor the trailing block can ever be sacrificed
  @Test
  void edgeOneBlocksCannotBeTraded() {
    assertThat(sut.maxActiveSectionsAfterTrade("10")).isEqualTo(1);
    assertThat(sut.maxActiveSectionsAfterTrade("001")).isEqualTo(1);
  }

  // Step 8: LeetCode Example 2 — "0100": trade the middle '1' to merge zero-runs of
  //         lengths 1 and 2, gaining 3 on top of the single existing one
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxActiveSectionsAfterTrade("0100")).isEqualTo(4);
  }

  // Step 9: LeetCode Example 3 — "1000100": the interior '1' is flanked by zero-runs of
  //         lengths 3 and 2, so the trade activates the whole string
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxActiveSectionsAfterTrade("1000100")).isEqualTo(7);
  }

  // Step 10: LeetCode Example 4 — "01010": two interior candidates gain the same 1 + 1,
  //          and only one trade is allowed, so the answer is 2 existing + 2 gained
  @Test
  void leetCodeExample4() {
    assertThat(sut.maxActiveSectionsAfterTrade("01010")).isEqualTo(4);
  }

  // Step 11: unequal candidates — the first '1' is flanked by zero-runs 1 + 2, the second
  //          by 2 + 3; the optimal trade takes the larger surrounding sum
  @Test
  void picksTheCandidateWithLargerSurroundingZeroRuns() {
    assertThat(sut.maxActiveSectionsAfterTrade("01001000")).isEqualTo(7);
  }

  // Step 12: a sacrificed one-block may span multiple characters — "0110" trades the "11"
  //          block and still activates the full string
  @Test
  void multiCharacterOneBlockCanBeSacrificed() {
    assertThat(sut.maxActiveSectionsAfterTrade("0110")).isEqualTo(4);
  }

  // Step 13: trap — the middle "00" is surrounded by ones and looks fillable, but a trade
  //          must start by sacrificing a one-block surrounded by zeros, and none exists
  @Test
  void zeroBlockSurroundedByOnesStillNeedsASacrifice() {
    assertThat(sut.maxActiveSectionsAfterTrade("110011")).isEqualTo(4);
  }

  // Step 14: the gain is exactly the two flanking zero-runs — the sacrificed '1' turns to
  //          zero but is re-activated when the merged block is filled, so it cancels out
  @Test
  void sacrificedOnesAreRecoveredByTheFill() {
    assertThat(sut.maxActiveSectionsAfterTrade("00100")).isEqualTo(5);
  }

  // Step 15: the upper constraint bound (n = 100000) — a single interior '1' merges the two
  //          huge zero-runs and activates the entire string
  @Test
  void maximumLengthSingleOneActivatesTheWholeString() {
    String s = "0".repeat(49999) + "1" + "0".repeat(50000);
    assertThat(sut.maxActiveSectionsAfterTrade(s)).isEqualTo(100000);
  }

  // Step 16: maximum-length alternating pattern — every interior '1' gains only 1 + 1, and
  //          the trailing '1' is edge-fused, so one trade adds exactly 2 to the 50000 ones
  @Test
  void maximumLengthAlternatingPatternGainsOnlyTwo() {
    String s = "01".repeat(50000);
    assertThat(sut.maxActiveSectionsAfterTrade(s)).isEqualTo(50002);
  }
}
