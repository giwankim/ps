package boj.boj11659;

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
      int[] a = new int[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }

      int[] prefix = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] + a[i - 1];
      }

      while (m-- > 0) {
        st = new StringTokenizer(r.readLine());
        int i = Integer.parseInt(st.nextToken());
        int j = Integer.parseInt(st.nextToken());
        pw.println(prefix[j] - prefix[i - 1]);
      }
    }
  }
}
