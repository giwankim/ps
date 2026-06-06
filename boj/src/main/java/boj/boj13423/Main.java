package boj.boj13423;

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
      StringBuilder result = new StringBuilder();
      while (t-- > 0) {
        int n = Integer.parseInt(r.readLine());
        StringTokenizer st = new StringTokenizer(r.readLine());
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
          a[i] = Long.parseLong(st.nextToken());
        }

        Arrays.sort(a);

        int cnt = 0;
        for (int i = 0; i < n; i++) {
          for (int j = i + 1; j < n; j++) {
            long xa = a[i];
            long xb = a[j];
            if (Arrays.binarySearch(a, 2 * xb - xa) >= 0) {
              cnt++;
            }
          }
        }
        result.append(cnt).append('\n');
      }
      pw.print(result);
    }
  }
}
