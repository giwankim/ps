package leetcode.p0901_1000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumAddToMakeParenthesesValidTest {
  MinimumAddToMakeParenthesesValid sut = new MinimumAddToMakeParenthesesValid();

  // ===========================================================================================
  // Smallest inputs (Steps 1-3).
  // ===========================================================================================

  // Step 1: the length floor is 1, so the empty base case is never an input. A lone open needs
  //         exactly one inserted close. Inserting a whole "()" pair per stray character answers 2
  @Test
  void singleOpenNeedsOneInsertedClose() {
    assertThat(sut.minAddToMakeValid("(")).isEqualTo(1);
  }

  // Step 2: the mirror of Step 1. A lone close needs one open inserted before it. Returning the
  //         final balance answers -1, and clamping that balance at zero answers 0
  @Test
  void singleCloseNeedsOneInsertedOpen() {
    assertThat(sut.minAddToMakeValid(")")).isEqualTo(1);
  }

  // Step 3: the smallest valid input costs nothing. Charging a move per close or per character
  //         answers 1 or 2, so the minimum must be able to reach zero
  @Test
  void matchedPairNeedsNoMoves() {
    assertThat(sut.minAddToMakeValid("()")).isZero();
  }

  // ===========================================================================================
  // Valid strings built by the (A) and AB rules (Steps 4-5).
  // ===========================================================================================

  // Step 4: the (A) rule with a nonempty interior. Matched pairs need not be adjacent, so deleting
  //         every adjacent "()" in a single pass leaves "()" behind and answers 2
  @Test
  void nestedPairsNeedNoMoves() {
    assertThat(sut.minAddToMakeValid("(())")).isZero();
  }

  // Step 5: the AB rule. The balance returns to zero mid-string and the whole is still valid.
  //         Applying only the (A) rule, peeling one outer pair at a time, strands ")(" and
  //         answers 2
  @Test
  void concatenatedPairsNeedNoMoves() {
    assertThat(sut.minAddToMakeValid("()()")).isZero();
  }

  // ===========================================================================================
  // Order, not counts (Steps 6-9).
  // ===========================================================================================

  // Step 6: equal counts do not mean balanced. A close can only match an earlier open, so both
  //         characters here are stray. Comparing the counts of opens and closes answers 0
  @Test
  void closeBeforeOpenCannotPair() {
    assertThat(sut.minAddToMakeValid(")(")).isEqualTo(2);
  }

  // Step 7: a deficit is paid when it happens. Balances 1, 0, -1 strand the third character, and
  //         the final open is a new, separate debt. Letting the balance go negative so the trailing
  //         open cancels the earlier deficit answers 1
  @Test
  void laterOpenCannotRepayAnEarlierDeficit() {
    assertThat(sut.minAddToMakeValid("())(")).isEqualTo(2);
  }

  // Step 8: balances 1, 2, 1, 0, -1 strand the close at index 4, and the balance restarts at zero
  //         there, so both trailing opens are stray: 1 + 2 = 3. Adding the deepest deficit to the
  //         final unclamped balance of 1 measures those opens from the wrong zero and answers 2
  @Test
  void trailingOpensAreCountedFromWhereTheBalanceRestarts() {
    assertThat(sut.minAddToMakeValid("(()))((")).isEqualTo(3);
  }

  // Step 9: stray closes and stray opens are separate debts. The middle "()" matches, the two
  //         leading closes need opens, and the two trailing opens need closes. No one insertion
  //         serves both sides, so taking the larger debt answers 2 and comparing counts answers 0
  @Test
  void strayClosesAndStrayOpensAroundAValidBlockAreBothPaid() {
    assertThat(sut.minAddToMakeValid("))()((")).isEqualTo(4);
  }

  // ===========================================================================================
  // What one move can do (Steps 10-11).
  // ===========================================================================================

  // Step 10: an insertion cannot repurpose an existing character. Reading a move as flipping a
  //          parenthesis, as the reversal variants of this problem do, turns "((((" into "(())"
  //          with 2 flips, but here each stray open needs its own inserted close
  @Test
  void everyStrayOpenCostsItsOwnMove() {
    assertThat(sut.minAddToMakeValid("((((")).isEqualTo(4);
  }

  // Step 11: the statement's own illustration. Inserting an open gives "(()))" and inserting a
  //          close gives "())))". Both are legal moves, and neither result is valid. Reading them
  //          as fixes suggests 1, and flipping the third character answers 1 too, but the two
  //          stray closes need two inserted opens
  @Test
  void statementIllustrationNeedsTwoInsertedOpens() {
    assertThat(sut.minAddToMakeValid("()))")).isEqualTo(2);
  }

  // ===========================================================================================
  // Official examples (Steps 12-13).
  // ===========================================================================================

  // Step 12: a move may land anywhere, including before the first character. Reading a move as an
  //          append can never repair the stray close at the end, but one open inserted at the
  //          front or after the leading "()" makes the string valid
  @Test
  void leetCodeExample1() {
    assertThat(sut.minAddToMakeValid("())")).isEqualTo(1);
  }

  // Step 13: three stray opens cost three moves. Counting only stray closes answers 0, and the
  //          flip reading halves the debt to 2 even though no odd-length string can be made valid
  //          by flipping
  @Test
  void leetCodeExample2() {
    assertThat(sut.minAddToMakeValid("(((")).isEqualTo(3);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-20).
  // At 1000 characters, a linear scan, repeated deletion of adjacent pairs (at most 500 passes),
  // and even an O(n^3) interval DP (about 1.7 * 10^8 split steps) all finish well inside the
  // limit. The timeouts catch exponential search over insertion sequences.
  // ===========================================================================================

  // Step 14: the largest possible answer, one inserted close for each of 1000 opens. Counting
  //          only stray closes answers 0, and the flip reading answers 500
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllOpensNeedsOneCloseEach() {
    assertThat(sut.minAddToMakeValid("(".repeat(1000))).isEqualTo(1000);
  }

  // Step 15: the mirror of Step 14. Clamping the final balance at zero answers 0, so stray closes
  //          must be counted during the scan rather than recovered from the end state
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllClosesNeedsOneOpenEach() {
    assertThat(sut.minAddToMakeValid(")".repeat(1000))).isEqualTo(1000);
  }

  // Step 16: 500 closes, then 500 opens. Every character is stray, yet the counts tie, so
  //          comparing counts answers 0, and both an unclamped balance and taking the larger debt
  //          answer 500
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthClosesBeforeOpensLeavesEveryCharacterStray() {
    assertThat(sut.minAddToMakeValid(")".repeat(500) + "(".repeat(500))).isEqualTo(1000);
  }

  // Step 17: the deepest valid nesting, 500 levels. A recursive parser must survive the depth,
  //          and deleting adjacent "()" in a single pass removes only the innermost pair and
  //          answers 998
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNestingDepthNeedsNoMoves() {
    assertThat(sut.minAddToMakeValid("(".repeat(500) + ")".repeat(500))).isZero();
  }

  // Step 18: 500 adjacent pairs, the widest valid concatenation. Peeling outer pairs strips only
  //          the first open and the last close, strands the 998 characters between, and answers 998
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthFlatConcatenationNeedsNoMoves() {
    assertThat(sut.minAddToMakeValid("()".repeat(500))).isZero();
  }

  // Step 19: ")(" 500 times. Only the first close and the last open are stray, and the 499 pairs
  //          between them match across the repetition boundaries. An unclamped balance charges
  //          every close and answers 500, and comparing counts answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlternationStartingWithACloseNeedsOnlyTwoMoves() {
    assertThat(sut.minAddToMakeValid(")(".repeat(500))).isEqualTo(2);
  }

  // Step 20: "())" 333 times, then "(". Each block strands one close and the final open is stray:
  //          333 + 1 = 334. Comparing 334 opens against 666 closes answers 332, and taking the
  //          larger debt answers 333
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRepeatedStrayClosesPlusATrailingOpen() {
    assertThat(sut.minAddToMakeValid("())".repeat(333) + "(")).isEqualTo(334);
  }

  // ===========================================================================================
  // State isolation (Step 21). Strings are immutable, so no input-mutation spec is needed.
  // ===========================================================================================

  // Step 21: one instance answers inputs of different sizes, with the 1000-character worst case
  //          in the middle and the first input repeated last. A stray-close counter or open count
  //          kept on the instance must reset before every call
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputsWithoutLeakingState() {
    assertThat(sut.minAddToMakeValid("())(")).isEqualTo(2);
    assertThat(sut.minAddToMakeValid("(()))((")).isEqualTo(3);
    assertThat(sut.minAddToMakeValid(")".repeat(500) + "(".repeat(500))).isEqualTo(1000);
    assertThat(sut.minAddToMakeValid("(")).isEqualTo(1);
    assertThat(sut.minAddToMakeValid("()()")).isZero();
    assertThat(sut.minAddToMakeValid("())(")).isEqualTo(2);
  }
}
