package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IPOTest {
  IPO sut = new IPO();

  @Test
  void noProjectEverAffordableReturnsInitialCapital() {
    assertThat(sut.findMaximizedCapital(4, 0, new int[] {5, 6, 7}, new int[] {1, 1, 1}))
        .isEqualTo(0);
    assertThat(sut.findMaximizedCapital2(4, 0, new int[] {5, 6, 7}, new int[] {1, 1, 1}))
        .isEqualTo(0);
  }

  @Test
  void singleProjectUnaffordableReturnsInitialCapital() {
    assertThat(sut.findMaximizedCapital(1, 2, new int[] {10}, new int[] {5})).isEqualTo(2);
    assertThat(sut.findMaximizedCapital2(1, 2, new int[] {10}, new int[] {5})).isEqualTo(2);
  }

  @Test
  void singleProjectWithCapitalExactlyEqualToRequirement() {
    assertThat(sut.findMaximizedCapital(1, 5, new int[] {10}, new int[] {5})).isEqualTo(15);
    assertThat(sut.findMaximizedCapital2(1, 5, new int[] {10}, new int[] {5})).isEqualTo(15);
  }

  @Test
  void stopsAtKEvenWhenMoreProjectsAvailable() {
    assertThat(sut.findMaximizedCapital(1, 0, new int[] {5, 4, 3, 2, 1}, new int[] {0, 0, 0, 0, 0}))
        .isEqualTo(5);
    assertThat(
            sut.findMaximizedCapital2(1, 0, new int[] {5, 4, 3, 2, 1}, new int[] {0, 0, 0, 0, 0}))
        .isEqualTo(5);
  }

  @Test
  void picksHighestProfitAmongAffordable() {
    assertThat(sut.findMaximizedCapital(1, 10, new int[] {1, 5, 3, 2}, new int[] {1, 1, 1, 1}))
        .isEqualTo(15);
    assertThat(sut.findMaximizedCapital2(1, 10, new int[] {1, 5, 3, 2}, new int[] {1, 1, 1, 1}))
        .isEqualTo(15);
  }

  @Test
  void allFreeProjectsPickTopKProfits() {
    assertThat(
            sut.findMaximizedCapital(3, 0, new int[] {10, 5, 1, 8, 3}, new int[] {0, 0, 0, 0, 0}))
        .isEqualTo(23);
    assertThat(
            sut.findMaximizedCapital2(3, 0, new int[] {10, 5, 1, 8, 3}, new int[] {0, 0, 0, 0, 0}))
        .isEqualTo(23);
  }

  @Test
  void kLargerThanProjectCountTakesEveryProject() {
    assertThat(sut.findMaximizedCapital(10, 0, new int[] {1, 2, 3}, new int[] {0, 1, 2}))
        .isEqualTo(6);
    assertThat(sut.findMaximizedCapital2(10, 0, new int[] {1, 2, 3}, new int[] {0, 1, 2}))
        .isEqualTo(6);
  }

  @Test
  void smallProjectUnlocksMuchLargerOne() {
    assertThat(sut.findMaximizedCapital(2, 1, new int[] {1, 100}, new int[] {1, 2}))
        .isEqualTo(102);
    assertThat(sut.findMaximizedCapital2(2, 1, new int[] {1, 100}, new int[] {1, 2}))
        .isEqualTo(102);
  }

  @Test
  void reEvaluatesAffordableProjectsAfterEachRound() {
    assertThat(sut.findMaximizedCapital(2, 0, new int[] {3, 1, 2}, new int[] {0, 1, 1}))
        .isEqualTo(5);
    assertThat(sut.findMaximizedCapital2(2, 0, new int[] {3, 1, 2}, new int[] {0, 1, 1}))
        .isEqualTo(5);
  }

  @Test
  void example1FromProblemStatement() {
    assertThat(sut.findMaximizedCapital(2, 0, new int[] {1, 2, 3}, new int[] {0, 1, 1}))
        .isEqualTo(4);
    assertThat(sut.findMaximizedCapital2(2, 0, new int[] {1, 2, 3}, new int[] {0, 1, 1}))
        .isEqualTo(4);
  }

  @Test
  void example2FromProblemStatement() {
    assertThat(sut.findMaximizedCapital(3, 0, new int[] {1, 2, 3}, new int[] {0, 1, 2}))
        .isEqualTo(6);
    assertThat(sut.findMaximizedCapital2(3, 0, new int[] {1, 2, 3}, new int[] {0, 1, 2}))
        .isEqualTo(6);
  }

  @Test
  void handlesMaxConstraintScaleWithoutOverflow() {
    assertThat(sut.findMaximizedCapital(
            1, 1_000_000_000, new int[] {10_000}, new int[] {1_000_000_000}))
        .isEqualTo(1_000_010_000);
    assertThat(sut.findMaximizedCapital2(
            1, 1_000_000_000, new int[] {10_000}, new int[] {1_000_000_000}))
        .isEqualTo(1_000_010_000);
  }

  @Test
  void zeroProfitProjectsDoNotChangeCapital() {
    assertThat(sut.findMaximizedCapital(3, 0, new int[] {0, 0, 5}, new int[] {0, 0, 0}))
        .isEqualTo(5);
    assertThat(sut.findMaximizedCapital2(3, 0, new int[] {0, 0, 5}, new int[] {0, 0, 0}))
        .isEqualTo(5);
  }
}
