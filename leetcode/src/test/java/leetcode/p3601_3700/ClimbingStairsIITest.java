package leetcode.p3601_3700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ClimbingStairsIITest {
  ClimbingStairsII sut = new ClimbingStairsII();

  // ===========================================================================================
  // The floor: n = 1 allows exactly one jump (Steps 1-2).
  // ===========================================================================================

  // Step 1: the smallest input. The only jump is 0 -> 1 and it costs costs[1] + 1 squared = 2.
  //         Step 0 is free, so there is nothing else to add. Dropping the squared term answers 1
  @Test
  void n1CheapestStepPaysCostPlusUnitJump() {
    assertThat(sut.climbStairs(1, new int[] {1})).isEqualTo(2);
  }

  // Step 2: the same jump at the cost ceiling. The price belongs to the step landed on, so a
  //         solution that charges the step it leaves pays nothing for step 0 and answers 1
  @Test
  void n1MostExpensiveStepIsChargedOnLanding() {
    assertThat(sut.climbStairs(1, new int[] {10_000})).isEqualTo(10_001);
  }

  // ===========================================================================================
  // One rule at a time: squared penalty, landing cost, jump limit (Steps 3-13).
  // ===========================================================================================

  // Step 3: two single jumps cost 2 + 2 = 4, one double jump costs 1 + 4 = 5. Fewer jumps is not
  //         cheaper, so a solution that always takes the longest legal jump answers 5
  @Test
  void n2EqualCostsPreferTwoSingleJumps() {
    assertThat(sut.climbStairs(2, new int[] {1, 1})).isEqualTo(4);
  }

  // Step 4: the mirror of Step 3. Jumping over the peak costs 1 + 4 = 5. A solution that only ever
  //         moves one step answers 103
  @Test
  void n2DoubleJumpSkipsLeadingPeak() {
    assertThat(sut.climbStairs(2, new int[] {100, 1})).isEqualTo(5);
  }

  // Step 5: step n cannot be skipped, so its cost is paid whatever the route. Best is
  //         2 + 11 = 13. Charging the step you leave answers 3, reading costs as 0-indexed by step
  //         number answers 4
  @Test
  void n2LastStepPeakMustStillBePaid() {
    assertThat(sut.climbStairs(2, new int[] {1, 10})).isEqualTo(13);
  }

  // Step 6: the same two values in the other order give 5 rather than 13, so the answer depends on
  //         which step carries which cost. Anything that sorts or sums the costs cannot tell these
  //         inputs apart
  @Test
  void n2SwappingTheCostsChangesTheAnswer() {
    assertThat(sut.climbStairs(2, new int[] {10, 1})).isEqualTo(5);
  }

  // Step 7: a genuine tie. Singles cost 3 + 2 = 5 and the double costs 1 + 4 = 5. Only the total
  //         is returned, so either route must produce the same answer
  @Test
  void n2TiedRoutesReturnTheSharedTotal() {
    assertThat(sut.climbStairs(2, new int[] {2, 1})).isEqualTo(5);
  }

  // Step 8: the penalty is squared. Singles cost 2 + 2 + 2 = 6, the triple costs 1 + 9 = 10. With
  //         a linear penalty the triple would look like 1 + 3, and the answer would be 4
  @Test
  void n3EqualCostsExposeSquaredPenalty() {
    assertThat(sut.climbStairs(3, new int[] {1, 1, 1})).isEqualTo(6);
  }

  // Step 9: two adjacent peaks can only be cleared by a triple jump, 1 + 9 = 10. A solution that
  //         carries over Climbing Stairs and allows only jumps of 1 or 2 answers 10006
  @Test
  void n3TwoPeaksRequireTripleJump() {
    assertThat(sut.climbStairs(3, new int[] {10_000, 10_000, 1})).isEqualTo(10);
  }

  // Step 10: step 1 is expensive but optional. 0 -> 2 -> 3 costs (1 + 4) + (1 + 1) = 7. Charging
  //          the step you leave answers 6, and reading costs as 0-indexed by step number answers 5
  @Test
  void n3FirstStepPeakIsSkippable() {
    assertThat(sut.climbStairs(3, new int[] {10_000, 1, 1})).isEqualTo(7);
  }

  // Step 11: the destination is step n itself and landing there is mandatory, unlike Min Cost
  //          Climbing Stairs where the top sits beyond the last priced step. Singles cost
  //          2 + 2 + 10001 = 10005. A solution that finishes from any of the last steps without
  //          paying costs[n] answers 0
  @Test
  void n3FinalStepCostCannotBeAvoided() {
    assertThat(sut.climbStairs(3, new int[] {1, 1, 10_000})).isEqualTo(10_005);
  }

  // Step 12: the jump limit is exactly 3. A jump of 4 would cost only 1 + 16 = 17. The legal best
  //          is 0 -> 2 -> 4, (100 + 4) + (1 + 4) = 109
  @Test
  void n4JumpOfFourIsIllegalEvenWhenCheapest() {
    assertThat(sut.climbStairs(4, new int[] {100, 100, 100, 1})).isEqualTo(109);
  }

  // Step 13: a triple jump in the middle of the route, not only from step 0. 0 -> 1 -> 4 costs
  //          (1 + 1) + (1 + 9) = 12. Allowing only jumps of 1 or 2 answers 10008
  @Test
  void n4TripleJumpFromAnInnerStep() {
    assertThat(sut.climbStairs(4, new int[] {1, 10_000, 10_000, 1})).isEqualTo(12);
  }

  // ===========================================================================================
  // Interactions: why this has to be a DP (Steps 14-17).
  // ===========================================================================================

  // Step 14: the smallest input that defeats greedy. From step 0 the single costs 3 + 1 = 4 and
  //          the double costs 1 + 4 = 5, so taking the cheapest next jump goes 4 + 2 = 6. The
  //          double alone is 5
  @Test
  void n2CheapestNextJumpIsNotOptimal() {
    assertThat(sut.climbStairs(2, new int[] {3, 1})).isEqualTo(5);
  }

  // Step 15: the unique optimum uses all three jump lengths, 0 -> 1 -> 3 -> 6, which costs
  //          2 + 5 + 19 = 26. Greedy answers 40, and allowing only jumps of 1 or 2 answers 32
  @Test
  void n6UniqueOptimumMixesEveryJumpLength() {
    assertThat(sut.climbStairs(6, new int[] {1, 10, 1, 10, 10, 10})).isEqualTo(26);
  }

  // Step 16: with cheap steps the penalty dominates. Per step climbed, a single costs 2, a double
  //          2.5 and a triple about 3.33, so six singles win with 12
  @Test
  void n6CheapStairsFavorSingleJumps() {
    assertThat(sut.climbStairs(6, new int[] {1, 1, 1, 1, 1, 1})).isEqualTo(12);
  }

  // Step 17: the mirror of Step 16. With expensive steps landing dominates, so two triples win
  //          with 10009 + 10009 = 20018. Greedy sees 10001 as the cheapest next jump every time
  //          and answers 60006
  @Test
  void n6ExpensiveStairsFavorTripleJumps() {
    assertThat(sut.climbStairs(6, new int[] {10_000, 10_000, 10_000, 10_000, 10_000, 10_000}))
        .isEqualTo(20_018);
  }

  // ===========================================================================================
  // Official examples, verbatim (Steps 18-20).
  // ===========================================================================================

  // Step 18: LeetCode Example 1. 0 -> 1 -> 2 -> 4 costs 2 + 3 + 8 = 13. All singles answer 14, so
  //          even increasing costs need a jump at the right moment
  @Test
  void leetCodeExample1() {
    assertThat(sut.climbStairs(4, new int[] {1, 2, 3, 4})).isEqualTo(13);
  }

  // Step 19: LeetCode Example 2. 0 -> 2 -> 4 costs 5 + 6 = 11. Alternating costs punish a solution
  //          that moves one step at a time, which answers 18
  @Test
  void leetCodeExample2() {
    assertThat(sut.climbStairs(4, new int[] {5, 1, 6, 2})).isEqualTo(11);
  }

  // Step 20: LeetCode Example 3. 0 -> 3 costs 3 + 9 = 12. Greedy answers 17, and allowing only
  //          jumps of 1 or 2 answers 16
  @Test
  void leetCodeExample3() {
    assertThat(sut.climbStairs(3, new int[] {9, 8, 3})).isEqualTo(12);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 21-26). n = 100 already has far too many routes to enumerate, so
  // Step 21 rejects unmemoized recursion. n = 10^5 rejects relaxing from every earlier step, about
  // 5 * 10^9 operations, while the O(n) DP looks at three predecessors per step. A memoized
  // recursion also descends 10^5 frames here, so mind the stack.
  // ===========================================================================================

  // Step 21: a ramp that is short by array standards but has exponentially many routes
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100RampRejectsExponentialSearch() {
    int[] costs = new int[100];
    Arrays.setAll(costs, i -> i + 1);

    assertThat(sut.climbStairs(100, costs)).isEqualTo(2013);
  }

  // Step 22: every cost at the ceiling, the priciest staircase of this length. Triples win and the
  //          total still fits an int. Jumps of 1 or 2 alone would answer 500200000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000AllCostsAtCeiling() {
    assertThat(sut.climbStairs(100_000, filled(100_000, 10_000))).isEqualTo(333_639_996);
  }

  // Step 23: every cost at the floor, where single jumps win at 2 per step. A linear penalty
  //          would answer 133334
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000AllCostsAtFloor() {
    assertThat(sut.climbStairs(100_000, filled(100_000, 1))).isEqualTo(200_000);
  }

  // Step 24: a repeating ramp 1, 2, ..., 10000. Greedy keeps taking singles while costs rise and
  //          answers 499970063
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000RepeatingRamp() {
    int[] costs = new int[100_000];
    Arrays.setAll(costs, i -> i % 10_000 + 1);

    assertThat(sut.climbStairs(100_000, costs)).isEqualTo(166_956_645);
  }

  // Step 25: a scattered deterministic pattern with no exploitable shape. A jump limit of 4
  //          instead of 3 answers 70943750
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000ScatteredCosts() {
    int[] costs = new int[100_000];
    Arrays.setAll(costs, i -> i * 7919 % 10_000 + 1);

    assertThat(sut.climbStairs(100_000, costs)).isEqualTo(121_770_050);
  }

  // Step 26: only every third step is cheap, so the route rides triples and step n, which is not a
  //          multiple of 3, must still be paid at 10000. Jumps of 1 or 2 alone answer 333539999
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000OnlyEveryThirdStepIsCheap() {
    int[] costs = new int[100_000];
    Arrays.setAll(costs, i -> (i + 1) % 3 == 0 ? 1 : 10_000);

    assertThat(sut.climbStairs(100_000, costs)).isEqualTo(343_331);
  }

  // ===========================================================================================
  // Hygiene (Step 27).
  // ===========================================================================================

  // Step 27: one instance answers several sizes, deliberately out of order with the largest in the
  //          middle. A memo kept on the instance would return stale answers for the smaller inputs
  @Test
  void oneInstanceAnswersManyInputs() {
    int[] scattered = new int[1000];
    Arrays.setAll(scattered, i -> i * 7919 % 10_000 + 1);

    assertThat(sut.climbStairs(2, new int[] {100, 1})).isEqualTo(5);
    assertThat(sut.climbStairs(1000, scattered)).isEqualTo(1_215_322);
    assertThat(sut.climbStairs(1, new int[] {1})).isEqualTo(2);
    assertThat(sut.climbStairs(2, new int[] {1, 10})).isEqualTo(13);
  }

  private static int[] filled(int n, int value) {
    int[] costs = new int[n];
    Arrays.fill(costs, value);
    return costs;
  }
}
