package leetcode.p1001_1100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class HighFiveTest {
  HighFive sut = new HighFive();

  // ===========================================================================================
  // One student: which five scores count and how they are averaged (Steps 1-7).
  // ===========================================================================================

  // Step 1: the floor. items.length may be 1 by the numeric constraint, but every student has at
  //         least five scores, so five rows for one student is the smallest legal input. All five
  //         count: 80 + 90 + 100 + 70 + 60 = 400 -> 80. The result is one [id, average] row.
  @Test
  void oneStudentWithExactlyFiveScoresAveragesAllOfThem() {
    int[][] items = {{1, 80}, {1, 90}, {1, 100}, {1, 70}, {1, 60}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 80}});
  }

  // Step 2: integer division truncates. 99 + 99 + 99 + 99 + 98 = 494 -> 494 / 5 = 98.8 -> 98.
  //         A solution that rounds to nearest, or that divides in floating point and rounds,
  //         answers 99
  @Test
  void averageTruncatesTowardZero() {
    int[][] items = {{3, 99}, {3, 99}, {3, 99}, {3, 99}, {3, 98}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{3, 98}});
  }

  // Step 3: 0 is a legal score (0 <= score) and a top-five sum below 5 truncates all the way to
  //         0: 0 + 1 + 1 + 1 + 1 = 4 -> 0. A solution that rounds answers 1
  @Test
  void zeroScoreIsLegalAndSmallSumTruncatesToZero() {
    int[][] items = {{1, 0}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 0}});
  }

  // Step 4: with six scores only the top five count, so the 0 is dropped:
  //         100 + 90 + 80 + 70 + 60 = 400 -> 80. A solution that averages every score, or one that
  //         sums the top five but divides by the number of scores, answers 66
  @Test
  void sixthLowestScoreIsDropped() {
    int[][] items = {{1, 100}, {1, 90}, {1, 80}, {1, 70}, {1, 60}, {1, 0}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 80}});
  }

  // Step 5: "top five" means the five largest, not a position in the input. The two 100s sit at
  //         both ends: top five = 100 + 100 + 50 + 40 + 30 = 320 -> 64. Taking the first five rows
  //         answers 40, taking the last five answers 48
  @Test
  void topFiveAreTheLargestValuesNotTheFirstOrLastFiveRows() {
    int[][] items = {{1, 100}, {1, 10}, {1, 20}, {1, 30}, {1, 40}, {1, 50}, {1, 100}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 64}});
  }

  // Step 6: repeated scores each count. Five 100s and a 0 -> 500 / 5 = 100. A solution that
  //         collects scores into a set collapses them to {100, 0} and answers 20
  @Test
  void duplicateScoresCountSeparately() {
    int[][] items = {{1, 100}, {1, 100}, {1, 100}, {1, 100}, {1, 100}, {1, 0}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 100}});
  }

  // Step 7: a tie at the cutoff. The fifth- and sixth-highest are both 90, and exactly five
  //         scores are summed no matter which 90 is "chosen": 95 + 90 + 90 + 90 + 90 = 455 -> 91.
  //         A solution that keeps every score tied with the fifth sums six and answers 109
  @Test
  void tieAtTheCutoffStillSumsExactlyFiveScores() {
    int[][] items = {{1, 95}, {1, 90}, {1, 90}, {1, 90}, {1, 90}, {1, 90}, {1, 10}};

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 91}});
  }

  // ===========================================================================================
  // Several students: grouping by id and ordering the result (Steps 8-11).
  // ===========================================================================================

  // Step 8: rows of different students interleave, so scores must be grouped by id before the
  //         top five are chosen. Student 1: 60 + 70 + 80 + 90 + 100 = 400 -> 80. Student 2: five
  //         100s -> 100. A solution that pools every score answers a single row of 100
  @Test
  void interleavedRowsAreGroupedByStudentId() {
    int[][] items = {
      {1, 60}, {2, 100}, {1, 70}, {2, 100}, {1, 80}, {2, 100}, {1, 90}, {2, 100}, {1, 100}, {2, 100}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 80}, {2, 100}});
  }

  // Step 9: the result is sorted by id, not by first appearance. Student 7 appears first in the
  //         input, yet student 1 leads the result. A solution that walks a map in insertion order
  //         answers [[7, 100], [1, 50]]
  @Test
  void resultIsSortedByIdNotByFirstAppearance() {
    int[][] items = {
      {7, 100}, {7, 100}, {7, 100}, {7, 100}, {7, 100}, {1, 50}, {1, 50}, {1, 50}, {1, 50}, {1, 50}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 50}, {7, 100}});
  }

  // Step 10: ids sort numerically. 9 comes before 10, which comes before 100. A solution that keys
  //          students by string, or sorts the rows lexicographically, answers
  //          [[10, 10], [100, 100], [9, 9]]
  @Test
  void idsSortNumericallyNotLexicographically() {
    int[][] items = {
      {100, 100},
      {100, 100},
      {100, 100},
      {100, 100},
      {100, 100},
      {9, 9},
      {9, 9},
      {9, 9},
      {9, 9},
      {9, 9},
      {10, 10},
      {10, 10},
      {10, 10},
      {10, 10},
      {10, 10}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{9, 9}, {10, 10}, {100, 100}});
  }

  // Step 11: ids are sparse and 1000 is the ceiling. Only students that appear get a row, so the
  //          result has two rows, not one per id from 1 to 1000. A solution that indexes an array
  //          by id and emits every slot returns 1000 rows
  @Test
  void onlyStudentsPresentInTheInputAppearInTheResult() {
    int[][] items = {
      {1, 100}, {1, 100}, {1, 100}, {1, 100}, {1, 100}, {1000, 0}, {1000, 0}, {1000, 0}, {1000, 0},
      {1000, 0}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 100}, {1000, 0}});
  }

  // ===========================================================================================
  // Official examples (Steps 12-13).
  // ===========================================================================================

  // Step 12: LeetCode Example 1. Student 1 has six scores; the explanation rules out averaging
  //          all six (82) and taking the first five rows (79). Student 2's 443 / 5 = 88.6 rules out
  //          rounding (89)
  @Test
  void leetCodeExample1() {
    int[][] items = {
      {1, 91}, {1, 92}, {2, 93}, {2, 97}, {1, 60}, {2, 77}, {1, 65}, {1, 87}, {1, 100}, {2, 100},
      {2, 76}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 87}, {2, 88}});
  }

  // Step 13: LeetCode Example 2. Two students with ten identical 100s each, alternating. Rules
  //          out set-based deduplication, which leaves one 100 per student and answers 20
  @Test
  void leetCodeExample2() {
    int[][] items = {
      {1, 100}, {7, 100}, {1, 100}, {7, 100}, {1, 100}, {7, 100}, {1, 100}, {7, 100}, {1, 100},
      {7, 100}
    };

    assertThat(sut.highFive(items)).isDeepEqualTo(new int[][] {{1, 100}, {7, 100}});
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-16). items.length <= 1000, so even an O(n^2) solution finishes
  // instantly; the timeouts only catch a pathological approach or a loop that never terminates.
  // These steps pin correctness at the ceilings: 1000 scores for one student, 200 distinct ids
  // (the most that fit at five scores each), and every student holding more than five scores.
  // ===========================================================================================

  // Step 14: one student, 1000 scores. Scores cycle 0..95 except positions 500-504, which hold
  //          96..100, so the top five are buried mid-input: 96 + 97 + 98 + 99 + 100 = 490 -> 98.
  //          First five rows answer 2, last five answer 37, averaging all 1000 answers 46
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneStudentWithThousandScoresFindsTopFiveInTheMiddle() {
    assertThat(sut.highFive(oneStudentPeakingInTheMiddle())).isDeepEqualTo(new int[][] {{1, 98}});
  }

  // Step 15: 200 students with exactly five scores each is the most distinct ids 1000 rows can
  //          hold. Ids 1000, 995, ..., 5 arrive in descending order, each student scoring id % 101
  //          five times, so row j of the result is [5(j+1), 5(j+1) % 101] in ascending id order
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void twoHundredStudentsArrivingInDescendingIdOrderComeBackAscending() {
    int[][] items = new int[1000][];
    Arrays.setAll(items, i -> {
      int id = 1000 - 5 * (i / 5);
      return new int[] {id, id % 101};
    });
    int[][] expected = new int[200][];
    Arrays.setAll(expected, j -> new int[] {5 * (j + 1), 5 * (j + 1) % 101});

    assertThat(sut.highFive(items)).isDeepEqualTo(expected).hasNumberOfRows(200);
  }

  // Step 16: 100 students, ten scores each, dealt round-robin: row i belongs to student i % 100
  //          and is that student's round i / 100 score, 10 * round + id % 10. Rounds 5..9 are the
  //          top five: 350 + 5 * (id % 10) -> 70 + id % 10 for student id = j + 1. Taking each
  //          student's first five rows answers 20 + id % 10
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void hundredStudentsDealtRoundRobinEachKeepTheirTopFive() {
    int[][] items = new int[1000][];
    Arrays.setAll(items, i -> new int[] {i % 100 + 1, i / 100 * 10 + i % 10});
    int[][] expected = new int[100][];
    Arrays.setAll(expected, j -> new int[] {j + 1, 70 + j % 10});

    assertThat(sut.highFive(items)).isDeepEqualTo(expected).hasNumberOfRows(100);
  }

  // ===========================================================================================
  // Hygiene (Steps 17-18).
  // ===========================================================================================

  // Step 17: the caller's array is left alone. Sorting items in place by id or by score is the
  //          tempting shortcut, and it reorders the caller's rows
  @Test
  void inputArrayIsNotModified() {
    int[][] items = {
      {1, 91}, {1, 92}, {2, 93}, {2, 97}, {1, 60}, {2, 77}, {1, 65}, {1, 87}, {1, 100}, {2, 100},
      {2, 76}
    };
    int[][] original = Arrays.stream(items).map(int[]::clone).toArray(int[][]::new);

    sut.highFive(items);

    assertThat(items).isDeepEqualTo(original);
  }

  // Step 18: one instance answers several inputs of different sizes, largest in the middle. A
  //          per-student map or score buffer kept on the instance instead of per call would leak
  //          the 1000-score student into the later answers
  @Test
  void oneInstanceAnswersSeveralInputsOutOfOrder() {
    assertThat(sut.highFive(new int[][] {{1, 80}, {1, 90}, {1, 100}, {1, 70}, {1, 60}}))
        .isDeepEqualTo(new int[][] {{1, 80}});
    assertThat(sut.highFive(oneStudentPeakingInTheMiddle())).isDeepEqualTo(new int[][] {{1, 98}});
    assertThat(sut.highFive(new int[][] {
          {7, 100}, {7, 100}, {7, 100}, {7, 100}, {7, 100}, {1, 50}, {1, 50}, {1, 50}, {1, 50},
          {1, 50}
        }))
        .isDeepEqualTo(new int[][] {{1, 50}, {7, 100}});
  }

  /** Student 1 with 1000 scores cycling 0..95, except positions 500-504 which hold 96..100. */
  private static int[][] oneStudentPeakingInTheMiddle() {
    int[][] items = new int[1000][];
    Arrays.setAll(items, i -> new int[] {1, 500 <= i && i < 505 ? i - 404 : i % 96});
    return items;
  }
}
