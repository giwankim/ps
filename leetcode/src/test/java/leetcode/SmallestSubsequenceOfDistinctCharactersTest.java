package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SmallestSubsequenceOfDistinctCharactersTest {
  SmallestSubsequenceOfDistinctCharacters sut = new SmallestSubsequenceOfDistinctCharacters();

  // Step 1: smallest valid input (length >= 1) — one character is its own answer
  @Test
  void singleCharacterReturnsItself() {
    assertThat(sut.smallestSubsequence("a")).isEqualTo("a");
  }

  // Step 2: "each distinct character exactly once" — duplicates collapse to a single copy
  @Test
  void allSameCharacterCollapsesToOne() {
    assertThat(sut.smallestSubsequence("aaaa")).isEqualTo("a");
  }

  // Step 3: distinct ascending input is already the lexicographically smallest arrangement
  @Test
  void sortedDistinctStringIsUnchanged() {
    assertThat(sut.smallestSubsequence("abc")).isEqualTo("abc");
  }

  // Step 4: a subsequence cannot reorder — with every character appearing once,
  //         even a descending string must be kept as-is
  @Test
  void descendingStringWithoutDuplicatesIsForced() {
    assertThat(sut.smallestSubsequence("cba")).isEqualTo("cba");
  }

  // Step 5: the core greedy trade — drop the leading 'b' because it reappears
  //         after the 'a', turning "ba" into the smaller "ab"
  @Test
  void leadingCharacterDroppedWhenItReappearsLater() {
    assertThat(sut.smallestSubsequence("bab")).isEqualTo("ab");
  }

  // Step 6: LeetCode Example 1 — 'a' pops both 'b' and 'c' in cascade since both recur
  @Test
  void leetCodeExample1() {
    assertThat(sut.smallestSubsequence("bcabc")).isEqualTo("abc");
  }

  // Step 7: a character already kept is skipped — the second 'a' must not
  //         disturb the growing result
  @Test
  void duplicateOfKeptCharacterIsIgnored() {
    assertThat(sut.smallestSubsequence("abacb")).isEqualTo("abc");
  }

  // Step 8: LeetCode Example 2 — popping is blocked when the kept character has no later
  //         occurrence: 'b' cannot evict 'd', so "acdb" beats the unreachable "abcd"
  @Test
  void leetCodeExample2() {
    assertThat(sut.smallestSubsequence("cbacdcbc")).isEqualTo("acdb");
  }

  // Step 9: the classic 316 case — the unique leading 'e' can never be evicted,
  //         while the later characters still reorder greedily behind it
  @Test
  void uniqueFirstCharacterAnchorsTheResult() {
    assertThat(sut.smallestSubsequence("ecbacba")).isEqualTo("eacb");
  }

  // Step 10: mixed unique and repeated characters — only the duplicate 'e's collapse;
  //          every unique character is locked into its original relative order
  @Test
  void mixedUniqueAndRepeatedCharacters() {
    assertThat(sut.smallestSubsequence("leetcode")).isEqualTo("letcod");
  }

  // Step 11: full alphabet descending, each letter once — nothing can be dropped or
  //          reordered, so the 26-character input survives intact
  @Test
  void fullDescendingAlphabetIsForced() {
    assertThat(sut.smallestSubsequence("zyxwvutsrqponmlkjihgfedcba"))
        .isEqualTo("zyxwvutsrqponmlkjihgfedcba");
  }

  // Step 12: the upper constraint bound (length 1000) — "ba" repeated still reduces
  //          to "ab", proving the drop logic holds at scale
  @Test
  void maximumLengthAlternatingPairReducesToTwoCharacters() {
    assertThat(sut.smallestSubsequence("ba".repeat(500))).isEqualTo("ab");
  }
}
