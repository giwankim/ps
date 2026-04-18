package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinimumGeneticMutationTest {
  MinimumGeneticMutation sut = new MinimumGeneticMutation();

  // --- Edge cases ---

  @Test
  void startEqualsEnd() {
    assertThat(sut.minMutation("AACCGGTT", "AACCGGTT", new String[] {})).isZero();
  }

  @Test
  void emptyBank() {
    assertThat(sut.minMutation("AACCGGTT", "AACCGGTA", new String[] {})).isEqualTo(-1);
  }

  @Test
  void endGeneNotInBank() {
    assertThat(sut.minMutation("AACCGGTT", "AACCGGTA", new String[] {"AACCGGTC"}))
        .isEqualTo(-1);
  }

  // --- Single mutation ---

  @Test
  void singleMutation() {
    assertThat(sut.minMutation("AACCGGTT", "AACCGGTA", new String[] {"AACCGGTA"}))
        .isOne();
  }

  @Test
  void noMutationExists() {
    assertThat(sut.minMutation("AACCGGTT", "AACCGGTA", new String[] {"AACCGGTC"}))
        .isEqualTo(-1);
  }

  // --- Multi-step paths ---

  @Test
  void twoMutations() {
    // AACCGGTT → AACCGGTA (pos 7: T→A) → AAACGGTA (pos 2: C→A)
    assertThat(sut.minMutation("AACCGGTT", "AAACGGTA", new String[] {"AACCGGTA", "AAACGGTA"}))
        .isEqualTo(2);
  }

  @Test
  void threeMutations() {
    // AAAAACCC → AAAAACCA (pos 7) → AAAACCCA (pos 3) → AAACCCCA (pos 2)
    assertThat(sut.minMutation(
            "AAAAACCC", "AAACCCCA", new String[] {"AAAAACCA", "AAAACCCA", "AAACCCCA"}))
        .isEqualTo(3);
  }

  @Test
  void leetCodeExample2() {
    // AACCGGTT → AACCGGTA → AAACGGTA
    assertThat(sut.minMutation(
            "AACCGGTT", "AAACGGTA", new String[] {"AACCGGTA", "AACCGCTA", "AAACGGTA"}))
        .isEqualTo(2);
  }

  // --- Branching BFS ---

  @Test
  void multipleNeighborsAtSameLevel() {
    // Start has 3 neighbors at level 1; target is at level 2.
    // AAAAAAAA → {AAAAAAAC, AAAAAAAG, AAAAAAAT} → CAAAAAAC
    assertThat(sut.minMutation(
            "AAAAAAAA", "CAAAAAAC", new String[] {"AAAAAAAC", "AAAAAAAG", "AAAAAAAT", "CAAAAAAC"}))
        .isEqualTo(2);
  }

  @Test
  void shortestAmongMultiplePaths() {
    // Short path (2 steps): AACCGGTT → AACCGGTA → AAACGGTA
    // Long path (3 steps):  AACCGGTT → AACCGGTC → AACCGCTC → AAACGGTA (not valid, illustrative)
    // Bank includes extra genes that create branches but shortest path is still 2.
    assertThat(sut.minMutation(
            "AACCGGTT", "AAACGGTA", new String[] {"AACCGGTA", "AACCGGTC", "AACCGGCA", "AAACGGTA"}))
        .isEqualTo(2);
  }
}
