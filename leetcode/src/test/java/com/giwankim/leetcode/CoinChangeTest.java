package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoinChangeTest {
  CoinChange sut = new CoinChange();

  @Test
  void combinesCoinsForTheCanonicalExample() {
    assertThat(sut.coinChange(new int[] {1, 2, 5}, 11)).isEqualTo(3);
  }

  @Test
  void returnsMinusOneWhenTheAmountCannotBeFormed() {
    assertThat(sut.coinChange(new int[] {2}, 3)).isEqualTo(-1);
  }

  @Test
  void zeroAmountNeedsNoCoins() {
    assertThat(sut.coinChange(new int[] {1}, 0)).isZero();
  }

  @Test
  void zeroAmountWithSeveralDenominationsStillNeedsNoCoins() {
    assertThat(sut.coinChange(new int[] {1, 2, 5}, 0)).isZero();
  }

  @Test
  void amountOfOneWithAUnitCoinNeedsOneCoin() {
    assertThat(sut.coinChange(new int[] {1}, 1)).isEqualTo(1);
  }

  @Test
  void amountEqualToASingleDenominationNeedsOneCoin() {
    assertThat(sut.coinChange(new int[] {1, 2, 5}, 5)).isEqualTo(1);
  }

  @Test
  void singleDenominationDividingTheAmountEvenly() {
    assertThat(sut.coinChange(new int[] {5}, 15)).isEqualTo(3);
  }

  @Test
  void singleDenominationThatDoesNotDivideTheAmount() {
    assertThat(sut.coinChange(new int[] {5}, 12)).isEqualTo(-1);
  }

  @Test
  void reusesOneDenominationManyTimes() {
    assertThat(sut.coinChange(new int[] {1}, 4)).isEqualTo(4);
  }

  @Test
  void ignoresDenominationsLargerThanTheAmountButStillSolves() {
    assertThat(sut.coinChange(new int[] {1, 7}, 3)).isEqualTo(3);
  }

  @Test
  void returnsMinusOneWhenEveryDenominationExceedsTheAmount() {
    assertThat(sut.coinChange(new int[] {5, 6, 7}, 3)).isEqualTo(-1);
  }

  @Test
  void choosesTheOptimalCombinationOverTheGreedyOne() {
    assertThat(sut.coinChange(new int[] {1, 3, 4}, 6)).isEqualTo(2);
  }

  @Test
  void handlesUnorderedDenominations() {
    assertThat(sut.coinChange(new int[] {5, 2, 1}, 11)).isEqualTo(3);
  }

  @Test
  void handlesTheLargestAllowedAmount() {
    assertThat(sut.coinChange(new int[] {1, 2, 5}, 10000)).isEqualTo(2000);
  }

  @Test
  void hugeDenominationDoesNotOverflowAndFallsBackToSmallerCoins() {
    assertThat(sut.coinChange(new int[] {1, Integer.MAX_VALUE}, 3)).isEqualTo(3);
  }

  @Test
  void singleHugeDenominationCannotFormASmallAmount() {
    assertThat(sut.coinChange(new int[] {Integer.MAX_VALUE}, 5)).isEqualTo(-1);
  }
}
