package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SerializeDeserializeBinaryTree {
  private static final String SEPARATOR = ",";
  private static final String NULL_PLACEHOLDER = "X";

  // Encodes a tree to a single string.
  public String serialize(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    serialize(root, sb);
    return sb.toString();
  }

  private void serialize(TreeNode root, StringBuilder sb) {
    if (root == null) {
      sb.append(NULL_PLACEHOLDER).append(SEPARATOR);
      return;
    }
    sb.append(root.val).append(SEPARATOR);
    serialize(root.left, sb);
    serialize(root.right, sb);
  }

  // Decodes your encoded data to tree.
  public TreeNode deserialize(String data) {
    Queue<String> tokens = new LinkedList<>(Arrays.asList(data.split(SEPARATOR)));
    return deserialize(tokens);
  }

  private TreeNode deserialize(Queue<String> tokens) {
    String token = tokens.poll();
    if (NULL_PLACEHOLDER.equals(token)) {
      return null;
    }
    TreeNode root = new TreeNode(Integer.parseInt(token));
    root.left = deserialize(tokens);
    root.right = deserialize(tokens);
    return root;
  }
}
