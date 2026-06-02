package boj.boj1687;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int m = Integer.parseInt(st.nextToken());
      int[][] grid = new int[n][m];
      for (int i = 0; i < n; i++) {
        String row = r.readLine();
        for (int j = 0; j < m; j++) {
          grid[i][j] = row.charAt(j) - '0';
        }
      }

      int[][] P = new int[n + 1][m + 1];
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
          P[i][j] = P[i][j - 1] + P[i - 1][j] - P[i - 1][j - 1] + grid[i - 1][j - 1];
        }
      }

      int ans = 0;
      for (int c1 = 1; c1 <= m; c1++) {
        for (int c2 = c1; c2 <= m; c2++) {
          int endingAt = 0;
          for (int i = 1; i <= n; i++) {
            int strip = P[i][c2] - P[i][c1 - 1] - P[i - 1][c2] + P[i - 1][c1 - 1];
            if (strip == 0) {
              endingAt += c2 - c1 + 1;
              ans = Math.max(ans, endingAt);
            } else {
              endingAt = 0;
            }
          }
        }
      }
      pw.println(ans);
    }
  }
}
