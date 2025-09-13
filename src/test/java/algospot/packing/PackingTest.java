package algospot.packing;

import static algospot.packing.Main.pack;
import static algospot.packing.Main.reconstruct;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PackingTest {

  @ParameterizedTest
  @MethodSource
  void packing(int n, int capacity, String[] names, int[] volumes, int[] priorities, int expected) {
    int[][] cache = prepareCache(n, capacity);

    int actual = pack(capacity, 0, n, volumes, priorities, cache);

    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> packing() {
    return Stream.of(
        Arguments.of(
            6,
            10,
            new String[] {"laptop", "camera", "xbox", "grinder", "dumbbell", "encyclopedia"},
            new int[] {4, 2, 6, 4, 2, 10},
            new int[] {7, 10, 6, 7, 5, 4},
            24),
        Arguments.of(
            6,
            17,
            new String[] {"laptop", "camera", "xbox", "grinder", "dumbbell", "encyclopedia"},
            new int[] {4, 2, 6, 4, 2, 10},
            new int[] {7, 10, 6, 7, 5, 4},
            30));
  }

  @ParameterizedTest
  @MethodSource
  void reconstruction(
      int n, int capacity, String[] names, int[] volumes, int[] priorities, List<String> expected) {
    int[][] cache = prepareCache(n, capacity);

    List<String> picked = new ArrayList<>();

    reconstruct(capacity, 0, n, picked, names, volumes, priorities, cache);

    assertThat(picked).containsExactlyInAnyOrderElementsOf(expected);
  }

  static Stream<Arguments> reconstruction() {
    return Stream.of(
        Arguments.of(
            6,
            10,
            new String[] {"laptop", "camera", "xbox", "grinder", "dumbbell", "encyclopedia"},
            new int[] {4, 2, 6, 4, 2, 10},
            new int[] {7, 10, 6, 7, 5, 4},
            List.of("laptop", "camera", "grinder")),
        Arguments.of(
            6,
            17,
            new String[] {"laptop", "camera", "xbox", "grinder", "dumbbell", "encyclopedia"},
            new int[] {4, 2, 6, 4, 2, 10},
            new int[] {7, 10, 6, 7, 5, 4},
            List.of("laptop", "camera", "xbox", "grinder")));
  }

  static int[][] prepareCache(int n, int capacity) {
    int[][] cache = new int[capacity + 1][n];
    for (int[] row : cache) {
      Arrays.fill(row, -1);
    }
    return cache;
  }
}
