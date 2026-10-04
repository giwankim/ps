package leetcode.p0601_0700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ValidParenthesisStringTest {
  ValidParenthesisString sut = new ValidParenthesisString();

  // ===========================================================================================
  // Smallest inputs and ordinary parentheses (Steps 1-7).
  // ===========================================================================================

  // Step 1: length one is legal. A lone star must disappear, so rejecting every odd-length input
  //         or requiring a star to become a parenthesis loses this smallest valid input
  @Test
  void singleStarCanDisappear() {
    assertThat(sut.checkValidString("*")).isTrue();
  }

  // Step 2: a close needs a preceding open. Accepting every single-character input because a
  //         single star is valid would also accept this unmatched literal close
  @Test
  void singleCloseHasNoMatchingOpen() {
    assertThat(sut.checkValidString(")")).isFalse();
  }

  // Step 3: matching need not be adjacent. A checker that accepts only repetitions of "()"
  //         rejects these nested pairs
  @Test
  void nestedPairsAreValid() {
    assertThat(sut.checkValidString("(())")).isTrue();
  }

  // Step 4: the balance may return to zero before the end. Requiring one outer pair enclosing
  //         the entire string rejects two valid strings placed next to each other
  @Test
  void concatenatedPairsAreValid() {
    assertThat(sut.checkValidString("()()")).isTrue();
  }

  // Step 5: equal counts do not imply a match. The literal open comes after the close and cannot
  //         repair it, so a counts-only check answers true incorrectly
  @Test
  void equalCountsInReverseOrderAreInvalid() {
    assertThat(sut.checkValidString(")(")).isFalse();
  }

  // Step 6: checking only the first character and final counts misses a negative prefix in the
  //         middle. The balances 1, 0, -1, 0 make the third character impossible to match
  @Test
  void negativePrefixInTheMiddleIsInvalid() {
    assertThat(sut.checkValidString("())(")).isFalse();
  }

  // Step 7: a valid prefix does not validate the whole string. Returning true after the initial
  //         pair closes overlooks the unmatched open at the end
  @Test
  void validPrefixDoesNotExcuseAnUnclosedSuffix() {
    assertThat(sut.checkValidString("()(")).isFalse();
  }

  // ===========================================================================================
  // Wildcard roles, capacity, and position (Steps 8-16).
  // ===========================================================================================

  // Step 8: the star must close the literal open. Treating every star as empty or as an open
  //         rejects a match that the statement explicitly permits
  @Test
  void starCanActAsAClose() {
    assertThat(sut.checkValidString("(*")).isTrue();
  }

  // Step 9: the mirror of Step 8. This star must open the literal close, so always assigning a
  //         star the closing role also loses valid inputs
  @Test
  void starCanActAsAnOpen() {
    assertThat(sut.checkValidString("*)")).isTrue();
  }

  // Step 10: both literal pairs are already balanced. Only deleting the middle star works, so
  //          allowing just the two parenthesis roles incorrectly rejects an odd-length string
  @Test
  void starBetweenBalancedPairsMustDisappear() {
    assertThat(sut.checkValidString("()*()")).isTrue();
  }

  // Step 11: a star represents at most one character. Letting it stand for an arbitrary closing
  //          sequence would close both literal opens, but one star cannot do that
  @Test
  void singleStarCannotCloseMultipleOpens() {
    assertThat(sut.checkValidString("((*")).isFalse();
  }

  // Step 12: the mirror of Step 11. One star cannot supply an arbitrary opening sequence for two
  //          literal closes
  @Test
  void singleStarCannotOpenMultipleCloses() {
    assertThat(sut.checkValidString("*))")).isFalse();
  }

  // Step 13: wildcard capacity alone is insufficient. The star precedes the unmatched open, so
  //          using it as a close would reverse the required matching order
  @Test
  void earlierStarCannotCloseALaterOpen() {
    assertThat(sut.checkValidString("*(")).isFalse();
  }

  // Step 14: the mirror of Step 13. A later star cannot open a close that already made the first
  //          prefix invalid, even though the counts could balance afterward
  @Test
  void laterStarCannotOpenAnEarlierClose() {
    assertThat(sut.checkValidString(")*")).isFalse();
  }

  // Step 15: the only valid assignment makes the first star open, the middle disappear, and the
  //          last close, producing "()()". One global role for all stars cannot represent this
  @Test
  void differentStarsCanRequireAllThreeRoles() {
    assertThat(sut.checkValidString("*)*(*")).isTrue();
  }

  // Step 16: the literal close matches the second open, leaving the star to close the first.
  //          Spending a star before a pending literal open rejects this valid "(())" assignment
  @Test
  void literalCloseCanLeaveAnEarlierStarForTheOuterPair() {
    assertThat(sut.checkValidString("(*()")).isTrue();
  }

  // ===========================================================================================
  // Official LeetCode examples (Steps 17-20).
  // ===========================================================================================

  // Step 17: the simplest literal pair needs no wildcard. Requiring at least one star rejects
  //          the first official example
  @Test
  void leetCodeExample1() {
    assertThat(sut.checkValidString("()")).isTrue();
  }

  // Step 18: the star disappears inside a matched pair. Requiring every star to become a
  //          parenthesis incorrectly rejects this odd-length official example
  @Test
  void leetCodeExample2() {
    assertThat(sut.checkValidString("(*)")).isTrue();
  }

  // Step 19: the star opens another pair, producing "(())". Deleting every star leaves an extra
  //          close and incorrectly rejects the official example
  @Test
  void leetCodeExample3() {
    assertThat(sut.checkValidString("(*))")).isTrue();
  }

  // Step 20: a lone open has no matching close. A checker that only rejects negative prefixes
  //          accepts it, but the fourth official example requires a complete match
  @Test
  void leetCodeExample4() {
    assertThat(sut.checkValidString("(")).isFalse();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 21-27).
  //
  // The length ceiling is 100. In the false cases, 49 stars offer 3^49 assignments before
  // pruning, so plain substitution search can explode. A linear scan or polynomial dynamic
  // program fits these timeouts without imposing one particular solution strategy.
  // ===========================================================================================

  // Step 21: the deepest literal nesting possible in a valid 100-character input reaches
  //          balance 50. A fixed stack or balance table that is too small fails at this boundary
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthNestedPairsAreValid() {
    assertThat(sut.checkValidString("(".repeat(50) + ")".repeat(50))).isTrue();
  }

  // Step 22: the other literal extreme returns to zero after every pair. Requiring the balance
  //          to stay strictly positive before the final character rejects all fifty pairs
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthConcatenatedPairsAreValid() {
    assertThat(sut.checkValidString("()".repeat(50))).isTrue();
  }

  // Step 23: every character is flexible at the length ceiling, and all can disappear. A balance
  //          bound sized only for the 50 literal opens in Step 21 may overflow while scanning
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllStarsAreValid() {
    assertThat(sut.checkValidString("*".repeat(100))).isTrue();
  }

  // Step 24: even making all 49 stars close leaves two of the 51 opens unmatched. A checker that
  //          accepts any nonnegative possible balance, instead of requiring zero, answers true
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthStarsCannotCloseMoreOpensThanTheirCount() {
    assertThat(sut.checkValidString("(".repeat(51) + "*".repeat(49))).isFalse();
  }

  // Step 25: the literal suffix has 26 opens and 25 closes. No nonnegative balance after the
  //          initial stars can make that suffix end at zero. Counting stars without their
  //          positions incorrectly accepts it, and naive branching explores many dead choices
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthEarlierStarsCannotRepairAnUnclosedSuffix() {
    assertThat(sut.checkValidString("*".repeat(49) + "(".repeat(26) + ")".repeat(25)))
        .isFalse();
  }

  // Step 26: every star must close one of the fifty literal opens. Treating any star as empty or
  //          opening instead leaves a positive balance at the end
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthCanRequireEveryStarToClose() {
    assertThat(sut.checkValidString("(".repeat(50) + "*".repeat(50))).isTrue();
  }

  // Step 27: the mirror of Step 26. All fifty stars must open, so deleting stars whenever the
  //          current balance is zero strands the literal closes that follow
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthCanRequireEveryStarToOpen() {
    assertThat(sut.checkValidString("*".repeat(50) + ")".repeat(50))).isTrue();
  }

  // ===========================================================================================
  // State isolation (Step 28). Strings are immutable, so no input-mutation spec is needed.
  // ===========================================================================================

  // Step 28: one instance handles different sizes and answers, with length 100 in the middle.
  //          Cached balances, stacks, or a memo keyed only by position leak between calls, and
  //          "*(" followed by "(*" also catches a cache that distinguishes only input length
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyStringsWithoutLeakingState() {
    assertThat(sut.checkValidString("*")).isTrue();
    assertThat(sut.checkValidString("*(")).isFalse();
    assertThat(sut.checkValidString("*".repeat(100))).isTrue();
    assertThat(sut.checkValidString("*".repeat(49) + "(".repeat(26) + ")".repeat(25)))
        .isFalse();
    assertThat(sut.checkValidString("(*")).isTrue();
    assertThat(sut.checkValidString(")")).isFalse();
    assertThat(sut.checkValidString("*(")).isFalse();
    assertThat(sut.checkValidString("*")).isTrue();
  }
}
