package com.giwankim.leetcode;

public class DesignAddAndSearchWordsDataStructure {
  public static class WordDictionary {
    private final Node root;

    public WordDictionary() {
      root = new Node();
    }

    /**
     * Time complexity: {@code O(m)}. Space complexity: {@code O(m)}.
     *
     * <p>Where {@code m = word.length()}.
     */
    public void addWord(String word) {
      Node curr = root;
      for (char c : word.toCharArray()) {
        if (curr.children[c - 'a'] == null) {
          curr.children[c - 'a'] = new Node();
        }
        curr = curr.children[c - 'a'];
      }
      curr.isWord = true;
    }

    /**
     * Time complexity: {@code O(m * 26^k)}. Space complexity: {@code O(m)}.
     *
     * <p>Where k is number of . characters. {@code m = word.length()}. Without wildcards, runs in
     * {@code O(m)}.
     */
    public boolean search(String word) {
      return search(root, word, 0);
    }

    private boolean search(Node node, String word, int i) {
      if (node == null) {
        return false;
      }
      if (i == word.length()) {
        return node.isWord;
      }
      char c = word.charAt(i);
      if (c != '.') {
        return search(node.children[c - 'a'], word, i + 1);
      }
      for (int j = 0; j < 26; j++) {
        if (search(node.children[j], word, i + 1)) {
          return true;
        }
      }
      return false;
    }

    private static class Node {
      boolean isWord;
      Node[] children;

      public Node() {
        isWord = false;
        children = new Node[26];
      }
    }
  }
}
