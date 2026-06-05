package boj.boj27728;

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
      int q = Integer.parseInt(st.nextToken());
      int[][] a = new int[n][n];
      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(r.readLine());
        for (int j = 0; j < n; j++) {
          a[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      int[][] suf = new int[n][n + 1];
      for (int i = 0; i < n; i++) {
        for (int j = n - 1; j >= 0; j--) {
          suf[i][j] = suf[i][j + 1] + a[i][j];
        }
      }

      int[][] dp = new int[n][n];
      dp[0] = suf[0].clone();
      for (int i = 1; i < n; i++) {
        for (int j = 0; j < n; j++) {
          dp[i][j] = Math.min(dp[i - 1][j], suf[i][j]);
        }
      }

      StringBuilder result = new StringBuilder();
      while (q-- > 0) {
        st = new StringTokenizer(r.readLine());
        int sx = Integer.parseInt(st.nextToken()) - 1;
        int sy = Integer.parseInt(st.nextToken()) - 1;
        int l = Integer.parseInt(st.nextToken());
        result.append(query(a, suf, dp, sx, sy, l)).append('\n');
      }
      pw.print(result);
    }
  }

  private static int query(int[][] a, int[][] suf, int[][] dp, int sx, int sy, int l) {
    int n = a.length;
    int ans = suf[sx][sy];
    for (int col = sy; col < n; col++) {
      int currentRow = suf[sx][sy] - suf[sx][col];
      ans = Math.min(ans, currentRow + dp[sx - l][col]);
    }
    return ans;
  }
}
