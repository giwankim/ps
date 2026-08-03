package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StoneGameIIITest {
  StoneGameIII sut = new StoneGameIII();

  // Step 1: smallest valid input (1 <= stoneValue.length) — Alice takes the only stone and wins
  //         1 to 0
  @Test
  void singleStoneGoesToAlice() {
    assertThat(sut.stoneGameIII(new int[] {1})).isEqualTo("Alice");
  }

  // Step 2: a turn is mandatory — Alice cannot pass on a stone she does not want, so she takes
  //         the -1 and finishes behind Bob's untouched 0
  @Test
  void singleNegativeStoneMustStillBeTakenAndLosesForAlice() {
    assertThat(sut.stoneGameIII(new int[] {-1})).isEqualTo("Bob");
  }

  // Step 3: the third verdict at the smallest size — 0 to 0
  @Test
  void singleZeroStoneIsATie() {
    assertThat(sut.stoneGameIII(new int[] {0})).isEqualTo("Tie");
  }

  // Step 4: both value bounds (-1000 <= stoneValue[i] <= 1000) on a one-stone row
  @Test
  void singleStoneAtEitherValueBound() {
    assertThat(sut.stoneGameIII(new int[] {1000})).isEqualTo("Alice");
    assertThat(sut.stoneGameIII(new int[] {-1000})).isEqualTo("Bob");
  }

  // Step 5: fewer than three stones remain, so the whole row is a legal move — Alice must take
  //         both, since leaving the 2 behind loses 1 to 2
  @Test
  void twoStonesAreBothWorthTaking() {
    assertThat(sut.stoneGameIII(new int[] {1, 2})).isEqualTo("Alice");
  }

  // Step 6: clearing the row is legal but never obligatory — Alice takes only the -1 and leaves
  //         the -2 for Bob, winning -1 to -2. A solution that sweeps the tail whenever three or
  //         fewer stones remain answers Bob here
  @Test
  void twoNegativeStonesAreSplitRatherThanSwept() {
    assertThat(sut.stoneGameIII(new int[] {-1, -2})).isEqualTo("Alice");
  }

  // Step 7: three stones is the largest legal move — Alice clears the row for 6 to 0
  @Test
  void threeStonesCanBeTakenInASingleMove() {
    assertThat(sut.stoneGameIII(new int[] {1, 2, 3})).isEqualTo("Alice");
  }

  // Step 8: the same length, all negative — the only move that avoids a loss is taking exactly
  //         two, ending -3 to -3. Taking the best-looking prefix instead (just the -1) hands Bob
  //         the pair and answers Bob, so a greedy solution fails here
  @Test
  void threeNegativeStonesTieOnlyWhenAliceTakesExactlyTwo() {
    assertThat(sut.stoneGameIII(new int[] {-1, -2, -3})).isEqualTo("Tie");
  }

  // Step 9: LeetCode Example 1 — Alice's best is to clear 1+2+3 for 6, and Bob answers with the
  //         7 he was left
  @Test
  void leetCodeExample1BobTakesTheStoneAliceCannotReach() {
    assertThat(sut.stoneGameIII(new int[] {1, 2, 3, 7})).isEqualTo("Bob");
  }

  // Step 10: LeetCode Example 2 — the same opening three stones, and now Alice must take all
  //          three to strand Bob on the -9, winning 6 to -9. Taking one or two loses, so this is
  //          the case that pins down "three" as a real option
  @Test
  void leetCodeExample2AliceMustTakeAllThreeToStrandBobOnTheNegative() {
    assertThat(sut.stoneGameIII(new int[] {1, 2, 3, -9})).isEqualTo("Alice");
  }

  // Step 11: LeetCode Example 3 — the same opening three again, and taking all of them is now
  //          exactly enough to draw, 6 to 6. Steps 9 to 11 differ only in the final value, so
  //          any solution that decides from the stones in front of it fails at least two of them
  @Test
  void leetCodeExample3TakingAllThreeIsOnlyEnoughToDraw() {
    assertThat(sut.stoneGameIII(new int[] {1, 2, 3, 6})).isEqualTo("Tie");
  }

  // Step 12: the unique optimal first move is one stone — Alice banks the 2 alone so the three
  //          -1s fall to Bob and the 10 comes back to her, 10 to -1
  @Test
  void aliceTakesOneStoneToLeaveBobHoldingTheNegatives() {
    assertThat(sut.stoneGameIII(new int[] {2, -1, -1, -1, 10})).isEqualTo("Alice");
  }

  // Step 13: the unique optimal first move is two stones — Alice absorbs the first -4 to reach
  //          the 2 and leaves the second -4 for Bob, -2 to -4. Taking one stone loses -4 to 2
  //          and taking all three loses -6 to 0
  @Test
  void aliceTakesTwoStonesAbsorbingALossToReachTheGain() {
    assertThat(sut.stoneGameIII(new int[] {-4, 2, -4})).isEqualTo("Alice");
  }

  // Step 14: the largest stone is out of reach — one, two, or three ones all leave Bob able to
  //          claim the 10, and Alice's best is 3 to 10
  @Test
  void bobClaimsTheLargeStoneWhicheverPrefixAliceTakes() {
    assertThat(sut.stoneGameIII(new int[] {1, 1, 1, 10})).isEqualTo("Bob");
  }

  // Step 15: every stone is 0 except the last — taking one, two, or three zeros all leave Bob
  //          able to reach the 1, so Alice loses 0 to 1 with only one point in the whole row
  @Test
  void bobReachesTheLoneScoringStoneAtTheEnd() {
    assertThat(sut.stoneGameIII(new int[] {0, 0, 0, 1})).isEqualTo("Bob");
  }

  // Step 16: an all-zero row draws — the verdict turns on comparing the final margin against
  //          zero, so a solution that folds "not ahead" into a Bob win fails here
  @Test
  void allZeroStonesIsATie() {
    assertThat(sut.stoneGameIII(new int[] {0, 0, 0, 0})).isEqualTo("Tie");
  }

  // Step 17: both value bounds alternating — opening on the 1000 lets Alice take three stones
  //          for 1000 and leave the last -1000 to Bob
  @Test
  void alternatingValueBoundsLedByTheGainAreWonByAlice() {
    assertThat(sut.stoneGameIII(new int[] {1000, -1000, 1000, -1000})).isEqualTo("Alice");
  }

  // Step 18: the same four values rotated so the row opens with -1000 — both players clear a
  //          -1000/1000 pair for 0 apiece, so the multiset of values alone does not decide the
  //          game, the order does
  @Test
  void alternatingValueBoundsLedByTheLossAreATie() {
    assertThat(sut.stoneGameIII(new int[] {-1000, 1000, -1000, 1000})).isEqualTo("Tie");
  }

  // Step 19: a row of ones is decided by its length mod 6 — six ones split 3 to 3, while five
  //          and seven both give Alice the odd point. An off-by-one in the move loop, such as
  //          allowing only one or two stones, moves these boundaries
  @Test
  void rowsOfOnesTieOnlyAtLengthsDivisibleBySix() {
    assertThat(sut.stoneGameIII(new int[] {1, 1, 1, 1, 1})).isEqualTo("Alice");
    assertThat(sut.stoneGameIII(new int[] {1, 1, 1, 1, 1, 1})).isEqualTo("Tie");
    assertThat(sut.stoneGameIII(new int[] {1, 1, 1, 1, 1, 1, 1})).isEqualTo("Alice");
  }

  // ===========================================================================================
  // Maximum length (stoneValue.length <= 5 * 10^4).
  //
  // These pin down the property the small cases cannot: the search has to be linear in the number
  // of stones rather than exponential in it.
  //
  // What they deliberately do not pin down is recursion depth. ps.test-conventions runs tests with
  // -Xss64m, so a top-down solution recursing once per stone clears 50000 frames here even though
  // a 1MB stack gives out around n = 17000. Passing this section is therefore not evidence that a
  // recursive solution survives a judge; only a bottom-up loop makes the depth question moot.
  // ===========================================================================================

  // Step 20: maximum length of ones — 50000 is not a multiple of six, so Alice edges it 25001
  //          to 24999
  @Test
  void maximumLengthOfOnesIsWonByASinglePoint() {
    int[] stoneValue = new int[50000];
    Arrays.fill(stoneValue, 1);
    assertThat(sut.stoneGameIII(stoneValue)).isEqualTo("Alice");
  }

  // Step 21: two stones shorter is a multiple of six and draws instead, 24999 apiece — so the
  //          Step 20 verdict is a property of the length, not of the shape
  @Test
  void maximumLengthOfOnesTiesTwoStonesShorter() {
    int[] stoneValue = new int[49998];
    Arrays.fill(stoneValue, 1);
    assertThat(sut.stoneGameIII(stoneValue)).isEqualTo("Tie");
  }

  // Step 22: both upper bounds together — 50000 stones worth 1000 each put the two scores near
  //          2.5 * 10^7 and the whole row at 5 * 10^7, and Alice still wins by exactly one
  //          stone, 25001000 to 24999000
  @Test
  void maximumLengthAtTheUpperValueBoundIsWonByOneStone() {
    int[] stoneValue = new int[50000];
    Arrays.fill(stoneValue, 1000);
    assertThat(sut.stoneGameIII(stoneValue)).isEqualTo("Alice");
  }

  // Step 23: the lower value bound at maximum length — an odd count of -1000s forces Alice to
  //          swallow one extra stone, -25000000 to -24999000, so the running totals reach the
  //          negative end of the same range
  @Test
  void maximumLengthAtTheLowerValueBoundIsLostByOneStone() {
    int[] stoneValue = new int[49999];
    Arrays.fill(stoneValue, -1000);
    assertThat(sut.stoneGameIII(stoneValue)).isEqualTo("Bob");
  }

  // Step 24: maximum length settled by a single point — 49999 zeros and one 1 at the very end,
  //          which lands on Bob, 0 to 1. Every stone but the last is worth nothing, so any drift
  //          in the DP over 50000 states shows up as the wrong name
  @Test
  void maximumLengthDecidedByTheFinalStoneGoesToBob() {
    int[] stoneValue = new int[50000];
    stoneValue[stoneValue.length - 1] = 1;
    assertThat(sut.stoneGameIII(stoneValue)).isEqualTo("Bob");
  }
}
