package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindTheLexicographicallySmallestValidSequenceTest {
  FindTheLexicographicallySmallestValidSequence sut =
      new FindTheLexicographicallySmallestValidSequence();

  // Step 1: smallest valid input (1 <= word2.length < word1.length) with an exact match waiting at
  // index 0
  @Test
  void singleCharacterWord2MatchesAtIndexZero() {
    assertThat(sut.validSequence("ab", "a")).containsExactly(0);
  }

  // Step 2: a one-character word2 always succeeds — even with no shared letter anywhere, the single
  // allowed change covers index 0
  @Test
  void singleCharacterWord2SpendsTheChangeOnIndexZero() {
    assertThat(sut.validSequence("zz", "a")).containsExactly(0);
  }

  // Step 3: LeetCode Example 4 — word2 is already a prefix of word1, so no change is needed
  @Test
  void leetCodeExample4TakesTheMatchingPrefix() {
    assertThat(sut.validSequence("abc", "ab")).containsExactly(0, 1);
  }

  // Step 4: the same shape one character longer — the unused trailing 'd' must not disturb the
  // answer
  @Test
  void matchingPrefixIgnoresTheUnusedTailOfWord1() {
    assertThat(sut.validSequence("abcd", "abc")).containsExactly(0, 1, 2);
  }

  // Step 5: every character matches everywhere, so the earliest indices win outright
  @Test
  void repeatedIdenticalCharactersTakeTheEarliestIndices() {
    assertThat(sut.validSequence("aaaa", "aaa")).containsExactly(0, 1, 2);
  }

  // Step 6: LeetCode Example 1 — index 0 mismatches ('v' vs 'a') but the suffix "bcca" still
  // supplies "bc", so the change is spent immediately
  @Test
  void leetCodeExample1SpendsTheChangeOnTheLeadingMismatch() {
    assertThat(sut.validSequence("vbcca", "abc")).containsExactly(0, 1, 2);
  }

  // Step 7: LeetCode Example 2 — the mirror image of Step 6: spending the change on index 0 would
  // leave only "acdc" to supply "bc" exactly, which fails, so index 0 must be skipped
  @Test
  void leetCodeExample2DeclinesTheChangeAtIndexZero() {
    assertThat(sut.validSequence("bacdc", "abc")).containsExactly(1, 2, 4);
  }

  // Step 8: the core trap — "ace" sits in "abcde" as an exact subsequence [0,2,4], yet burning the
  // change on index 1 yields the strictly smaller [0,1,4]; a solution that only spends the change
  // when forced fails here
  @Test
  void changeIsSpentEarlyEvenThoughAnExactSubsequenceExists() {
    assertThat(sut.validSequence("abcde", "ace")).containsExactly(0, 1, 4);
  }

  // Step 9: the same preference one character longer — exact match [0,1,3,4] loses to [0,1,2,4]
  @Test
  void longerWordStillPrefersTheEarlierIndexOverTheExactMatch() {
    assertThat(sut.validSequence("abcde", "abde")).containsExactly(0, 1, 2, 4);
  }

  // Step 10: the change is spent on the very next index after a matching prefix — [0,1] beats the
  // exact [0,2]
  @Test
  void changeIsSpentImmediatelyAfterAMatchingPrefix() {
    assertThat(sut.validSequence("acb", "ab")).containsExactly(0, 1);
  }

  // Step 11: filler characters between the matches — the change goes on the first filler at index 1
  // rather than on the exact 'b' at index 2, because "bxc" can still supply the trailing "c"
  @Test
  void changeIsSpentOnTheFirstFillerCharacter() {
    assertThat(sut.validSequence("axbxc", "abc")).containsExactly(0, 1, 4);
  }

  // Step 12: a leading mismatch is usable when what follows still spells the rest of word2 exactly
  @Test
  void leadingMismatchIsUsableWhenTheSuffixStillFits() {
    assertThat(sut.validSequence("cab", "ab")).containsExactly(0, 2);
  }

  // Step 13: the guard binds — "bb" cannot supply "a" exactly, so index 0 is unusable and the
  // change has to be saved for the final position
  @Test
  void leadingMismatchIsRejectedWhenTheSuffixCannotFinish() {
    assertThat(sut.validSequence("zbb", "ba")).containsExactly(1, 2);
  }

  // Step 14: the guard binds mid-scan — index 1 looks spendable, but it would consume word2's 'b'
  // and leave "bz" unable to supply the trailing 'c', so the change waits for that 'c' instead
  @Test
  void changeIsWithheldMidScanAndSpentOnTheFinalCharacter() {
    assertThat(sut.validSequence("azbz", "abc")).containsExactly(0, 2, 3);
  }

  // Step 15: word2's last character appears nowhere in word1, so the change lands on the last pick
  @Test
  void changeLandsOnTheLastPickWhenWord2EndsWithAnAbsentCharacter() {
    assertThat(sut.validSequence("abzz", "abc")).containsExactly(0, 1, 2);
  }

  // Step 16: a longer scan with the same tension — index 2 is a tempting mismatch, but spending
  // there strands the trailing "bc", so the change is held back for the absent 'c' at index 5
  @Test
  void longerScanWithholdsTheChangeUntilTheUnmatchableCharacter() {
    assertThat(sut.validSequence("aaabbb", "aabbc")).containsExactly(0, 1, 3, 4, 5);
  }

  // Step 17: word2 is exactly one character shorter than word1 and matches its prefix outright
  @Test
  void word2OneCharacterShorterThanWord1TakesTheWholePrefix() {
    assertThat(sut.validSequence("abcabc", "abcab")).containsExactly(0, 1, 2, 3, 4);
  }

  // Step 18: LeetCode Example 3 — 'b' and 'c' are both absent from "aaaaaa", so two changes would
  // be required and the answer is the empty array (not null)
  @Test
  void leetCodeExample3ReturnsEmptyWhenTwoChangesWouldBeNeeded() {
    assertThat(sut.validSequence("aaaaaa", "aaabc")).isEmpty();
  }

  // Step 19: two copies of an absent character exhaust the one-change budget even though the rest
  // of word2 matches comfortably
  @Test
  void twoOccurrencesOfAnAbsentCharacterReturnEmpty() {
    assertThat(sut.validSequence("aaabbb", "aabcc")).isEmpty();
  }

  // Step 20: no character is shared at all, so every pick would be a change
  @Test
  void disjointAlphabetsReturnEmpty() {
    assertThat(sut.validSequence("abc", "xy")).isEmpty();
  }

  // Step 21: constraint ceiling (word1.length == 3 * 10^5) built so the guard binds at index 0:
  // word1's only 'b' sits there, and word2 ends in 'b', so the change must be saved for the final
  // pick and the answer shifts to [1 .. word2.length]. Anything worse than a linear scan with a
  // precomputed suffix table will not finish in time.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void ceilingSizedInputWithdrawsTheChangeAtIndexZero() {
    int n = 300_000;
    int m = n - 2;
    String word1 = "b" + "a".repeat(n - 1);
    String word2 = "a".repeat(m - 1) + "b";
    assertThat(sut.validSequence(word1, word2))
        .containsExactly(IntStream.rangeClosed(1, m).toArray());
  }
}
