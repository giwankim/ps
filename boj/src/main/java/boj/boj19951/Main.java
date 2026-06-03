package boj.boj19951;

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

      int[] A = new int[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        A[i] = Integer.parseInt(st.nextToken());
      }

      int[] P = new int[n + 1];
      while (m-- > 0) {
        st = new StringTokenizer(r.readLine());
        int a = Integer.parseInt(st.nextToken()) - 1;
        int b = Integer.parseInt(st.nextToken()) - 1;
        int k = Integer.parseInt(st.nextToken());
        P[a] += k;
        P[b + 1] -= k;
      }

      for (int i = 1; i <= n; i++) {
        P[i] += P[i - 1];
      }

      for (int i = 0; i < n; i++) {
        pw.print(A[i] + P[i] + " ");
      }
    }
  }
}
