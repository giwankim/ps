package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordBreakTest {
  WordBreak sut = new WordBreak();

  @Test
  void segmentsIntoTwoDictionaryWords() {
    assertThat(sut.wordBreak("leetcode", List.of("leet", "code"))).isTrue();
    assertThat(sut.wordBreak2("leetcode", List.of("leet", "code"))).isTrue();
  }

  @Test
  void reusesTheSameDictionaryWord() {
    assertThat(sut.wordBreak("applepenapple", List.of("apple", "pen"))).isTrue();
    assertThat(sut.wordBreak2("applepenapple", List.of("apple", "pen"))).isTrue();
  }

  @Test
  void returnsFalseWhenTheTailCannotBeSegmented() {
    assertThat(sut.wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")))
        .isFalse();
    assertThat(sut.wordBreak2("catsandog", List.of("cats", "dog", "sand", "and", "cat")))
        .isFalse();
  }

  @Test
  void singleCharacterMatchingItsOnlyDictionaryWord() {
    assertThat(sut.wordBreak("a", List.of("a"))).isTrue();
    assertThat(sut.wordBreak2("a", List.of("a"))).isTrue();
  }

  @Test
  void singleCharacterAbsentFromTheDictionary() {
    assertThat(sut.wordBreak("a", List.of("b"))).isFalse();
    assertThat(sut.wordBreak2("a", List.of("b"))).isFalse();
  }

  @Test
  void entireStringIsExactlyOneDictionaryWord() {
    assertThat(sut.wordBreak("apple", List.of("apple"))).isTrue();
    assertThat(sut.wordBreak2("apple", List.of("apple"))).isTrue();
  }

  @Test
  void repeatsASingleCharacterWordToCoverTheString() {
    assertThat(sut.wordBreak("aaaa", List.of("a"))).isTrue();
    assertThat(sut.wordBreak2("aaaa", List.of("a"))).isTrue();
  }

  @Test
  void requiresMixingTwoDifferentWordLengths() {
    assertThat(sut.wordBreak("aaaaaaa", List.of("aaaa", "aaa"))).isTrue();
    assertThat(sut.wordBreak2("aaaaaaa", List.of("aaaa", "aaa"))).isTrue();
  }

  @Test
  void trailingCharacterCannotBeConsumed() {
    assertThat(sut.wordBreak("aaaab", List.of("a", "aa"))).isFalse();
    assertThat(sut.wordBreak2("aaaab", List.of("a", "aa"))).isFalse();
  }

  @Test
  void dictionaryWordLongerThanTheStringNeverMatches() {
    assertThat(sut.wordBreak("abc", List.of("abcd"))).isFalse();
    assertThat(sut.wordBreak2("abc", List.of("abcd"))).isFalse();
  }

  @Test
  void noDictionaryWordAppearsInTheString() {
    assertThat(sut.wordBreak("abc", List.of("x", "y", "z"))).isFalse();
    assertThat(sut.wordBreak2("abc", List.of("x", "y", "z"))).isFalse();
  }

  @Test
  void trailingCharacterPreventsSegmentation() {
    assertThat(sut.wordBreak("leetcodes", List.of("leet", "code"))).isFalse();
    assertThat(sut.wordBreak2("leetcodes", List.of("leet", "code"))).isFalse();
  }

  @Test
  void leadingCharacterPreventsSegmentation() {
    assertThat(sut.wordBreak("aleetcode", List.of("leet", "code"))).isFalse();
    assertThat(sut.wordBreak2("aleetcode", List.of("leet", "code"))).isFalse();
  }

  @Test
  void prefersTheLongerOverlappingWordWhenTheShorterDeadEnds() {
    assertThat(sut.wordBreak("goalspecial", List.of("go", "goal", "goals", "special")))
        .isTrue();
    assertThat(sut.wordBreak2("goalspecial", List.of("go", "goal", "goals", "special")))
        .isTrue();
  }

  @Test
  void emptyStringIsVacuouslySegmentable() {
    // Beyond LeetCode's stated 1 <= s.length constraint, but documents the empty-prefix base case.
    assertThat(sut.wordBreak("", List.of("a"))).isTrue();
    assertThat(sut.wordBreak2("", List.of("a"))).isTrue();
  }
}
