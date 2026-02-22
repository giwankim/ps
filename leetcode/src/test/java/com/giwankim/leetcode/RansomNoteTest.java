package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RansomNoteTest {
  private RansomNote sut = new RansomNote();

  @Test
  void can() {
    String ransomNote = "a";
    String magazine = "ab";
    boolean actual = sut.canConstruct(ransomNote, magazine);
    assertThat(actual).isTrue();
  }

  @Test
  void cannot() {
    String ransomNote = "cd";
    String magazine = "ab";
    boolean actual = sut.canConstruct(ransomNote, magazine);
    assertThat(actual).isFalse();
  }

  @Test
  void canWithRepeatedCharacters() {
    String ransomNote = "aa";
    String magazine = "aab";
    boolean actual = sut.canConstruct(ransomNote, magazine);
    assertThat(actual).isTrue();
  }

  @Test
  void cannotBecauseRepeatedCharacters() {
    String ransomNote = "aa";
    String magazine = "abbb";
    boolean actual = sut.canConstruct(ransomNote, magazine);
    assertThat(actual).isFalse();
  }
}
