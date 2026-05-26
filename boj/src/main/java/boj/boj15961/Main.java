package boj.boj15961;

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
      int d = Integer.parseInt(st.nextToken());
      int k = Integer.parseInt(st.nextToken());
      int c = Integer.parseInt(st.nextToken());
      int[] sushi = new int[n + k - 1];
      for (int i = 0; i < n; i++) {
        sushi[i] = Integer.parseInt(r.readLine());
      }
      // extend the belt by k-1 plates so a linear window sweeps every circular window
      System.arraycopy(sushi, 0, sushi, n, k - 1);

      int[] freq = new int[d + 1];
      int distinct = 0;

      // initial window
      for (int i = 0; i < k; i++) {
        if (freq[sushi[i]]++ == 0) {
          distinct++;
        }
      }

      int ans = distinct + (freq[c] == 0 ? 1 : 0);

      // sliding window
      for (int i = k; i < sushi.length; i++) {
        if (freq[sushi[i]]++ == 0) {
          distinct++;
        }
        if (--freq[sushi[i - k]] == 0) {
          distinct--;
        }
        ans = Math.max(ans, distinct + (freq[c] == 0 ? 1 : 0));
      }

      pw.println(ans);
    }
  }
}
