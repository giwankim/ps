package boj.boj20033;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 20033 Square, Not Rectangle -- the largest-square-in-a-histogram twist on the classic
 * largest-rectangle problem, solved by "binary search on the answer" (parametric search), making it
 * a sibling of {@code boj2110}'s maximin search.
 *
 * <p>A histogram has {@code N} unit-width bars; the {@code i}-th bar has height {@code H_i}. The
 * task is the side length of the largest axis-aligned square that fits inside it -- the side, not
 * the area (the title's joke: you came for the largest rectangle, but you must report a square, and
 * a square is pinned by one number).
 *
 * <p>The whole problem collapses to one monotone predicate: a square of side {@code s} fits <em>iff
 * there is a run of at least {@code s} consecutive bars each with height at least {@code s}</em> --
 * an {@code s}-wide, {@code s}-tall block has to sit on {@code s} adjacent bars that all clear
 * height {@code s}. Feasibility is downward-closed (an {@code s x s} square contains an
 * {@code (s-1) x (s-1)} one), so the feasible sides form {@code [1, answer]} and the answer is its
 * top -- the largest {@code s} with {@code feasible(s)}. That makes binary search over {@code s}
 * with an {@code O(N)} run-scan predicate the intended {@code O(N log answer)} solution.
 *
 * <p>Two ceilings fight for the answer, and good fixtures must pin each one and the trade-off
 * between them:
 *
 * <ul>
 *   <li><b>Width.</b> A side needs that many <em>consecutive</em> bars, so the answer never exceeds
 *       {@code N} -- a tall-but-narrow plateau is capped by how wide it is
 *       ({@link #singleTallBarIsWidthLimitedToOne}, {@link #twoTallBarsAreWidthLimited},
 *       {@link #plateauTallerThanWideIsCappedByWidth},
 *       {@link #billionTallBarsAreStillWidthLimited}).
 *   <li><b>Height.</b> Each of those bars must clear the side, so the answer never exceeds the
 *       window's minimum height -- a wide-but-short plateau is capped by how tall it is
 *       ({@link #flatUnitHistogramIsHeightLimited}, {@link #wideShortPlateauIsHeightLimited}).
 *   <li><b>A short bar breaks the run.</b> The run-scan must reset on any bar below the side, so a
 *       dip splits the histogram into independent segments ({@link #aShortBarResetsTheRun},
 *       {@link #innerPlateauExcludesTheShortEnds}).
 * </ul>
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 300,000}); line 2 is {@code N} space-separated
 * heights {@code H_i} ({@code 1 <= H_i <= 10^9}) -- all on one line, so the parser must tokenize
 * that line rather than read one height per line
 * ({@link #multipleSpacesBetweenHeightsAreTolerated}). Output: the single side length.
 *
 * <p>The statement's single worked sample ({@code 6 / 3 4 4 4 4 3 -> 4}, pinned by
 * {@link #officialSample}) was recovered from an Internet Archive snapshot because acmicpc.net was
 * serving a maintenance page; the rest of the spec was triangulated across two independent
 * reference solutions (a C++ and a Java AC that agree exactly on the binary-search-plus-run-scan
 * formulation), so every other expected value here is hand-derived from the predicate above rather
 * than copied from a judge sample. {@link #randomizedInputsMatchBruteForceOracle} backstops those
 * by cross-checking the judged output against the {@code O(N^2)} definition -- the maximum over all
 * contiguous windows of {@code min(width, minHeight)} -- which shares no greedy or binary-search
 * logic with a judge solution, and {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N
 * log answer)} search against a slower approach at the {@code N = 300,000} ceiling.
 */
class MainTest {

  // --- The statement's only worked sample: heights 3 4 4 4 4 3. The four middle bars form a 4-wide
  // run that all clear height 4, so a 4x4 square fits; side 5 is impossible because no bar reaches
  // 5. Answer 4. The end-to-end smoke test, and a clean "width caps a tall plateau" case -- the
  // plateau is exactly as wide (4) as it is tall (4) once the two height-3 ends are excluded. ---

  @Test
  @StdIo({"6", "3 4 4 4 4 3"})
  void officialSample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The floor of everything: one bar of height 1. A 1x1 square fits, and there is no second bar
  // to widen it, so the side is 1. The minimal N=1 input. ---

  @Test
  @StdIo({"1", "1"})
  void singleBarOfHeightOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Width caps a single bar at side 1 no matter how tall it is: one bar is one wide, so a 2x2
  // square would need a second bar beside it. height 7, answer 1. ---

  @Test
  @StdIo({"1", "7"})
  void singleTallBarIsWidthLimitedToOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Height caps a wide, flat histogram. Five bars give ample width, but every bar is height 1,
  // so no bar clears side 2; the answer is 1. The pure height-limited case. ---

  @Test
  @StdIo({"5", "1 1 1 1 1"})
  void flatUnitHistogramIsHeightLimited(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Taller than wide: two bars of height 5. Side 2 fits (two adjacent bars both clear 2), but
  // side 3 needs a third bar, so width caps the answer at 2 even though the bars are much taller.
  // ---

  @Test
  @StdIo({"2", "5 5"})
  void twoTallBarsAreWidthLimited(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Wider than tall: four bars of height 2. Width is 4, but no bar clears side 3, so height
  // caps the answer at 2. The mirror image of twoTallBarsAreWidthLimited. ---

  @Test
  @StdIo({"4", "2 2 2 2"})
  void wideShortPlateauIsHeightLimited(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The two ceilings meet exactly: three bars of height 3. Three consecutive bars all clear 3,
  // so a 3x3 square fits; a fourth bar would be needed for side 4. Answer 3. ---

  @Test
  @StdIo({"3", "3 3 3"})
  void exactSquarePlateau(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The headline case: a plateau taller than it is wide. The three middle bars 4,4,4 hold a 3x3
  // square -- limited to side 3 by the *width* of the plateau, not its height (the bars are 4
  // tall).
  // Side 4 would need four consecutive bars >= 4, but the plateau is only three wide. Answer 3. ---

  @Test
  @StdIo({"6", "3 1 4 4 4 2"})
  void plateauTallerThanWideIsCappedByWidth(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A short bar resets the run. Two plateaus of 5s -- one three wide, one two wide -- are split
  // by a single height-1 bar. Side 3 fits in the left run; side 4 would need four consecutive bars
  // >= 4, but the lone 1 caps every run at length 3, so the answer is 3. Pins that the run-scan
  // resets its counter on a bar below the side rather than spanning the dip. ---

  @Test
  @StdIo({"6", "5 5 5 1 5 5"})
  void aShortBarResetsTheRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Ascending heights: the best square lives in the tall tail. 1 2 3 4 5 -- bars 3,4,5 clear
  // side 3 (a 3-wide run), but only bars 4,5 clear side 4 (a 2-wide run), so the answer is 3. ---

  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void ascendingHeightsBestSquareInTheTallTail(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Descending heights: the same answer from the other side, guarding against a scan that only
  // works left to right. 5 4 3 2 1 -- bars 5,4,3 form a 3-wide run >= 3, while 5,4 is only a 2-wide
  // run >= 4. Answer 3. ---

  @Test
  @StdIo({"5", "5 4 3 2 1"})
  void descendingHeightsBestSquareInTheTallHead(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- An inner plateau, hemmed in by short ends. The four central 5s clear side 4 (a 4-wide run),
  // so a 4x4 square fits; side 5 would need five such bars, but there are only four. The height-1
  // ends are excluded from the run entirely. Answer 4. ---

  @Test
  @StdIo({"6", "1 5 5 5 5 1"})
  void innerPlateauExcludesTheShortEnds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The far end of the height range: two bars at 1,000,000,000. Even a billion-tall plateau is
  // capped by its width of 2, so the answer is 2. Pins that 10-digit heights are read without
  // truncation and that the side, not the height, is what is reported. ---

  @Test
  @StdIo({"2", "1000000000 1000000000"})
  void billionTallBarsAreStillWidthLimited(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Heights are split on "one or more spaces": "3  3  3" carries two blanks between values. A
  // parser that tokenizes the line (StringTokenizer / split on whitespace runs) reads three 3s; one
  // that splits on a single literal space would read empty tokens and break. Answer 3. ---

  @Test
  @StdIo({"3", "3  3  3"})
  void multipleSpacesBetweenHeightsAreTolerated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Width is ample, height decides: 100 bars all of height 50. A 50-wide run of 50-tall bars
  // holds a 50x50 square; side 51 needs each bar to clear 51, and none do. Answer 50 = min(N=100,
  // height=50). Also pins that the binary search returns the *maximum* feasible side, not the first
  // one it happens to test. ---

  @Test
  void uniformTallHistogramReturnsTheFullHeight() throws IOException {
    int[] heights = new int[100];
    Arrays.fill(heights, 50);
    assertThat(runMain(histogramInput(heights))).isEqualTo("50");
  }

  // --- Upper bound on N: 300,000 bars, each 1,000,000,000 tall. The whole histogram is one giant
  // plateau, so the largest square is capped by the width: side 300,000 = min(N, height). The
  // O(N log answer) search runs ~19 run-scans of 300,000 bars (~5.7e6 ops); an O(answer * N) linear
  // probe of every candidate side would do ~9e10 and time out. Doubles as a 10-digit-parse stress
  // at scale. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 300_000;
    int[] heights = new int[n];
    Arrays.fill(heights, 1_000_000_000);
    assertThat(runMain(histogramInput(heights))).isEqualTo(Integer.toString(n));
  }

  // --- Small random histograms checked against the brute-force definition of the problem. N bars
  // of height in [1, 8] -- ranges narrow enough that squares of several sizes compete. The oracle
  // takes the maximum over every contiguous window of min(width, minHeight), the literal "largest
  // square that fits in this window," sharing no run-scan or binary-search logic with a judge
  // solution, so the two agree only when both are right. ---

  @Test
  void randomizedInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(20033);
    for (int trial = 0; trial < 1000; trial++) {
      int n = 1 + rnd.nextInt(12); // N in [1, 12]
      int[] heights = randomHeights(rnd, n, 8); // heights in [1, 8]
      String expected = Integer.toString(bruteForceMaxSquare(heights));
      assertThat(runMain(histogramInput(heights)))
          .as("heights=%s", Arrays.toString(heights))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the brute-force definition of the largest square side. For every contiguous
   * window of bars it takes {@code min(width, minHeight)} -- the side of the biggest square that
   * fits in that window, bounded by the window's width and by its shortest bar -- and returns the
   * maximum over all windows. Uses neither the run-scan predicate nor binary search, so it shares
   * no logic with a judge solution.
   *
   * @implNote {@code O(N^2)} time, where {@code N} is {@code heights.length} (the bar count) --
   *     acceptable only because the randomized fixtures keep {@code N <= 12}.
   */
  private static int bruteForceMaxSquare(int[] heights) {
    int best = 0;
    for (int i = 0; i < heights.length; i++) {
      int minHeight = Integer.MAX_VALUE;
      for (int j = i; j < heights.length; j++) {
        minHeight = Math.min(minHeight, heights[j]);
        int width = j - i + 1;
        best = Math.max(best, Math.min(width, minHeight));
      }
    }
    return best;
  }

  /**
   * Draws {@code n} bar heights uniformly from {@code [1, maxHeight]} (heights are always >= 1).
   */
  private static int[] randomHeights(Random rnd, int n, int maxHeight) {
    int[] heights = new int[n];
    for (int i = 0; i < n; i++) {
      heights[i] = 1 + rnd.nextInt(maxHeight);
    }
    return heights;
  }

  /**
   * Renders the bars as BOJ 20033 input: {@code N} on line 1, the heights space-separated on line
   * 2.
   */
  private static String histogramInput(int[] heights) {
    StringBuilder sb = new StringBuilder();
    sb.append(heights.length).append('\n');
    for (int i = 0; i < heights.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(heights[i]);
    }
    return sb.append('\n').toString();
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
