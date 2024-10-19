package in.the.wild.apple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestUniqueSubstringSetTest {

  @ParameterizedTest
  @MethodSource
  void longestUniqueSubstringSet(String s, List<String> expected) {
    List<String> actual = new LongestUniqueSubstringSet().longestUniqueSubstringSet(s);
    assertThat(actual).hasSize(expected.size()).containsAll(expected);
  }

  private static Stream<Arguments> longestUniqueSubstringSet() {
    return Stream.of(
        Arguments.of("abcbedfed", List.of("a", "bcb", "edfed")),
        Arguments.of("abcdefg", List.of("a", "b", "c", "d", "e", "f", "g")),
        Arguments.of("abmowodfsxadejihgepczpc", List.of("abmowodfsxad", "ejihge", "pczpc")));
  }
}
