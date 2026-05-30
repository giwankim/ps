package boj.boj14846;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int[][] matrix = new int[n][n];
      StringTokenizer st;
      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(r.readLine());
        for (int j = 0; j < n; j++) {
          matrix[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      // compute prefix sums
      int[][][] prefix = new int[n + 1][n + 1][11];
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
          for (int k = 1; k <= 10; k++) {
            prefix[i][j][k] = prefix[i - 1][j][k] + prefix[i][j - 1][k] - prefix[i - 1][j - 1][k];
            prefix[i][j][k] += matrix[i - 1][j - 1] == k ? 1 : 0;
          }
        }
      }

      int q = Integer.parseInt(r.readLine());
      while (q-- > 0) {
        st = new StringTokenizer(r.readLine());
        int x1 = Integer.parseInt(st.nextToken());
        int y1 = Integer.parseInt(st.nextToken());
        int x2 = Integer.parseInt(st.nextToken());
        int y2 = Integer.parseInt(st.nextToken());
        int ans = 0;
        for (int k = 1; k <= 10; k++) {
          if (prefix[x2][y2][k]
                  - prefix[x2][y1 - 1][k]
                  - prefix[x1 - 1][y2][k]
                  + prefix[x1 - 1][y1 - 1][k]
              > 0) {
            ans++;
          }
        }
        pw.println(ans);
      }
    }
  }
}
