package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GroupAnagramsTest {
  @ParameterizedTest
  @MethodSource
  void groupAnagrams(String[] strs, List<List<String>> expected) {
    List<List<String>> actual = new GroupAnagrams().groupAnagrams(strs);
    actual.forEach(l -> l.sort(null));
    assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
  }

  private static Stream<Arguments> groupAnagrams() {
    return Stream.of(
        Arguments.of(
            new String[] {"eat", "tea", "tan", "ate", "nat", "bat"},
            List.of(List.of("bat"), List.of("nat", "tan"), List.of("ate", "eat", "tea"))),
        Arguments.of(
            new String[] {
              "",
            },
            List.of(List.of(""))),
        Arguments.of(new String[] {"a"}, List.of((List.of("a")))));
  }
}
