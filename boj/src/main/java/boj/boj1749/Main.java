package boj.boj1749;

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
      int[][] matrix = new int[n][m];
      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(r.readLine());
        for (int j = 0; j < m; j++) {
          matrix[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      int[][] prefix = new int[n + 1][m + 1];
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
          prefix[i][j] =
              prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1] + matrix[i - 1][j - 1];
        }
      }

      int ans = Integer.MIN_VALUE;
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
          for (int k = 1; k <= i; k++) {
            for (int l = 1; l <= j; l++) {
              // (k, l)
              //
              //            (i, j)
              int sum = prefix[i][j] - prefix[i][l - 1] - prefix[k - 1][j] + prefix[k - 1][l - 1];
              ans = Math.max(ans, sum);
            }
          }
        }
      }
      pw.println(ans);
    }
  }
}
