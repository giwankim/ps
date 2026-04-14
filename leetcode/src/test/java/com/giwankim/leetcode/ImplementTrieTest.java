package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.ImplementTrie.Trie;
import org.junit.jupiter.api.Test;

class ImplementTrieTest {
  Trie sut = new Trie();

  // Step 1: insert a single word and search for it — baseline for
  // trie node creation and traversal.
  @Test
  void searchFindsInsertedWord() {
    sut.insert("apple");
    assertThat(sut.search("apple")).isTrue();
  }

  // Step 2: searching an empty trie returns false.
  @Test
  void searchReturnsFalseForAbsentWord() {
    assertThat(sut.search("apple")).isFalse();
  }

  // Step 3: "app" is a prefix of "apple" but was never inserted as a
  // complete word. Forces the end-of-word flag in each trie node.
  @Test
  void searchReturnsFalseForPrefixOfInsertedWord() {
    sut.insert("apple");
    assertThat(sut.search("app")).isFalse();
  }

  // Step 4: startsWith returns true for any prefix of an inserted
  // word — unlike search, it ignores the end-of-word flag.
  @Test
  void startsWithReturnsTrueForPrefix() {
    sut.insert("apple");
    assertThat(sut.startsWith("app")).isTrue();
  }

  // Step 5: startsWith returns false when no inserted word shares
  // the given prefix.
  @Test
  void startsWithReturnsFalseForAbsentPrefix() {
    sut.insert("apple");
    assertThat(sut.startsWith("ban")).isFalse();
  }

  // Step 6: startsWith also matches when the prefix equals the full
  // inserted word (every word is a prefix of itself).
  @Test
  void startsWithMatchesFullWord() {
    sut.insert("apple");
    assertThat(sut.startsWith("apple")).isTrue();
  }

  // Step 7: inserting "app" after "apple" marks the shorter path as a
  // complete word without corrupting the longer one.
  @Test
  void insertPrefixOfExistingWordMakesBothSearchable() {
    sut.insert("apple");
    sut.insert("app");
    assertThat(sut.search("app")).isTrue();
    assertThat(sut.search("apple")).isTrue();
  }

  // Step 8: canonical LeetCode example — interleaved insert, search,
  // and startsWith operations on the same trie instance.
  @Test
  void leetCodeExample() {
    sut.insert("apple");
    assertThat(sut.search("apple")).isTrue();
    assertThat(sut.search("app")).isFalse();
    assertThat(sut.startsWith("app")).isTrue();
    sut.insert("app");
    assertThat(sut.search("app")).isTrue();
  }
}
