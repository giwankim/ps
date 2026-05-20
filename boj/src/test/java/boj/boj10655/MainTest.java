package boj.boj10655;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 10655 마라톤 1 (Marathon 1, USACO 2014 December Bronze).
 */
class MainTest {

  // --- The single sample published with the problem. ---

  @Test
  @StdIo({"4", "0 0", "8 3", "11 -1", "10 0"})
  void publishedSampleSkipsTheBigDetourAtTheSecondCheckpoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 11 + 7 + 2 = 20.
    // Skip (8,3): reroute 11+7 -> |0-11|+|0-(-1)| = 12, gain 6, total 14.
    // Skip (11,-1): reroute 7+2 -> |8-10|+|3-0|  = 5, gain 4, total 16.
    // Minimum is 14.
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // --- N = 3 is the minimum input: only a single interior checkpoint exists, so the optimum is
  //     either that single skip or the full 1-2-3 walk. ---

  @Test
  @StdIo({"3", "0 0", "5 5", "10 0"})
  void minimumSizeInputHasASingleInteriorCheckpointToSkip(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 10 + 10 = 20. Skipping (5,5) routes (0,0)->(10,0) directly for 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Every checkpoint at the same location: the no-skip total is already 0 and every skip
  //     has zero gain. The answer is 0. ---

  @Test
  @StdIo({"4", "5 5", "5 5", "5 5", "5 5"})
  void allCheckpointsCoincideAtASinglePointGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every segment has length 0, and so does every reroute. The result is 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A monotonic walk along the x-axis is already optimal: each interior point lies exactly on
  //     the segment between its neighbors, so every skip gain is 0 (tight Manhattan triangle
  //     inequality). The "up to one" wording means no-skip is always allowed, and is best here. ---

  @Test
  @StdIo({"4", "0 0", "1 0", "2 0", "3 0"})
  void collinearMonotonicCheckpointsGainNothingFromSkipping(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 1 + 1 + 1 = 3. Skipping (1,0) or (2,0) reroutes 2 -> 2; no improvement.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A sharp backtrack is where the skip helps most: the interior point lies far off the
  //     straight line between start and finish, so the rerouted segment is much shorter. ---

  @Test
  @StdIo({"3", "0 0", "100 0", "0 0"})
  void sharpBacktrackingDetourYieldsTheLargestPossibleGain(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 100 + 100 = 200. Skipping (100,0) routes (0,0)->(0,0) = 0, gain 200.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The optimum can be at the first interior index (index 1). ---

  @Test
  @StdIo({"4", "0 0", "99 99", "1 0", "2 0"})
  void bestSkipIsAdjacentToTheStartingCheckpoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 198 + 197 + 1 = 396.
    //   Skip (99,99) [idx 1]: reroute (0,0)->(1,0)   = 1;   gain = 198+197-1   = 394 -> 2.
    //   Skip (1,0)   [idx 2]: reroute (99,99)->(2,0) = 196; gain = 197+1-196   = 2   -> 394.
    // Minimum is 2 via skipping the first interior checkpoint.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The optimum can be at the last interior index (index N−2). ---

  @Test
  @StdIo({"4", "0 0", "1 0", "99 99", "2 0"})
  void bestSkipIsAdjacentToTheFinishingCheckpoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 1 + 197 + 196 = 394.
    //   Skip (1,0)   [idx 1]: reroute (0,0)->(99,99) = 198; gain = 1+197-198   = 0   -> 394.
    //   Skip (99,99) [idx 2]: reroute (1,0)->(2,0)   = 1;   gain = 197+196-1   = 392 -> 2.
    // Minimum is 2 via skipping the last interior checkpoint.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The starting checkpoint must never be skipped, even when skipping it would massively
  //     shorten the route. The skip loop must exclude index 0. ---

  @Test
  @StdIo({"4", "1000 0", "0 0", "1 0", "2 0"})
  void firstCheckpointCannotBeSkippedEvenWhenItIsTheBiggestDetour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 1000 + 1 + 1 = 1002. The only legal skips are interior:
    //   Skip (0,0) [idx 1]: reroute (1000,0)->(1,0) = 999; gain = 1000+1-999 = 2 -> 1000.
    //   Skip (1,0) [idx 2]: reroute (0,0)->(2,0)   = 2;   gain = 1+1-2     = 0 -> 1002.
    // Minimum is 1000. A bug that allowed skipping (1000,0) would print 2.
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // --- The finishing checkpoint must also never be skipped. The skip loop must exclude index
  //     N−1, no matter how far the last checkpoint sits from its predecessor. ---

  @Test
  @StdIo({"4", "0 0", "1 0", "2 0", "1000 0"})
  void lastCheckpointCannotBeSkippedEvenWhenItIsTheBiggestDetour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 1 + 1 + 998 = 1000. The only legal skips are interior:
    //   Skip (1,0) [idx 1]: reroute (0,0)->(2,0)     = 2;   gain = 1+1-2     = 0 -> 1000.
    //   Skip (2,0) [idx 2]: reroute (1,0)->(1000,0)  = 999; gain = 1+998-999 = 0 -> 1000.
    // Minimum is 1000. A bug that allowed skipping (1000,0) would print 2.
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // --- The problem note: when checkpoints coincide, skipping one removes only that single
  //     instance, not every co-located checkpoint. With two adjacent copies of (5,5), neither
  //     interior skip helps — the remaining copy still forces the full detour out and back. ---

  @Test
  @StdIo({"4", "0 0", "5 5", "5 5", "0 0"})
  void duplicateCheckpointAtTheSameLocationIsOnlySkippedOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No-skip total = 10 + 0 + 10 = 20.
    //   Skip idx 1 (one of the (5,5)'s): reroute (0,0)->(5,5) = 10; gain = 10+0-10 = 0 -> 20.
    //   Skip idx 2 (the other (5,5)):    reroute (5,5)->(0,0) = 10; gain = 0+10-10 = 0 -> 20.
    // An implementation that merged duplicates and skipped both copies would wrongly print 0.
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- Coordinates may be negative and reach the ±1000 bounds. The Manhattan formula uses
  //     |dx| + |dy|, so signs must be absorbed consistently and the totals must stay correct
  //     across the full coordinate range. ---

  @Test
  @StdIo({"4", "-1000 -1000", "1000 1000", "0 0", "1000 -1000"})
  void negativeAndMaximumBoundCoordinatesAreHandledByTheManhattanMetric(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // No-skip total = (2000+2000) + (1000+1000) + (1000+1000) = 4000 + 2000 + 2000 = 8000.
    //   Skip (1000,1000) [idx 1]: reroute (-1000,-1000)->(0,0)    = 2000; gain = 4000+2000-2000 =
    // 4000.
    //   Skip (0,0)       [idx 2]: reroute (1000,1000)->(1000,-1000)= 2000; gain = 2000+2000-2000 =
    // 2000.
    // Minimum is 8000 − 4000 = 4000.
    assertThat(out.capturedString().trim()).isEqualTo("4000");
  }
}
