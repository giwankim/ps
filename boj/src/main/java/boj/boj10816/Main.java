package boj.boj10816;

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

      Arrays.sort(a);

      StringBuilder ans = new StringBuilder();

      int m = Integer.parseInt(r.readLine());
      st = new StringTokenizer(r.readLine());
      while (m-- > 0) {
        int num = Integer.parseInt(st.nextToken());
        int lb = lowerBound(a, num);
        int ub = upperBound(a, num);
        if (lb == -1) {
          ans.append(0).append(' ');
          continue;
        }
        ans.append(ub - lb).append(' ');
      }
      pw.println(ans);
    }
  }

  private static int lowerBound(int[] a, int target) {
    int ans = -1;
    int lo = 0;
    int hi = a.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (a[mid] >= target) {
        ans = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return ans;
  }

  private static int upperBound(int[] a, int target) {
    int ans = a.length;
    int lo = 0;
    int hi = a.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (a[mid] > target) {
        ans = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return ans;
  }
}
