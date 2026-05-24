package boj.boj9007;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int t = Integer.parseInt(r.readLine());
      while (t-- > 0) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        int k = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        int[][] weights = new int[4][n];
        for (int i = 0; i < 4; i++) {
          st = new StringTokenizer(r.readLine());
          for (int j = 0; j < n; j++) {
            weights[i][j] = Integer.parseInt(st.nextToken());
          }
        }

        int[] a = new int[n * n];
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            a[i * n + j] = weights[0][i] + weights[1][j];
          }
        }
        int[] b = new int[n * n];
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            b[i * n + j] = weights[2][i] + weights[3][j];
          }
        }

        Arrays.sort(a);
        Arrays.sort(b);

        int lo = 0;
        int hi = b.length - 1;
        int ans = a[lo] + b[hi];
        while (lo < a.length && hi >= 0) {
          int sum = a[lo] + b[hi];
          if (isCloser(sum, ans, k)) {
            ans = sum;
          }
          if (sum == k) {
            ans = sum;
            break;
          } else if (sum < k) {
            lo++;
          } else {
            hi--;
          }
        }
        pw.println(ans);
      }
    }
  }

  private static boolean isCloser(int sum, int best, int k) {
    int ds = Math.abs(k - sum);
    int db = Math.abs(k - best);
    return ds < db || (ds == db && sum < best);
  }
}
