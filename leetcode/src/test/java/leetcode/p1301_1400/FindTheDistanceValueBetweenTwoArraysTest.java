package leetcode.p1301_1400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindTheDistanceValueBetweenTwoArraysTest {
  FindTheDistanceValueBetweenTwoArrays sut = new FindTheDistanceValueBetweenTwoArrays();

  // Step 1: smallest valid input (1 <= arr1.length, arr2.length). The gap is far beyond d, so the
  //         element counts. Dropping the absolute value makes 1 - 100 look close and answers 0
  @Test
  void singlePairFartherThanDCounts() {
    assertThat(sut.findTheDistanceValue(new int[] {1}, new int[] {100}, 2)).isEqualTo(1);
  }

  // Step 2: the same shape inside d, so the element does not count. The answer is the number of
  //         elements with no close partner, and counting those that have one answers 1
  @Test
  void singlePairWithinDDoesNotCount() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {6}, 2)).isZero();
  }

  // ===========================================================================================
  // The inclusive boundary (Steps 3-5).
  // ===========================================================================================

  // Step 3: the statement disqualifies on <= d, so a gap of exactly d is already too close. Keeping
  //         elements whose every gap is >= d answers 1
  @Test
  void gapExactlyDDisqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {7}, 2)).isZero();
  }

  // Step 4: the other side of the boundary. A gap of d + 1 is the first one that counts, and an
  //         off-by-one that demands a gap above d + 1 answers 0
  @Test
  void gapOfDPlusOneCounts() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {8}, 2)).isEqualTo(1);
  }

  // Step 5: d = 0 is legal (0 <= d). Equal values have a gap of 0 <= 0 and still disqualify, so a
  //         solution that treats d = 0 as "nothing can be close" answers 1
  @Test
  void d0EqualValuesDisqualify() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {5}, 0)).isZero();
  }

  // ===========================================================================================
  // The difference is absolute (Steps 6-7).
  // ===========================================================================================

  // Step 6: the mirror of Step 1, with arr2 below arr1. Subtracting in the opposite order, as
  //         arr2[j] - arr1[i], makes 5 - 100 look close and answers 0
  @Test
  void arr2FarBelowCounts() {
    assertThat(sut.findTheDistanceValue(new int[] {100}, new int[] {5}, 2)).isEqualTo(1);
  }

  // Step 7: values can be negative (-1000 <= arr1[i], arr2[j]). -5 and 5 are 10 apart. Comparing
  //         magnitudes, as |(|arr1[i]| - |arr2[j]|)|, sees a gap of 0 and answers 0
  @Test
  void oppositeSignsWithEqualMagnitudeCount() {
    assertThat(sut.findTheDistanceValue(new int[] {-5}, new int[] {5}, 0)).isEqualTo(1);
  }

  // ===========================================================================================
  // One close partner is enough to disqualify (Steps 8-9).
  // ===========================================================================================

  // Step 8: "not any element" means every arr2 element must be far. The only close one is last, so
  //         counting an element as soon as it has one far partner answers 1, and so does comparing
  //         against arr2[0] alone
  @Test
  void closeElementLastInArr2Disqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {100, 200, 6}, 2))
        .isZero();
  }

  // Step 9: the mirror of Step 8, with the close element first. The inner loop must inspect every
  //         position, so comparing against the last arr2 element alone answers 1
  @Test
  void closeElementFirstInArr2Disqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {6, 100, 200}, 2))
        .isZero();
  }

  // ===========================================================================================
  // What gets counted (Steps 10-14).
  // ===========================================================================================

  // Step 10: the answer counts arr1 elements, not pairs. Both elements are far from all three arr2
  //          values, so counting far pairs answers 6
  @Test
  void countsElementsNotPairs() {
    assertThat(sut.findTheDistanceValue(new int[] {1, 100}, new int[] {50, 60, 70}, 2))
        .isEqualTo(2);
  }

  // Step 11: arr1 is not a set. Each index counts on its own, so deduplicating arr1 first answers 1
  @Test
  void duplicatesInArr1EachCount() {
    assertThat(sut.findTheDistanceValue(new int[] {1, 1, 1}, new int[] {100}, 2))
        .isEqualTo(3);
  }

  // Step 12: duplicates in arr2 disqualify an element only once. Starting from arr1.length and
  //          subtracting one per close pair removes 3 from 2 and answers -1
  @Test
  void duplicatesInArr2DisqualifyOnlyOnce() {
    assertThat(sut.findTheDistanceValue(new int[] {1, 50}, new int[] {2, 2, 2}, 2))
        .isEqualTo(1);
  }

  // Step 13: the definition is asymmetric. Only 1 and 3 have no partner equal to them, and swapping
  //          the arguments answers 0
  @Test
  void onlyArr1ElementsAreCounted() {
    assertThat(sut.findTheDistanceValue(new int[] {1, 2, 3}, new int[] {2}, 0)).isEqualTo(2);
  }

  // Step 14: the mirror of Step 13. The extra arr2 values 1 and 3 count for nothing, and swapping
  //          the arguments answers 2
  @Test
  void arr2ElementsAreNeverCounted() {
    assertThat(sut.findTheDistanceValue(new int[] {2}, new int[] {1, 2, 3}, 0)).isZero();
  }

  // ===========================================================================================
  // Unsorted input, with the nearest arr2 value on either side (Steps 15-20).
  // These are the traps in the sorting approaches the problem's hint points toward.
  // ===========================================================================================

  // Step 15: the first value >= 5 in sorted arr2 is 100, but the close one sits below it at 3.
  //          Checking only that ceiling answers 1
  @Test
  void nearestArr2ValueBelowDisqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {3, 100}, 2)).isZero();
  }

  // Step 16: the mirror of Step 15. The last value <= 5 is -100, but the close one sits above it at
  //          7. Checking only that floor answers 1
  @Test
  void nearestArr2ValueAboveDisqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {-100, 7}, 2)).isZero();
  }

  // Step 17: 50 is above all of arr2, so its insertion point is arr2.length and reading that
  //          neighbor without a bounds check throws
  @Test
  void valueAboveAllOfArr2Counts() {
    assertThat(sut.findTheDistanceValue(new int[] {50}, new int[] {1, 2, 3}, 2)).isEqualTo(1);
  }

  // Step 18: the mirror of Step 17. -50 is below all of arr2, so its insertion point is 0 and
  //          reading the neighbor at index -1 throws
  @Test
  void valueBelowAllOfArr2Counts() {
    assertThat(sut.findTheDistanceValue(new int[] {-50}, new int[] {1, 2, 3}, 2))
        .isEqualTo(1);
  }

  // Step 19: arr2 is not sorted. A lower-bound search on it as given probes 6, then 100, stops at
  //          index 0, never compares against 6 again, and answers 1
  @Test
  void arr2MustBeSortedBeforeBinarySearch() {
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {100, 6}, 2)).isZero();
  }

  // Step 20: arr1 is not sorted either. A two-pointer sweep that takes arr1 as given runs its arr2
  //          pointer off the end for 100, never rewinds, also counts 1, and answers 2
  @Test
  void arr1MustBeSortedBeforeTwoPointerSweep() {
    assertThat(sut.findTheDistanceValue(new int[] {100, 1}, new int[] {1, 2}, 0))
        .isEqualTo(1);
  }

  // ===========================================================================================
  // Official examples (Steps 21-23).
  // ===========================================================================================

  // Step 21: the Explanation's third element is the point. 8 is 7 away from the value 1, but its
  //          gaps of 2, 1, and 0 to the others disqualify it. Counting on any far partner answers 3
  @Test
  void leetCodeExample1() {
    assertThat(sut.findTheDistanceValue(new int[] {4, 5, 8}, new int[] {10, 9, 1, 8}, 2))
        .isEqualTo(2);
  }

  // Step 22: 3 sits exactly d away from 6, and only 1 and 2 count. The exclusive reading of the
  //          boundary answers 3
  @Test
  void leetCodeExample2() {
    assertThat(
            sut.findTheDistanceValue(new int[] {1, 4, 2, 3}, new int[] {-4, -3, 6, 10, 20, 30}, 3))
        .isEqualTo(2);
  }

  // Step 23: only 100 counts, and it sits mid-array. Counting the disqualified elements instead
  //          answers 3, and a two-pointer sweep that takes arr1 as given answers 2
  @Test
  void leetCodeExample3() {
    assertThat(sut.findTheDistanceValue(new int[] {2, 1, 100, 3}, new int[] {-5, -2, 10, -3, 7}, 6))
        .isEqualTo(1);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 24-27).
  // Both arrays top out at 500 elements, so the plain double loop makes at most 250,000
  // comparisons and finishes in milliseconds, just as sorting plus binary search does. These
  // timeouts separate no complexity classes. They only make a binary search or sweep that fails to
  // terminate report a failure instead of hanging the build.
  // ===========================================================================================

  // Step 24: lowest value with the largest d. The window starts at -1100, below the value range, so
  //          a prefix-count array over -1000 to 1000 indexed without clamping throws. The gap is
  //          exactly d, so the element is still disqualified
  @Test
  void lowestValueWithMaxDGapExactlyDDisqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {-1000}, new int[] {-900}, 100))
        .isZero();
  }

  // Step 25: the mirror of Step 24. The window ends at 1100, above the value range
  @Test
  void highestValueWithMaxDGapExactlyDDisqualifies() {
    assertThat(sut.findTheDistanceValue(new int[] {1000}, new int[] {900}, 100)).isZero();
  }

  // Step 26: both maximum lengths at opposite ends of the value range, so all 500 count.
  //          Deduplicating arr1 answers 1, and counting far pairs answers 250,000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthsAtOppositeExtremesAllCount() {
    assertThat(sut.findTheDistanceValue(filled(500, -1000), filled(500, 1000), 100))
        .isEqualTo(500);
  }

  // Step 27: both maximum lengths spread across the value range, with arr2 scattered out of order.
  //          Wrong answers separate clearly here: an exclusive boundary answers 98, a threshold of
  //          d + 1 answers 44, ceiling only 111, floor only 112, and an unsorted search 413
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthsSpreadAcrossValueRange() {
    assertThat(sut.findTheDistanceValue(ascendingArr1(), scatteredArr2(), 3)).isEqualTo(71);
  }

  // ===========================================================================================
  // Hygiene (Step 28).
  // ===========================================================================================

  // Step 28: one instance answers several inputs, largest in the middle. A count kept on a field
  //          answers 72 on the second call, and arr2 cached from the first call answers 499
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersSeveralInputs() {
    assertThat(sut.findTheDistanceValue(new int[] {1}, new int[] {100}, 2)).isEqualTo(1);
    assertThat(sut.findTheDistanceValue(ascendingArr1(), scatteredArr2(), 3)).isEqualTo(71);
    assertThat(sut.findTheDistanceValue(new int[] {5}, new int[] {5}, 0)).isZero();
    assertThat(sut.findTheDistanceValue(new int[] {1, 100}, new int[] {50, 60, 70}, 2))
        .isEqualTo(2);
  }

  private static int[] filled(int n, int value) {
    int[] arr = new int[n];
    Arrays.fill(arr, value);
    return arr;
  }

  /** The values -1000, -996, ..., 996. */
  private static int[] ascendingArr1() {
    int[] arr = new int[500];
    Arrays.setAll(arr, i -> -1000 + 4 * i);
    return arr;
  }

  /** 500 distinct values from -1000 to 998, stepping by 37 and wrapping around the value range. */
  private static int[] scatteredArr2() {
    int[] arr = new int[500];
    Arrays.setAll(arr, i -> i * 37 % 2001 - 1000);
    return arr;
  }
}
