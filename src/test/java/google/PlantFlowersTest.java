package google;

import static org.assertj.core.api.Assertions.assertThat;

import google.PlantFlowers;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class PlantFlowersTest {
  @ParameterizedTest
  @MethodSource
  void plant(int n, char[][] map, List<char[][]> expected) {
    List<char[][]> actual = new PlantFlowers().plant(n, map);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  private static Stream<Arguments> plant() {
    return Stream.of(
        Arguments.of(
            4,
            new char[][] {
              {'H', '0', '0', '0'}, {'H', '0', '0', '0'}, {'0', '0', '0', 'H'}, {'0', '0', '0', 'H'}
            },
            Collections.singletonList(
                new char[][] {
                  {'H', 'X', '0', '0'},
                  {'H', '0', '0', 'X'},
                  {'X', '0', '0', 'H'},
                  {'0', '0', 'X', 'H'}
                })),
        Arguments.of(
            4,
            new char[][] {
              {'0', '0', 'H', '0'}, {'0', '0', '0', '0'}, {'0', 'H', '0', '0'}, {'H', 'H', '0', '0'}
            },
            Collections.singletonList(
                new char[][] {
                  {'0', '0', 'H', 'X'},
                  {'0', 'X', '0', '0'},
                  {'X', 'H', '0', '0'},
                  {'H', 'H', 'X', '0'}
                })),
        Arguments.of(
            5,
            new char[][] {
              {'0', '0', '0', '0', 'H'},
              {'0', '0', '0', '0', '0'},
              {'0', '0', 'H', '0', 'H'},
              {'H', '0', '0', '0', '0'},
              {'H', '0', '0', '0', '0'}
            },
            List.of(
                new char[][] {
                  {'0', '0', '0', 'X', 'H'},
                  {'0', '0', 'X', '0', '0'},
                  {'X', '0', 'H', '0', 'H'},
                  {'H', '0', '0', '0', 'X'},
                  {'H', 'X', '0', '0', '0'}
                },
                new char[][] {
                  {'0', '0', '0', 'X', 'H'},
                  {'0', '0', '0', '0', 'X'},
                  {'X', '0', 'H', '0', 'H'},
                  {'H', '0', 'X', '0', '0'},
                  {'H', 'X', '0', '0', '0'}
                })));
  }
}
