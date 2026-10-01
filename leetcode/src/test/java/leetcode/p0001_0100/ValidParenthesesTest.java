package leetcode.p0001_0100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ValidParenthesesTest {
  ValidParentheses sut = new ValidParentheses();

  // ===========================================================================================
  // The floor (Steps 1-3).
  // ===========================================================================================

  // Step 1: smallest input the constraints allow (1 <= s.length). No single bracket is valid,
  //         and a lone '(' is never closed. A scan that rejects bad closers but never checks
  //         that the stack is empty at the end answers true
  @Test
  void loneOpenerIsInvalid() {
    assertThat(sut.isValid("(")).isFalse();
  }

  // Step 2: the mirror of Step 1, a closer with nothing open. Popping without checking for an
  //         empty stack throws NoSuchElementException from ArrayDeque.pop instead of answering
  //         false, and skipping a closer when nothing is open answers true
  @Test
  void loneCloserIsInvalid() {
    assertThat(sut.isValid(")")).isFalse();
  }

  // Step 3: the smallest valid strings have length 2. ')' is one code point past '(', but ']'
  //         and '}' are two past '[' and '{', so matching by c == top + 1 answers false
  @Test
  void squarePairIsValid() {
    assertThat(sut.isValid("[]")).isTrue();
  }

  // ===========================================================================================
  // Open brackets must be closed by the same type (Steps 4-5).
  // ===========================================================================================

  // Step 4: the mirror of Example 3, with the square bracket opening and the round one closing.
  //         One counter that ignores bracket types sees +1 then -1 and answers true
  @Test
  void roundCloserCannotCloseASquareOpener() {
    assertThat(sut.isValid("[)")).isFalse();
  }

  // Step 5: every type has as many closers as openers, and the running total never dips below
  //         zero, yet '(' is closed by ']'. Counting answers true, whether it keeps one running
  //         counter or one total per type checked at the end
  @Test
  void balancedCountsOfEachTypeAreNotEnough() {
    assertThat(sut.isValid("(][)")).isFalse();
  }

  // ===========================================================================================
  // Open brackets must be closed in the correct order (Steps 6-8).
  // ===========================================================================================

  // Step 6: brackets close in reverse order of opening, so the most recent opener is matched
  //         first. Checking adjacent characters two at a time answers false, and so does an
  //         ArrayDeque filled with add or offer but drained with pop or poll, which matches the
  //         oldest opener as a queue would
  @Test
  void innermostPairClosesFirst() {
    assertThat(sut.isValid("{[()]}")).isTrue();
  }

  // Step 7: pairs inside a pair need not mirror each other. Matching s[i] against s[n-1-i]
  //         compares '(' with ']' and answers false, as do the adjacent check and the queue
  @Test
  void siblingsInsideAnOuterPairAreValid() {
    assertThat(sut.isValid("{()[]}")).isTrue();
  }

  // Step 8: '}' arrives while '[' is still open, so the pairs cross instead of nesting. One
  //         running counter per type never dips below zero and ends at zero, so it answers
  //         true, and so does the queue from Step 6
  @Test
  void crossedPairsAreInvalid() {
    assertThat(sut.isValid("{[}]")).isFalse();
  }

  // ===========================================================================================
  // Every close bracket has a corresponding open bracket (Steps 9-10).
  // ===========================================================================================

  // Step 9: a closer arrives after everything before it has balanced out. Skipping a closer
  //         when the stack is empty, to dodge the empty pop, answers true, and so does
  //         returning true as soon as the stack first empties
  @Test
  void strayCloserAfterABalancedPrefixIsInvalid() {
    assertThat(sut.isValid("()]")).isFalse();
  }

  // Step 10: the mirror of Step 9, an opener after a balanced prefix that is never closed.
  //          Returning true as soon as the stack empties answers true, and so does a missing
  //          final emptiness check
  @Test
  void openerLeftAfterABalancedPrefixIsInvalid() {
    assertThat(sut.isValid("()(")).isFalse();
  }

  // ===========================================================================================
  // LeetCode examples (Steps 11-15).
  // ===========================================================================================

  // Step 11: LeetCode Example 1, the round pair alone. Every wrong approach named in this file
  //          passes it, so on its own it proves only that the method is wired up
  @Test
  void leetCodeExample1() {
    assertThat(sut.isValid("()")).isTrue();
  }

  // Step 12: LeetCode Example 2, all three types side by side. Matching s[i] against s[n-1-i]
  //          compares '(' with '}' and answers false, and so does matching by c == top + 1
  @Test
  void leetCodeExample2() {
    assertThat(sut.isValid("()[]{}")).isTrue();
  }

  // Step 13: LeetCode Example 3, one opener and one closer of different types. One counter that
  //          ignores bracket types answers true
  @Test
  void leetCodeExample3() {
    assertThat(sut.isValid("(]")).isFalse();
  }

  // Step 14: LeetCode Example 4, a square pair nested in a round one. The adjacent check pairs
  //          '(' with '[' and answers false, and the queue matches ']' against '(' and answers
  //          false
  @Test
  void leetCodeExample4() {
    assertThat(sut.isValid("([])")).isTrue();
  }

  // Step 15: LeetCode Example 5, the same four characters as Example 4 with the pairs crossed.
  //          One running counter per type answers true, and so does the queue. Only remembering
  //          which opener is innermost, not how many of each are open, tells the two apart
  @Test
  void leetCodeExample5() {
    assertThat(sut.isValid("([)]")).isFalse();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 16-19). s.length <= 10^4 is small: one stack scan is 10^4 steps,
  // and even erasing "()", "[]" and "{}" until nothing changes, which is quadratic, takes about
  // 3300 passes on Step 16 and finishes in tens of milliseconds. The timeouts reject only worse
  // than quadratic work, such as an interval DP over every substring, which is O(n^3) and about
  // 1.7 x 10^11 steps at this length.
  // ===========================================================================================

  // Step 16: maximum length as 5000 levels cycling through the three types, "([{([{" in, then
  //          closed innermost first. The adjacent check and the queue both answer false
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthFullyNestedIsValid() {
    String s = nested(5000);

    assertThat(sut.isValid(s)).isTrue();
  }

  // Step 17: maximum length as 5000 sibling pairs cycling "()[]{}". Matching s[i] against
  //          s[n-1-i] answers false
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthOfSiblingPairsIsValid() {
    String s = siblings(5000);

    assertThat(sut.isValid(s)).isTrue();
  }

  // Step 18: maximum length with nothing ever closed, so the stack grows to 10^4 openers. A
  //          missing final emptiness check answers true
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthOfOpenersIsInvalid() {
    String s = "(".repeat(10_000);

    assertThat(sut.isValid(s)).isFalse();
  }

  // Step 19: Step 16's string with the outermost ')' replaced by ']', so every comparison
  //          succeeds until the very last character. One counter that ignores bracket types
  //          answers true
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthWithAMismatchedFinalCloserIsInvalid() {
    String valid = nested(5000);
    String s = valid.substring(0, valid.length() - 1) + ']';

    assertThat(sut.isValid(s)).isFalse();
  }

  // ===========================================================================================
  // Hygiene (Step 20).
  // ===========================================================================================

  // Step 20: one instance answers several inputs of different lengths, deliberately out of
  //          order with the longest in the middle. A stack kept on the instance instead of
  //          created per call still holds the '{' that the early return on "{[}]" left behind,
  //          so it answers false for the nested string and for "[]" after it
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.isValid("()")).isTrue();
    assertThat(sut.isValid("{[}]")).isFalse();
    assertThat(sut.isValid(nested(5000))).isTrue();
    assertThat(sut.isValid("(")).isFalse();
    assertThat(sut.isValid("[]")).isTrue();
  }

  private static String nested(int levels) {
    StringBuilder sb = new StringBuilder(2 * levels);
    for (int i = 0; i < levels; i++) {
      sb.append("([{".charAt(i % 3));
    }
    for (int i = levels - 1; i >= 0; i--) {
      sb.append(")]}".charAt(i % 3));
    }
    return sb.toString();
  }

  private static String siblings(int pairs) {
    StringBuilder sb = new StringBuilder(2 * pairs);
    for (int i = 0; i < pairs; i++) {
      sb.append("()[]{}", 2 * (i % 3), 2 * (i % 3) + 2);
    }
    return sb.toString();
  }
}
