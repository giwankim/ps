package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicateSubtrees {
  public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    List<TreeNode> result = new ArrayList<>();
    Map<String, Integer> counts = new HashMap<>();
    serializeTree(root, counts, result);
    return result;
  }

  private String serializeTree(TreeNode root, Map<String, Integer> counts, List<TreeNode> result) {
    if (root == null) {
      return "#";
    }

    // serialize the tree
    String s =
        new StringBuilder()
            .append(root.val)
            .append(",")
            .append(serializeTree(root.left, counts, result))
            .append(",")
            .append(serializeTree(root.right, counts, result))
            .toString();

    // increment the count for this serialized tree
    counts.put(s, counts.getOrDefault(s, 0) + 1);

    // when a duplicate is first encountered, store it in the result list
    if (counts.get(s) == 2) {
      result.add(root);
    }

    return s;
  }
}
