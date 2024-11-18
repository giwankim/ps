package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinaryTreeRightSideViewTest {

  @ParameterizedTest
  @MethodSource
  void rightSideView(TreeNode root, List<Integer> expected) {
    List<Integer> actual = new BinaryTreeRightSideView().rightSideView(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> rightSideView() {
    return Stream.of(
        Arguments.of(TreeNode.from(1, 2, 3, null, 5, null, 4), List.of(1, 3, 4)),
        Arguments.of(TreeNode.from(1, null, 3), List.of(1, 3)),
        Arguments.of(TreeNode.from(), Collections.emptyList()));
  }
}
