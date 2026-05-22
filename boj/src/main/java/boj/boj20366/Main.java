package boj.boj20366;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }

      Arrays.sort(a);

      int result = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
          int target = a[i] + a[j];
          int lo = 0;
          int hi = n - 1;
          while (lo < hi) {
            while (lo == i || lo == j) {
              lo++;
            }
            while (hi == i || hi == j) {
              hi--;
            }
            if (lo >= hi) {
              break;
            }
            int sum = a[lo] + a[hi];
            result = Math.min(result, Math.abs(target - sum));
            if (sum < target) {
              lo++;
            } else {
              hi--;
            }
          }
        }
      }
      pw.println(result);
    }
  }
}
