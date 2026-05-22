package boj.boj3273;

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
      int n = Integer.parseInt(r.readLine());
      int[] a = new int[n];
      StringTokenizer st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }
      int x = Integer.parseInt(r.readLine());

      Arrays.sort(a);

      int result = 0;
      int lo = 0;
      int hi = n - 1;
      while (lo < hi) {
        int sum = a[lo] + a[hi];
        if (sum == x) {
          result += 1;
          lo += 1;
          hi -= 1;
        } else if (sum < x) {
          lo += 1;
        } else {
          hi -= 1;
        }
      }
      pw.println(result);
    }
  }
}
