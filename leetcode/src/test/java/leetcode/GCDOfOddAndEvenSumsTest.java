package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GCDOfOddAndEvenSumsTest {
  GCDOfOddAndEvenSums sut = new GCDOfOddAndEvenSums();

  // Step 1: constraint minimum n = 1 — sumOdd = 1, sumEven = 2; consecutive integers
  //         are coprime, so GCD(1, 2) = 1
  @Test
  void singleTermSumsAreCoprime() {
    assertThat(sut.gcdOfOddEvenSums(1)).isEqualTo(1);
  }

  // Step 2: n = 2 — sumOdd = 1 + 3 = 4, sumEven = 2 + 4 = 6; GCD(4, 6) = 2
  @Test
  void twoTermSumsShareFactorTwo() {
    assertThat(sut.gcdOfOddEvenSums(2)).isEqualTo(2);
  }

  // Step 3: n = 3 — sumOdd = 1 + 3 + 5 = 9, sumEven = 2 + 4 + 6 = 12; GCD(9, 12) = 3
  @Test
  void threeTermSumsShareFactorThree() {
    assertThat(sut.gcdOfOddEvenSums(3)).isEqualTo(3);
  }

  // Step 4: LeetCode Example 1 — sumOdd = 1 + 3 + 5 + 7 = 16, sumEven = 2 + 4 + 6 + 8 = 20;
  //         GCD(16, 20) = 4
  @Test
  void leetCodeExample1() {
    assertThat(sut.gcdOfOddEvenSums(4)).isEqualTo(4);
  }

  // Step 5: LeetCode Example 2 — sumOdd = 1 + 3 + 5 + 7 + 9 = 25,
  //         sumEven = 2 + 4 + 6 + 8 + 10 = 30; GCD(25, 30) = 5
  @Test
  void leetCodeExample2() {
    assertThat(sut.gcdOfOddEvenSums(5)).isEqualTo(5);
  }

  // Step 6: prime n = 7 — sumOdd = 7^2 = 49, sumEven = 7 * 8 = 56; the prime itself is the
  //         only shared factor, GCD(49, 56) = 7
  @Test
  void primeTermCountDividesBothSums() {
    assertThat(sut.gcdOfOddEvenSums(7)).isEqualTo(7);
  }

  // Step 7: composite n = 12 — sumOdd = 144, sumEven = 156 = 12 * 13; GCD(144, 156) = 12,
  //         not any larger multiple of 12's factors
  @Test
  void compositeTermCountIsTheFullSharedFactor() {
    assertThat(sut.gcdOfOddEvenSums(12)).isEqualTo(12);
  }

  // Step 8: constraint maximum n = 1000 — sumOdd = 1_000_000, sumEven = 1_001_000;
  //         GCD stays exactly n and both sums fit comfortably in int
  @Test
  void maximumConstraintStaysWithinInt() {
    assertThat(sut.gcdOfOddEvenSums(1000)).isEqualTo(1000);
  }
}
