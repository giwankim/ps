package boj.boj11728;

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

      st = new StringTokenizer(r.readLine());
      int[] b = new int[m];
      for (int i = 0; i < m; i++) {
        b[i] = Integer.parseInt(st.nextToken());
      }

      int[] c = new int[n + m];
      int i = 0;
      int j = 0;
      int idx = 0;
      while (i < n && j < m) {
        if (a[i] < b[j]) {
          c[idx++] = a[i++];
        } else {
          c[idx++] = b[j++];
        }
      }
      while (i < n) {
        c[idx++] = a[i++];
      }
      while (j < m) {
        c[idx++] = b[j++];
      }

      StringBuilder sb = new StringBuilder();
      for (int x : c) {
        sb.append(x).append(' ');
      }
      pw.println(sb);
    }
  }
}
