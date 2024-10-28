package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinaryTreeInorderTraversalTest {

  @ParameterizedTest
  @MethodSource
  void inorderTraversal(TreeNode root, List<Integer> expected) {
    List<Integer> actual = new BinaryTreeInorderTraversal().inorderTraversal(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> inorderTraversal() {
    return Stream.of(
        Arguments.of(TreeNode.from(1, null, 2, null, null, 3), List.of(1, 3, 2)),
        Arguments.of(
            TreeNode.from(1, 2, 3, 4, 5, null, 8, null, null, 6, 7, null, null, 9),
            List.of(4, 2, 6, 5, 7, 1, 3, 9, 8)),
        Arguments.of(TreeNode.from(), Collections.emptyList()),
        Arguments.of(TreeNode.from(1), Collections.singletonList(1)));
  }
}
