package boj.boj9007;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/** BOJ 9007 카누 선수 (Canoe Racer). */
class MainTest {

  // --- Official sample. Three test cases in one stream; anchors the suite against the published
  // example exactly as printed on the problem page. ---

  // Case 1 (k=300): nearest reachable sum is 301 (60+75+93+73), no exact hit.
  // Case 2 (k=8):   an exact sum is reachable (2+2+2+2).
  // Case 3 (k=32):  sums 31 and 33 are both distance 1 -> the smaller, 31, is chosen.
  @Test
  @StdIo({
    "3",
    "300 4",
    "60 52 80 40",
    "75 68 88 63",
    "48 93 48 54",
    "56 73 49 75",
    "8 3",
    "1 2 3",
    "1 2 3",
    "1 2 3",
    "1 2 3",
    "32 2",
    "2 5",
    "9 4",
    "10 20",
    "4 2"
  })
  void officialSampleMatchesPublishedOutput(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("301", "8", "31");
  }

  // --- Minimum size (n = 1): one student per class, so there is exactly one quadruple. Its sum is
  // the answer no matter how far it lands from k. ---

  @Test
  @StdIo({"1", "10 1", "3", "4", "5", "6"})
  void singleStudentPerClassReturnsTheOnlyPossibleSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("18");
  }

  // --- Exact hit: when a quadruple equals k, distance 0 must beat every near miss. Reachable sums
  // are {4,6,8,10,12} (count of 3s chosen); k=8 is reachable, so it wins over 6 and 10. ---

  @Test
  @StdIo({"1", "8 2", "1 3", "1 3", "1 3", "1 3"})
  void exactMatchIsSelected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("8");
  }

  // --- Tie-break: two reachable sums equally distant from k resolve to the smaller one. Three
  // classes are fixed at 1 (3 total), the fourth contributes 5 or 9 -> sums {8, 12}; k=10 sits
  // exactly between them (distance 2 each), so 8 is chosen. ---

  @Test
  @StdIo({"1", "10 2", "1 1", "1 1", "1 1", "5 9"})
  void equidistantSumsSelectSmaller(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("8");
  }

  // --- The spec's own narrative example: k=200 with reachable sums 198 and 202 (distance 2 each)
  // must yield 198. Three classes fixed at 50 (150 total), the fourth contributes 48 or 52. ---

  @Test
  @StdIo({"1", "200 2", "50 50", "50 50", "50 50", "48 52"})
  void specExampleTieSelectsOneHundredNinetyEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("198");
  }

  // --- "Smaller wins" applies ONLY on ties. A larger sum that is strictly closer must still win.
  // Sums are {7, 11}; k=10 is distance 3 from 7 but distance 1 from 11, so 11 (the larger) wins.
  // Guards against a solution that always prefers the smaller candidate. ---

  @Test
  @StdIo({"1", "10 2", "1 1", "1 1", "1 1", "4 8"})
  void closerLargerSumBeatsFartherSmallerSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("11");
  }

  // --- Target above every reachable sum: the answer is the maximum sum (largest of each class).
  // Every sum <= 8 < 1000, so the binary search must clamp to the upper end. ---

  @Test
  @StdIo({"1", "1000 2", "1 2", "1 2", "1 2", "1 2"})
  void targetAboveAllSumsReturnsMaximumSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("8");
  }

  // --- Target below every reachable sum: the answer is the minimum sum. Every sum >= 20 > 1, so a
  // lower_bound-style search returns index 0 and must still report the first (smallest) sum rather
  // than running off the front of the array. A classic MITM off-by-one trap. ---

  @Test
  @StdIo({"1", "1 2", "5 9", "5 9", "5 9", "5 9"})
  void targetBelowAllSumsReturnsMinimumSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("20");
  }

  // --- Upper constraint bound: weights at 10,000,000 and k at 40,000,000. The single reachable sum
  // is exactly 40,000,000, the largest the spec allows. Confirms the maximum sum and target are
  // handled (it fits comfortably in an int, well under Integer.MAX_VALUE). ---

  @Test
  @StdIo({"1", "40000000 1", "10000000", "10000000", "10000000", "10000000"})
  void maximumWeightsAndTargetAtConstraintBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("40000000");
  }

  // --- Large distance with max-scale weights below the target: every sum is 20,000,000 and k=1, a
  // distance of ~2*10^7. Exercises the distance magnitude (still int-safe) alongside below-all
  // selection. ---

  @Test
  @StdIo({"1", "1 2", "5000000 5000000", "5000000 5000000", "5000000 5000000", "5000000 5000000"})
  void largeDistanceBelowTargetReturnsOnlySum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("20000000");
  }

  // --- Multiple test cases must be solved independently. A solution that fails to reset its
  // running-best between cases would leak case 1's answer into case 2. Case 1 -> 8, case 2 -> 20.
  // ---

  @Test
  @StdIo({"2", "10 1", "2", "2", "2", "2", "100 1", "5", "5", "5", "5"})
  void perTestCaseStateIsResetAcrossCases(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("8", "20");
  }

  // --- Performance: brute force is O(n^4). At n=1000 that is 10^12 quadruples and blows the 3s
  // limit; the intended meet-in-the-middle (~O(n^2 log n)) returns almost instantly. All weights
  // equal 1, so the only reachable sum is 4 regardless of k. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeInputRulesOutBruteForce() throws IOException {
    assertThat(runMain(equalWeightsInput(40000000, 1000, 1))).isEqualTo("4");
  }

  // --- Performance with a non-trivial binary search at full scale. Classes 1-3 are all 1s and
  // class 4 is 1..1000, so reachable sums are 3 + c4 (range 4..1003). With k=503 the closest is
  // exactly 503 (c4=500). Forces the search across ~10^6 paired sums to land on a precise answer.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeInputBinarySearchAtScale() throws IOException {
    assertThat(runMain(scaleInput(503))).isEqualTo("503");
  }

  // --- Randomized cross-check against an O(n^4) brute-force oracle over many small inputs. k is
  // sampled to fall below, within, and above the reachable sum range, so ties, exact hits, and both
  // clamp directions all occur. Catches closest-selection and tie-break bugs the hand cases miss.
  // ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(42);
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rnd.nextInt(6);
      int[] a = randomArray(n, rnd);
      int[] b = randomArray(n, rnd);
      int[] c = randomArray(n, rnd);
      int[] d = randomArray(n, rnd);
      int k = 1 + rnd.nextInt(70); // sums span ~4..60, so k reaches both ends and the middle
      String input = inputFor(k, a, b, c, d);
      String expected = Integer.toString(bruteForceClosest(k, a, b, c, d));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference O(n^4) selection: the sum minimizing (|sum - k|, sum) lexicographically. Correct but
  // far too slow for the judge; trustworthy only for tiny n.
  private static int bruteForceClosest(int k, int[] a, int[] b, int[] c, int[] d) {
    int best = 0;
    long bestDist = Long.MAX_VALUE;
    for (int x : a) {
      for (int y : b) {
        for (int z : c) {
          for (int w : d) {
            int sum = x + y + z + w;
            long dist = Math.abs((long) sum - k);
            if (dist < bestDist || (dist == bestDist && sum < best)) {
              bestDist = dist;
              best = sum;
            }
          }
        }
      }
    }
    return best;
  }

  private static int[] randomArray(int n, Random rnd) {
    int[] values = new int[n];
    for (int i = 0; i < n; i++) {
      values[i] = 1 + rnd.nextInt(15); // weights are >= 1; small range to force frequent ties
    }
    return values;
  }

  // One test case with a single weight repeated across all four classes.
  private static String equalWeightsInput(int k, int n, int w) {
    StringBuilder row = new StringBuilder();
    for (int i = 0; i < n; i++) {
      row.append(i == 0 ? "" : " ").append(w);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("1\n").append(k).append(' ').append(n).append('\n');
    for (int i = 0; i < 4; i++) {
      sb.append(row).append('\n');
    }
    return sb.toString();
  }

  // One test case: classes 1-3 are all 1s, class 4 is 1..1000 (n = 1000).
  private static String scaleInput(int k) {
    int n = 1000;
    StringBuilder ones = new StringBuilder();
    for (int i = 0; i < n; i++) {
      ones.append(i == 0 ? "" : " ").append(1);
    }
    StringBuilder ramp = new StringBuilder();
    for (int i = 1; i <= n; i++) {
      ramp.append(i == 1 ? "" : " ").append(i);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("1\n").append(k).append(' ').append(n).append('\n');
    sb.append(ones).append('\n').append(ones).append('\n').append(ones).append('\n');
    sb.append(ramp).append('\n');
    return sb.toString();
  }

  private static String inputFor(int k, int[] a, int[] b, int[] c, int[] d) {
    int n = a.length;
    StringBuilder sb = new StringBuilder();
    sb.append("1\n").append(k).append(' ').append(n).append('\n');
    appendRow(sb, a);
    appendRow(sb, b);
    appendRow(sb, c);
    appendRow(sb, d);
    return sb.toString();
  }

  private static void appendRow(StringBuilder sb, int[] row) {
    for (int i = 0; i < row.length; i++) {
      sb.append(i == 0 ? "" : " ").append(row[i]);
    }
    sb.append('\n');
  }

  private static String[] linesOf(StdOut out) {
    return out.capturedString().replace("\r\n", "\n").trim().split("\n");
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
