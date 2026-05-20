package boj.boj15970;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15970 화살표 그리기 (Drawing Arrows).
 */
class MainTest {

  // --- The published sample: a={0,c1}, b={1,c2}, c={3,c1}, d={4,c2}, e={5,c1}. Arrows from
  //     left to right are a->c=3, b->d=3, c->e=2 (the right neighbor is closer than a),
  //     d->b=3, e->c=2; total = 13. ---

  @Test
  @StdIo({"5", "0 1", "1 2", "3 1", "4 2", "5 1"})
  void publishedSampleSumsTheFiveArrowLengthsToThirteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("13");
  }

  // --- Minimum input: two points of one color shoot at each other and contribute |delta| twice.
  // ---

  @Test
  @StdIo({"2", "0 1", "10 1"})
  void twoPointsOfTheSameColorContributeTheGapTwice(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 10 + 10 = 20.
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- Color buckets are independent: a far-apart color-1 pair and a close color-2 pair sum
  //     without any cross-color interaction. A bug that mixed colors would see the much closer
  //     other-color point and shrink the total. ---

  @Test
  @StdIo({"4", "0 1", "100 1", "5 2", "10 2"})
  void differentColorsDoNotInterfereWithEachOther(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Color 1: 100 + 100 = 200. Color 2: 5 + 5 = 10. Total = 210.
    assertThat(out.capturedString().trim()).isEqualTo("210");
  }

  // --- Asymmetric interior: the middle point must pick the closer side, not just a fixed side.
  //     With positions 0, 1, 100 of the same color, the point at 1 is one unit from 0 and 99 from
  //     100 — picking the right neighbor would inflate the total dramatically. ---

  @Test
  @StdIo({"3", "0 1", "1 1", "100 1"})
  void middlePointTakesTheCloserNeighborNotAFixedSide(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 0 -> 1 = 1; 1 -> min(1, 99) = 1; 100 -> 1 = 99. Sum = 101.
    assertThat(out.capturedString().trim()).isEqualTo("101");
  }

  // --- Input order is unsorted: positions arrive 50, 10, 20, 30 but must be processed in sorted
  //     order 10, 20, 30, 50. A solution that walked the input order instead of sorting per color
  //     would compute different (and wrong) neighbor distances. ---

  @Test
  @StdIo({"4", "50 1", "10 1", "20 1", "30 1"})
  void inputOrderIsIndependentOfSortedPositionOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Sorted: 10, 20, 30, 50.
    //   10 -> 20 = 10
    //   20 -> min(10, 10) = 10
    //   30 -> min(10, 20) = 10
    //   50 -> 30 = 20
    // Sum = 50.
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }

  // --- Adjacent positions yield arrow length 1 each. A four-point evenly-spaced color group
  //     stresses the endpoint-vs-interior rule: endpoints take their only neighbor (1 each),
  //     interiors take min of two equal neighbors (still 1 each). ---

  @Test
  @StdIo({"4", "0 1", "1 1", "2 1", "3 1"})
  void evenlySpacedSameColorPointsEachShootDistanceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 0 -> 1 = 1; 1 -> min(1, 1) = 1; 2 -> min(1, 1) = 1; 3 -> 2 = 1. Sum = 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- A close-cluster surrounded by far outliers exercises both rules simultaneously: the
  //     interior points 10 and 11 reach for the *cluster*, not the outliers, even though picking
  //     the wrong side would yield 10 vs 1 or 89 vs 1. ---

  @Test
  @StdIo({"4", "0 1", "10 1", "11 1", "100 1"})
  void tightInteriorClusterPullsArrowsAwayFromDistantOutliers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Sorted: 0, 10, 11, 100.
    //   0   -> 10 = 10
    //   10  -> min(10, 1) = 1
    //   11  -> min(1, 89) = 1
    //   100 -> 11 = 89
    // Sum = 101.
    assertThat(out.capturedString().trim()).isEqualTo("101");
  }

  // --- Many colors each with exactly two points: every point's "nearest same-color neighbor" is
  //     forced to be the other one in its pair. This locks in that the algorithm doesn't look
  //     outside the color bucket even when other-color points are closer in raw position. ---

  @Test
  @StdIo({"6", "0 1", "5 1", "10 2", "20 2", "30 3", "45 3"})
  void manyColorsWithPairsSumsEachPairsGapTwice(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Color 1: |5-0|*2 = 10. Color 2: |20-10|*2 = 20. Color 3: |45-30|*2 = 30. Sum = 60.
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }

  // --- Larger position values verify that the running sum and per-arrow distances handle inputs
  //     beyond a tiny hand-traced range. The Manhattan/absolute math is straightforward but a
  //     solution using a fixed-size small array indexed by position would break here. ---

  @Test
  @StdIo({"2", "0 1", "100000 1"})
  void largePositionGapAccumulatesIntoTheTotalWithoutTruncation(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 100000 + 100000 = 200000.
    assertThat(out.capturedString().trim()).isEqualTo("200000");
  }

  // --- Interleaved colors with asymmetric same-color spacing: by raw position the points alternate
  //     1, 2, 3, 4, 5, 6 but the color groups are {1,3,5} and {2,4,6}. Each color's interior point
  //     sees equal neighbors at distance 2, so every arrow has length 2. ---

  @Test
  @StdIo({"6", "1 1", "2 2", "3 1", "4 2", "5 1", "6 2"})
  void interleavedColorsResolveByColorBucketNotRawAdjacency(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Color 1: positions 1,3,5. 1->3=2; 3->min(2,2)=2; 5->3=2. Subtotal = 6.
    // Color 2: positions 2,4,6. 2->4=2; 4->min(2,2)=2; 6->4=2. Subtotal = 6.
    // Sum = 12.
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }
}
