package usaco.jan2016.bronze.mowing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Mowing {
  private static final int[][] lastTime = new int[2001][2001];

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("mowing.in"));
        PrintWriter pw = new PrintWriter("mowing.out")) {
      mowing(r, pw);
    }
  }

  public static void mowing(BufferedReader r, PrintWriter pw) throws IOException {
    int N = Integer.parseInt(r.readLine());
    int x = 1000;
    int y = 1000;
    int t = 1;

    for (int[] row : lastTime) {
      Arrays.fill(row, -1);
    }
    lastTime[1000][1000] = 0;

    int ans = Integer.MAX_VALUE;

    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      String d = st.nextToken();
      int dx = 0;
      int dy = 0;
      int s = Integer.parseInt(st.nextToken());
      if ("N".equals(d)) {
        dx = -1;
      } else if ("S".equals(d)) {
        dx = 1;
      } else if ("E".equals(d)) {
        dy = 1;
      } else if ("W".equals(d)) {
        dy = -1;
      }
      while (s-- > 0) {
        x += dx;
        y += dy;
        if (lastTime[x][y] != -1) {
          ans = Math.min(ans, t - lastTime[x][y]);
        }
        lastTime[x][y] = t;
        t += 1;
      }
    }
    pw.println(ans == Integer.MAX_VALUE ? -1 : ans);
  }
}
