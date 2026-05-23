package boj.boj7453;

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
      int[] b = new int[n];
      int[] c = new int[n];
      int[] d = new int[n];
      for (int i = 0; i < n; i++) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        a[i] = Integer.parseInt(st.nextToken());
        b[i] = Integer.parseInt(st.nextToken());
        c[i] = Integer.parseInt(st.nextToken());
        d[i] = Integer.parseInt(st.nextToken());
      }

      int[] ab = new int[n * n];
      int idx = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          ab[idx++] = a[i] + b[j];
        }
      }
      int[] cd = new int[n * n];
      idx = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          cd[idx++] = c[i] + d[j];
        }
      }

      Arrays.sort(ab);
      Arrays.sort(cd);

      long cnt = 0L;
      int lo = 0;
      int hi = cd.length - 1;
      while (lo < ab.length && hi >= 0) {
        int sum = ab[lo] + cd[hi];
        if (sum == 0) {
          int v1 = ab[lo];
          long cnt1 = 0L;
          while (lo < ab.length && ab[lo] == v1) {
            cnt1++;
            lo++;
          }
          int v2 = cd[hi];
          long cnt2 = 0L;
          while (hi >= 0 && cd[hi] == v2) {
            cnt2++;
            hi--;
          }
          cnt += cnt1 * cnt2;
        } else if (sum < 0) {
          lo++;
        } else {
          hi--;
        }
      }
      pw.println(cnt);
    }
  }
}
