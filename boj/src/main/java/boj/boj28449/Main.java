package boj.boj28449;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int m = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }
      st = new StringTokenizer(r.readLine());
      int[] b = new int[m];
      for (int i = 0; i < m; i++) {
        b[i] = Integer.parseInt(st.nextToken());
      }

      Arrays.sort(a);
      Arrays.sort(b);

      long hi = 0;
      long arc = 0;
      long draw = 0;
      for (int i = 0; i < n; i++) {
        int lb = lowerBound(b, a[i]);
        int ub = upperBound(b, a[i]);
        hi += lb;
        arc += m - ub;
        draw += ub - lb;
      }
      pw.println(hi + " " + arc + " " + draw);
    }
  }

  private static int lowerBound(int[] arr, int target) {
    int result = arr.length;
    int lo = 0;
    int hi = arr.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (arr[mid] >= target) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }

  private static int upperBound(int[] arr, int target) {
    int result = arr.length;
    int lo = 0;
    int hi = arr.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (arr[mid] > target) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }
}
