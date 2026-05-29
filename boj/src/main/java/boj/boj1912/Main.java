package boj.boj1912;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = Integer.parseInt(st.nextToken());
      }

      int ans = Integer.MIN_VALUE;
      int bestEndingAt = 0;
      for (int i = 0; i < n; i++) {
        bestEndingAt = Math.max(bestEndingAt + arr[i], arr[i]);
        ans = Math.max(ans, bestEndingAt);
      }
      pw.println(ans);
    }
  }
}
