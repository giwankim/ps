package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinimumNumberOfPushesToTypeWordIITest {
  MinimumNumberOfPushesToTypeWordII sut = new MinimumNumberOfPushesToTypeWordII();

  // Step 1: constraint minimum — the only letter can occupy the first position on any key,
  //         so typing it requires exactly one push
  @Test
  void minimumLengthNeedsOnePush() {
    assertThat(sut.minimumPushes("a")).isEqualTo(1);
  }

  // Step 2: repeats are paid per occurrence — unlike part I a letter may appear many times,
  //         and a first-position letter costs one push on every one of its four occurrences
  @Test
  void repeatedLetterPaysOnePushPerOccurrence() {
    assertThat(sut.minimumPushes("aaaa")).isEqualTo(4);
  }

  // Step 3: LeetCode Example 1 — five distinct letters fit on five different keys, each in
  //         the first position, so every letter costs one push
  @Test
  void leetCodeExample1FitsInFirstPressLayer() {
    assertThat(sut.minimumPushes("abcde")).isEqualTo(5);
  }

  // Step 4: first layer boundary — all eight available keys are used once, so eight distinct
  //         letters still require only one push each
  @Test
  void eightDistinctLettersFillTheFirstPressLayer() {
    assertThat(sut.minimumPushes("abcdefgh")).isEqualTo(8);
  }

  // Step 5: LeetCode Example 2 — only three distinct letters, so each gets its own key in the
  //         first position and all twelve characters cost one push apiece
  @Test
  void leetCodeExample2ReusesThreeFirstLayerKeys() {
    assertThat(sut.minimumPushes("xyzxyzxyzxyz")).isEqualTo(12);
  }

  // Step 6: crossing the first boundary — the first test whose answer differs from the word
  //         length; eight letters cost one push and the ninth needs two, for 8 * 1 + 1 * 2 = 10
  @Test
  void ninthDistinctLetterStartsTheSecondPressLayer() {
    assertThat(sut.minimumPushes("abcdefghi")).isEqualTo(10);
  }

  // Step 7: frequency drives the assignment — 'i' occurs three times and a..h once each, so 'i'
  //         claims a first-layer slot and one single-occurrence letter is demoted to the second
  //         layer, for 3 * 1 + 7 * 1 + 1 * 2 = 12; assigning keys in alphabetical order costs 14
  @Test
  void mostFrequentLetterClaimsAFirstLayerSlot() {
    assertThat(sut.minimumPushes("abcdefghiii")).isEqualTo(12);
  }

  // Step 8: LeetCode Example 3 — 'i' occurs six times and a..h twice each, so 'i' plus seven of
  //         those letters fill the first layer and the eighth is demoted, for
  //         6 * 1 + 7 * (2 * 1) + 1 * (2 * 2) = 24
  @Test
  void leetCodeExample3DemotesOneOfTheTiedLetters() {
    assertThat(sut.minimumPushes("aabbccddeeffgghhiiiiii")).isEqualTo(24);
  }

  // Step 9: second layer boundary — eight first-position letters and eight second-position
  //         letters require 8 * 1 + 8 * 2 = 24 pushes
  @Test
  void sixteenDistinctLettersFillTheSecondPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnop")).isEqualTo(24);
  }

  // Step 10: crossing the second boundary — the seventeenth distinct letter is the first that
  //          needs three pushes, for 8 + 16 + 3 = 27
  @Test
  void seventeenthDistinctLetterStartsTheThirdPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnopq")).isEqualTo(27);
  }

  // Step 11: deep layers still weight every occurrence — 'q' occurs five times so it takes a
  //          first-layer slot and pushes a single-occurrence letter down to the third layer,
  //          for 5 * 1 + 7 * 1 + 8 * 2 + 1 * 3 = 31
  @Test
  void thirdLayerWeightsEveryOccurrenceOfItsLetter() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqqqqq")).isEqualTo(31);
  }

  // Step 12: crossing the third boundary — after the first three layers cost 48 pushes, the
  //          twenty-fifth distinct letter starts a fourth layer and raises the total to 52
  @Test
  void twentyFifthDistinctLetterStartsTheFourthPressLayer() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwxy")).isEqualTo(52);
  }

  // Step 13: every lowercase letter once — three full layers plus two fourth-position slots,
  //          for 8 * 1 + 8 * 2 + 8 * 3 + 2 * 4 = 56
  @Test
  void everyLowercaseLetterOnceUsesTwoFourthLayerSlots() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwxyz")).isEqualTo(56);
  }

  // Step 14: uniform frequencies scale the whole layout — every letter occurs 1000 times, so
  //          the 56-push single-pass cost is multiplied by 1000
  @Test
  void uniformFrequenciesScaleTheFullLayout() {
    assertThat(sut.minimumPushes("abcdefghijklmnopqrstuvwxyz".repeat(1000))).isEqualTo(56_000);
  }

  // Step 15: constraint maximum with one letter — 100000 occurrences all cost one push, which
  //          also rules out any per-character work that is worse than linear
  @Test
  void maximumLengthOfASingleLetterCostsOnePushEach() {
    assertThat(sut.minimumPushes("a".repeat(100_000))).isEqualTo(100_000);
  }

  // Step 16: constraint maximum across every letter — 26 * 3846 + 4 = 100000 characters, where
  //          a..d occur 3847 times and e..z occur 3846 times; the four extra occurrences land in
  //          the first layer, for 3846 * 56 + 4 * 1 = 215380
  @Test
  void maximumLengthAcrossEveryLetterStaysWithinIntRange() {
    String word = "abcdefghijklmnopqrstuvwxyz".repeat(3846) + "abcd";
    assertThat(word).hasSize(100_000);
    assertThat(sut.minimumPushes(word)).isEqualTo(215_380);
  }
}
