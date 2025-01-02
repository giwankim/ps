package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiagonalTraverseIITest {
  private DiagonalTraverseII sut;

  @BeforeEach
  void setUp() {
    sut = new DiagonalTraverseII();
  }

  @Test
  void square() {
    var nums = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));

    int[] actual = sut.findDiagonalOrder(nums);

    var expected = new int[] {1, 4, 2, 7, 5, 3, 8, 6, 9};
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void unevenShape() {
    var nums =
        List.of(
            List.of(1, 2, 3, 4, 5),
            List.of(6, 7),
            List.of(8),
            List.of(9, 10, 11),
            List.of(12, 13, 14, 15, 16));

    int[] actual = sut.findDiagonalOrder(nums);

    var expected = new int[] {1, 6, 2, 8, 7, 3, 9, 4, 12, 10, 5, 13, 11, 14, 15, 16};
    assertThat(actual).isEqualTo(expected);
  }
}
