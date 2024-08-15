package com.giwankim.programmers;

@SuppressWarnings("NonAsciiCharacters")
public class 삼각_달팽이 {
  public int[] solution(int n) {
    int[][] a = new int[n][n];
    int curr = 1;
    int x = 0;
    int y = 0;

    while (true) {
      // 아래로 이동
      while (true) {
        a[y][x] = curr++;
        if (y + 1 == n || a[y + 1][x] != 0) {
          break;
        }
        y += 1;
      }
      if (x + 1 == n || a[y][x + 1] != 0) {
        break;
      }
      x += 1;

      // 오른쪽으로 이동
      while (true) {
        a[y][x] = curr++;
        if (x + 1 == n || a[y][x + 1] != 0) {
          break;
        }
        x += 1;
      }
      if (a[y - 1][x - 1] != 0) {
        break;
      }
      x -= 1;
      y -= 1;

      // 대각선으로 이동
      while (true) {
        a[y][x] = curr++;
        if (a[y - 1][x - 1] != 0) {
          break;
        }
        x -= 1;
        y -= 1;
      }
      if (y + 1 == n || a[y + 1][x] != 0) {
        break;
      }
      y += 1;
    }

    int index = 0;
    int[] result = new int[n * (n + 1) / 2];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j <= i; j++) {
        result[index++] = a[i][j];
      }
    }
    return result;
  }

  public static void print(int[][] a, int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j <= i; j++) {
        System.out.printf("%d, ", a[i][j]);
      }
      System.out.println();
    }
  }
}
