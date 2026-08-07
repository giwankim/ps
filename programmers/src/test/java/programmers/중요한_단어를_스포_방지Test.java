package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 중요한_단어를_스포_방지Test {
  중요한_단어를_스포_방지 sut = new 중요한_단어를_스포_방지();

  // Step 1: smallest possible input — a single word covered end to end. It is a spoiler word, it
  //         never appears in the clear, and nothing precedes it, so it is important.
  @Test
  void 단어_전체가_가려진_한_단어짜리_메시지는_1을_반환한다() {
    assertThat(sut.solution("secret", new int[][] {{0, 5}})).isEqualTo(1);
  }

  // Step 2: partial coverage still taints the whole word — only the 's' of "secret" is hidden, yet
  //         the entire word counts as a spoiler word.
  @Test
  void 단어의_한_글자만_가려져도_단어_전체가_스포_단어가_된다() {
    assertThat(sut.solution("muzi secret", new int[][] {{5, 5}})).isEqualTo(1);
  }

  // Step 3: a range may land entirely on a space, revealing no word at all.
  @Test
  void 공백만_가리는_구간은_어떤_단어도_공개하지_않아_0을_반환한다() {
    assertThat(sut.solution("ab cd", new int[][] {{2, 2}})).isEqualTo(0);
  }

  // Step 4: the clear occurrence sits *after* the hidden one, and still disqualifies it — the rule
  //         is "never appears in the clear", not "has not appeared yet".
  @Test
  void 뒤쪽에_가려지지_않은_같은_단어가_있으면_중요한_단어가_아니다() {
    assertThat(sut.solution("abc abc", new int[][] {{0, 2}})).isEqualTo(0);
  }

  // Step 5: the mirror image of Step 4 — the clear occurrence comes first.
  @Test
  void 앞쪽에_가려지지_않은_같은_단어가_있으면_중요한_단어가_아니다() {
    assertThat(sut.solution("abc abc", new int[][] {{4, 6}})).isEqualTo(0);
  }

  // Step 6: words are compared as whole tokens, not substrings — "category" in the clear must not
  //         disqualify the hidden "cat".
  @Test
  void 가려지지_않은_단어의_부분_문자열과_같아도_다른_단어이므로_중요한_단어다() {
    assertThat(sut.solution("cat category", new int[][] {{0, 2}})).isEqualTo(1);
  }

  // Step 7: one range can straddle a space and reveal two words at once, touching only part of
  // each.
  @Test
  void 한_구간이_두_단어에_걸치면_두_단어_모두_공개되어_2를_반환한다() {
    assertThat(sut.solution("abcd efgh", new int[][] {{2, 6}})).isEqualTo(2);
  }

  // Step 8: duplicates revealed by the *same* range are judged left to right, so the second "cat"
  //         collides with the first and only one is counted.
  @Test
  void 같은_구간에서_같은_단어가_두_번_공개되면_한_번만_센다() {
    assertThat(sut.solution("cat cat", new int[][] {{0, 6}})).isEqualTo(1);
  }

  // Step 9: a single word can straddle several ranges — "abcdef" is hidden by two disjoint ranges
  //         but is still one word.
  @Test
  void 한_단어가_여러_구간에_걸쳐도_한_단어로_센다() {
    assertThat(sut.solution("abcdef", new int[][] {{0, 0}, {5, 5}})).isEqualTo(1);
  }

  // Step 10: duplicates across *different* ranges — the later "cat" is already-revealed, and the
  //          clear "dog" between them is not a spoiler word at all.
  @Test
  void 다른_구간에서_같은_단어가_다시_공개되면_세지_않는다() {
    assertThat(sut.solution("cat dog cat", new int[][] {{0, 2}, {8, 10}})).isEqualTo(1);
  }

  // Step 11: official example 1 — "here" is disqualified by its clear twin later in the message,
  //          leaving only "secret".
  @Test
  void 공식_예제1_중요한_단어는_secret_하나뿐이라_1을_반환한다() {
    assertThat(sut.solution(
            "here is muzi here is a secret message", new int[][] {{0, 3}, {23, 28}}))
        .isEqualTo(1);
  }

  // Step 12: official example 2 — every rule fires at once: partial coverage ("phone"), a range
  //          spanning two words plus the space between them, an already-revealed duplicate
  //          ("phone" again) and a word disqualified by the clear region ("number").
  @Test
  void 공식_예제2_중요한_단어_네_개를_찾아_4를_반환한다() {
    assertThat(sut.solution(
            "my phone number is 01012345678 and may i have your phone number",
            new int[][] {{5, 5}, {25, 28}, {34, 40}, {53, 59}}))
        .isEqualTo(4);
  }

  // Step 13: scale — 1,000 distinct words with every other one hidden. No hidden word ever appears
  //          in the clear, so all 500 count; a per-spoiler-word rescan of the message would be
  //          quadratic here.
  @Test
  void 대규모_메시지에서_겹치지_않는_스포_단어_500개를_모두_센다() {
    StringBuilder message = new StringBuilder();
    List<int[]> ranges = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      if (i > 0) {
        message.append(' ');
      }
      int start = message.length();
      message.append(String.format("word%04d", i));
      if (i % 2 == 0) {
        ranges.add(new int[] {start, message.length() - 1});
      }
    }

    assertThat(sut.solution(message.toString(), ranges.toArray(new int[0][]))).isEqualTo(500);
  }

  // Steps 14-17 pin down the case Steps 1-13 left open: a spoiler range may cover a *space*.
  // Covering a space hides no word at all, so both neighbors keep whatever status they already
  // had — but any approach that masks the message and then re-splits it on " " merges the two
  // neighbors into a single token and silently loses them.

  // Step 14: minimal reproduction — the range covers the first "a" and the space after it. The
  //          second "a" is untouched, so it is a clear word and disqualifies the hidden one.
  @Test
  void 구간이_단어와_뒤따르는_공백을_함께_덮어도_다음_단어는_가려지지_않은_단어다() {
    assertThat(sut.solution("a a", new int[][] {{0, 1}})).isEqualTo(0);
  }

  // Step 15: a range covering nothing but a space reveals no word, and must not change either
  //          neighbor — "cat" and "dog" both stay clear, so the hidden "cat" is not important.
  @Test
  void 공백만_덮는_구간은_양옆_단어가_가려지지_않은_단어라는_사실을_바꾸지_않는다() {
    assertThat(sut.solution("cat dog cat", new int[][] {{3, 3}, {8, 10}})).isEqualTo(0);
  }

  // Step 16: the range spans a space plus the head of the next word, so "dog" is hidden while the
  //          preceding "cat" stays clear and still disqualifies the hidden "cat".
  @Test
  void 구간이_공백과_다음_단어의_앞부분을_덮어도_앞_단어는_가려지지_않은_단어다() {
    assertThat(sut.solution("cat dog cat", new int[][] {{3, 5}, {8, 10}})).isEqualTo(1);
  }

  // Step 17: one range swallows several words and the spaces around them — the same shape as
  //          official example 2's [34, 40]. The words just outside it stay clear, so the later
  //          hidden "and" is not important while "may" and "i" are.
  @Test
  void 여러_단어와_공백을_함께_덮는_구간의_바깥_단어는_가려지지_않은_단어다() {
    assertThat(sut.solution("and may i have and", new int[][] {{3, 9}, {15, 17}}))
        .isEqualTo(2);
  }
}
