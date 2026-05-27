package boj.boj22862;

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
      int k = Integer.parseInt(st.nextToken());
      int[] s = new int[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        s[i] = Integer.parseInt(st.nextToken());
      }

      int ans = 0;
      int left = 0;
      int skip = 0;
      for (int i = 0; i < n; i++) {
        // expand
        if (s[i] % 2 == 1) {
          skip++;
        }

        // prune
        while (left < i && skip > k) {
          if (s[left++] % 2 == 1) {
            skip--;
          }
        }

        // update
        ans = Math.max(ans, i - left + 1 - skip);
      }

      pw.println(ans);
    }
  }
}
