package programmers;

@SuppressWarnings("NonAsciiCharacters")
public class 삼각_달팽이 {
  private static final int[] dx = {0, 1, -1};
  private static final int[] dy = {1, 0, -1};

  public int[] solution(int n) {
    int[][] a = new int[n][n];
    int v = 1;
    int x = 0;
    int y = 0;
    int d = 0;

    while (true) {
      a[y][x] = v++;
      int nx = x + dx[d];
      int ny = y + dy[d];
      if (nx == n || ny == n || nx == -1 || ny == -1 || a[ny][nx] != 0) {
        d = (d + 1) % 3;
        nx = x + dx[d];
        ny = y + dy[d];
        if (nx == n || ny == n || nx == -1 || ny == -1 || a[ny][nx] != 0) {
          break;
        }
      }
      x = nx;
      y = ny;
    }

    int[] result = new int[v - 1];
    int index = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j <= i; j++) {
        result[index++] = a[i][j];
      }
    }
    return result;
  }
}
