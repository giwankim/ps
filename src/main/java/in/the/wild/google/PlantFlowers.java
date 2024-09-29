package in.the.wild.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantFlowers {
  private static final int[] dx = {-1, 1, 0, 0};
  private static final int[] dy = {0, 0, -1, 1};

  private Home[] homes;
  private char[][] map;
  private boolean[] rows;
  private boolean[] cols;

  public List<char[][]> plant(int n, char[][] map) {
    homes = new Home[n];
    int cnt = 0;
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (map[i][j] == 'H') {
          homes[cnt] = new Home(i, j);
          cnt += 1;
        }
      }
    }
    List<char[][]> result = new ArrayList<>();
    this.map = map;
    this.rows = new boolean[n];
    this.cols = new boolean[n];
    plant(0, result);
    return result;
  }

  private void plant(int i, List<char[][]> result) {
    if (i == homes.length) {
      char[][] copy = Arrays.stream(map).map(char[]::clone).toArray(char[][]::new);
      result.add(copy);
      return;
    }
    Home home = homes[i];
    for (int d = 0; d < dx.length; d++) {
      int nx = home.x + dx[d];
      int ny = home.y + dy[d];
      if (isValid(nx, ny)) {
        map[nx][ny] = 'X';
        rows[nx] = true;
        cols[ny] = true;
        plant(i + 1, result);
        map[nx][ny] = '0';
        rows[nx] = false;
        cols[ny] = false;
      }
    }
  }

  private boolean isValid(int x, int y) {
    if (x < 0 || x >= map.length || y < 0 || y >= map[x].length) {
      return false;
    }
    if (map[x][y] != '0') {
      return false;
    }
    return !rows[x] && !cols[y];
  }

  private static class Home {
    int x;
    int y;

    public Home(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
