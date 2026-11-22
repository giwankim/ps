package leetcode.p2101_2200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CheckIfAllASAppearsBeforeAllBSTest {
  CheckIfAllASAppearsBeforeAllBS sut = new CheckIfAllASAppearsBeforeAllBS();

  // ===========================================================================================
  // The floor and the basic rule (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor, 1 <= s.length. With no 'b' at all there is no pair to violate the rule, so
  //         the claim holds vacuously. A solution that compares indexOf('b') > lastIndexOf('a')
  //         reads the missing 'b' as -1 and answers false, as does one that insists the string
  //         ends with 'b'
  @Test
  void singleAIsTrue() {
    assertThat(sut.checkString("a")).isTrue();
  }

  // Step 2: the mirror of Step 1. No 'a' is just as vacuous as no 'b'. A solution that insists
  //         the string starts with 'a', or that both letters occur equally often, answers false
  @Test
  void singleBIsTrue() {
    assertThat(sut.checkString("b")).isTrue();
  }

  // Step 3: the smallest string holding both letters, in the required order
  @Test
  void aThenBIsTrue() {
    assertThat(sut.checkString("ab")).isTrue();
  }

  // Step 4: the mirror of Step 3 and the smallest violation. The rule is directional. A solution
  //         that only checks the letters form at most two runs, or that the counts match,
  //         answers true
  @Test
  void bThenAIsFalse() {
    assertThat(sut.checkString("ba")).isFalse();
  }

  // ===========================================================================================
  // Which 'a' is compared with which 'b' (Steps 5-10).
  // ===========================================================================================

  // Step 5: an 'a' returns after the 'b'. The deciding pair is the last 'a' against the first 'b'.
  //         A solution that compares the first 'a' with the first 'b', or that stops at the first
  //         in-order pair, answers true
  @Test
  void aAfterTheBIsFalse() {
    assertThat(sut.checkString("aba")).isFalse();
  }

  // Step 6: the mirror of Step 5. The violation sits at the front and the string then ends
  //         correctly. A solution that compares the last 'a' with the last 'b', or that only
  //         checks the string ends with 'b', answers true
  @Test
  void bBeforeTheAIsFalse() {
    assertThat(sut.checkString("bab")).isFalse();
  }

  // Step 7: the boundary between the runs need not be in the middle. A solution that requires
  //         equal counts, or that tests the first half for 'a' and the second half for 'b',
  //         answers false
  @Test
  void manyAsThenOneBIsTrue() {
    assertThat(sut.checkString("aaaab")).isTrue();
  }

  // Step 8: the mirror of Step 7, with the boundary right after the first character
  @Test
  void oneAThenManyBsIsTrue() {
    assertThat(sut.checkString("abbbb")).isTrue();
  }

  // Step 9: two clean runs in the wrong order. Equal counts and a single change of letter, so
  //         both the counting solution and the at-most-two-runs solution answer true
  @Test
  void allBsThenAllAsIsFalse() {
    assertThat(sut.checkString("bbaa")).isFalse();
  }

  // Step 10: a correct prefix followed by one stray 'a' at the very end. The scan has to reach the
  //          last character. A solution that returns true once it has seen the 'a' run give way
  //          to a 'b' answers true
  @Test
  void strayAAtTheEndIsFalse() {
    assertThat(sut.checkString("aabba")).isFalse();
  }

  // ===========================================================================================
  // The official examples (Steps 11-13).
  // ===========================================================================================

  // Step 11: every 'a' is at indices 0 to 2 and every 'b' at indices 3 to 5
  @Test
  void leetCodeExample1() {
    assertThat(sut.checkString("aaabbb")).isTrue();
  }

  // Step 12: the 'a' at index 2 follows the 'b' at index 1. The string starts with 'a' and ends
  //          with 'b', so a solution that only inspects the two ends answers true
  @Test
  void leetCodeExample2() {
    assertThat(sut.checkString("abab")).isFalse();
  }

  // Step 13: there is no 'a', so every 'a' trivially precedes every 'b'. This rules out reading
  //          the statement as requiring at least one of each letter
  @Test
  void leetCodeExample3() {
    assertThat(sut.checkString("bbb")).isTrue();
  }

  // ===========================================================================================
  // The constraint bound, s.length = 100 (Steps 14-19). At this size every polynomial solution
  // finishes instantly, so the timeouts separate no complexity classes. They only stop a scan
  // that fails to advance from hanging the build.
  // ===========================================================================================

  // Step 14: the longest string with no 'b'. The vacuous case of Step 1 at full length
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAllAsIsTrue() {
    assertThat(sut.checkString("a".repeat(100))).isTrue();
  }

  // Step 15: the longest string with no 'a'
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAllBsIsTrue() {
    assertThat(sut.checkString("b".repeat(100))).isTrue();
  }

  // Step 16: 50 of each in order. A scan that reads s[i + 1] without stopping at index 98 runs off
  //          the end here
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAsThenBsIsTrue() {
    assertThat(sut.checkString("a".repeat(50) + "b".repeat(50))).isTrue();
  }

  // Step 17: 98 times 'a', then "ba". The only violating pair is the last two characters, so a
  //          scan that stops one pair short answers true
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthViolationAtTheEndIsFalse() {
    assertThat(sut.checkString("a".repeat(98) + "ba")).isFalse();
  }

  // Step 18: "ba", then 98 times 'b'. The only violating pair is the first two characters, so a
  //          scan that starts one pair late answers true
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthViolationAtTheStartIsFalse() {
    assertThat(sut.checkString("ba" + "b".repeat(98))).isFalse();
  }

  // Step 19: 49 times 'a', then "ba", then 49 times 'b'. One swapped pair buried in the middle of
  //          an otherwise ordered string, with equal counts of both letters
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthViolationInTheMiddleIsFalse() {
    assertThat(sut.checkString("a".repeat(49) + "ba" + "b".repeat(49))).isFalse();
  }

  // ===========================================================================================
  // Hygiene (Step 20).
  // ===========================================================================================

  // Step 20: one instance answers several inputs of different lengths, longest in the middle and
  //          a false between trues. A solution that keeps a seen-b flag on the instance and
  //          never resets it answers false for every call after the first 'b'
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.checkString("ab")).isTrue();
    assertThat(sut.checkString("ba")).isFalse();
    assertThat(sut.checkString("a".repeat(50) + "b".repeat(50))).isTrue();
    assertThat(sut.checkString("a")).isTrue();
    assertThat(sut.checkString("aabba")).isFalse();
    assertThat(sut.checkString("aaa")).isTrue();
  }
}
