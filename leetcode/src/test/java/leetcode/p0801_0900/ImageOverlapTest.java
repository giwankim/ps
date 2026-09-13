package leetcode.p0801_0900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ImageOverlapTest {
  private final ImageOverlap sut = new ImageOverlap();

  @ParameterizedTest(name = "example {0}")
  @MethodSource("examples")
  void matchesOfficialExamples(int example, int[][] img1, int[][] img2, int expected) {
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(expected);
  }

  private static Stream<Arguments> examples() {
    return Stream.of(
        Arguments.of(
            1,
            new int[][] {{1, 1, 0}, {0, 1, 0}, {0, 1, 0}},
            new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 0, 1}},
            3),
        Arguments.of(2, new int[][] {{1}}, new int[][] {{1}}, 1),
        Arguments.of(3, new int[][] {{0}}, new int[][] {{0}}, 0));
  }

  @ParameterizedTest
  @CsvSource({"0, 1", "1, 0"})
  void differentSinglePixelsCannotOverlap(int first, int second) {
    assertThat(sut.largestOverlap(new int[][] {{first}}, new int[][] {{second}}))
        .isZero();
  }

  @ParameterizedTest
  @CsvSource({"0, 1", "1, 0"})
  void eitherImageWithoutOnesHasZeroOverlap(int first, int second) {
    assertThat(sut.largestOverlap(filledImage(3, first), filledImage(3, second)))
        .isZero();
  }

  @Test
  void zeroTranslationCanBeOptimalAndOnlyOnesCount() {
    int[][] img1 = {{1, 0, 1}, {0, 1, 0}, {1, 0, 0}};
    int[][] img2 = {{1, 0, 1}, {0, 1, 0}, {1, 0, 0}};

    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(4);
  }

  @ParameterizedTest(name = "row shift {0}, column shift {1}")
  @CsvSource({"-1, -1", "-1, 0", "-1, 1", "0, -1", "0, 1", "1, -1", "1, 0", "1, 1", "2, -1", "-1, 2"
  })
  void translatesTheWholeShapeInEveryDirection(int rowShift, int columnShift) {
    int[][] img1 = new int[5][5];
    img1[1][1] = 1;
    img1[1][2] = 1;
    img1[2][1] = 1;
    int[][] img2 = new int[5][5];
    img2[1 + rowShift][1 + columnShift] = 1;
    img2[1 + rowShift][2 + columnShift] = 1;
    img2[2 + rowShift][1 + columnShift] = 1;

    // The same three-pixel shape fits completely after the specified translation.
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(3);
  }

  @Test
  void doesNotRotateAHorizontalLineToMatchAVerticalLine() {
    int[][] img1 = {{1, 1, 1}, {0, 0, 0}, {0, 0, 0}};
    int[][] img2 = {{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};

    // A horizontal line and a vertical line can share at most one position.
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(1);
  }

  @Test
  void doesNotReflectAnAsymmetricShape() {
    int[][] img1 = {{1, 1, 0}, {1, 0, 0}, {0, 0, 0}};
    int[][] img2 = {{1, 1, 0}, {0, 1, 0}, {0, 0, 0}};

    // The top rows already overlap twice; translation cannot align all three ones.
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(2);
  }

  @Test
  void pixelsLeavingTheHorizontalBorderDoNotWrapAround() {
    int[][] img1 = {{1, 0, 1}, {0, 0, 0}, {0, 0, 0}};
    int[][] img2 = {{1, 1, 0}, {0, 0, 0}, {0, 0, 0}};

    // The source pixels are two columns apart; the target pixels are adjacent.
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(1);
  }

  @Test
  void pixelsLeavingTheVerticalBorderDoNotWrapAround() {
    int[][] img1 = {{1, 0, 0}, {0, 0, 0}, {1, 0, 0}};
    int[][] img2 = {{1, 0, 0}, {1, 0, 0}, {0, 0, 0}};

    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(1);
  }

  @Test
  void maximumSizeAllZeroImagesHaveZeroOverlap() {
    assertThat(sut.largestOverlap(new int[30][30], new int[30][30])).isZero();
  }

  @Test
  void maximumSizeAllOneImagesOverlapInAllNineHundredPositions() {
    assertThat(sut.largestOverlap(filledImage(30, 1), filledImage(30, 1))).isEqualTo(900);
  }

  @ParameterizedTest(name = "({0}, {1}) to ({2}, {3})")
  @CsvSource({"0, 0, 29, 29", "29, 29, 0, 0", "0, 29, 29, 0", "29, 0, 0, 29"})
  void singlePixelsOverlapAcrossTheMaximumDistance(
      int firstRow, int firstColumn, int secondRow, int secondColumn) {
    int[][] img1 = new int[30][30];
    img1[firstRow][firstColumn] = 1;
    int[][] img2 = new int[30][30];
    img2[secondRow][secondColumn] = 1;

    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(1);
  }

  @Test
  void maximumSizeComplementaryCheckerboardsNeedTranslationAndClipping() {
    int[][] img1 = new int[30][30];
    int[][] img2 = new int[30][30];
    for (int row = 0; row < 30; row++) {
      for (int column = 0; column < 30; column++) {
        img1[row][column] = (row + column) % 2;
        img2[row][column] = 1 - img1[row][column];
      }
    }

    // Zero shift matches nothing. A one-cell shift matches half of a 30-by-29 region;
    // larger shifts cannot retain a larger region, so the maximum is 435.
    assertThat(sut.largestOverlap(img1, img2)).isEqualTo(435);
  }

  private static int[][] filledImage(int n, int value) {
    int[][] image = new int[n][n];
    for (int[] row : image) {
      Arrays.fill(row, value);
    }
    return image;
  }
}
