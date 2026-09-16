package leetcode.p1601_1700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class NumberOfSetsOfKNonOverlappingLineSegmentsTest {
  NumberOfSetsOfKNonOverlappingLineSegments sut = new NumberOfSetsOfKNonOverlappingLineSegments();

  // Step 1: the floor. The constraints are 2 <= n and 1 <= k <= n-1, so there is no single-point
  //         input and no zero-segment input to specify. Two points admit exactly one segment, the
  //         one joining point 0 to point 1
  @Test
  void twoPointsAdmitTheSingleSegmentBetweenThem() {
    assertThat(sut.numberOfSets(2, 1)).isEqualTo(1);
  }

  // Step 2: a single segment is just a choice of two distinct endpoints, so the count is the number
  //         of pairs, 6. A solution that only joins adjacent points answers 3, and so does one that
  //         reads "two or more points" as "three or more points"
  @Test
  void oneSegmentCountsEveryPairOfPoints() {
    assertThat(sut.numberOfSets(4, 1)).isEqualTo(6);
  }

  // Step 3: separates the two misreadings Step 2 leaves tied. Over five points an adjacent-only
  //         solution answers 4 and a three-or-more-points reading answers 6, while the true count
  //         is every pair of endpoints, 10
  @Test
  void oneSegmentOverFivePointsSeparatesTheTwoMisreadings() {
    assertThat(sut.numberOfSets(5, 1)).isEqualTo(10);
  }

  // Step 4: "The k line segments do not have to cover all n points." One segment over twenty points
  //         leaves at least eighteen of them uncovered and that costs nothing, so the answer is
  //         still the pair count 190. A solution that insists on full coverage answers 1, counting
  //         only the segment that spans the whole line
  @Test
  void uncoveredPointsAreFree() {
    assertThat(sut.numberOfSets(20, 1)).isEqualTo(190);
  }

  // ===========================================================================================
  // Sharing endpoints and non-overlap (Steps 5-8).
  //
  // "find the number of ways we can draw exactly k non-overlapping line segments ... they are
  // allowed to share endpoints." Two segments may meet at a point but may not share any interior.
  // ===========================================================================================

  // Step 5: over three points the only set of two segments is point 0 to point 1 together with
  //         point 1 to point 2, and it exists only because the two may meet at point 1. A solution
  //         that requires the segments to be disjoint answers 0
  @Test
  void twoSegmentsOverThreePointsMustMeetAtTheMiddlePoint() {
    assertThat(sut.numberOfSets(3, 2)).isEqualTo(1);
  }

  // Step 6: sharing at a scale where it is not the whole answer. Requiring four distinct endpoints
  //         answers 5, so two thirds of the real count of 15 comes from sets that meet at a point
  @Test
  void sharedEndpointsTripleTheTwoSegmentCount() {
    assertThat(sut.numberOfSets(5, 2)).isEqualTo(15);
  }

  // Step 7: non-overlap actually binds here. Six points offer 15 distinct segments, and choosing
  //         any three of them ignoring overlap answers 455, while requiring the three to be
  //         pairwise disjoint leaves only the unit tiling 0-1, 2-3, 4-5 and answers 1. The truth,
  //         28, sits between the two
  @Test
  void overlappingSegmentsAreNotCounted() {
    assertThat(sut.numberOfSets(6, 3)).isEqualTo(28);
  }

  // Step 8: a set, not a sequence. Non-overlapping segments have exactly one left-to-right order,
  //         so a solution that counts ordered triples multiplies by 3! and answers 5544
  @Test
  void segmentSetsAreCountedOnceNotOncePerOrdering() {
    assertThat(sut.numberOfSets(10, 3)).isEqualTo(924);
  }

  // ===========================================================================================
  // Exactly k, and the ceiling k = n-1 (Steps 9-13).
  // ===========================================================================================

  // Step 9: "exactly k", not "at most k". Summing the counts for one, two and three segments
  //         answers 364, and 365 if the empty set is counted as well
  @Test
  void exactlyKSegmentsNotAtMostK() {
    assertThat(sut.numberOfSets(8, 3)).isEqualTo(210);
  }

  // Step 10: the ceiling, since the constraints cap k at n-1. Three segments over four points force
  //          every segment to join adjacent points and every interior point to be shared, so the
  //          tiling 0-1, 1-2, 2-3 is the only set
  @Test
  void kEqualToNMinusOneForcesTheUnitTiling() {
    assertThat(sut.numberOfSets(4, 3)).isEqualTo(1);
  }

  // Step 11: the ceiling stays forced as n grows. Nine segments over ten points leave no slack at
  //          all, so the answer is 1 and not some count that grows with n
  @Test
  void kEqualToNMinusOneStaysForcedAsNGrows() {
    assertThat(sut.numberOfSets(10, 9)).isEqualTo(1);
  }

  // Step 12: one step below the ceiling the answer is no longer 1. Eight segments over ten points
  //          leave exactly one unit of slack, spendable as a gap before, between or after the
  //          segments or as one over-long segment, which is 17 distinct sets
  @Test
  void oneBelowTheCeilingAdmitsSeventeenSets() {
    assertThat(sut.numberOfSets(10, 8)).isEqualTo(17);
  }

  // Step 13: the count is not monotone in k. Over ten points it climbs from 45 at one segment to a
  //          peak of 1287 at four, then falls away to 1 at the ceiling. A solution that assumes
  //          more segments always means fewer sets, or that shortcuts large k to 1, fails here
  @Test
  void theCountRisesToAPeakThenFallsAsKGrows() {
    assertThat(sut.numberOfSets(10, 1)).isEqualTo(45);
    assertThat(sut.numberOfSets(10, 4)).isEqualTo(1287);
    assertThat(sut.numberOfSets(10, 9)).isEqualTo(1);
  }

  // ===========================================================================================
  // The official examples (Steps 14-16).
  // ===========================================================================================

  // Step 14: LeetCode Example 1. The explanation lists five sets and four of them share an
  //          endpoint, the sole disjoint one being 0-1 together with 2-3. A solution that forbids
  //          shared endpoints therefore answers 1, and one that ignores overlap entirely and picks
  //          any two of the six available segments answers 15
  @Test
  void leetCodeExample1() {
    assertThat(sut.numberOfSets(4, 2)).isEqualTo(5);
  }

  // Step 15: LeetCode Example 2. The three sets are the segments 0 to 1, 0 to 2 and 1 to 2. The
  //          middle one is exactly what an adjacent-points-only reading drops, answering 2
  @Test
  void leetCodeExample2() {
    assertThat(sut.numberOfSets(3, 1)).isEqualTo(3);
  }

  // Step 16: LeetCode Example 3, and the only example whose explanation prints the unreduced total,
  //          3796297200. That is past Integer.MAX_VALUE, so this example exists to catch a solution
  //          accumulating in int. Note that the disjoint-only misreading answers 145422675 here,
  //          which is below the modulus and so would survive an example that never had to reduce
  @Test
  void leetCodeExample3() {
    assertThat(sut.numberOfSets(30, 7)).isEqualTo(796297179);
  }

  // ===========================================================================================
  // The modulus (Steps 17-19).
  //
  // "Since this number can be huge, return it modulo 10^9 + 7." These three steps walk the exact
  // count past the int range, then past the long range, then far past any fixed-width accumulator.
  // ===========================================================================================

  // Step 17: the exact count is 28277527346376 — past int, but still comfortably inside a long. A
  //          solution that reduces only once at the very end still passes this, which is the point:
  //          it isolates "forgot the modulo entirely" from "reduced too late"
  @Test
  void countPastIntRangeIsReducedModuloOneBillionAndSeven() {
    assertThat(sut.numberOfSets(40, 10)).isEqualTo(527148437);
  }

  // Step 18: the exact count here has 23 digits, past Long.MAX_VALUE, so the reduction has to
  //          happen inside the recurrence. A solution that accumulates into a long and takes the
  //          modulo last silently overflows and answers a value derived from wrapped arithmetic
  @Test
  void countPastLongRangeForcesReductionInsideTheRecurrence() {
    assertThat(sut.numberOfSets(60, 20)).isEqualTo(860298066);
  }

  // Step 19: the same trap at a scale no fixed-width accumulator survives — the exact count has 77
  //          digits — while the input stays small enough that this step is about the modulus alone
  //          and not about running time
  @Test
  void countOfSeventySevenDigitsIsStillReducedCorrectly() {
    assertThat(sut.numberOfSets(200, 60)).isEqualTo(713589965);
  }

  // ===========================================================================================
  // The constraint bounds (Steps 20-25).
  //
  // "2 <= n <= 1000" and "1 <= k <= n-1" give up to about 10^6 states, each a point index, a
  // count of segments still to place, and a flag for whether a segment is open. The O(n*k) DP
  // that carries that flag fills them in milliseconds. The O(n^2*k) DP that instead loops over
  // every candidate endpoint needs on the order of 5*10^8 steps at n = 1000 and k = 500 and will
  // not finish inside the timeout, and enumerating segment sets is hopeless far below this scale.
  // ===========================================================================================

  // Step 20: maximum n with the minimum k. One segment over a thousand points is still just the
  //          pair count, 499500, which is the largest answer in the whole problem that a careless
  //          solution can get right without ever reducing
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNWithOneSegmentIsThePairCount() {
    assertThat(sut.numberOfSets(1000, 1)).isEqualTo(499500);
  }

  // Step 21: maximum n with k = 2. The unreduced count is already 41583291750, so the modulus bites
  //          at the second-smallest k the constraints allow
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNWithTwoSegmentsAlreadyNeedsTheModulus() {
    assertThat(sut.numberOfSets(1000, 2)).isEqualTo(583291463);
  }

  // Step 22: the worst case for the DP — maximum n with k in the middle of its range, where the
  //          state count and the per-state work are both at their largest. This is the step the
  //          O(n^2*k) formulation cannot finish
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNWithKInTheMiddleOfItsRange() {
    assertThat(sut.numberOfSets(1000, 500)).isEqualTo(70047606);
  }

  // Step 23: maximum n at the ceiling k = n-1. The answer is 1 again, as in Step 11, but a solution
  //          has to arrive there by filling a table of about 10^6 states or by a closed form — a
  //          plain recursion over 999 nested segments overflows the stack instead
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNAtTheCeilingIsStillTheForcedTiling() {
    assertThat(sut.numberOfSets(1000, 999)).isEqualTo(1);
  }

  // Step 24: the large-scale mirror of Step 12. Just below the ceiling at maximum n the answer is
  //          1997, small but emphatically not 1, so a solution that shortcuts every large k to the
  //          forced tiling fails here while passing Step 23
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneBelowTheCeilingAtMaximumNIsNotOne() {
    assertThat(sut.numberOfSets(1000, 998)).isEqualTo(1997);
  }

  // Step 25: a second value at full scale but at an odd n, so a solution tuned or hardcoded around
  //          n = 1000 has nothing to lean on and any off-by-one in the table size shows up
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oddMaximalNWithKInTheMiddleOfItsRange() {
    assertThat(sut.numberOfSets(999, 499)).isEqualTo(970118741);
  }

  // ===========================================================================================
  // Hygiene (Steps 26-27).
  // ===========================================================================================

  // Step 26: the same arguments must give the same answer every time. A factorial or Pascal table
  //          built lazily on the instance and then appended to across calls, or a DP array reused
  //          without being cleared, answers the first call correctly and later ones from stale
  //          entries
  @Test
  void repeatedIdenticalCallsAgree() {
    assertThat(sut.numberOfSets(30, 7)).isEqualTo(796297179);
    assertThat(sut.numberOfSets(30, 7)).isEqualTo(796297179);
    assertThat(sut.numberOfSets(30, 7)).isEqualTo(796297179);
  }

  // Step 27: one instance answers inputs of wildly different sizes, deliberately out of order with
  //          the largest in the middle. A solution that sizes a table to the first n it sees reads
  //          the later inputs out of a table that is too small, and one that keeps state between
  //          calls reads them out of another input's entries
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersInputsOfAnySizeInAnyOrder() {
    assertThat(sut.numberOfSets(4, 2)).isEqualTo(5);
    assertThat(sut.numberOfSets(1000, 500)).isEqualTo(70047606);
    assertThat(sut.numberOfSets(2, 1)).isEqualTo(1);
    assertThat(sut.numberOfSets(30, 7)).isEqualTo(796297179);
    assertThat(sut.numberOfSets(10, 4)).isEqualTo(1287);
  }
}
