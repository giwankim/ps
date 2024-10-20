package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SameTreeTest {
  @ParameterizedTest
  @MethodSource
  void isSameTree(TreeNode p, TreeNode q, boolean expected) {
    boolean actual = new SameTree().isSameTree(p, q);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isSameTree() {
    return Stream.of(
        Arguments.of(TreeNode.from(1, 2, 3), TreeNode.from(1, 2, 3), true),
        Arguments.of(TreeNode.from(1, 2), TreeNode.from(1, null, 2), false),
        Arguments.of(TreeNode.from(1, 2, 1), TreeNode.from(1, 1, 2), false));
  }
}
