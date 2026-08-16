package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StoneGameIXTest {
  StoneGameIX sut = new StoneGameIX();

  // Step 1: LeetCode Example 2 — Alice takes the only stone, the running sum is 2 and the board is
  // empty; nobody ever hit a multiple of 3, so Bob wins by the "no stones remaining" rule
  @Test
  void singleStoneLeavesBobWinningByExhaustion() {
    assertThat(sut.stoneGameIX(new int[] {2})).isFalse();
  }

  // Step 2: the other way to lose — a lone stone divisible by 3 forces Alice to make the sum 3 on
  // move one. Bob wins here too, but by divisibility rather than exhaustion
  @Test
  void loneMultipleOfThreeLosesForAliceOnHerFirstMove() {
    assertThat(sut.stoneGameIX(new int[] {3})).isFalse();
  }

  // Step 3: LeetCode Example 1 — one stone of each nonzero remainder. Whichever Alice takes, Bob is
  // left with the other and is forced to complete 1 + 2 = 3, so Alice wins
  @Test
  void oneStoneOfEachRemainderWinsForAlice() {
    assertThat(sut.stoneGameIX(new int[] {2, 1})).isTrue();
  }

  // Step 4: the same board written with large values — 10000 % 3 == 1 and 10001 % 3 == 2. Nothing
  // but the remainder can matter, so a solution comparing raw values fails here
  @Test
  void onlyTheRemainderOfEachValueMatters() {
    assertThat(sut.stoneGameIX(new int[] {10000, 10001})).isTrue();
  }

  // Step 5: every stone leaves remainder 1. Alice takes one (sum 1), Bob takes one (sum 2), and
  // Alice must take the third, reaching 3 — with a single remainder available she cannot dodge
  @Test
  void everyStoneSharingRemainderOneLosesForAlice() {
    assertThat(sut.stoneGameIX(new int[] {1, 4, 7})).isFalse();
  }

  // Step 6: the mirror image, every stone leaving remainder 2 — sums run 2, 4, 6 and Alice is again
  // the one who lands on a multiple of 3. Alice needs *both* remainders present, not just one
  @Test
  void everyStoneSharingRemainderTwoLosesForAlice() {
    assertThat(sut.stoneGameIX(new int[] {2, 5, 8})).isFalse();
  }

  // Step 7: LeetCode Example 3 — remainders are 2,1,2,1,0, so two of each nonzero remainder plus a
  // single multiple of 3. Both remainders are present yet Bob still wins: presence alone is not
  // enough once an odd number of multiples of 3 is in play
  @Test
  void balancedBoardWithOneMultipleOfThreeLosesForAlice() {
    assertThat(sut.stoneGameIX(new int[] {5, 1, 2, 4, 3})).isFalse();
  }

  // Step 8: stones divisible by 3 never move the running sum, so they are pass moves. An even
  // number of them cancels out — this board plays exactly like the winning {2,1} of step 3
  @Test
  void multiplesOfThreeInPairsCancelOut() {
    assertThat(sut.stoneGameIX(new int[] {3, 6, 1, 2})).isTrue();
  }

  // Step 9: add a single unpaired pass move to that same winning board and it becomes a loss. Only
  // the parity of the multiples of 3 matters, so the count must be reduced mod 2, never ignored
  @Test
  void oneUnpairedMultipleOfThreeFlipsTheWinningBoard() {
    assertThat(sut.stoneGameIX(new int[] {3, 1, 2})).isFalse();
  }

  // Step 10: an even number of multiples of 3 does not by itself hand Alice the win — with no
  // stone of remainder 2 left she is back in the step 5 position and loses
  @Test
  void evenMultiplesOfThreeStillRequireBothRemainders() {
    assertThat(sut.stoneGameIX(new int[] {3, 6, 1, 4})).isFalse();
  }

  // Step 11: odd multiples of 3, remainder counts 3 and 1 — a gap of exactly 2 is still a loss.
  // With the parity flipped Alice needs a surplus large enough to outlast the extra pass move
  @Test
  void oddMultiplesOfThreeLoseWhenTheRemainderGapIsTwo() {
    assertThat(sut.stoneGameIX(new int[] {3, 1, 1, 1, 2})).isFalse();
  }

  // Step 12: one more stone of remainder 1 widens the gap to 3 and Alice wins. Paired with step 11
  // this pins the threshold at strictly greater than 2 rather than at 2 or more
  @Test
  void oddMultiplesOfThreeWinWhenTheRemainderGapIsThree() {
    assertThat(sut.stoneGameIX(new int[] {3, 1, 1, 1, 1, 2})).isTrue();
  }

  // Step 13: the same gap of 3 built out of remainder 2 instead. The surplus may sit on either
  // side, so the comparison has to be on the absolute difference
  @Test
  void theRemainderGapCountsInEitherDirection() {
    assertThat(sut.stoneGameIX(new int[] {3, 2, 2, 2, 2, 1})).isTrue();
  }

  // Step 14: the constraint ceiling, 1e5 stones split evenly between the two nonzero remainders.
  // No multiples of 3 and both remainders present, so Alice wins — and counting must be linear,
  // since anything that simulates the turn order stone by stone will not finish
  @Test
  void maxSizeBoardOfBothRemaindersIsDecidedByCounting() {
    assertThat(sut.stoneGameIX(board(0, 50_000, 50_000))).isTrue();
  }

  // Step 15: 1e5 stones again, now with one unpaired multiple of 3 and a remainder gap of just 1.
  // The step 11 boundary has to hold at full scale too, where the two counts are nearly equal
  @Test
  void maxSizeBoardWithOddMultiplesOfThreeNeedsMoreThanANarrowGap() {
    assertThat(sut.stoneGameIX(board(1, 50_000, 49_999))).isFalse();
  }

  /** A board holding the given number of stones congruent to 0, 1 and 2 modulo 3. */
  private static int[] board(int zeros, int ones, int twos) {
    int[] stones = new int[zeros + ones + twos];
    Arrays.fill(stones, 0, ones, 1);
    Arrays.fill(stones, ones, ones + zeros, 3);
    Arrays.fill(stones, ones + zeros, stones.length, 2);
    return stones;
  }
}
