package leetcode.p0601_0700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CanPlaceFlowersTest {
  private static final int MAX = 20_000;

  CanPlaceFlowers sut = new CanPlaceFlowers();

  // ===========================================================================================
  // The floor and the two edges (Steps 1-6).
  // ===========================================================================================

  // Step 1: smallest input the constraints allow (1 <= flowerbed.length) - one empty plot. It has
  //         no neighbors at all, so it takes a flower. A scan that reads flowerbed[i - 1] or
  //         flowerbed[i + 1] unguarded throws ArrayIndexOutOfBoundsException here, and one that
  //         only ever considers interior plots answers false
  @Test
  void singleEmptyPlotTakesOneFlower() {
    assertThat(sut.canPlaceFlowers(new int[] {0}, 1)).isTrue();
  }

  // Step 2: the only plot is already planted, so there is no room for one more
  @Test
  void singlePlantedPlotHasNoRoom() {
    assertThat(sut.canPlaceFlowers(new int[] {1}, 1)).isFalse();
  }

  // Step 3: n = 0 is allowed (0 <= n) and always fits, even when the bed's one empty plot is
  //         hemmed in on both sides so nothing could be planted. A solution that answers false
  //         whenever it finds no free plot fails here
  @Test
  void n0AlwaysFitsEvenWhenNothingCanBePlanted() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 1}, 0)).isTrue();
  }

  // Step 4: the left edge. Plot 0 has no left neighbor, so only its right neighbor has to be
  //         empty. A solution that demands a real, empty plot on both sides never plants at an
  //         edge and answers false. Treating the leading run of 2 zeros like an interior gap,
  //         (2 - 1) / 2, also gives 0
  @Test
  void leftEdgePlotNeedsOnlyItsRightNeighborEmpty() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 1}, 1)).isTrue();
  }

  // Step 5: the mirror of Step 4. The last plot has no right neighbor, so only its left neighbor
  //         has to be empty
  @Test
  void rightEdgePlotNeedsOnlyItsLeftNeighborEmpty() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0}, 1)).isTrue();
  }

  // Step 6: the no-adjacent rule applies between two new flowers too. Two empty plots hold one
  //         flower, not two. A scan that judges each plot against the original bed sees both plots
  //         as free and answers true for n = 2
  @Test
  void twoEmptyPlotsHoldOnlyOneFlower() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0}, 2)).isFalse();
  }

  // ===========================================================================================
  // Both neighbors matter (Steps 7-9).
  // ===========================================================================================

  // Step 7: the left edge plot is empty, but its right neighbor already holds a flower. A solution
  //         that only checks the left neighbor (and treats the edge as empty) plants here and
  //         answers true
  @Test
  void edgePlotNextToAFlowerOnTheRightIsNotFree() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 1}, 1)).isFalse();
  }

  // Step 8: the mirror of Step 7. A solution that only checks the right neighbor plants at the
  //         last plot and answers true
  @Test
  void edgePlotNextToAFlowerOnTheLeftIsNotFree() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0}, 1)).isFalse();
  }

  // Step 9: a gap of 2 between flowers holds nothing - each empty plot touches a flower. Halving
  //         the number of zeros gives 1, checking only one neighbor gives 1, and the edge-gap
  //         formula 2 / 2 gives 1. All three answer true
  @Test
  void gapOf2BetweenFlowersHoldsNothing() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 1}, 1)).isFalse();
  }

  // ===========================================================================================
  // Planting changes the bed (Steps 10-13).
  // ===========================================================================================

  // Step 10: three empty plots hold two flowers, at both edges. A solution that only plants in the
  //          interior finds just the middle plot and answers false
  @Test
  void threeEmptyPlotsHoldTwoFlowersAtTheEdges() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 0}, 2)).isTrue();
  }

  // Step 11: but not three. Once plot 0 is planted, plot 1 is no longer free. A scan that judges
  //          every plot against the original bed counts all three as free and answers true
  @Test
  void threeEmptyPlotsDoNotHoldThreeFlowers() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 0}, 3)).isFalse();
  }

  // Step 12: a gap of 4 between flowers holds one flower, not two - the two middle plots are free
  //          in the original bed but adjacent to each other. Counting free plots without planting
  //          gives 2, halving the zeros gives 2, and checking only the left neighbor gives 2
  @Test
  void gapOf4BetweenFlowersHoldsOnlyOne() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 0, 0, 1}, 2)).isFalse();
  }

  // Step 13: a gap of 5 between flowers holds two, at plots 2 and 4. A scan that plants at most
  //          once per gap and then skips ahead to the next flower answers false
  @Test
  void gapOf5BetweenFlowersHoldsTwo() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 0, 0, 0, 1}, 2)).isTrue();
  }

  // ===========================================================================================
  // n is a lower bound, not an exact count (Steps 14-16).
  // ===========================================================================================

  // Step 14: two flowers fit (plots 0 and 4) and only one is asked for. The answer is true: n is
  //          the number that must fit, not the number that fits. A solution that compares the
  //          count with n for equality answers false
  @Test
  void fewerThanTheMaximumIsStillTrue() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 1, 0, 0}, 1)).isTrue();
  }

  // Step 15: an entirely empty bed of 5 takes 3 flowers, at plots 0, 2 and 4 - both edges count.
  //          Applying the interior-gap formula (5 - 1) / 2 to the whole bed gives 2 and answers
  //          false
  @Test
  void entirelyEmptyBedTakesEveryOtherPlotIncludingBothEdges() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 0, 0, 0}, 3)).isTrue();
  }

  // Step 16: three empty plots and not one of them is free - each touches a flower. Empty is not
  //          the same as free: halving the zeros gives 1 and checking only the left neighbor
  //          gives 1, so both answer true
  @Test
  void emptyPlotsAreNotFreePlots() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 1, 0, 1, 0}, 1)).isFalse();
  }

  // ===========================================================================================
  // The official examples (Steps 17-18).
  // ===========================================================================================

  // Step 17: LeetCode Example 1 - a gap of 3 between flowers holds exactly one, in the middle
  @Test
  void leetCodeExample1() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 0, 1}, 1)).isTrue();
  }

  // Step 18: LeetCode Example 2 - the same bed cannot take two. A solution that counts empty
  //          plots sees 3 >= 2 and answers true
  @Test
  void leetCodeExample2() {
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 0, 1}, 2)).isFalse();
  }

  // ===========================================================================================
  // Constraint bounds: flowerbed.length <= 2 * 10^4 (Steps 19-23).
  //
  // A single left-to-right scan is O(n) and finishes in microseconds. Trying subsets of the empty
  // plots is exponential and cannot finish. Re-validating the whole bed after each planting is
  // O(n^2), around 4 * 10^8 steps, which sits at the edge of what a 5 second timeout separates.
  // Beyond speed, these steps check that the edge and gap arithmetic holds at scale, where an
  // off-by-one that a 5 plot bed hides changes the answer.
  // ===========================================================================================

  // Step 19: 20000 empty plots take 10000 flowers - every even plot, both edges included
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumEmptyBedTakesHalf() {
    assertThat(sut.canPlaceFlowers(new int[MAX], MAX / 2)).isTrue();
  }

  // Step 20: and not one more. n may go up to flowerbed.length (0 <= n <= flowerbed.length), so
  //          10001 is a legal ask that must be refused
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumEmptyBedDoesNotTakeHalfPlusOne() {
    assertThat(sut.canPlaceFlowers(new int[MAX], MAX / 2 + 1)).isFalse();
  }

  // Step 21: 10000 flowers already alternate with 10000 empty plots - the densest bed the
  //          constraints allow. Every empty plot touches a flower, so nothing fits
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumAlternatingBedHasNoRoom() {
    assertThat(sut.canPlaceFlowers(flowerEvery(2), 1)).isFalse();
  }

  // Step 22: a flower every 4 plots. 4999 interior gaps of 3 hold one each, and the trailing run of
  //          3 empty plots at the right edge holds one more: 5000 in total
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void flowerEvery4PlotsLeavesRoomForOnePerGap() {
    assertThat(sut.canPlaceFlowers(flowerEvery(4), 5000)).isTrue();
  }

  // Step 23: and not 5001. In the trailing run the last two plots both look free in the original
  //          bed, so a scan that never plants counts 5001 and answers true. Step 11 at scale
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void flowerEvery4PlotsDoesNotLeaveRoomForOneMore() {
    assertThat(sut.canPlaceFlowers(flowerEvery(4), 5001)).isFalse();
  }

  // ===========================================================================================
  // Hygiene (Step 24).
  // ===========================================================================================

  // Step 24: one instance answers many beds of different sizes, deliberately out of order with the
  //          largest in the middle. A count or a previous-plot flag kept on the instance instead of
  //          reset per call carries over and corrupts the later answers
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.canPlaceFlowers(new int[] {0, 0, 1, 0, 0}, 2)).isTrue();
    assertThat(sut.canPlaceFlowers(new int[MAX], MAX / 2)).isTrue();
    assertThat(sut.canPlaceFlowers(new int[] {1, 0, 0, 1}, 1)).isFalse();
    assertThat(sut.canPlaceFlowers(new int[] {0}, 1)).isTrue();
  }

  /** A bed of {@link #MAX} plots with a flower wherever the index is a multiple of period. */
  private static int[] flowerEvery(int period) {
    int[] bed = new int[MAX];
    Arrays.setAll(bed, i -> i % period == 0 ? 1 : 0);
    return bed;
  }
}
