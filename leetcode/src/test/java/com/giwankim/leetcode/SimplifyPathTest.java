package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SimplifyPathTest {
  SimplifyPath sut = new SimplifyPath();

  @ParameterizedTest
  @CsvSource(
      value = {
        "/home/:/home",
        "/home//foo:/home/foo",
        "/home/user/Documents/../Pictures:/home/user/Pictures",
        "/../:/",
        "/a/./b/../../c/:/c"
      },
      delimiter = ':')
  void simplifyPath(String path, String expected) {
    assertThat(sut.simplifyPath(path)).isEqualTo(expected);
  }
}
