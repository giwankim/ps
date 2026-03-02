package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GroupAnagramsTest {
  GroupAnagrams sut = new GroupAnagrams();

  @ParameterizedTest
  @ValueSource(strings = {"", "a"})
  void singleton(String str) {
    List<List<String>> actual = sut.groupAnagrams(new String[] {str});
    assertThat(actual).isEqualTo(List.of(List.of(str)));
  }

  @Test
  void groupAnagrams() {
    String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    List<List<String>> expected =
        List.of(List.of("bat"), List.of("nat", "tan"), List.of("ate", "eat", "tea"));

    List<List<String>> actual = sut.groupAnagrams(strs);

    actual.forEach(l -> l.sort(null));
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }
}
