package boj.boj14453;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      char[] hands = new char[n];
      for (int i = 0; i < n; i++) {
        hands[i] = r.readLine().charAt(0);
      }

      int[][] prefix = new int[n + 1][3];
      for (int i = 1; i <= n; i++) {
        prefix[i][0] = prefix[i - 1][0];
        prefix[i][1] = prefix[i - 1][1];
        prefix[i][2] = prefix[i - 1][2];
        char c = hands[i - 1];
        if (c == 'H') {
          prefix[i][0]++;
        } else if (c == 'P') {
          prefix[i][1]++;
        } else if (c == 'S') {
          prefix[i][2]++;
        }
      }

      int ans = 0;
      for (int x = 1; x <= n; x++) {
        int left = Math.max(
            Math.max(prefix[x][0] - prefix[0][0], prefix[x][1] - prefix[0][1]),
            prefix[x][2] - prefix[0][2]);
        int right = Math.max(
            Math.max(prefix[n][0] - prefix[x][0], prefix[n][1] - prefix[x][1]),
            prefix[n][2] - prefix[x][2]);
        ans = Math.max(ans, left + right);
      }
      pw.println(ans);
    }
  }
}
