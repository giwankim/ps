package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.DesignAddAndSearchWordsDataStructure.WordDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DesignAddAndSearchWordsDataStructureTest {
  WordDictionary sut;

  @BeforeEach
  void setUp() {
    sut = new WordDictionary();
  }

  @Test
  void searchOnEmptyDictionaryReturnsFalse() {
    assertThat(sut.search("cat")).isFalse();
  }

  @Test
  void searchReturnsTrueForExactAddedWord() {
    sut.addWord("cat");
    assertThat(sut.search("cat")).isTrue();
  }

  @Test
  void searchReturnsFalseForWordNotAdded() {
    sut.addWord("cat");
    assertThat(sut.search("dog")).isFalse();
  }

  @Test
  void searchReturnsFalseWhenQueryIsLongerThanAddedWord() {
    sut.addWord("cat");
    assertThat(sut.search("cats")).isFalse();
  }

  @Test
  void searchReturnsFalseWhenQueryIsShorterThanAddedWord() {
    sut.addWord("cats");
    assertThat(sut.search("cat")).isFalse();
  }

  @Test
  void searchFindsEachOfMultipleAddedWords() {
    sut.addWord("cat");
    sut.addWord("dog");
    assertThat(sut.search("cat")).isTrue();
    assertThat(sut.search("dog")).isTrue();
  }

  @Test
  void searchReturnsFalseForWordAmongOthers() {
    sut.addWord("cat");
    sut.addWord("dog");
    assertThat(sut.search("bird")).isFalse();
  }

  @Test
  void addingSameWordTwiceStillMatches() {
    sut.addWord("apple");
    sut.addWord("apple");
    assertThat(sut.search("apple")).isTrue();
  }

  @Test
  void singleDotMatchesSingleLetterWord() {
    sut.addWord("a");
    assertThat(sut.search(".")).isTrue();
  }

  @Test
  void dotMatchesCharacterInMiddleOfWord() {
    sut.addWord("bad");
    assertThat(sut.search("b.d")).isTrue();
  }

  @Test
  void allDotsMatchAnyWordOfEqualLength() {
    sut.addWord("cat");
    assertThat(sut.search("...")).isTrue();
  }

  @Test
  void dottedQueryOfWrongLengthDoesNotMatch() {
    sut.addWord("bad");
    assertThat(sut.search("b..d")).isFalse();
  }

  @Test
  void wildcardMatchesAtLeastOneOfMultipleWords() {
    sut.addWord("bad");
    sut.addWord("dad");
    sut.addWord("mad");
    assertThat(sut.search(".ad")).isTrue();
  }

  @Test
  void wildcardReturnsFalseWhenNoWordMatches() {
    sut.addWord("bad");
    sut.addWord("dad");
    assertThat(sut.search(".op")).isFalse();
  }

  @Test
  void leetcodeExample() {
    sut.addWord("bad");
    sut.addWord("dad");
    sut.addWord("mad");
    assertThat(sut.search("pad")).isFalse();
    assertThat(sut.search("bad")).isTrue();
    assertThat(sut.search(".ad")).isTrue();
    assertThat(sut.search("b..")).isTrue();
  }
}
