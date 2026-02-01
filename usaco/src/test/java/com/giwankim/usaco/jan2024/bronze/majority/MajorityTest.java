package com.giwankim.usaco.jan2024.bronze.majority;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.ResourceUtils;

class MajorityTest {
  @ParameterizedTest
  @MethodSource
  void majority(String input, String output) throws URISyntaxException, IOException {
    String prefix = "usaco/jan2024/bronze/majority/";
    Path inputPath = ResourceUtils.getPath(prefix + input);
    Path outputPath = ResourceUtils.getPath(prefix + output);
    StringWriter out = new StringWriter();
    BufferedReader r = Files.newBufferedReader(inputPath);
    PrintWriter pw = new PrintWriter(out);

    Majority.majority(r, pw);

    assertThat(out).hasToString(Files.readString(outputPath));
  }

  private static Stream<Arguments> majority() {
    return Stream.of(
        Arguments.of("1.in", "1.out"),
        Arguments.of("2.in", "2.out"),
        Arguments.of("3.in", "3.out"),
        Arguments.of("4.in", "4.out"),
        Arguments.of("5.in", "5.out"),
        Arguments.of("6.in", "6.out"),
        Arguments.of("7.in", "7.out"),
        Arguments.of("8.in", "8.out"),
        Arguments.of("9.in", "9.out"),
        Arguments.of("10.in", "10.out"),
        Arguments.of("11.in", "11.out"),
        Arguments.of("12.in", "12.out"),
        Arguments.of("13.in", "13.out"),
        Arguments.of("14.in", "14.out"),
        Arguments.of("15.in", "15.out"));
  }
}
