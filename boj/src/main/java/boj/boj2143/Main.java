package boj.boj2143;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int t = Integer.parseInt(r.readLine());

      int n = Integer.parseInt(r.readLine());
      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] A = new int[n];
      for (int i = 0; i < n; i++) {
        A[i] = Integer.parseInt(st.nextToken());
      }

      int m = Integer.parseInt(r.readLine());
      st = new StringTokenizer(r.readLine());
      int[] B = new int[m];
      for (int i = 0; i < m; i++) {
        B[i] = Integer.parseInt(st.nextToken());
      }

      // prefix sums
      int[] PA = new int[n + 1];
      for (int i = 0; i < n; i++) {
        PA[i + 1] = PA[i] + A[i];
      }
      int[] PB = new int[m + 1];
      for (int i = 0; i < m; i++) {
        PB[i + 1] = PB[i] + B[i];
      }

      // count partial sums
      Map<Integer, Integer> countsB = new HashMap<>();
      for (int i = 1; i <= m; i++) {
        for (int j = i; j <= m; j++) {
          countsB.merge(PB[j] - PB[i - 1], 1, Integer::sum);
        }
      }

      long ans = 0;
      for (int i = 1; i <= n; i++) {
        for (int j = i; j <= n; j++) {
          int sumA = PA[j] - PA[i - 1];
          ans += countsB.getOrDefault(t - sumA, 0);
        }
      }
      pw.println(ans);
    }
  }
}
