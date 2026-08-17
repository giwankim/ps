package grind75;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TwoSumTest {

  TwoSum sut = new TwoSum();

  @Test
  void pairAtTheFrontOfTheArray() {
    int[] nums = {2, 7, 11, 15};
    int target = 9;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 1);
  }

  @Test
  void doesNotReuseTheSameElement() {
    int[] nums = {3, 2, 4};
    int target = 6;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(1, 2);
  }

  @Test
  void duplicateValuesInMinimumLengthArray() {
    int[] nums = {3, 3};
    int target = 6;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 1);
  }

  @Test
  void repeatedValueResolvesToTwoDistinctIndices() {
    int[] nums = {5, 5, 11, 15};
    int target = 10;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 1);
  }

  @Test
  void pairAtTheEndOfTheArray() {
    int[] nums = {1, 2, 3, 4};
    int target = 7;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(2, 3);
  }

  @Test
  void pairStraddlesTheArray() {
    int[] nums = {8, 1, 5, 10, 20};
    int target = 28;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 4);
  }

  @Test
  void negativeValues() {
    int[] nums = {-3, 4, 3, 90};
    int target = 0;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 2);
  }

  @Test
  void negativeTarget() {
    int[] nums = {-1, -2, -3, -4};
    int target = -7;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(2, 3);
  }

  @Test
  void valuesAtTheConstraintBounds() {
    int[] nums = {1_000_000_000, -1_000_000_000, 5};
    int target = 0;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 1);
  }

  @Test
  void targetAtTheUpperConstraintBound() {
    int[] nums = {999_999_999, 1, 500_000_000};
    int target = 1_000_000_000;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(0, 1);
  }

  @Test
  void maximumLengthArray() {
    int n = 10_000;
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i + 1;
    }
    int target = 2 * n - 1;

    int[] actual = sut.twoSum(nums, target);

    assertThat(actual).containsExactlyInAnyOrder(n - 2, n - 1);
  }
}
