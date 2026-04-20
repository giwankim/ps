package com.giwankim.leetcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordSearchII {
  static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  /**
   * @implNote Time {@code O(W + m * n * 4 * 3^(L - 1))}, space {@code O(W)}.
   *     <p>{@code m} x {@code n} = board dimensions, {@code L} = length of the
   *     longest word, {@code W} = sum of lengths of all words.
   */
  public List<String> findWords(char[][] board, String[] words) {
    Set<String> result = new HashSet<>();
    TrieNode root = TrieNode.of(words);
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        TrieNode nextNode = root.children[board[i][j] - 'a'];
        findWords(nextNode, i, j, board, new StringBuilder(), result);
      }
    }
    return List.copyOf(result);
  }

  private void findWords(
      TrieNode node, int i, int j, char[][] board, StringBuilder current, Set<String> result) {
    if (board[i][j] == '#') {
      return;
    }
    if (node == null) {
      return;
    }

    current.append(board[i][j]);
    if (node.isWord) {
      result.add(current.toString());
    }

    char c = board[i][j];
    board[i][j] = '#';
    for (int[] direction : DIRECTIONS) {
      int ni = i + direction[0];
      int nj = j + direction[1];
      if (ni < 0 || ni >= board.length || nj < 0 || nj >= board[i].length || board[ni][nj] == '#') {
        continue;
      }
      findWords(node.children[board[ni][nj] - 'a'], ni, nj, board, current, result);
    }
    board[i][j] = c;
    current.deleteCharAt(current.length() - 1);
  }

  private static class TrieNode {
    boolean isWord = false;
    TrieNode[] children = new TrieNode[26];

    static TrieNode of(String[] words) {
      TrieNode root = new TrieNode();
      for (String word : words) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
          if (curr.children[c - 'a'] == null) {
            curr.children[c - 'a'] = new TrieNode();
          }
          curr = curr.children[c - 'a'];
        }
        curr.isWord = true;
      }
      return root;
    }
  }
}
