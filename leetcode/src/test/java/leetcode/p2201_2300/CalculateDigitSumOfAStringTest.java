package leetcode.p2201_2300;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CalculateDigitSumOfAStringTest {
  CalculateDigitSumOfAString sut = new CalculateDigitSumOfAString();

  // ===========================================================================================
  // When no round runs at all (Steps 1-3).
  // ===========================================================================================

  // Step 1: the floor. 1 <= s.length and 2 <= k, so the smallest input is one digit against k = 2.
  //         Its length is not greater than k, so no round runs and s comes back untouched
  @Test
  void singleDigitShorterThanKIsReturnedUnchanged() {
    assertThat(sut.digitSum("0", 2)).isEqualTo("0");
  }

  // Step 2: a round needs length strictly greater than k. At length == k nothing happens. A
  //         solution that always performs one round answers "18", and one that loops while
  //         length >= k answers "9"
  @Test
  void lengthEqualToKIsReturnedUnchanged() {
    assertThat(sut.digitSum("99", 2)).isEqualTo("99");
  }

  // Step 3: k may exceed the length by a wide margin. The answer is still s itself, not its digit
  //         sum. A solution that sums the one short group anyway answers "45"
  @Test
  void kFarLargerThanLengthIsReturnedUnchanged() {
    assertThat(sut.digitSum("123456789", 100)).isEqualTo("123456789");
  }

  // ===========================================================================================
  // One round (Steps 4-8).
  // ===========================================================================================

  // Step 4: the length is an exact multiple of k, so every group is full.
  //         groups "12" and "34" -> sums 3 and 7 -> "37", whose length equals k, so stop
  @Test
  void fullGroupsAreEachReplacedByTheirDigitSum() {
    assertThat(sut.digitSum("1234", 2)).isEqualTo("37");
  }

  // Step 5: the last group may be smaller than k and still counts. groups "12" and "3" -> "33".
  //         A solution that drops the incomplete tail answers "3"
  @Test
  void shortLastGroupStillContributesItsSum() {
    assertThat(sut.digitSum("123", 2)).isEqualTo("33");
  }

  // Step 6: a group sum is written out in full, even when it has two digits. groups "999" and "9"
  //         -> sums 27 and 9 -> "279". A solution that reduces each group to its digital root
  //         answers "99"
  @Test
  void twoDigitGroupSumIsWrittenInFull() {
    assertThat(sut.digitSum("9999", 3)).isEqualTo("279");
  }

  // Step 7: the result is a string of digits, not a number. groups "00" and "12" -> "0" and "3"
  //         -> "03". A solution that round-trips the merged string through an integer answers "3"
  @Test
  void leadingZeroOfTheMergedStringIsKept() {
    assertThat(sut.digitSum("0012", 2)).isEqualTo("03");
  }

  // Step 8: the mirror of Step 7. A group of zeros sums to 0 and contributes the character "0" at
  //         the end as well. groups "10" and "00" -> "1" and "0" -> "10". A solution that skips
  //         zero sums answers "1"
  @Test
  void zeroSumGroupContributesAZeroCharacter() {
    assertThat(sut.digitSum("1000", 2)).isEqualTo("10");
  }

  // ===========================================================================================
  // Several rounds (Steps 9-11).
  // ===========================================================================================

  // Step 9: a round does not have to shrink the string. "9999" -> "18" + "18" = "1818", still
  //         longer than k, so a second round gives "99". A solution that runs a single round
  //         answers "1818"
  @Test
  void roundThatDoesNotShrinkIsFollowedByAnother() {
    assertThat(sut.digitSum("9999", 2)).isEqualTo("99");
  }

  // Step 10: each round regroups the merged string from its start, ignoring where the previous
  //          sums began and ended. "98765" -> "24" + "11" = "2411", then the groups are "241" and
  //          "1", which splits the old "11" -> "7" + "1" = "71"
  @Test
  void nextRoundRegroupsAcrossPreviousSumBoundaries() {
    assertThat(sut.digitSum("98765", 3)).isEqualTo("71");
  }

  // Step 11: the rounds keep going for as long as it takes, here four of them.
  //          "19999" -> "10189" -> "199" -> "109" -> "19"
  @Test
  void roundsRepeatUntilLengthIsAtMostK() {
    assertThat(sut.digitSum("19999", 2)).isEqualTo("19");
  }

  // ===========================================================================================
  // The official examples (Steps 12-13).
  // ===========================================================================================

  // Step 12: LeetCode Example 1. Two rounds, "11111222223" -> "3465" -> "135", with a short last
  //          group in both and a two digit sum in the second. The final length equals k
  @Test
  void leetCodeExample1() {
    assertThat(sut.digitSum("11111222223", 3)).isEqualTo("135");
  }

  // Step 13: LeetCode Example 2. Rules out reading the result as a number, since every group sums
  //          to 0 and the answer is three zero characters, and rules out a second round at
  //          length == k
  @Test
  void leetCodeExample2() {
    assertThat(sut.digitSum("00000000", 3)).isEqualTo("000");
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-18). s.length <= 100 and k <= 100, so any simulation finishes in
  // microseconds. The timeouts are there to turn a loop that never terminates into a failure.
  // ===========================================================================================

  // Step 14: both ceilings at once. 100 digits against k = 100 is length == k, so no round runs
  @Test
  void maximumLengthWithMaximumKIsReturnedUnchanged() {
    String s = "9".repeat(100);
    assertThat(sut.digitSum(s, 100)).isEqualTo(s).hasSize(100);
  }

  // Step 15: the largest possible group sum. 99 nines sum to 891, a three digit sum, followed by
  //          the lone last 9 -> "8919", which is far shorter than k
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumGroupSumHasThreeDigits() {
    assertThat(sut.digitSum("9".repeat(100), 99)).isEqualTo("8919");
  }

  // Step 16: the longest run of rounds. 100 nines with k = 2 alternates between a round that
  //          keeps the length ("99" -> "18") and one that halves it ("18" -> "9"), twelve rounds
  //          in all, down to "99"
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfNinesWithMinimumK() {
    assertThat(sut.digitSum("9".repeat(100), 2)).isEqualTo("99");
  }

  // Step 17: 100 zeros with k = 2 halve six times, 100 -> 50 -> 25 -> 13 -> 7 -> 4 -> 2, and
  //          never stop being zeros. A solution that parses the string as a number answers "0"
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfZerosKeepsOneZeroPerGroup() {
    assertThat(sut.digitSum("0".repeat(100), 2)).isEqualTo("00");
  }

  // Step 18: 100 mixed digits, the ramp 0123456789 repeated ten times, with k = 3. Six rounds of
  //          lengths 57, 29, 14, 7, 5 and 2 mix one and two digit sums and short last groups
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRampOfMixedDigits() {
    assertThat(sut.digitSum("0123456789".repeat(10), 3)).isEqualTo("36");
  }

  // ===========================================================================================
  // Hygiene (Step 19).
  // ===========================================================================================

  // Step 19: one instance answers several inputs of different sizes, the largest in the middle.
  //          A builder or buffer kept on the instance and not reset per call leaks digits from one
  //          answer into the next
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.digitSum("123", 2)).isEqualTo("33");
    assertThat(sut.digitSum("0123456789".repeat(10), 3)).isEqualTo("36");
    assertThat(sut.digitSum("0", 2)).isEqualTo("0");
    assertThat(sut.digitSum("11111222223", 3)).isEqualTo("135");
  }
}
