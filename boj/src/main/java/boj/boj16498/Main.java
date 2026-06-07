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

      int ans = Integer.MAX_VALUE;
      int i = 0;
      int j = 0;
      int k = 0;
      while (i < na && j < nb && k < nc) {
        int max = Math.max(a[i], Math.max(b[j], c[k]));
        int min = Math.min(a[i], Math.min(b[j], c[k]));
        ans = Math.min(ans, max - min);
        if (a[i] == min) {
          i++;
        } else if (b[j] == min) {
          j++;
        } else {
          k++;
        }
      }
      pw.println(ans);
    }
  }
}
