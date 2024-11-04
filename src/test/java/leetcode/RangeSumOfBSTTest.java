package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RangeSumOfBSTTest {

  @ParameterizedTest
  @MethodSource
  void rangeSumBST(TreeNode root, int low, int high, int expected) {
    int actual = new RangeSumOfBST().rangeSumBST(root, low, high);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> rangeSumBST() {
    return Stream.of(
        Arguments.of(TreeNode.from(10, 5, 15, 3, 7, null, 18), 7, 15, 32),
        Arguments.of(TreeNode.from(10, 5, 15, 3, 7, 13, 18, 1, null, 6), 6, 10, 23));
  }
}
