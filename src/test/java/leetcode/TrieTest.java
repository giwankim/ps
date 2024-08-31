package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrieTest {
  private Trie trie;

  @BeforeEach
  void setUp() {
    trie = new Trie();
  }

  @Test
  void insert() {
    trie.insert("apple");
    assertThat(trie.search("apple")).isTrue();
  }

  @Test
  void search() {
    trie.insert("apple");
    assertThat(trie.search("apple")).isTrue();
    assertThat(trie.search("app")).isFalse();
  }

  @Test
  void startsWith() {
    trie.insert("apple");
    assertThat(trie.startsWith("app")).isTrue();
  }

  @Test
  void trie() {
    trie.insert("apple");

    assertThat(trie.search("apple")).isTrue();
    assertThat(trie.search("app")).isFalse();
    assertThat(trie.startsWith("app")).isTrue();

    trie.insert("app");

    assertThat(trie.search("app")).isTrue();
  }
}
