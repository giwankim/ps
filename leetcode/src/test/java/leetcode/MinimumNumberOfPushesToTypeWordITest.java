package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinimumNumberOfPushesToTypeWordITest {
  MinimumNumberOfPushesToTypeWordI sut = new MinimumNumberOfPushesToTypeWordI();

  // Step 1: constraint minimum — the only letter can occupy the first position on any key,
  //         so typing it requires exactly one push
  @Test
  void minimumLengthNeedsOnePush() {
    assertThat(sut.minimumPushes("a")).isEqualTo(1);
  }

  // Step 2: LeetCode Example 1 — five distinct letters fit on five different keys, each in
  //         the first position, so every letter costs one push
  @Test
  void leetCodeExample1FitsInFirstPressLayer() {
    assertThat(sut.minimumPushes("abcde")).isEqualTo(5);
  }

  // Step 3: first layer boundary — all eight available keys are used once, so eight distinct
  //         letters still require only one push each
  @Test
  void eightLettersFillTheFirstPressLayer() {
    assertThat(sut.minimumPushes("abcdefgh")).isEqualTo(8);
  }

  // Step 4: crossing the first boundary — eight letters cost one push each and the ninth must
  //         occupy a second position, for 8 * 1 + 1 * 2 = 10 pushes
  @Test
  void ninthLetterStartsTheSecondPressLayer() {
    assertThat(sut.minimumPushes("abcdefghi")).isEqualTo(10);
  }

  // Step 5: LeetCode Example 2 — letter identity and order do not affect an optimal remapping;
  //         eight letters cost one push and two letters cost two pushes, totaling 12
  @Test
  void leetCodeExample2UsesTwoSecondLayerSlots() {
    assertThat(sut.minimumPushes("xycdefghij")).isEqualTo(12);
  }

  // Step 6: second layer boundary — eight first-position letters and eight second-position
  //         letters require 8 * 1 + 8 * 2 = 24 pushes
  @Test
  void sixteenLettersFillTheSecondPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnop")).isEqualTo(24);
  }

  // Step 7: crossing the second boundary — the seventeenth letter is the first that needs
  //         three pushes, for 8 + 16 + 3 = 27 pushes
  @Test
  void seventeenthLetterStartsTheThirdPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnopq")).isEqualTo(27);
  }

  // Step 8: third layer boundary — three complete layers across eight keys require
  //         8 * 1 + 8 * 2 + 8 * 3 = 48 pushes
  @Test
  void twentyFourLettersFillTheThirdPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwx")).isEqualTo(48);
  }

  // Step 9: crossing the third boundary — after the first three layers cost 48 pushes, the
  //         twenty-fifth letter starts a fourth layer and raises the total to 52
  @Test
  void twentyFifthLetterStartsTheFourthPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwxy")).isEqualTo(52);
  }

  // Step 10: constraint maximum — all 26 lowercase letters use three full layers and two
  //          fourth-position slots, for 48 + 2 * 4 = 56 pushes
  @Test
  void maximumLengthUsesTwoFourthLayerSlots() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwxyz")).isEqualTo(56);
  }
}
