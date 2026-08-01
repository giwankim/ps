package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PredictTheWinnerTest {
  PredictTheWinner sut = new PredictTheWinner();

  // Step 1: smallest valid input (1 <= nums.length) — player 1 takes the only number
  @Test
  void singleElementIsAlwaysAWin() {
    assertThat(sut.predictTheWinner(new int[] {1})).isTrue();
  }

  // Step 2: the lower value bound (0 <= nums[i]) — 0 vs 0 is a tie, and a tie is a player 1 win
  @Test
  void singleZeroTiesAndStillCountsAsAWin() {
    assertThat(sut.predictTheWinner(new int[] {0})).isTrue();
  }

  // Step 3: with two numbers player 1 simply claims the larger end, whichever side it sits on
  @Test
  void twoElementsLetPlayerOneClaimTheLargerEnd() {
    assertThat(sut.predictTheWinner(new int[] {1, 2})).isTrue();
    assertThat(sut.predictTheWinner(new int[] {2, 1})).isTrue();
  }

  // Step 4: the tie rule again with a real score — 2 vs 2 must be true, so the final
  //         comparison is >= and not >
  @Test
  void equalPairEndsInATie() {
    assertThat(sut.predictTheWinner(new int[] {2, 2})).isTrue();
  }

  // Step 5: odd length with equal values — player 1 takes the extra turn and leads 14 to 7
  @Test
  void oddLengthOfEqualValuesGivesPlayerOneTheExtraTurn() {
    assertThat(sut.predictTheWinner(new int[] {7, 7, 7})).isTrue();
  }

  // Step 6: LeetCode Example 1 — the first losing position. Whichever end player 1 takes,
  //         player 2 answers with the 5, so player 1 finishes 3 to 5
  @Test
  void leetCodeExample1PlayerOneCannotWin() {
    assertThat(sut.predictTheWinner(new int[] {1, 5, 2})).isFalse();
  }

  // Step 7: the game is symmetric under reversal — both ends are always available, so
  //         mirroring the array cannot change the outcome
  @Test
  void reversedArrayProducesTheSameResult() {
    assertThat(sut.predictTheWinner(new int[] {2, 5, 1})).isFalse();
  }

  // Step 8: for three numbers the rule is exactly ends >= middle — here 3 + 3 beats 5
  @Test
  void threeElementsWinWhenTheEndsOutweighTheMiddle() {
    assertThat(sut.predictTheWinner(new int[] {3, 5, 3})).isTrue();
  }

  // Step 9: the exact boundary of that rule — 2 + 3 equals 5, a 5-5 tie that must be true
  @Test
  void threeElementsTieWhenTheEndsExactlyMatchTheMiddle() {
    assertThat(sut.predictTheWinner(new int[] {2, 5, 3})).isTrue();
  }

  // Step 10: an even length is always a player 1 win — by committing to the odd indices they
  //          collect both 100s even though every end they can reach starts out small
  @Test
  void evenLengthAlwaysFavorsPlayerOne() {
    assertThat(sut.predictTheWinner(new int[] {1, 100, 1, 100})).isTrue();
  }

  // Step 11: appending one element flips that same array to a loss — player 1 is forced to
  //          spend the first turn on a 1, handing player 2 the even-length advantage
  @Test
  void oneExtraElementFlipsTheEvenLengthWinIntoALoss() {
    assertThat(sut.predictTheWinner(new int[] {1, 100, 1, 100, 1})).isFalse();
  }

  // Step 12: optimal play is not greedy — grabbing the larger end (3) loses 5 to 11, while
  //          conceding the 1 first exposes the 10 and wins 11 to 5
  @Test
  void takingTheLargerEndGreedilyWouldLose() {
    assertThat(sut.predictTheWinner(new int[] {1, 2, 10, 3})).isTrue();
  }

  // Step 13: LeetCode Example 2 — player 1 opens with 1 so that the 233 is unreachable for
  //          player 2 on the next turn, finishing 234 to 12
  @Test
  void leetCodeExample2PlayerOneSacrificesToReachTheLargestValue() {
    assertThat(sut.predictTheWinner(new int[] {1, 5, 233, 7})).isTrue();
  }

  // Step 14: every value at the lower bound — no choice matters, 0 vs 0 is a tie
  @Test
  void allZerosTieRegardlessOfLength() {
    assertThat(sut.predictTheWinner(new int[] {0, 0, 0, 0, 0})).isTrue();
  }

  // Step 15: the upper bounds together (length 20, every value 10^7) — the totals reach
  //          10^8 each, so the answer is a tie and the running scores must not overflow
  @Test
  void maximumLengthOfMaximumValuesTies() {
    int[] nums = new int[20];
    Arrays.fill(nums, 10_000_000);
    assertThat(sut.predictTheWinner(nums)).isTrue();
  }

  // Step 16: maximum length with the big values on the even indices — player 1 holds that
  //          parity for all ten turns and wins 10^8 to 0
  @Test
  void maximumLengthZigzagStartingHighIsAWin() {
    int[] nums = new int[20];
    for (int i = 0; i < nums.length; i += 2) {
      nums[i] = 10_000_000;
    }
    assertThat(sut.predictTheWinner(nums)).isTrue();
  }

  // Step 17: the same zigzag one element shorter, so both ends are 0 — player 1 must burn a
  //          turn on a zero and player 2 then sweeps all nine 10^7 values
  @Test
  void oddLengthZigzagStartingLowIsALoss() {
    int[] nums = new int[19];
    for (int i = 1; i < nums.length; i += 2) {
      nums[i] = 10_000_000;
    }
    assertThat(sut.predictTheWinner(nums)).isFalse();
  }
}
