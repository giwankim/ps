package leetcode.p1001_1100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class RemoveOutermostParenthesesTest {
  RemoveOutermostParentheses sut = new RemoveOutermostParentheses();

  // ===========================================================================================
  // The floor (Step 1).
  // ===========================================================================================

  // Step 1: the length floor reads 1, but s must be valid and every valid string has even length,
  //         so "()" is the smallest legal input. It is one primitive with nothing inside, and the
  //         answer is the empty string, not null. An open is kept when the depth before it is
  //         positive and a close when the depth after it is: testing the depth after each
  //         character for both kinds answers "(", and testing the depth before it answers ")"
  @Test
  void singlePairLeavesEmptyString() {
    assertThat(sut.removeOuterParentheses("()")).isEmpty();
  }

  // ===========================================================================================
  // One primitive (Steps 2-3).
  // ===========================================================================================

  // Step 2: exactly one layer comes off. Running the removal again on its own output until nothing
  //         changes answers "", and the two mixed-up depth tests from Step 1 answer "())" and "(()"
  @Test
  void nestedPairLosesOnlyItsOuterLayer() {
    assertThat(sut.removeOuterParentheses("(())")).isEqualTo("()");
  }

  // Step 3: the interior "()()" is itself two primitives, but s is decomposed once, and what is
  //         left is not decomposed and stripped again, which would answer "". The outer close is
  //         the last character, not the first close: pairing the first open with the first close
  //         answers "(())", and starting a new primitive at every ")" followed by "(" answers "()"
  @Test
  void flatInteriorIsKeptWhole() {
    assertThat(sut.removeOuterParentheses("(()())")).isEqualTo("()()");
  }

  // ===========================================================================================
  // Several primitives (Steps 4-6).
  // ===========================================================================================

  // Step 4: the AB rule. The depth returns to zero after the first pair, so s is "()" + "(())",
  //         and each primitive loses its own outer pair. Stripping only the first and last
  //         characters of s answers ")(()". A loop that emits a primitive only when the next one
  //         opens never flushes the last one and answers "", and stripping only the first primitive
  //         while copying the rest through answers "(())"
  @Test
  void barePairThenNestedPair() {
    assertThat(sut.removeOuterParentheses("()(())")).isEqualTo("()");
  }

  // Step 5: the mirror of Step 4. The final primitive is now the bare pair, so dropping it goes
  //         unnoticed, but stripping only the first primitive answers "()()" and stripping only
  //         the ends of s answers "())("
  @Test
  void nestedPairThenBarePair() {
    assertThat(sut.removeOuterParentheses("(())()")).isEqualTo("()");
  }

  // Step 6: the stripped pieces keep their order, "()" + "(())". Collecting them on a stack and
  //         popping answers "(())()". Example 1 cannot catch this, because its pieces read the
  //         same in either order
  @Test
  void primitivesKeepTheirOrder() {
    assertThat(sut.removeOuterParentheses("(())((()))")).isEqualTo("()(())");
  }

  // ===========================================================================================
  // Official examples (Steps 7-9).
  // ===========================================================================================

  // Step 7: the Explanation decomposes s as "(()())" + "(())". The first piece's interior "()()"
  //         stays whole, so stripping it again answers "". Starting a new primitive at every ")"
  //         followed by "(" cuts "(()())" in the middle and answers "()()", and pairing each first
  //         open with its first close answers "(())()"
  @Test
  void leetCodeExample1() {
    assertThat(sut.removeOuterParentheses("(()())(())")).isEqualTo("()()()");
  }

  // Step 8: three primitives of different shapes, the last holding a bare pair beside a nested
  //         one. Never flushing the last primitive answers "()()()", popping the pieces from a
  //         stack answers "()(())()()()", and splitting at every ")" followed by "(" answers
  //         "()()(())"
  @Test
  void leetCodeExample2() {
    assertThat(sut.removeOuterParentheses("(()())(())(()(()))")).isEqualTo("()()()()(())");
  }

  // Step 9: every primitive is a bare pair, so a nonempty s yields an empty answer. Stripping only
  //         the ends of s answers ")(", and stripping only the first primitive answers "()"
  @Test
  void leetCodeExample3() {
    assertThat(sut.removeOuterParentheses("()()")).isEmpty();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 10-13), all at the 10^5-character ceiling.
  // One depth-counting pass finishes in milliseconds. Timed in Java, building the answer with
  // String += (under 1 s) and slicing each primitive off with substring (under 0.5 s) both pass,
  // and recomputing every prefix's depth from scratch, about 5 * 10^9 steps, takes 3.5-5 s, too
  // close to call. These timeouts turn a hang into a failure rather than separate complexity
  // classes. Step 10 is the one bound that rejects a technique outright.
  // ===========================================================================================

  // Step 10: the deepest legal nesting, one primitive 50000 levels deep, of which only the
  //          outermost pair comes off. A recursive-descent parser overflows a default thread stack
  //          here, and never flushing the last primitive answers "", since the only primitive is
  //          also the last
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNestingDepthLosesOnlyItsOuterLayer() {
    assertThat(sut.removeOuterParentheses(nested(50_000)))
        .isEqualTo(nested(49_999))
        .hasSize(99_998);
  }

  // Step 11: the most primitives possible, 50000 bare pairs, all of which vanish. Stripping only
  //          the ends of s answers 99998 characters, stripping only the first primitive answers
  //          49999 pairs, and testing the depth before each character keeps all 50000 closes
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthBarePairsAllVanish() {
    assertThat(sut.removeOuterParentheses("()".repeat(50_000))).isEmpty();
  }

  // Step 12: 25000 primitives that each leave one pair. Never flushing the last primitive answers
  //          49998 characters, a loss that a check of the answer's prefix would miss, and
  //          stripping only the ends of s answers 99998
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthNestedPairsEachLeaveOnePair() {
    assertThat(sut.removeOuterParentheses("(())".repeat(25_000)))
        .isEqualTo("()".repeat(25_000))
        .hasSize(50_000);
  }

  // Step 13: one primitive wrapped around 49999 bare pairs, the widest interior. Splitting at every
  //          ")" followed by "(" answers "()", stripping the interior again answers "", and pairing
  //          the first open with the first close answers 49998 pairs inside one more pair, which
  //          has the right length and balance but is the wrong string
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthSinglePrimitiveWithWidestInterior() {
    assertThat(sut.removeOuterParentheses("(" + "()".repeat(49_999) + ")"))
        .isEqualTo("()".repeat(49_999))
        .hasSize(99_998);
  }

  // ===========================================================================================
  // State isolation (Step 14). Strings are immutable, so no input-mutation spec is needed.
  // ===========================================================================================

  // Step 14: one instance answers inputs of different sizes, with the 50000-level worst case in
  //          the middle and the first input repeated last. A StringBuilder kept on the instance
  //          must start empty on every call, or the bare pair right after the first input answers
  //          "()(())" instead of ""
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputsWithoutLeakingState() {
    assertThat(sut.removeOuterParentheses("(())((()))")).isEqualTo("()(())");
    assertThat(sut.removeOuterParentheses("()")).isEmpty();
    assertThat(sut.removeOuterParentheses(nested(50_000))).isEqualTo(nested(49_999));
    assertThat(sut.removeOuterParentheses("(()())(())(()(()))")).isEqualTo("()()()()(())");
    assertThat(sut.removeOuterParentheses("(())")).isEqualTo("()");
    assertThat(sut.removeOuterParentheses("(())((()))")).isEqualTo("()(())");
  }

  private static String nested(int depth) {
    return "(".repeat(depth) + ")".repeat(depth);
  }
}
