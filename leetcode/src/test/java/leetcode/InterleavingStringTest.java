package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InterleavingStringTest {
  InterleavingString sut = new InterleavingString();

  // Step 1: smallest valid input — the empty interleaving of two empty strings (LeetCode Example 3)
  @Test
  void allEmptyStringsInterleave() {
    assertThat(sut.isInterleave("", "", "")).isTrue();
  }

  // Step 2: with s1 empty, the interleaving degenerates to s2 itself
  @Test
  void emptyS1MakesS3EqualS2() {
    assertThat(sut.isInterleave("", "abc", "abc")).isTrue();
  }

  // Step 3: symmetrically, with s2 empty the interleaving degenerates to s1 itself
  @Test
  void emptyS2MakesS3EqualS1() {
    assertThat(sut.isInterleave("abc", "", "abc")).isTrue();
  }

  // Step 4: the degenerate case still compares characters — a single wrong letter rejects
  @Test
  void emptyS1WithMismatchedS3IsRejected() {
    assertThat(sut.isInterleave("", "abc", "abd")).isFalse();
  }

  // Step 5: length guard — s3 is a valid prefix but leaves s2 unconsumed, so it is not an
  // interleaving; catches solutions that only walk s3 without requiring s1 and s2 to be exhausted
  @Test
  void s3ShorterThanCombinedLengthIsRejected() {
    assertThat(sut.isInterleave("a", "b", "a")).isFalse();
  }

  // Step 6: the other half of the length guard — s3 has characters no split can supply
  @Test
  void s3LongerThanCombinedLengthIsRejected() {
    assertThat(sut.isInterleave("a", "b", "aab")).isFalse();
    assertThat(sut.isInterleave("", "", "a")).isFalse();
  }

  // Step 7: one character from each source, taken in either order
  @Test
  void singleCharacterFromEachSourceInEitherOrder() {
    assertThat(sut.isInterleave("a", "b", "ab")).isTrue();
    assertThat(sut.isInterleave("a", "b", "ba")).isTrue();
  }

  // Step 8: s1 contributes its characters in order — a reversal is not an interleaving even though
  // the character multiset matches
  @Test
  void charactersOfS1MustKeepTheirRelativeOrder() {
    assertThat(sut.isInterleave("ab", "", "ba")).isFalse();
  }

  // Step 9: the same order constraint applies to s2 — "abdc" needs "dc" out of s2 = "cd"
  @Test
  void charactersOfS2MustKeepTheirRelativeOrder() {
    assertThat(sut.isInterleave("ab", "cd", "abdc")).isFalse();
  }

  // Step 10: splits may be empty, so plain concatenation in either direction is a legal
  // interleaving (n = m = 1)
  @Test
  void plainConcatenationIsAnInterleaving() {
    assertThat(sut.isInterleave("abc", "def", "abcdef")).isTrue();
    assertThat(sut.isInterleave("abc", "def", "defabc")).isTrue();
  }

  // Step 11: the opposite extreme — every split is one character long
  @Test
  void strictAlternationIsAnInterleaving() {
    assertThat(sut.isInterleave("abc", "def", "adbecf")).isTrue();
  }

  // Step 12: interior splits — s1 = "ab"+"cd"+"e" woven with s2 = "fg"+"hi"+"j"
  @Test
  void blockwiseInterleavingOfLongerStringsIsAccepted() {
    assertThat(sut.isInterleave("abcde", "fghij", "abfgcdhiej")).isTrue();
  }

  // Step 13: same sources as Step 12 with two s1 characters transposed ("dc" instead of "cd"),
  // which no split can produce
  @Test
  void transposedS1CharactersInLongerStringAreRejected() {
    assertThat(sut.isInterleave("abcde", "fghij", "abfgdchiej")).isFalse();
  }

  // Step 14: the greedy trap — matching both leading 'a's against s1 dead-ends, but taking one
  // from each source succeeds, so a two-pointer scan is not enough
  @Test
  void greedyFirstMatchDeadEndsWhereAnInterleavingExists() {
    assertThat(sut.isInterleave("aa", "ab", "aaba")).isTrue();
  }

  // Step 15: matching multisets are not sufficient — "abba" needs a 'b' after s1 is spent
  @Test
  void matchingMultisetWithImpossibleOrderIsRejected() {
    assertThat(sut.isInterleave("ab", "ab", "abba")).isFalse();
  }

  // Step 16: fully repeated characters interleave under every split
  @Test
  void repeatedIdenticalCharactersAlwaysInterleave() {
    assertThat(sut.isInterleave("aaa", "aaa", "aaaaaa")).isTrue();
  }

  // Step 17: correct length and a matching prefix, but the final character is unavailable
  @Test
  void unavailableTrailingCharacterIsRejected() {
    assertThat(sut.isInterleave("aaa", "aaa", "aaaaab")).isFalse();
  }

  // Step 18: LeetCode Example 1 — "aa" + "dbbc" + "bc" + "a" + "c"
  @Test
  void leetcodeExampleOneIsAnInterleaving() {
    assertThat(sut.isInterleave("aabcc", "dbbca", "aadbbcbcac")).isTrue();
  }

  // Step 19: LeetCode Example 2 — same sources, no split of s2 reaches this arrangement
  @Test
  void leetcodeExampleTwoIsNotAnInterleaving() {
    assertThat(sut.isInterleave("aabcc", "dbbca", "aadbbbaccc")).isFalse();
  }
}
