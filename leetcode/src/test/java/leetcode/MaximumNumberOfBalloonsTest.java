package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximumNumberOfBalloonsTest {
  MaximumNumberOfBalloons sut = new MaximumNumberOfBalloons();

  // Step 1: the minimal happy path — exactly the seven letters of "balloon", once each
  // (b1 a1 l2 o2 n1), forms exactly one instance. Pins the base case that a single complete
  // multiset returns 1, ruling out an implementation that always returns 0.
  @Test
  void exactLettersFormOneBalloon() {
    assertThat(sut.maxNumberOfBalloons("balloon")).isEqualTo(1);
  }

  // Step 2: 'b' is required. "alloon" has every letter of "balloon" except 'b' (a1 l2 o2 n1).
  // A missing single-count letter forces the count to 0. Catches an implementation that never
  // counts 'b'.
  @Test
  void missingBYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("alloon")).isZero();
  }

  // Step 3: 'a' is required. "blloon" drops the 'a' (b1 l2 o2 n1) and must return 0. Catches an
  // implementation that forgets the 'a' slot.
  @Test
  void missingAYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("blloon")).isZero();
  }

  // Step 4: 'n' is required. "balloo" drops the trailing 'n' (b1 a1 l2 o2) and must return 0.
  // Catches an implementation that forgets the 'n' slot — easy to miss since 'n' appears once
  // at the very end of the word.
  @Test
  void missingNYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("balloo")).isZero();
  }

  // Step 5: 'l' needs a PAIR. "baloon" supplies only one 'l' (b1 a1 l1 o2 n1); since each
  // balloon consumes two 'l's, floor(1/2) = 0 caps the answer at 0. Catches an implementation
  // that treats 'l' as a single-count letter (which would wrongly return 1).
  @Test
  void oneLShortOfAPairYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("baloon")).isZero();
  }

  // Step 6: 'o' needs a PAIR. "ballon" supplies only one 'o' (b1 a1 l2 o1 n1); floor(1/2) = 0
  // caps the answer at 0. The 'o' twin of Step 5 — pins that BOTH doubled letters require a
  // full pair, not just 'l'.
  @Test
  void oneOShortOfAPairYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("ballon")).isZero();
  }

  // Step 7: counts scale linearly. Two complete words back to back (b2 a2 l4 o4 n2) form two
  // instances. Catches a hard-coded "return 1 when possible" that ignores surplus.
  @Test
  void twoCompleteSetsFormTwoBalloons() {
    assertThat(sut.maxNumberOfBalloons("balloonballoon")).isEqualTo(2);
  }

  // Step 8: the doubled letters are the bottleneck. "bbaalloonn" gives b2 a2 l2 o2 n2 — the
  // single-count letters allow 2, but l and o supply only one pair each, so the answer is
  // min(2, 2, l/2=1, o/2=1, 2) = 1. This is the key discriminator for the `/2`: an
  // implementation that forgets to halve 'l' and 'o' would wrongly return 2.
  @Test
  void doubledLettersBottleneckCapsTheCount() {
    assertThat(sut.maxNumberOfBalloons("bbaalloonn")).isEqualTo(1);
  }

  // Step 9: a single-count letter is the bottleneck even when the pairs are abundant.
  // "balloonalloon" gives b1 a2 l4 o4 n2 — l and o each allow 2, but only one 'b' exists, so
  // min(b=1, a=2, l/2=2, o/2=2, n=2) = 1. Catches an implementation that computes the minimum
  // over only the doubled letters and ignores the single-count ones (which would return 2).
  @Test
  void singleCountLetterBottleneckCapsTheCount() {
    assertThat(sut.maxNumberOfBalloons("balloonalloon")).isEqualTo(1);
  }

  // Step 10: floor division must round down. "bbaallloooonn" gives b2 a2 l3 o4 n2 — three 'l's
  // make only one full pair (the third is wasted), so min(2, 2, floor(3/2)=1, floor(4/2)=2, 2)
  // = 1. Catches ceiling/rounding bugs (ceil(3/2)=2 would wrongly return 2).
  @Test
  void oddSurplusOfADoubledLetterFloorsDown() {
    assertThat(sut.maxNumberOfBalloons("bbaallloooonn")).isEqualTo(1);
  }

  // Step 11: LeetCode Example 1 — letters out of order and padded with irrelevant characters.
  // "nlaebolko" contains b1 a1 l2 o2 n1 plus stray 'e' and 'k', forming one instance. Pins that
  // order does not matter and that surplus unrelated letters are ignored.
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxNumberOfBalloons("nlaebolko")).isEqualTo(1);
  }

  // Step 12: LeetCode Example 2 — two scrambled instances with noise. "loonbalxballpoon" yields
  // b2 a2 l4 o4 n2 (plus stray 'x' and 'p'), so the answer is 2. End-to-end acceptance of the
  // multi-instance path over a realistic scrambled input.
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxNumberOfBalloons("loonbalxballpoon")).isEqualTo(2);
  }

  // Step 13: LeetCode Example 3 — no 'b' (and no 'a') at all. "leetcode" cannot form any
  // instance, so the answer is 0. Confirms the all-zero path on a word that shares some letters
  // ('l', 'e', 'o') but not the required ones.
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxNumberOfBalloons("leetcode")).isZero();
  }

  // Step 14: the smallest legal input (text.length == 1). A lone 'b' is missing every other
  // required letter, so the answer is 0. Pins the lower constraint boundary.
  @Test
  void singleCharacterInputYieldsZero() {
    assertThat(sut.maxNumberOfBalloons("b")).isZero();
  }

  // Step 15: constraint ceiling — "balloon" repeated 1428 times is 9996 characters, just under
  // the 10^4 maximum, and supplies 1428 of every letter (2856 each of 'l' and 'o'). The answer
  // is 1428. Pins linear scaling at the upper bound and that the count is not capped or overflowed.
  @Test
  void handlesMaximumConstraintInput() {
    assertThat(sut.maxNumberOfBalloons("balloon".repeat(1428))).isEqualTo(1428);
  }
}
