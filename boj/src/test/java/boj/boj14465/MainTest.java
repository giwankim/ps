package boj.boj14465;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/** BOJ 14465 소가 길을 건너간 이유 5 (Why Did the Cow Cross the Road 5) */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // N=10, K=6, broken {1,2,5,9,10}: the cheapest length-6 window is [3..8], which holds only the
  // single broken light 5, so one repair suffices -> 1. Note the broken numbers arrive unsorted.
  @Test
  @StdIo({"10 6 5", "2", "10", "1", "5", "9"})
  void officialSampleNeedsOneRepair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Zero repairs: a run of K consecutive working lights already exists. ---

  // N=7, K=3, broken only at the ends {1,7}: the window [2,3,4] (and several others) is entirely
  // working, so no repair is needed -> 0.
  @Test
  @StdIo({"7 3 2", "1", "7"})
  void existingRunOfWorkingLightsNeedsNoRepair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Window position: the optimal window can sit at the start, the end, or in the interior. ---

  // N=6, K=3, broken {3,4,5,6}: counts per window are [1,2,3]->1, [2,3,4]->2, [3,4,5]->3,
  // [4,5,6]->3. The unique minimum is the leftmost window, so the scan must consider position 1.
  @Test
  @StdIo({"6 3 4", "3", "4", "5", "6"})
  void optimalWindowAtTheStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // N=6, K=3, broken {1,2,3,4}: counts are [1,2,3]->3, [2,3,4]->3, [3,4,5]->2, [4,5,6]->1. The
  // unique minimum is the rightmost window, so the scan must reach the final position N.
  @Test
  @StdIo({"6 3 4", "1", "2", "3", "4"})
  void optimalWindowAtTheEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // N=9, K=3, broken {1,2,4,6,8,9}: every window holds at least one broken light and the minimum
  // (1) is reached only by interior windows [3,4,5] and [5,6,7], never at either edge -> 1.
  @Test
  @StdIo({"9 3 6", "1", "2", "4", "6", "8", "9"})
  void optimalWindowInTheInterior(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Returning the global minimum, not the first window found. ---

  // N=10, K=4, broken {1,2,3,5,7,9}: the first window [1..4] holds 3 broken, yet every window holds
  // at least 2 and later windows such as [3,4,5,6]->{3,5} reach exactly 2. A scan that reported the
  // first window would over-count; the global minimum is 2.
  @Test
  @StdIo({"10 4 6", "1", "2", "3", "5", "7", "9"})
  void reportsGlobalMinimumNotFirstWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Degenerate window size K = 1: a single working light is enough. ---

  // N=5, K=1, broken {2,4}: any single working light (1, 3, or 5) forms a length-1 working run, so
  // no repair is needed -> 0.
  @Test
  @StdIo({"5 1 2", "2", "4"})
  void windowSizeOneWithSomeWorkingLightNeedsNoRepair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // N=4, K=1, every light broken: each length-1 window holds exactly one broken light, so one
  // repair is unavoidable -> 1.
  @Test
  @StdIo({"4 1 4", "1", "2", "3", "4"})
  void windowSizeOneOnFullyBrokenRoadRepairsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Degenerate window size K = N: exactly one window, spanning the whole road. ---

  // N=5, K=5, broken {1,3,5}: the only window is [1..5], so every broken light it contains must be
  // repaired -> 3 (the answer equals B when K = N).
  @Test
  @StdIo({"5 5 3", "1", "3", "5"})
  void wholeRoadWindowRepairsEveryBrokenLight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Saturation: when B = N every light is broken. ---

  // N=6, K=3, every light broken: every length-3 window holds 3 broken lights, so the answer is K
  // regardless of where the window sits -> 3.
  @Test
  @StdIo({"6 3 6", "1", "2", "3", "4", "5", "6"})
  void fullyBrokenRoadAlwaysRepairsK(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // N=4, K=4, every light broken: a single whole-road window over an all-broken road forces every
  // light to be repaired -> 4 (the K = N and B = N extremes coincide).
  @Test
  @StdIo({"4 4 4", "1", "2", "3", "4"})
  void wholeRoadWindowOnFullyBrokenRoadRepairsN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Input robustness. ---

  // N=10, K=3, broken supplied out of order {7,1,4,9,2}: the answer must not depend on input order;
  // the cheapest window holds a single broken light -> 1.
  @Test
  @StdIo({"10 3 5", "7", "1", "4", "9", "2"})
  void brokenLightsMayArriveOutOfOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Smallest possible road: N=1, K=1, B=1. The lone light is broken and the only window is itself,
  // so it must be repaired -> 1.
  @Test
  @StdIo({"1 1 1", "1"})
  void singleBrokenLightRoadRepairsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Upper size bound, N = 100,000: must run in linear time over the windows. ---

  // N=100,000, K=50,000, every light broken: each of the 50,001 windows holds 50,000 broken lights,
  // so the answer is K. A per-window recount would be quadratic; the intended sliding-window sweep
  // returns quickly -> 50000.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maxSizeFullyBrokenRoadRepairsK() throws IOException {
    assertThat(runMain(allLightsBrokenInput(MAX_N, MAX_N / 2))).isEqualTo("50000");
  }

  // N=100,000, K=50,000, broken = the first 50,000 lights: the trailing 50,000 lights form an
  // entirely working run of length K, so the sweep must find the zero-cost window at the far end ->
  // 0.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maxSizeWithFullyWorkingTailNeedsNoRepair() throws IOException {
    assertThat(runMain(brokenPrefixInput(MAX_N, MAX_N / 2, MAX_N / 2))).isEqualTo("0");
  }

  private static final int MAX_N = 100_000;

  /**
   * Builds an input where all {@code n} lights are broken: line "n k n", then 1..n one per line.
   */
  private static String allLightsBrokenInput(int n, int k) {
    StringBuilder sb =
        new StringBuilder().append(n).append(' ').append(k).append(' ').append(n);
    sb.append('\n');
    for (int i = 1; i <= n; i++) {
      sb.append(i).append('\n');
    }
    return sb.toString();
  }

  /** Builds an input where the first {@code b} lights are broken: line "n k b", then 1..b. */
  private static String brokenPrefixInput(int n, int k, int b) {
    StringBuilder sb =
        new StringBuilder().append(n).append(' ').append(k).append(' ').append(b);
    sb.append('\n');
    for (int i = 1; i <= b; i++) {
      sb.append(i).append('\n');
    }
    return sb.toString();
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
