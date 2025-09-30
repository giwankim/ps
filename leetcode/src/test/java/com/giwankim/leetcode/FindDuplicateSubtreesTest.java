package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindDuplicateSubtreesTest {

  @Test
  void findDuplicates() {
    TreeNode duplicate = new TreeNode(2, new TreeNode(4), null);
    TreeNode root =
        new TreeNode(
            1, duplicate, new TreeNode(3, new TreeNode(2, new TreeNode(4), null), new TreeNode(4)));

    List<TreeNode> duplicates = new FindDuplicateSubtrees().findDuplicateSubtrees(root);

    List<TreeNode> expected = List.of(duplicate, duplicate.left);
    assertThat(duplicates).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void duplicateLeafNodes() {
    TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(1));

    List<TreeNode> duplicates = new FindDuplicateSubtrees().findDuplicateSubtrees(root);

    List<TreeNode> expected = List.of(root.left);
    assertThat(duplicates).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void duplicatesInDifferentBranches() {
    TreeNode left = new TreeNode(2, new TreeNode(3), null);
    TreeNode root = new TreeNode(2, left, new TreeNode(2, new TreeNode(3), null));

    List<TreeNode> duplicates = new FindDuplicateSubtrees().findDuplicateSubtrees(root);

    List<TreeNode> expected = List.of(left, left.left);
    assertThat(duplicates).containsExactlyInAnyOrderElementsOf(expected);
  }
}
