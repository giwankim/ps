package boj.boj2309;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int[] a = new int[9];
      int total = 0;
      for (int i = 0; i < a.length; i++) {
        a[i] = Integer.parseInt(r.readLine());
        total += a[i];
      }

      Arrays.sort(a);

      int target = total - 100;
      int lo = 0;
      int hi = a.length - 1;
      while (lo < hi) {
        int sum = a[lo] + a[hi];
        if (sum == target) {
          break;
        } else if (sum < target) {
          lo++;
        } else {
          hi--;
        }
      }

      for (int x : a) {
        if (x != a[lo] && x != a[hi]) {
          pw.println(x);
        }
      }
    }
  }
}
