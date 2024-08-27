package usaco.dec2022.bronze.feeding;

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

class FeedingTest {
  @ParameterizedTest
  @MethodSource
  void feeding(String in, String out) throws IOException, URISyntaxException {
    String prefix = "usaco/dec2022/bronze/feeding/";
    Path input = ResourceUtils.getPath(prefix + in);
    Path expected = ResourceUtils.getPath(prefix + out);

    BufferedReader r = Files.newBufferedReader(input);
    StringWriter writer = new StringWriter();
    PrintWriter pw = new PrintWriter(writer);

    Feeding.feeding(r, pw);

    assertThat(writer).hasToString(Files.readString(expected));
  }

  private static Stream<Arguments> feeding() {
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
        Arguments.of("12.in", "12.out"));
  }
}
