package boj.boj22988;

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

/**
 * BOJ 22988 재활용 캠페인 (Recycling Campaign).
 *
 * <p>Hina owns N hair-essence bottles of capacity X; bottle i holds C_i. The shop's recycling
 * campaign takes any two returned bottles holding A and B and hands back one bottle filled to
 * {@code min(A + B + X/2, X)} -- the combined contents plus a half-capacity bonus, never
 * overflowing. The answer is the maximum number of full (exactly X) bottles she can end up with.
 *
 * <p>Greedy structure: a bottle already at X counts for free; two bottles fill one iff {@code 2*(A
 * + B) >= X}, which is the cheapest route (2 bottles per full), so a two-pointer pairs the smallest
 * with the largest to maximize such pairs; and any three otherwise-unusable bottles always fill one
 * more (two combine to at least X/2, the third adds its contents plus X/2 >= X), so the unpaired
 * remainder yields {@code remainder / 3}.
 */
class MainTest {

  // --- Official sample from the problem statement. N = 7 bottles, odd capacity X = 13. The 13 is
  // already full (counts for free); among {0,1,2,3,5,8} the two-pointer pairs (0,8) and (2,5) into
  // two more -- note (1,5) = 6 misses the odd-capacity threshold of ceil(13/2) = 7 -- and the two
  // leftovers {1,3} are too few to triple. Total 0+8, 2+5, and the pre-filled 13 -> 3. ---

  @Test
  @StdIo({"7 13", "0 1 2 3 5 8 13"})
  void officialSample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Smallest possible input: one already-full bottle (N = X = 1). Nothing to exchange, the lone
  // full bottle is the answer. Guards reading "N X" on one line and the trivial count. ---

  @Test
  @StdIo({"1 1", "1"})
  void singleFullBottleSmallestCapacity(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A single full bottle at a larger capacity still counts as one. ---

  @Test
  @StdIo({"1 10", "10"})
  void singleFullBottleCountsAsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A single non-full bottle: an exchange needs two bottles, so nothing can be done and the
  // answer is 0. Catches a solution that would "round up" a lone partial bottle. ---

  @Test
  @StdIo({"1 10", "5"})
  void singleNonFullBottleCannotBeFilled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A single empty bottle (C_i = 0 is allowed): still 0, for the same reason. ---

  @Test
  @StdIo({"1 10", "0"})
  void singleEmptyBottleCannotBeFilled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Every bottle is already full: the answer is simply N, with no exchanges. Confirms full
  // bottles are counted directly and never needlessly recombined. ---

  @Test
  @StdIo({"3 10", "10 10 10"})
  void allBottlesAlreadyFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The defining pair boundary at even capacity: A + B = 2 + 3 = 5 = X/2, the minimum sum that
  // refills to full (2 + 3 + 5 = 10). Pins the comparison as ">=", not ">". ---

  @Test
  @StdIo({"2 10", "2 3"})
  void pairExactlyMeetsHalfThreshold(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- One below that boundary: A + B = 4 < 5, so 4 + 5 = 9 < 10 and no full bottle results. With
  // only two bottles there is no third to triple, so the answer is 0. ---

  @Test
  @StdIo({"2 10", "2 2"})
  void pairJustBelowHalfThreshold(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A pair whose combined contents plus the bonus overshoots capacity: 6 + 7 + 5 = 18 is capped
  // back to X = 10. Confirms the min(..., X) cap still yields exactly one full bottle. ---

  @Test
  @StdIo({"2 10", "6 7"})
  void pairFarAboveThresholdIsCappedFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Odd capacity boundary: X = 5, A + B = 1 + 2 = 3. The real bonus is X/2 = 2.5, so the refill
  // is 3 + 2.5 = 5.5 -> capped full. The integer test 2*(1+2) = 6 >= 5 must hold; a floor(X/2) = 2
  // shortcut would wrongly compute 3 + 2 = 5 yet still pass here -- the companion test below is the
  // one that separates them. ---

  @Test
  @StdIo({"2 5", "1 2"})
  void pairMeetsThresholdOddCapacity(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Odd capacity, one below the boundary: X = 5, A + B = 1 + 1 = 2, refill 2 + 2.5 = 4.5 < 5.
  // 2*(1+1) = 4 < 5, so not full -> 0. ---

  @Test
  @StdIo({"2 5", "1 1"})
  void pairBelowThresholdOddCapacity(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Two empty bottles cannot reach full: 0 + 0 + 5 = 5 < 10, and there is no third bottle. ---

  @Test
  @StdIo({"2 10", "0 0"})
  void twoEmptyBottlesCannotMakeFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The "rule of three" in its purest form: three empty bottles always make one full. Combine
  // two empties -> 0 + 0 + 5 = 5 (half), then add the third -> 5 + 0 + 5 = 10 -> full. None of
  // these
  // form a valid pair on their own (0 + 0 < X/2), so this exercises the remainder/3 branch only.
  // ---

  @Test
  @StdIo({"3 10", "0 0 0"})
  void threeEmptyBottlesMakeOneFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Six empties -> two full via two independent triples (6 / 3 = 2). Confirms the remainder/3
  // count scales rather than capping at one. ---

  @Test
  @StdIo({"6 10", "0 0 0 0 0 0"})
  void sixEmptyBottlesMakeTwoFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Three unpairable-but-nonzero bottles: 1 + 1 = 2 < X/2 = 5 so no pair forms, but the three
  // together fill one (1+1 -> 1+1+5 = 7, then 7+1+5 capped to 10). Mirrors the empties case with
  // nonzero contents. ---

  @Test
  @StdIo({"3 10", "1 1 1"})
  void threeUnpairableBottlesMakeOneFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Four unpairable bottles: still only one triple's worth (4 / 3 = 1); the fourth bottle is
  // wasted. Guards an off-by-one that would round 4 up to 2. ---

  @Test
  @StdIo({"4 10", "1 1 1 1"})
  void fourUnpairableBottlesMakeOneFullDiscardingOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Core two-pointer behaviour. Input is deliberately unsorted (1 9 1 9). Each small bottle
  // pairs with a large one (1 + 9 = 10 >= 5), giving two full bottles. A solution that pairs
  // adjacent or same-end bottles (1 + 1 = 2, fails) would under-count, and one that forgets to sort
  // would mis-pair. ---

  @Test
  @StdIo({"4 10", "1 9 1 9"})
  void pairsSmallestWithLargest(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Why pairing must consume the largest, not waste it. From {9,1,1,1} the only full pair is
  // (1,9); the remaining {1,1} cannot pair (2 < 5) and there is no third small bottle to triple, so
  // the answer is 1. A greedy that pairs the two smallest first would strand the 9 and still get 1
  // here, but the randomized oracle below catches the general case. ---

  @Test
  @StdIo({"4 10", "9 1 1 1"})
  void smallBottlePairsWithLargeNotAnotherSmall(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A pre-existing full bottle is kept and the rest are combined: the 10 counts as-is and (3,7)
  // refills to full, total 2. Confirms full bottles are removed before pairing, not fed back in.
  // ---

  @Test
  @StdIo({"3 10", "10 3 7"})
  void existingFullBottlePlusNewPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Full bottles must not be recombined away. {10,10} give 2 for free and {1,1,1} adds one
  // triple -> 3. A solution that throws the full bottles into the exchange pool could destroy them
  // (e.g. combining two fulls yields a single full, losing one). ---

  @Test
  @StdIo({"5 10", "10 10 1 1 1"})
  void fullBottlesAreNotRecombined(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Odd count of all-pairable bottles: {5,5,5} forms one pair (5 + 5 = 10) and leaves a single
  // 5 that cannot triple alone, so the answer is 1. Exercises the lone leftover after the
  // two-pointer meets in the middle. ---

  @Test
  @StdIo({"3 10", "5 5 5"})
  void oddCountAllPairableLeavesOneLeftover(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Both branches at once. Sorted {0,0,0,0,1,9}: the two-pointer pairs (0,9) -> full, then
  // every
  // remaining (0,1) fails (1 < 5); the four leftovers {0,0,0,1} give one more via a triple. Total
  // 2.
  // Pins pairing and remainder/3 working together on one input. ---

  @Test
  @StdIo({"6 10", "0 0 0 0 1 9"})
  void mixesPairsWithLeftoverTriple(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Maximum capacity, pair exactly on the boundary: X = 10^18, A + B = 2*10^17 + 3*10^17 =
  // 5*10^17 = X/2, so 2*(A+B) = 10^18 >= X -> full. Any 32-bit accumulation of A + B or of 2*(A+B)
  // overflows; only long arithmetic gives 1. ---

  @Test
  @StdIo({"2 1000000000000000000", "200000000000000000 300000000000000000"})
  void largeCapacityPairAtBoundaryIsFull(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- One below the maximum-capacity boundary: A + B = 199999999999999999 + 300000000000000000 =
  // 499999999999999999, so 2*(A+B) = 999999999999999998 < 10^18 -> not full -> 0. The companion to
  // the test above, separated by a single millilitre at 10^18 scale. ---

  @Test
  @StdIo({"2 1000000000000000000", "199999999999999999 300000000000000000"})
  void largeCapacityPairJustBelowBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A single already-full bottle at the maximum capacity. Confirms X and C_i are both read as
  // 64-bit values; parsing 10^18 as an int would throw. ---

  @Test
  @StdIo({"1 1000000000000000000", "1000000000000000000"})
  void largeCapacityAlreadyFullBottle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The float-precision trap. X = 999999999999999999 (odd). A + B = 1 + 499999999999999998 =
  // 499999999999999999, so 2*(A+B) = 999999999999999998 < X -> NOT full -> 0. A solution comparing
  // against (double) X / 2 rounds X up to 10^18 and X/2 to 5*10^17, then sees A + B = 5*10^17 - 1
  // round to 5*10^17 and wrongly reports full. Only exact integer arithmetic gives 0. ---

  @Test
  @StdIo({"2 999999999999999999", "1 499999999999999998"})
  void largeOddCapacityPairBelowBoundaryNeedsIntegerMath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The matching just-reaches case at the same odd capacity. A + B = 1 + 499999999999999999 =
  // 500000000000000000, so 2*(A+B) = 10^18 >= 999999999999999999 -> full -> 1. Together with the
  // test above this brackets the exact odd-capacity boundary. ---

  @Test
  @StdIo({"2 999999999999999999", "1 499999999999999999"})
  void largeOddCapacityPairAtBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Performance, maximum N, all already full: 100,000 bottles at X return 100,000. Confirms the
  // O(N log N) sort/scan handles the largest input and prints a six-figure count correctly. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllFullRunsFast() throws IOException {
    assertThat(runMain(constantInput(100_000, 10, 10))).isEqualTo("100000");
  }

  // --- Performance, maximum N, all exactly half-full: 100,000 bottles of 5 with X = 10 pair up
  // perfectly (5 + 5 = 10) into 50,000 full bottles. Drives the two-pointer across the whole array.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllPairableRunsFast() throws IOException {
    assertThat(runMain(constantInput(100_000, 10, 5))).isEqualTo("50000");
  }

  // --- Performance, maximum N, all empty: 100,000 empties make 100000 / 3 = 33,333 full via
  // triples, discarding the leftover one. Stresses the remainder/3 path at scale and pins the floor
  // division. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllEmptyRunsFast() throws IOException {
    assertThat(runMain(constantInput(100_000, 10, 0))).isEqualTo("33333");
  }

  // --- Performance plus 64-bit arithmetic at scale: 100,000 bottles of 5*10^17 with X = 10^18 pair
  // into 50,000 full bottles. Every pair test computes 2*(5*10^17 + 5*10^17) = 2*10^18, which must
  // stay in long across the full input. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeCapacityAtScaleUsesLongArithmetic() throws IOException {
    assertThat(
            runMain(constantInput(100_000, 1_000_000_000_000_000_000L, 500_000_000_000_000_000L)))
        .isEqualTo("50000");
  }

  // --- Randomized cross-check against an independent brute force that simulates the exchange rule
  // directly rather than assuming the greedy. Tiny capacities and bottle counts make full bottles,
  // valid pairs, triples, and leftovers all frequent, with N spanning 1..6 and X spanning 1..15.
  // Catches pairing, ordering, boundary, and remainder bugs the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(22988);
    for (int trial = 0; trial < 500; trial++) {
      long x = 1 + rnd.nextInt(15); // 1..15
      int n = 1 + rnd.nextInt(6); // 1..6 bottles
      long[] volumes = new long[n];
      for (int i = 0; i < n; i++) {
        volumes[i] = rnd.nextInt((int) x + 1); // 0..x
      }
      String input = inputFor(x, volumes);
      String expected = Integer.toString(bruteForceMaxFull(x, volumes));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference brute force: simulates the campaign rule directly. From the current multiset of
  // bottle
  // volumes it may stop (counting bottles already at X) or pick any two to exchange into
  // min(A + B + X/2, X) and recurse, taking the maximum. Volumes are tracked doubled (2 * volume)
  // so
  // the X/2 bonus stays integral even for odd X. Obviously correct but exponential; trustworthy
  // only
  // for the tiny N used by the oracle.
  private static int bruteForceMaxFull(long x, long[] volumes) {
    long[] doubled = new long[volumes.length];
    for (int i = 0; i < volumes.length; i++) {
      doubled[i] = 2 * volumes[i];
    }
    return bruteForce(doubled, x);
  }

  private static int bruteForce(long[] doubled, long x) {
    long cap = 2 * x; // a bottle is full when its doubled volume reaches 2X
    int best = 0;
    for (long v : doubled) {
      if (v == cap) {
        best++;
      }
    }
    int n = doubled.length;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        long combined = Math.min(doubled[i] + doubled[j] + x, cap); // 2*min(A + B + X/2, X)
        long[] next = new long[n - 1];
        int idx = 0;
        for (int k = 0; k < n; k++) {
          if (k != i && k != j) {
            next[idx++] = doubled[k];
          }
        }
        next[idx] = combined;
        best = Math.max(best, bruteForce(next, x));
      }
    }
    return best;
  }

  private static String inputFor(long x, long[] volumes) {
    StringBuilder sb = new StringBuilder();
    sb.append(volumes.length).append(' ').append(x).append('\n');
    for (int i = 0; i < volumes.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(volumes[i]);
    }
    return sb.append('\n').toString();
  }

  private static String constantInput(int n, long x, long volume) {
    StringBuilder sb = new StringBuilder(n * 4 + 32);
    sb.append(n).append(' ').append(x).append('\n');
    for (int i = 0; i < n; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(volume);
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
