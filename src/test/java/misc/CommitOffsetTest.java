package misc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CommitOffsetTest {
  private CommitOffset sut;

  @BeforeEach
  void setUp() {
    sut = new CommitOffset();
  }

  @ParameterizedTest
  @MethodSource
  void commitOffsets(int[] offsets, int[] expected) {
    int[] actual = sut.commitOffsets(offsets);
    assertThat(actual).isEqualTo(expected);
  }

  public static Stream<Arguments> commitOffsets() {
    return Stream.of(
        Arguments.of(
            new int[]{
                2, 0, 1,
            },
            new int[]{-1, 0, 2}),
        Arguments.of(new int[]{0, 1, 2}, new int[]{0, 1, 2}),
        Arguments.of(new int[]{2, 1, 0, 5, 4}, new int[]{-1, -1, 2, -1, -1}),
        Arguments.of(new int[]{2, 1, 0, 5, 4, 3}, new int[]{-1, -1, 2, -1, -1, 5}),
        Arguments.of(
            new int[]{2, 1, 0, 5, 4, 3, 9, 7, 6, 8},
            new int[]{-1, -1, 2, -1, -1, 5, -1, -1, 7, 9}),
        Arguments.of(
            new int[]{3, 0, 2, 4, 1, 7, 6, 5, 9}, new int[]{-1, 0, -1, -1, 4, -1, -1, 7, -1}));
  }
}
