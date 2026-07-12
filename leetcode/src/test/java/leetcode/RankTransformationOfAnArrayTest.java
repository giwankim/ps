package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RankTransformationOfAnArrayTest {
  RankTransformationOfAnArray sut = new RankTransformationOfAnArray();

  // Step 1: smallest valid input — the constraints allow length 0, so empty in means empty out
  @Test
  void emptyArrayReturnsEmptyArray() {
    assertThat(sut.arrayRankTransform(new int[] {})).isEmpty();
  }

  // Step 2: a single element is always rank 1, whatever its value
  @Test
  void singleElementGetsRankOne() {
    assertThat(sut.arrayRankTransform(new int[] {5})).containsExactly(1);
  }

  // Step 3: two ascending elements rank in order
  @Test
  void twoAscendingElementsRankInOrder() {
    assertThat(sut.arrayRankTransform(new int[] {10, 20})).containsExactly(1, 2);
  }

  // Step 4: ranks follow value, not position — the output keeps the original element order
  @Test
  void twoDescendingElementsKeepOriginalPositions() {
    assertThat(sut.arrayRankTransform(new int[] {20, 10})).containsExactly(2, 1);
  }

  // Step 5: equal elements share the same rank (LeetCode Example 2)
  @Test
  void allEqualElementsShareRankOne() {
    assertThat(sut.arrayRankTransform(new int[] {100, 100, 100})).containsExactly(1, 1, 1);
  }

  // Step 6: "rank should be as small as possible" — a duplicate does not consume a rank,
  // so 20 is rank 2, not 3
  @Test
  void duplicatesDoNotConsumeARank() {
    assertThat(sut.arrayRankTransform(new int[] {10, 10, 20})).containsExactly(1, 1, 2);
  }

  // Step 7: rank is ordinal — magnitude gaps between values do not widen the ranks
  @Test
  void magnitudeGapsDoNotAffectRanks() {
    assertThat(sut.arrayRankTransform(new int[] {7, 700, 70000})).containsExactly(1, 2, 3);
  }

  // Step 8: distinct unsorted values map to their sorted positions (LeetCode Example 1)
  @Test
  void distinctValuesRankBySortedPosition() {
    assertThat(sut.arrayRankTransform(new int[] {40, 10, 20, 30})).containsExactly(4, 1, 2, 3);
  }

  // Step 9: duplicates interleaved among distinct values — both 12s get rank 3 and the
  // ranks above them stay dense (LeetCode Example 3)
  @Test
  void interleavedDuplicatesStayDense() {
    assertThat(sut.arrayRankTransform(new int[] {37, 12, 28, 9, 100, 56, 80, 5, 12}))
        .containsExactly(5, 3, 4, 2, 8, 6, 7, 1, 3);
  }

  // Step 10: values span the full range (-10^9 <= arr[i] <= 10^9) — negatives rank below zero,
  // and the extremes rank without overflow
  @Test
  void rangeExtremesAndNegativesRankCorrectly() {
    assertThat(sut.arrayRankTransform(new int[] {-1000000000, 1000000000, 0}))
        .containsExactly(1, 3, 2);
  }
}
