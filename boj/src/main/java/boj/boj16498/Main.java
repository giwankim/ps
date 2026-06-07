package boj.boj16498;

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
      int na = Integer.parseInt(st.nextToken());
      int nb = Integer.parseInt(st.nextToken());
      int nc = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] a = new int[na];
      for (int i = 0; i < na; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }
      st = new StringTokenizer(r.readLine());
      int[] b = new int[nb];
      for (int i = 0; i < nb; i++) {
        b[i] = Integer.parseInt(st.nextToken());
      }
      st = new StringTokenizer(r.readLine());
      int[] c = new int[nc];
      for (int i = 0; i < nc; i++) {
        c[i] = Integer.parseInt(st.nextToken());
      }

      Arrays.sort(a);
      Arrays.sort(b);
      Arrays.sort(c);

      int[] all = new int[na + nb + nc];
      System.arraycopy(a, 0, all, 0, na);
      System.arraycopy(b, 0, all, na, nb);
      System.arraycopy(c, 0, all, na + nb, nc);

      int ans = Integer.MAX_VALUE;
      for (int m : all) {
        int ai = lowerBound(a, m);
        int bi = lowerBound(b, m);
        int ci = lowerBound(c, m);
        if (ai != -1 && bi != -1 && ci != -1) {
          ans = Math.min(ans, Math.max(a[ai], Math.max(b[bi], c[ci])) - m);
        }
      }
      pw.println(ans);
    }
  }

  private static int lowerBound(int[] arr, int target) {
    int result = -1;
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
}
