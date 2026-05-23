package boj.boj14465;

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
      int b = Integer.parseInt(st.nextToken());
      int[] broken = new int[n + 1];

      for (int i = 0; i < b; i++) {
        int idx = Integer.parseInt(r.readLine());
        broken[idx] = 1;
      }

      int result = 0;
      for (int i = 1; i <= k; i++) {
        result += broken[i];
      }
      int window = result;
      int start = 1;
      for (int i = k + 1; i <= n; i++) {
        window += broken[i] - broken[start];
        start++;
        result = Math.min(result, window);
      }
      pw.println(result);
    }
  }
}
