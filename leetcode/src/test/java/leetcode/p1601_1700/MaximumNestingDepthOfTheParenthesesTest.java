package leetcode.p1601_1700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximumNestingDepthOfTheParenthesesTest {
  MaximumNestingDepthOfTheParentheses sut = new MaximumNestingDepthOfTheParentheses();

  // ===========================================================================================
  // The floor (Steps 1-2).
  // ===========================================================================================

  // Step 1: smallest input the constraints allow (1 <= s.length). A lone '(' or ')' is not a
  //         valid parentheses string, so a length-1 input never holds a parenthesis, and nothing
  //         encloses the digit. A stack that pushes every character and reports its peak size
  //         answers 1
  @Test
  void singleDigitIsNotNested() {
    assertThat(sut.maxDepth("7")).isZero();
  }

  // Step 2: the smallest pair is an empty one, and it still counts as a level. Recording the
  //         depth only while standing on a digit answers 0, and so does taking the max before
  //         incrementing on '('
  @Test
  void emptyPairCountsAsALevel() {
    assertThat(sut.maxDepth("()")).isEqualTo(1);
  }

  // ===========================================================================================
  // Nesting (Steps 3-5).
  // ===========================================================================================

  // Step 3: everything between one pair of brackets sits at depth 1, however much of it there
  //         is. A stack that pushes every character and reports its peak size answers 6
  @Test
  void charactersInsideAPairDoNotDeepenIt() {
    assertThat(sut.maxDepth("(1+2*3)")).isEqualTo(1);
  }

  // Step 4: each pair opened before the previous one closes adds a level. Taking the max before
  //         incrementing on '(' answers 2
  @Test
  void eachNestedPairAddsALevel() {
    assertThat(sut.maxDepth("((()))")).isEqualTo(3);
  }

  // Step 5: only ')' closes a level. Digits and all four operators leave the depth unchanged,
  //         here sitting between every pair of opening brackets. Treating every character other
  //         than '(' as a close answers 1, and so does the longest run of consecutive '('
  @Test
  void operatorsBetweenOpeningBracketsDoNotCloseALevel() {
    assertThat(sut.maxDepth("(1*(2/(3-(4+5))))")).isEqualTo(4);
  }

  // ===========================================================================================
  // Siblings versus nesting (Steps 6-10).
  // ===========================================================================================

  // Step 6: pairs side by side are each at depth 1, and depth is a maximum, not a total.
  //         Counting every '(' answers 3, and so does summing the depth of each top-level group
  @Test
  void siblingPairsDoNotAddUp() {
    assertThat(sut.maxDepth("()()()")).isEqualTo(1);
  }

  // Step 7: two siblings inside an outer pair share level 2. Counting the '(' in each top-level
  //         group answers 3
  @Test
  void siblingsInsideAnOuterPairShareALevel() {
    assertThat(sut.maxDepth("(()())")).isEqualTo(2);
  }

  // Step 8: a close does not end the climb. The '(' reach depths 1, 2, 2, 3, so the deepest pair
  //         is opened after an earlier one has closed. No run of consecutive '(' is longer than
  //         2, so the longest-run approach answers 2, and counting every '(' answers 4
  @Test
  void depthKeepsClimbingAfterAClose() {
    assertThat(sut.maxDepth("(()(()))")).isEqualTo(3);
  }

  // Step 9: the deepest group comes first, and the scan must remember it after falling back to
  //         depth 0. Reporting the depth of the last top-level group answers 1
  @Test
  void deepestGroupFirstStillWins() {
    assertThat(sut.maxDepth("(((1)))+(2)")).isEqualTo(3);
  }

  // Step 10: the mirror of Step 9, with the deepest group last. Stopping at the first return to
  //          depth 0, as a parser that handles one group and forgets concatenation does, answers 1
  @Test
  void deepestGroupLastStillWins() {
    assertThat(sut.maxDepth("(1)+(((2)))")).isEqualTo(3);
  }

  // ===========================================================================================
  // LeetCode examples (Steps 11-13).
  // ===========================================================================================

  // Step 11: LeetCode Example 1. The depth goes 1, 2, back to 1, then 2 and 3 around the 8. The
  //          deepest pair is reached through a run of only two '(', so the longest-run approach
  //          answers 2, and treating operators as closes answers 1
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxDepth("(1+(2*3)+((8)/4))+1")).isEqualTo(3);
  }

  // Step 12: LeetCode Example 2. Three separate groups of rising depth. Counting every '('
  //          answers 6, and handling only the first group answers 1
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxDepth("(1)+((2))+(((3)))")).isEqualTo(3);
  }

  // Step 13: LeetCode Example 3. No digits at all, so recording the depth only while standing
  //          on a digit answers 0. Counting the '(' in each top-level group answers 4
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxDepth("()(())((()()))")).isEqualTo(3);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-16). s.length <= 100 is tiny: a single scan is 100 steps, and
  // even a memoized O(n^3) parse of the recursive definition is about 10^6. The timeouts reject
  // only exponential work and loops that never advance. Trying every split of the string into
  // two valid halves without memoization makes about 5x more calls per extra sibling pair, so
  // it cannot finish on Step 15.
  // ===========================================================================================

  // Step 14: 100 characters nest at most 50 deep. Taking the max before incrementing on '('
  //          answers 49
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthFullyNestedIsHalfAsDeep() {
    String s = "(".repeat(50) + ")".repeat(50);

    assertThat(sut.maxDepth(s)).isEqualTo(50);
  }

  // Step 15: maximum length as 50 sibling pairs, all at depth 1. Counting every '(' answers 50
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthOfSiblingPairsStaysAtTheFirstLevel() {
    String s = "()".repeat(50);

    assertThat(sut.maxDepth(s)).isEqualTo(1);
  }

  // Step 16: 33 nested levels with a digit just inside each '(', so "(0(1(2)))" in miniature,
  //          99 characters in all. A stack of every character peaks at 66, and treating digits
  //          as closes answers 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void deepCombWithADigitAtEveryLevel() {
    String s = comb(33);

    assertThat(sut.maxDepth(s)).isEqualTo(33);
  }

  // ===========================================================================================
  // Hygiene (Step 17).
  // ===========================================================================================

  // Step 17: one instance answers several inputs of different depths, deliberately out of order
  //          with the deepest in the middle. A running max kept on the instance instead of reset
  //          per call answers 50 for every input after the deepest one
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.maxDepth("()")).isEqualTo(1);
    assertThat(sut.maxDepth("(".repeat(50) + ")".repeat(50))).isEqualTo(50);
    assertThat(sut.maxDepth("7")).isZero();
    assertThat(sut.maxDepth("(())")).isEqualTo(2);
    assertThat(sut.maxDepth("(1+(2*3)+((8)/4))+1")).isEqualTo(3);
  }

  private static String comb(int levels) {
    StringBuilder sb = new StringBuilder(3 * levels);
    for (int i = 0; i < levels; i++) {
      sb.append('(').append((char) ('0' + i % 10));
    }
    return sb.append(")".repeat(levels)).toString();
  }
}
