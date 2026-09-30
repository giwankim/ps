package leetcode.p1101_1200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximumNestingDepthOfTwoValidParenthesesStringsTest {
  MaximumNestingDepthOfTwoValidParenthesesStrings sut =
      new MaximumNestingDepthOfTwoValidParenthesesStrings();

  // Step 1: 1 <= seq.size <= 10000, but a nonempty VPS needs at least two characters.
  //         One group must be empty, so requiring both labels would reject every valid answer.
  @Test
  void singlePairAllowsAnEmptyGroup() {
    assertOptimalSplit("()", sut.maxDepthAfterSplit("()"), 1);
  }

  // Step 2: concatenation takes the maximum depth, not the sum of the component depths.
  @Test
  void adjacentPairsHaveDepth1() {
    assertOptimalSplit("()()", sut.maxDepthAfterSplit("()()"), 1);
  }

  // Step 3: depth 2 splits into two depth-1 VPS's only by taking noncontiguous subsequences.
  //         A contiguous-cut solver cannot reach the optimum.
  @Test
  void nestedPairsRequireNoncontiguousSubsequences() {
    assertOptimalSplit("(())", sut.maxDepthAfterSplit("(())"), 1);
  }

  // Step 4: an odd depth of 3 needs a ceiling of 2. Rounding down promises an impossible split.
  @Test
  void oddDepthRoundsTheOptimumUp() {
    assertOptimalSplit("((()))", sut.maxDepthAfterSplit("((()))"), 2);
  }

  // Step 5: depth 4 can be shared evenly. A valid but unsplit answer of depth 4 is insufficient.
  @Test
  void evenDepthCanBeSharedEvenly() {
    assertOptimalSplit("(((())))", sut.maxDepthAfterSplit("(((())))"), 2);
  }

  // ===========================================================================================
  // Branches and concatenation (Steps 6-10).
  // ===========================================================================================

  // Step 6: siblings inside one wrapper contribute a maximum, not one level per child.
  @Test
  void severalSiblingsInsideOneWrapperStillHaveDepth2() {
    assertOptimalSplit("(()()())", sut.maxDepthAfterSplit("(()()())"), 1);
  }

  // Step 7: siblings at depth 3 must leave both output groups balanced after each branch.
  //         Sending every opening to one group and every closing to the other is invalid.
  @Test
  void nestedSiblingPairsKeepBothGroupsBalanced() {
    assertOptimalSplit("((()()))", sut.maxDepthAfterSplit("((()()))"), 2);
  }

  // Step 8: a shallow first component must not hide a later depth-3 peak.
  @Test
  void deeperComponentAfterAFlatPrefixSetsTheOptimum() {
    assertOptimalSplit("()((()))", sut.maxDepthAfterSplit("()((()))"), 2);
  }

  // Step 9: a shallow suffix must not overwrite the depth-3 peak seen earlier.
  @Test
  void flatSuffixDoesNotEraseAnEarlierPeak() {
    assertOptimalSplit("((()))()", sut.maxDepthAfterSplit("((()))()"), 2);
  }

  // Step 10: concatenated depths 2 and 4 have optimum 2, not the sum of their optima, 3.
  @Test
  void unequalComponentDepthsUseTheMaximum() {
    assertOptimalSplit("(())(((())))", sut.maxDepthAfterSplit("(())(((())))"), 2);
  }

  // ===========================================================================================
  // Asymmetric branches and repeated peaks (Steps 11-15).
  // ===========================================================================================

  // Step 11: a shallow left child followed by a deeper right child changes the nesting pattern.
  //          Inspecting only the first child misses the depth-3 peak in the second child.
  @Test
  void deeperRightChildPreservesSubsequenceOrder() {
    assertOptimalSplit("(()(()))", sut.maxDepthAfterSplit("(()(()))"), 2);
  }

  // Step 12: mirror Step 11. Returning from a deep left child must restore the outer context.
  @Test
  void deeperLeftChildRestoresTheOuterContext() {
    assertOptimalSplit("((())())", sut.maxDepthAfterSplit("((())())"), 2);
  }

  // Step 13: two depth-3 peaks inside one wrapper need depth 2, not one extra level per peak.
  @Test
  void twoDeepSubtreesDoNotAccumulateDepth() {
    assertOptimalSplit("((())(()))", sut.maxDepthAfterSplit("((())(()))"), 2);
  }

  // Step 14: nested branches and several returns to shallower levels still have peak depth 4.
  //          Counting all opening parentheses instead of active nesting overestimates the answer.
  @Test
  void unevenBranchesUseActiveNestingRatherThanPairCount() {
    assertOptimalSplit("(((()())()))", sut.maxDepthAfterSplit("(((()())()))"), 2);
  }

  // Step 15: several complete components of depths 1, 2, 3, and 2 must all stay valid.
  //          Splitting only the deepest component leaves other positions unaccounted for.
  @Test
  void mixedComponentsAssignEveryCharacter() {
    String seq = "()(()())((()))(())";
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 2);
  }

  // ===========================================================================================
  // Official examples (Steps 16-17). Any optimal assignment is allowed, including swapped labels.
  // ===========================================================================================

  // Step 16: printed output [0,1,1,1,1,0] forms A="()" and B="()()", both depth 1.
  //          This rules out contiguous cuts. The Note permits other optimal assignments.
  @Test
  void leetCodeExample1() {
    assertOptimalSplit("(()())", sut.maxDepthAfterSplit("(()())"), 1);
  }

  // Step 17: printed output [0,0,0,1,1,0,1,1] forms A="()()" and B="()()".
  //          Different components can use different labels, so the printed array is not mandatory.
  @Test
  void leetCodeExample2() {
    assertOptimalSplit("()(())()", sut.maxDepthAfterSplit("()(())()"), 1);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 18-22): 10,000 characters should need only a linear scan.
  // Timeouts reject exponential assignment search and expensive repeated subsequence copying.
  // ===========================================================================================

  // Step 18: maximum length at minimum depth. Counting pairs would report 2,500 instead of 1.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthFlatSequenceHasDepth1() {
    String seq = "()".repeat(5_000);
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 1);
  }

  // Step 19: maximum legal depth is 5,000, giving optimum 2,500. Recursive parsing can overflow.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthFullyNestedSequenceSharesDepth5000() {
    String seq = "(".repeat(5_000) + ")".repeat(5_000);
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 2_500);
  }

  // Step 20: length 10,000 with odd peak 4,999 still needs 2,500. Floor division gives 2,499.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithAnOddPeakRoundsUp() {
    String seq = "(".repeat(4_999) + ")".repeat(4_999) + "()";
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 2_500);
  }

  // Step 21: 2,500 enclosing pairs around 2,500 sibling pairs have peak 2,501, not 5,000.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithWideBranchesUsesPeakDepth() {
    String seq = "(".repeat(2_500) + "()".repeat(2_500) + ")".repeat(2_500);
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 1_251);
  }

  // Step 22: repeated components of different depths keep the global peak at 3, not 5,000.
  //          Deep nesting alone would not catch a solver that accumulates depth across components.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthMixedComponentsResetNesting() {
    String seq = "((()))(())()".repeat(833) + "()()";
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 2);
  }

  // ===========================================================================================
  // Reuse and result ownership (Steps 23-24). The String input itself is immutable.
  // ===========================================================================================

  // Step 23: largest input in the middle, then a smaller odd peak and the original input again.
  //          Cached depth, write positions, or result lengths must not leak between calls.
  @Test
  void oneInstanceAnswersDifferentSizesAndDepths() {
    assertOptimalSplit("()", sut.maxDepthAfterSplit("()"), 1);
    String nested = "(".repeat(20) + ")".repeat(20);
    assertOptimalSplit(nested, sut.maxDepthAfterSplit(nested), 10);
    assertOptimalSplit("((()))", sut.maxDepthAfterSplit("((()))"), 2);
    assertOptimalSplit("()", sut.maxDepthAfterSplit("()"), 1);
  }

  // Step 24: the caller can edit a returned array. Returning that edited cached array on the next
  //          call would violate the required binary labels, even when the input String is
  // identical.
  @Test
  void editingAnEarlierAnswerDoesNotCorruptTheNextCall() {
    String seq = "(()())";
    int[] first = sut.maxDepthAfterSplit(seq);
    assertOptimalSplit(seq, first, 1);
    Arrays.fill(first, 2);
    assertOptimalSplit(seq, sut.maxDepthAfterSplit(seq), 1);
  }

  private static void assertOptimalSplit(String seq, int[] answer, int expectedDepth) {
    assertThat(answer).as("one assignment per input character").isNotNull().hasSize(seq.length());
    int[] balance = new int[2];
    int[] peak = new int[2];
    for (int i = 0; i < answer.length; i++) {
      int group = answer[i];
      assertThat(group).as("group at index %s", i).isIn(0, 1);
      balance[group] += seq.charAt(i) == '(' ? 1 : -1;
      assertThat(balance[group])
          .as("group %s must have a valid prefix through index %s", group, i)
          .isNotNegative();
      peak[group] = Math.max(peak[group], balance[group]);
    }
    assertThat(balance).as("both subsequences must be balanced").containsExactly(0, 0);
    assertThat(Math.max(peak[0], peak[1])).as("minimum possible depth").isEqualTo(expectedDepth);
  }
}
