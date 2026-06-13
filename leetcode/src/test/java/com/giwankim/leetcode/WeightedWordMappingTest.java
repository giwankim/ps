package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class WeightedWordMappingTest {
  WeightedWordMapping sut = new WeightedWordMapping();

  // Step 1: smallest valid input — one one-letter word. With weight 1, the value is
  // 1 % 26 = 1, which maps to 'y' under the reverse alphabet (0 -> 'z', 1 -> 'y', ...).
  // Pins the mapping DIRECTION: a forward map (value -> 'a' + value) would emit 'b' here,
  // so this single assertion rules out the most common off-by-direction mistake.
  @Test
  void singleLetterMapsThroughReverseAlphabet() {
    int[] weights = uniformWeights(1);
    assertThat(sut.mapWordWeights(new String[] {"a"}, weights)).isEqualTo("y");
  }

  // Step 2: a letter other than 'a' must index its own weight slot via `c - 'a'`. The word
  // is "c" with weights['c'] = 3 while every other letter stays at 1, so the value is
  // 3 % 26 = 3 -> 'w'. An implementation that always reads weights[0] (or otherwise ignores
  // the character) would compute 1 -> 'y' and fail. Pins per-letter index arithmetic.
  @Test
  void nonFirstLetterIndexesItsOwnWeight() {
    int[] weights = uniformWeights(1);
    weights['c' - 'a'] = 3;
    assertThat(sut.mapWordWeights(new String[] {"c"}, weights)).isEqualTo("w");
  }

  // Step 3: a word's weight is the SUM of its characters' weights. "ab" with a=2, b=3 has
  // weight 5 -> 'u'. Distinguishes a correct sum from the two degenerate shortcuts:
  // first-char-only (2 -> 'x') and last-char-only (3 -> 'w'). Pins accumulation across the
  // characters of a single word.
  @Test
  void wordWeightIsSumOfCharacterWeights() {
    int[] weights = uniformWeights(1);
    weights['a' - 'a'] = 2;
    weights['b' - 'a'] = 3;
    assertThat(sut.mapWordWeights(new String[] {"ab"}, weights)).isEqualTo("u");
  }

  // Step 4: a word weight above 26 must be reduced modulo 26. "aa" with a=20 sums to 40;
  // 40 % 26 = 14 -> 'l'. Without the modulo, 'z' - 40 underflows past the lowercase range
  // (char 82 = 'R'), so the result would not even be a lowercase letter. Pins the `% 26`.
  @Test
  void weightWrapsModulo26() {
    int[] weights = uniformWeights(1);
    weights['a' - 'a'] = 20;
    assertThat(sut.mapWordWeights(new String[] {"aa"}, weights)).isEqualTo("l");
  }

  // Step 5: the zero endpoint of the mapping. A weight that is an exact multiple of 26
  // reduces to 0, the ONLY value that maps to 'z'. "aa" with a=13 sums to 26; 26 % 26 = 0
  // -> 'z'. Together with Step 4 this pins both that the modulo can produce 0 and that 0 is
  // the bottom of the reverse alphabet. A missing modulo would give 'z' - 26 = '`'.
  @Test
  void weightThatIsAMultipleOf26MapsToZ() {
    int[] weights = uniformWeights(1);
    weights['a' - 'a'] = 13;
    assertThat(sut.mapWordWeights(new String[] {"aa"}, weights)).isEqualTo("z");
  }

  // Step 6: the high endpoint of the mapping. Value 25 maps to 'a', the top of the reverse
  // alphabet. "a" with a=25 gives 25 % 26 = 25 -> 'a'. With Step 5 this nails down both
  // extremes of the 0..25 -> 'z'..'a' range, so no constant-offset error in the mapping can
  // survive both tests.
  @Test
  void value25MapsToA() {
    int[] weights = uniformWeights(1);
    weights['a' - 'a'] = 25;
    assertThat(sut.mapWordWeights(new String[] {"a"}, weights)).isEqualTo("a");
  }

  // Step 7: multiple words produce one character each, concatenated in input order. With
  // a=25: "a" -> 25 -> 'a', and "aa" -> 50, 50 % 26 = 24 -> 'b', giving "ab". A reversed
  // iteration would yield "ba" and a first-word-only shortcut would yield "a". Pins both the
  // per-word loop and the left-to-right ordering of the result.
  @Test
  void wordsAreMappedInOrder() {
    int[] weights = uniformWeights(1);
    weights['a' - 'a'] = 25;
    assertThat(sut.mapWordWeights(new String[] {"a", "aa"}, weights)).isEqualTo("ab");
  }

  // Step 8: LeetCode Example 1 — full end-to-end acceptance over distinct multi-letter words
  // with the problem's own weight vector. "abcd" (34 -> 8 -> 'r'), "def" (17 -> 17 -> 'i'),
  // "xyz" (16 -> 16 -> 'j') concatenate to "rij". Exercises summation, modulo, reverse
  // mapping, high-index letters ('x','y','z'), and ordering together.
  @Test
  void leetCodeExample1() {
    int[] weights = {
      5, 3, 12, 14, 1, 2, 3, 2, 10, 6, 6, 9, 7, 8, 7, 10, 8, 9, 6, 9, 9, 8, 3, 7, 7, 2
    };
    assertThat(sut.mapWordWeights(new String[] {"abcd", "def", "xyz"}, weights)).isEqualTo("rij");
  }

  // Step 9: LeetCode Example 2 — three different single letters all weighted 1 each reduce to
  // 1 -> 'y', so the output is "yyy". Confirms that the mapping depends only on the reduced
  // value, not on the identity of the letter, and that distinct words can share an output
  // character.
  @Test
  void leetCodeExample2() {
    int[] weights = uniformWeights(1);
    assertThat(sut.mapWordWeights(new String[] {"a", "b", "c"}, weights)).isEqualTo("yyy");
  }

  // Step 10: LeetCode Example 3 — a single four-letter word with the problem's weight vector.
  // "abcd" sums to 7 + 5 + 3 + 4 = 19; 19 % 26 = 19 -> 'g'. A compact acceptance case for the
  // single-word path with a sub-26 sum that needs no wrap.
  @Test
  void leetCodeExample3() {
    int[] weights = {7, 5, 3, 4, 3, 5, 4, 9, 4, 2, 2, 7, 10, 2, 5, 10, 6, 1, 2, 2, 4, 1, 3, 4, 4, 5
    };
    assertThat(sut.mapWordWeights(new String[] {"abcd"}, weights)).isEqualTo("g");
  }

  // Step 11: constraint ceiling — 100 words (max words.length), each 10 letters (max
  // words[i].length), every letter at weight 100 (max weights[i]). Each word sums to
  // 10 * 100 = 1000; 1000 % 26 = 12 -> 'n', so the result is 'n' repeated 100 times. Confirms
  // correct modulo on large per-word sums and that the output length tracks the word count at
  // the problem's upper bounds.
  @Test
  void handlesMaximumConstraintInput() {
    int[] weights = uniformWeights(100);
    String[] words = new String[100];
    Arrays.fill(words, "aaaaaaaaaa");
    assertThat(sut.mapWordWeights(words, weights)).isEqualTo("n".repeat(100));
  }

  private static int[] uniformWeights(int weight) {
    int[] weights = new int[26];
    Arrays.fill(weights, weight);
    return weights;
  }
}
