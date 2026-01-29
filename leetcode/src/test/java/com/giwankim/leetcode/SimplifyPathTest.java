package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SimplifyPathTest {
  private SimplifyPath sut;

  @BeforeEach
  void setUp() {
    sut = new SimplifyPath();
  }

  @ParameterizedTest
  @MethodSource
  void simplifyPath(String path, String expected) {
    String simplifiedPath = sut.simplifyPath(path);
    assertThat(simplifiedPath).isEqualTo(expected);
  }

  static Stream<Arguments> simplifyPath() {
    return Stream.of(
        Arguments.of("/home/", "/home"),
        Arguments.of("/home//foo", "/home/foo"),
        Arguments.of("/home/user/Documents/../Pictures", "/home/user/Pictures"),
        Arguments.of("/../", "/"),
        Arguments.of("/a/./b/../../c/", "/c"));
  }
}
