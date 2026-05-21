package boj.boj23351;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 23351 물 주기 (Watering Plants).
 */
class MainTest {

  // --- Published sample 1: N=6, K=3, A=2, B=2. Three groups of two pots each. The keeper keeps
  //     topping up the lowest group every day; after five days the next "lowest" group has been
  //     overtaken by the passage of time. ---

  @Test
  @StdIo({"6 3 2 2"})
  void publishedSampleOneSurvivesFiveDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Published sample 2: N=2, K=2, A=1, B=1. Two pots, water one per day. Each day's +1 to the
  //     watered pot exactly cancels the -1 it would otherwise take, but the *other* pot still loses
  //     a unit. Initial budget K=2 across two pots gives three days. ---

  @Test
  @StdIo({"2 2 1 1"})
  void publishedSampleTwoSurvivesThreeDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The smallest possible input: K=1 means the unwatered pot's catnip dies during the very
  //     first day's decrement. A solution that returns 0 (catnip "already dead before any day") or
  //     2 (off-by-one in the death check) would be caught here. ---

  @Test
  @StdIo({"2 1 1 1"})
  void minimalInputAllowsCatnipOnlyOneDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- When there are more groups than the initial moisture K, no amount of B can save them all.
  //     With A=1 there are N=6 groups but K=3: by day 3, three groups have been watered (now huge)
  //     and the remaining three are still at 3 - they hit 0 at day 4's decrement, so output is 3.
  //     A solution that scales the answer with B (rather than recognizing the K-vs-groupCount cap)
  //     would over-report here. ---

  @Test
  @StdIo({"6 3 1 5"})
  void moreGroupsThanInitialMoistureCannotBeRescuedByLargeB(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A "thin and tall" stress case: two pots, watering exactly one per day. Each pair of days
  //     the system loses exactly one unit of total moisture, so starting from K=10 the catnip
  //     lasts 2·K − 1 = 19 days. A quadratic O(K²) simulation would still finish; the value just
  //     verifies the long-haul accounting stays consistent across many rounds. ---

  @Test
  @StdIo({"2 10 1 1"})
  void doublingInitialMoistureRoughlyDoublesSurvival(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  // --- Three groups (N=6, A=2) with B=2 < groupCount=3 means each round the lowest group's
  //     moisture drifts down by 1 relative to the global clock. Starting K=5 the catnip lasts 11
  //     days. The test pins down multi-group priority-queue behaviour, not just the two-bucket
  //     edge case. ---

  @Test
  @StdIo({"6 5 2 2"})
  void threeGroupsWithModerateBSurviveEleven(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Same A·B "water budget" of 2 per day, but split as fewer-larger groups (A=2, B=1) versus
  //     many-smaller (A=1, B=2). Fewer larger groups outlive many smaller ones because there are
  //     fewer mouths to feed. This test pins down the (A=2, B=1) variant: m=2 groups, output 5. ---

  @Test
  @StdIo({"4 3 2 1"})
  void fewerLargerGroupsOutlastForSameWaterBudget(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Companion to the previous: same N=4, K=3, same A·B=2 daily budget, but split as A=1, B=2
  //     gives m=4 groups. With m > K, the unwatered groups die before they can be reached, so the
  //     answer is just K=3. Pairing these two tests prevents a "regression by accident" where the
  //     algorithm only happens to work for one grouping shape. ---

  @Test
  @StdIo({"4 3 1 2"})
  void manySmallerGroupsDieFasterForSameWaterBudget(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The boundary case where group count exceeds K by exactly one. With m=3 groups and K=2,
  //     after two days of watering the lowest groups the third group has been waiting at moisture
  //     2 while the global clock advanced to 2, so it dies. Output is K=2 regardless of B. This
  //     catches "off-by-one in the death-check ordering" bugs distinctly from the much wider gap
  //     in `moreGroupsThanInitialMoistureCannotBeRescuedByLargeB`. ---

  @Test
  @StdIo({"6 2 2 2"})
  void groupCountExceedsInitialMoistureByOneForcesEarlyDeath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }
}
