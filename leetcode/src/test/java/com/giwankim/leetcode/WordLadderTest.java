package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordLadderTest {
  WordLadder sut = new WordLadder();

  // Step 1: degenerate empty word list — nothing can ever be reached.
  @Test
  void emptyWordListReturnsZero() {
    assertThat(sut.ladderLength("hit", "cog", List.of())).isEqualTo(0);
  }

  // Step 2: endWord must be present in the list; otherwise no sequence can end on it.
  @Test
  void endWordNotInWordListReturnsZero() {
    assertThat(sut.ladderLength("hit", "cog", List.of("hot", "dot", "dog"))).isEqualTo(0);
  }

  // Step 3: smallest positive — beginWord is one letter away from endWord.
  //   hit → hot
  @Test
  void singleStepTransformationReturnsTwo() {
    assertThat(sut.ladderLength("hit", "hot", List.of("hot"))).isEqualTo(2);
  }

  // Step 4: two-step transformation forces a real traversal, not just an adjacency check.
  //   hit → hot → hog
  @Test
  void twoStepTransformationReturnsThree() {
    assertThat(sut.ladderLength("hit", "hog", List.of("hot", "hog"))).isEqualTo(3);
  }

  // Step 5: endWord is in the list but has no bridge from beginWord — distinct from
  // the "endWord missing" case.
  //   hit (3 letters off from cog, no intermediates)
  @Test
  void unreachableEndWordReturnsZero() {
    assertThat(sut.ladderLength("hit", "cog", List.of("cog"))).isEqualTo(0);
  }

  // Step 6: classic LeetCode example 1 — two equally-short 5-word paths.
  //   hit → hot → dot → dog → cog
  //   hit → hot → lot → log → cog
  @Test
  void classicLeetcodeExampleReturnsFive() {
    assertThat(sut.ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")))
        .isEqualTo(5);
  }

  // Step 7: LeetCode example 2 — rich graph, but endWord absent from the list.
  @Test
  void classicLeetcodeExampleWithoutTargetReturnsZero() {
    assertThat(sut.ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log")))
        .isEqualTo(0);
  }

  // Step 8: beginWord appearing in wordList must not cause off-by-one or cycles.
  //   hit → hot (with "hit" also in the list)
  @Test
  void beginWordAlsoInWordListStillReturnsCorrectLength() {
    assertThat(sut.ladderLength("hit", "hot", List.of("hit", "hot"))).isEqualTo(2);
  }

  // Step 9: four-letter words — guards against any 3-letter hardcoding.
  //   cold → cord → word → ward → warm
  @Test
  void fourLetterWordsLadderReturnsFive() {
    assertThat(sut.ladderLength("cold", "warm", List.of("cord", "word", "ward", "warm")))
        .isEqualTo(5);
  }

  // Step 10: multiple paths exist — BFS must return the shortest.
  //   short: hit → hot → hog                    (length 3)
  //   long:  hit → bit → big → bog → hog        (length 5)
  @Test
  void returnsShortestWhenMultiplePathsExist() {
    assertThat(sut.ladderLength("hit", "hog", List.of("hot", "bit", "big", "bog", "hog")))
        .isEqualTo(3);
  }
}
