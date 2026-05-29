package boj.boj2559;

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
      int k = Integer.parseInt(st.nextToken());
      int[] arr = new int[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        arr[i] = Integer.parseInt(st.nextToken());
      }

      int[] prefix = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] + arr[i - 1];
      }

      int ans = Integer.MIN_VALUE;
      for (int i = k; i <= n; i++) {
        ans = Math.max(ans, prefix[i] - prefix[i - k]);
      }
      pw.println(ans);
    }
  }
}
