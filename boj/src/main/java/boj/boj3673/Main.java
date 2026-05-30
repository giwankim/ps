package boj.boj3673;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int c = Integer.parseInt(r.readLine());
      while (c-- > 0) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        int d = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        int[] arr = new int[n];
        st = new StringTokenizer(r.readLine());
        for (int i = 0; i < n; i++) {
          arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] remainders = new int[d];
        remainders[0] = 1;
        int curr = 0;
        for (int x : arr) {
          curr = (curr + x) % d;
          remainders[curr]++;
        }

        long ans = 0;
        for (int i = 0; i < d; i++) {
          ans += (long) remainders[i] * (remainders[i] - 1) / 2;
        }
        pw.println(ans);
      }
    }
  }
}
