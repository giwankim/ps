package leetcode.p0001_0100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class GenerateParenthesesTest {
  GenerateParentheses sut = new GenerateParentheses();

  // ===========================================================================================
  // Small n, listed in full (Steps 1-3). The judge accepts the strings in any order, so every
  // spec in this file compares contents and multiplicity, never sequence.
  // ===========================================================================================

  // Step 1: the floor, 1 <= n, which is also LeetCode Example 2 verbatim. Balanced counts are not
  //         enough: keeping every string with n of each parenthesis also returns ")(".
  @Test
  void n1ReturnsOnlyTheSinglePair() {
    assertThat(sut.generateParenthesis(1)).containsExactly("()");
  }

  // Step 2: nesting and concatenation are both well-formed. A solver that only nests returns
  //         "(())" alone, and one that only concatenates returns "()()" alone. Wrapping or
  //         flanking each n - 1 string without deduplicating builds "()()" twice.
  @Test
  void n2ReturnsTheNestedAndTheSideBySidePairs() {
    assertThat(sut.generateParenthesis(2)).containsExactlyInAnyOrder("(())", "()()");
  }

  // Step 3: C_4 = 14. Wrapping or flanking each n - 1 string, even deduplicated, yields
  //         1, 2, 5, 13, ... and matches the Catalan numbers only through n = 3. It first fails
  //         here: it never puts two nested blocks at the same level, so "(())(())" is missing.
  @Test
  void n4ReturnsAll14IncludingTwoNestedHalves() {
    assertThat(sut.generateParenthesis(4))
        .containsExactlyInAnyOrder(
            "(((())))",
            "((()()))",
            "((())())",
            "((()))()",
            "(()(()))",
            "(()()())",
            "(()())()",
            "(())(())",
            "(())()()",
            "()((()))",
            "()(()())",
            "()(())()",
            "()()(())",
            "()()()()");
  }

  // ===========================================================================================
  // Every arrangement, nothing extra (Steps 4-6). Past n = 4 a full listing stops being readable,
  // so these check three properties instead: no duplicates, exactly C_n strings, and each one
  // well-formed with n pairs. Only C_n such strings exist, so passing all three is as exact as
  // listing them. The constraints allow just eight values of n, and with Steps 1-3, Step 7, and
  // Step 8 the suite pins down every one of them.
  // ===========================================================================================

  // Step 4: C_5 = 42. Wrap-or-flank falls further behind, 34 of 42. All 8 strings it misses hold
  //         two nested blocks at one level, even apart, as in "(())()(())".
  @Test
  void n5ReturnsAll42Arrangements() {
    assertAllArrangements(5, sut.generateParenthesis(5), 42);
  }

  // Step 5: C_6 = 132. Dropping the prefix rule and keeping every string with six of each
  //         parenthesis answers C(12, 6) = 924, seven times too many.
  @Test
  void n6ReturnsAll132Arrangements() {
    assertAllArrangements(6, sut.generateParenthesis(6), 132);
  }

  // Step 6: C_7 = 429. Wrap-or-flank now builds only 233, barely more than half.
  @Test
  void n7ReturnsAll429Arrangements() {
    assertAllArrangements(7, sut.generateParenthesis(7), 429);
  }

  // ===========================================================================================
  // Official example (Step 7). Example 2 is the floor, Step 1.
  // ===========================================================================================

  // Step 7: printed in depth-first order, but any order is accepted. Wrapping or appending "()" on
  //         one side only never puts "()" in front of a nested pair, so it misses "()(())" and
  //         answers 4. Wrap-or-flank passes this example, which is why Step 3 exists.
  @Test
  void leetCodeExample1() {
    assertThat(sut.generateParenthesis(3))
        .containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
  }

  // ===========================================================================================
  // Constraint bound (Step 8). n = 8 is small: filtering all 2^16 = 65,536 strings and backtracking
  // over the 1,430 answers both finish in milliseconds. The timeout rejects only factorial work,
  // such as permuting the 16 characters through 16! (about 2.1 * 10^13) orderings, and a
  // recursion that never reaches its base case.
  // ===========================================================================================

  // Step 8: C_8 = 1,430, the largest answer allowed. Wrap-or-flank builds 610 of them.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n8ReturnsAll1430Arrangements() {
    assertAllArrangements(8, sut.generateParenthesis(8), 1_430);
  }

  // ===========================================================================================
  // Reuse and result ownership (Steps 9-10). The int input cannot be modified, so the state at
  // risk lives on the instance or in a cache.
  // ===========================================================================================

  // Step 9: largest input in the middle, then a smaller one, then the first again. A result list
  //         kept on the instance and never reset grows to 1,435 strings by the second call.
  @Test
  void oneInstanceAnswersDifferentSizes() {
    assertThat(sut.generateParenthesis(3))
        .containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
    assertAllArrangements(8, sut.generateParenthesis(8), 1_430);
    assertThat(sut.generateParenthesis(1)).containsExactly("()");
    assertThat(sut.generateParenthesis(3))
        .containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
  }

  // Step 10: a memoized DP that returns its cached lists lets one caller's edit corrupt later
  //          answers. After the n = 2 result is cleared, such a cache answers ["(())()"] for
  //          n = 3 and an empty list for n = 2. An unmodifiable result is just as safe.
  @Test
  void clearingOneResultDoesNotCorruptLaterAnswers() {
    List<String> two = sut.generateParenthesis(2);
    try {
      two.clear();
    } catch (UnsupportedOperationException unmodifiable) {
      // Refusing the edit protects later answers as well as copying does.
    }
    assertThat(sut.generateParenthesis(3))
        .containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
    assertThat(sut.generateParenthesis(2)).containsExactlyInAnyOrder("(())", "()()");
  }

  // Exactly C_n well-formed strings with n pairs exist, so `count` distinct ones are all of them.
  private static void assertAllArrangements(int n, List<String> actual, int count) {
    assertThat(actual)
        .doesNotHaveDuplicates()
        .hasSize(count)
        .allSatisfy(s -> assertThat(s)
            .hasSize(2 * n)
            .matches(GenerateParenthesesTest::isWellFormed, "well-formed"));
  }

  private static boolean isWellFormed(String s) {
    int depth = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        depth++;
      } else if (c == ')' && depth > 0) {
        depth--;
      } else {
        return false;
      }
    }
    return depth == 0;
  }
}
