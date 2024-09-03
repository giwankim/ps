package cses.sum.two.values;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.ResourceUtils;

class SumTwoValuesTest {
  @ParameterizedTest(name = "{index}")
  @MethodSource
  void sumTwoValues(Path input, Path output) throws IOException {
    BufferedReader r = Files.newBufferedReader(input);
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);
    String expected = Files.readString(output);

    SumTwoValues.sumTwoValues(r, pw);

    assertThat(out).hasToString(expected);
  }

  private static Stream<Arguments> sumTwoValues() {
    String prefix = "cses/sum/two/values/";
    return IntStream.rangeClosed(1, 27)
        .mapToObj(
            i -> {
              try {
                return Arguments.of(
                    ResourceUtils.getPath(prefix + i + ".in"),
                    ResourceUtils.getPath(prefix + i + ".out"));
              } catch (URISyntaxException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
