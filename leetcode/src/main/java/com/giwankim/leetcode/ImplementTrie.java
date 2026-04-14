package com.giwankim.leetcode;

public class ImplementTrie {
  public static class Trie {
    private final Node root;

    public Trie() {
      root = new Node();
    }

    public void insert(String word) {
      Node curr = root;
      for (char c : word.toCharArray()) {
        if (curr.children[c - 'a'] == null) {
          curr.children[c - 'a'] = new Node();
        }
        curr = curr.children[c - 'a'];
      }
      curr.isWord = true;
    }

    public boolean search(String word) {
      Node curr = root;
      for (char c : word.toCharArray()) {
        if (curr.children[c - 'a'] == null) {
          return false;
        }
        curr = curr.children[c - 'a'];
      }
      return curr.isWord;
    }

    public boolean startsWith(String prefix) {
      Node curr = root;
      for (char c : prefix.toCharArray()) {
        if (curr.children[c - 'a'] == null) {
          return false;
        }
        curr = curr.children[c - 'a'];
      }
      return true;
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
