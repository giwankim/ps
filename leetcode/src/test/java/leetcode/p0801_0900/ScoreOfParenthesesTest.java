package leetcode.p0801_0900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ScoreOfParenthesesTest {
  ScoreOfParentheses sut = new ScoreOfParentheses();

  // ===========================================================================================
  // Unit score, concatenation, and wrapping (Steps 1-5).
  // ===========================================================================================

  // Step 1: the legal floor is one pair, whose score is 1. Treating every pair as twice its
  //         interior's score incorrectly gives an empty interior a score of 0
  @Test
  void smallestBalancedStringHasUnitScore() {
    assertThat(sut.scoreOfParentheses("()")).isEqualTo(1);
  }

  // Step 2: three adjacent primitive pairs add to 3. Keeping only the latest completed component
  //         loses the earlier pairs, and multiplying concatenated components uses the wrong rule
  @Test
  void adjacentPrimitivePairsAddTheirScores() {
    assertThat(sut.scoreOfParentheses("()()()")).isEqualTo(3);
  }

  // Step 3: two wrappers around a primitive pair double twice, reaching 4. Pair count and maximum
  //         nesting depth both miss the multiplicative effect of successive wrappers
  @Test
  void successiveWrappersEachDoubleTheScore() {
    assertThat(sut.scoreOfParentheses("((()))")).isEqualTo(4);
  }

  // Step 4: the outer pair doubles the sum of both inner pairs: twice the sum of 1 and 1 is 4.
  //         Adding a wrapper's own unit score or doubling just the last child uses the wrong rule
  @Test
  void wrapperDoublesTheWholeInnerSequence() {
    assertThat(sut.scoreOfParentheses("(()())")).isEqualTo(4);
  }

  // Step 5: inner siblings score 1 and 2, then their parent doubles the total to 6. Giving every
  //         leaf the weight of the deepest leaf overcounts the shallower sibling
  @Test
  void unequalInnerSubtreesAddBeforeTheirParentDoubles() {
    assertThat(sut.scoreOfParentheses("(()(()))")).isEqualTo(6);
  }

  // ===========================================================================================
  // Independent components and repeated wrapping (Steps 6-10).
  // ===========================================================================================

  // Step 6: a primitive pair followed by a nested block scores 1 plus 2. Carrying the first pair's
  //         accumulated score into the next block incorrectly doubles it with that block
  @Test
  void primitivePairBeforeNestedBlockKeepsItsOwnScore() {
    assertThat(sut.scoreOfParentheses("()(())")).isEqualTo(3);
  }

  // Step 7: reverse Step 6. The nested block contributes 2 even after the final primitive pair
  //         closes, so overwriting the completed prefix with the last block loses that score
  @Test
  void nestedBlockBeforePrimitivePairKeepsItsOwnScore() {
    assertThat(sut.scoreOfParentheses("(())()")).isEqualTo(3);
  }

  // Step 8: sibling nested blocks each score 2, totaling 4. Failing to restore the parent's state
  //         after a block closes lets the second sibling inherit the first sibling's nesting
  @Test
  void adjacentNestedBlocksRestoreTheTopLevelBetweenThem() {
    assertThat(sut.scoreOfParentheses("(())(())")).isEqualTo(4);
  }

  // Step 9: two wrappers surround two primitive siblings, scoring twice twice their sum: 8.
  //         Applying doubling only once or only to a single child loses an enclosing contribution
  @Test
  void successiveWrappersDoubleEveryInnerSibling() {
    assertThat(sut.scoreOfParentheses("((()()))")).isEqualTo(8);
  }

  // Step 10: these top-level blocks contribute 1, 2, and 4, totaling 7. A single global maximum
  //          depth cannot supply the distinct weight required by each primitive pair
  @Test
  void separateComponentsAtThreeDepthsUseTheirOwnWeights() {
    assertThat(sut.scoreOfParentheses("()(())((()))")).isEqualTo(7);
  }

  // ===========================================================================================
  // Branching, duplicate subtrees, and traversal order (Steps 11-15).
  // ===========================================================================================

  // Step 11: a parent contains children scoring 2, 1, and 2, so its score is 10. Consecutive closes
  //          must restore the parent before its next child starts, instead of ending the parse
  @Test
  void parentResumesAfterADeepChildAndAcceptsMoreSiblings() {
    assertThat(sut.scoreOfParentheses("((())()(()))")).isEqualTo(10);
  }

  // Step 12: leaves at three different depths contribute 2, 4, and 8, totaling 14. Counting leaves
  //          and multiplying all of them by one shared depth loses their distinct ancestors
  @Test
  void leavesAtDifferentDepthsWithinOneComponentUseDifferentWeights() {
    assertThat(sut.scoreOfParentheses("(()(()(())))")).isEqualTo(14);
  }

  // Step 13: both identical balanced blocks score 4, and both occurrences contribute, totaling 8.
  //          Deduplicating repeated substrings as though only distinct components count loses one
  @Test
  void identicalBalancedComponentsEachContributeTheirScore() {
    assertThat(sut.scoreOfParentheses("(()())(()())")).isEqualTo(8);
  }

  // Step 14: the first child scores 4 and the trailing primitive child scores 1, then the parent
  //          doubles their sum to 10. Stopping after the complex child misses its later sibling
  @Test
  void complexFirstChildDoesNotHideTheTrailingPrimitiveChild() {
    assertThat(sut.scoreOfParentheses("((()())())")).isEqualTo(10);
  }

  // Step 15: reverse Step 14. The primitive child must survive while the later complex child is
  //          evaluated, and the outer wrapper doubles their combined score to 10
  @Test
  void primitiveFirstChildSurvivesEvaluationOfTheLaterComplexChild() {
    assertThat(sut.scoreOfParentheses("(()(()()))")).isEqualTo(10);
  }

  // ===========================================================================================
  // Official examples (Steps 16-18).
  // ===========================================================================================

  // Step 16: the official base case fixes the primitive score at 1, preventing a parser from
  //          treating an empty interior as a regular zero-score expression and doubling zero
  @Test
  void leetCodeExample1() {
    assertThat(sut.scoreOfParentheses("()")).isEqualTo(1);
  }

  // Step 17: the official nested case requires the wrapper to double its inner primitive's unit
  //          score. Returning only the completed inner primitive loses the enclosing multiplier
  @Test
  void leetCodeExample2() {
    assertThat(sut.scoreOfParentheses("(())")).isEqualTo(2);
  }

  // Step 18: the official adjacent case counts both primitive pairs. Multiplying their component
  //          scores or keeping only the final completed component discards a required contribution
  @Test
  void leetCodeExample3() {
    assertThat(sut.scoreOfParentheses("()()")).isEqualTo(2);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 19-23).
  // At 50 characters, both linear traversal and quadratic parsing have ample time. The limits
  // catch nontermination or uncontrolled enumeration of equivalent concatenation partitions.
  // ===========================================================================================

  // Step 19: 25 adjacent primitive pairs attain the maximum length and add to 25. A parser must
  //          accumulate every top-level component without turning concatenation into wrapping
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthFlatSequenceAddsAll25PrimitiveScores() {
    assertThat(sut.scoreOfParentheses("()".repeat(25))).isEqualTo(25);
  }

  // Step 20: 25 nested pairs give the primitive 24 enclosing multipliers, reaching 2^24. Using
  //          depth itself, counting all closing pairs, or shifting by the leaf's full depth fails
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNestingDepthProducesTheMaximumScore() {
    assertThat(sut.scoreOfParentheses("(".repeat(25) + ")".repeat(25))).isEqualTo(16_777_216);
  }

  // Step 21: 24 primitive siblings inside one wrapper fill all 50 characters and score 48. A
  //          parent must retain every child, then double their sum once rather than per sibling
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWideComponentDoublesTheSumOf24Children() {
    assertThat(sut.scoreOfParentheses("(" + "()".repeat(24) + ")")).isEqualTo(48);
  }

  // Step 22: a top-level primitive contributes 1 beside a 24-pair nested component worth 2^23.
  //          Reusing the later component's depth for the earlier primitive destroys the sum
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthMixedComponentsKeepSmallAndLargeScoresSeparate() {
    assertThat(sut.scoreOfParentheses("()" + "(".repeat(24) + ")".repeat(24))).isEqualTo(8_388_609);
  }

  // Step 23: two primitive siblings under 23 shared wrappers each contribute 2^23. Their sum
  //          reaches 2^24 even though the maximum depth is 24, ruling out a depth-only score
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthDeepBranchingRetainsBothPrimitiveContributions() {
    assertThat(sut.scoreOfParentheses("(".repeat(23) + "()()" + ")".repeat(23)))
        .isEqualTo(16_777_216);
  }

  // ===========================================================================================
  // State isolation (Step 24). String inputs are immutable, so caller mutation needs no spec.
  // ===========================================================================================

  // Step 24: one instance processes a medium tree, the deepest legal tree, a tiny pair, then two
  //          earlier shapes. A cached cursor, score, depth, or stack must reset before every call
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceScoresDifferentShapesWithoutLeakingStateAcrossCalls() {
    assertThat(sut.scoreOfParentheses("(()(()))")).isEqualTo(6);
    assertThat(sut.scoreOfParentheses("(".repeat(25) + ")".repeat(25))).isEqualTo(16_777_216);
    assertThat(sut.scoreOfParentheses("()")).isEqualTo(1);
    assertThat(sut.scoreOfParentheses("(()())")).isEqualTo(4);
    assertThat(sut.scoreOfParentheses("(()(()))")).isEqualTo(6);
  }
}
