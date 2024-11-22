package leetcode;

public class RotateImage {
  public void rotate(int[][] matrix) {
    // reverse rows
    for (int i = 0, j = matrix.length - 1; i < j; i++, j--) {
      int[] tmp = matrix[i];
      matrix[i] = matrix[j];
      matrix[j] = tmp;
    }

    // transpose
    for (int i = 0; i < matrix.length; i++) {
      for (int j = i + 1; j < matrix[i].length; j++) {
        int tmp = matrix[i][j];
        matrix[i][j] = matrix[j][i];
        matrix[j][i] = tmp;
      }
    }
  }
}
