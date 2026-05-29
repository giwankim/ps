package boj.boj11660;

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
      int[][] matrix = new int[n][n];
      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(r.readLine());
        for (int j = 0; j < n; j++) {
          matrix[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      int[][] prefix = new int[n + 1][n + 1];
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
          prefix[i][j] =
              prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1] + matrix[i - 1][j - 1];
        }
      }

      while (m-- > 0) {
        st = new StringTokenizer(r.readLine());
        int x1 = Integer.parseInt(st.nextToken());
        int y1 = Integer.parseInt(st.nextToken());
        int x2 = Integer.parseInt(st.nextToken());
        int y2 = Integer.parseInt(st.nextToken());
        pw.println(
            prefix[x2][y2] - prefix[x2][y1 - 1] - prefix[x1 - 1][y2] + prefix[x1 - 1][y1 - 1]);
      }
    }
  }
}
