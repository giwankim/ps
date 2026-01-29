package com.giwankim.leetcode;

public class Trie {
  private final TrieNode root;

  public Trie() {
    root = new TrieNode();
  }

  public void insert(String word) {
    TrieNode cur = root;
    for (char c : word.toLowerCase().toCharArray()) {
      if (cur.children[c - 'a'] == null) {
        cur.children[c - 'a'] = new TrieNode();
      }
      cur = cur.children[c - 'a'];
    }
    cur.isWord = true;
  }

  public boolean search(String word) {
    TrieNode cur = root;
    for (char c : word.toLowerCase().toCharArray()) {
      if (cur.children[c - 'a'] == null) {
        return false;
      }
      cur = cur.children[c - 'a'];
    }
    return cur.isWord;
  }

  public boolean startsWith(String prefix) {
    TrieNode cur = root;
    for (char c : prefix.toLowerCase().toCharArray()) {
      if (cur.children[c - 'a'] == null) {
        return false;
      }
      cur = cur.children[c - 'a'];
    }
    return true;
  }

  public static class TrieNode {
    private boolean isWord;
    private final TrieNode[] children;

    public TrieNode() {
      this.isWord = false;
      this.children = new TrieNode[26];
    }
  }
}
