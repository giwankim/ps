package boj.boj25332;

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
      int n = Integer.parseInt(r.readLine());

      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] A = new int[n];
      for (int i = 0; i < n; i++) {
        A[i] = Integer.parseInt(st.nextToken());
      }

      st = new StringTokenizer(r.readLine());
      int[] B = new int[n];
      for (int i = 0; i < n; i++) {
        B[i] = Integer.parseInt(st.nextToken());
      }

      int[] D = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        D[i] = D[i - 1] + A[i - 1] - B[i - 1];
      }

      Map<Integer, Long> prefixSumCounts = new HashMap<>();
      prefixSumCounts.put(0, 1L);
      long ans = 0;
      for (int i = 1; i <= n; i++) {
        ans += prefixSumCounts.getOrDefault(D[i], 0L);
        prefixSumCounts.merge(D[i], 1L, Long::sum);
      }
      pw.println(ans);
    }
  }
}
