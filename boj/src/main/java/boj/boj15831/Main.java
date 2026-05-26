package boj.boj15831;

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
      int b = Integer.parseInt(st.nextToken());
      int w = Integer.parseInt(st.nextToken());
      char[] pebbles = r.readLine().toCharArray();

      int ans = 0;
      int blacks = 0;
      int whites = 0;
      int left = 0;
      for (int i = 0; i < n; i++) {
        if (pebbles[i] == 'B') {
          blacks++;
        } else {
          whites++;
        }

        while (blacks > b) {
          if (pebbles[left] == 'B') {
            blacks--;
          } else {
            whites--;
          }
          left++;
        }

        if (whites >= w) {
          ans = Math.max(ans, i - left + 1);
        }
      }

      pw.println(ans);
    }
  }
}
