package boj.boj17611;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static final int OFFSET = 500_000;

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int[][] points = new int[n][2];
      for (int i = 0; i < n; i++) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        points[i][0] = Integer.parseInt(st.nextToken());
        points[i][1] = Integer.parseInt(st.nextToken());
        points[i][0] += OFFSET;
        points[i][1] += OFFSET;
      }

      int[] px = new int[2 * OFFSET + 1];
      int[] py = new int[2 * OFFSET + 1];
      for (int i = 0; i < n; i++) {
        int[] curr = points[i];
        int[] next = points[(i + 1) % n];
        int minX = Math.min(curr[0], next[0]);
        int maxX = Math.max(curr[0], next[0]);
        int minY = Math.min(curr[1], next[1]);
        int maxY = Math.max(curr[1], next[1]);
        if (curr[1] == next[1]) {
          // horizontal
          px[minX]++;
          px[maxX]--;
        } else {
          // vertical
          py[minY]++;
          py[maxY]--;
        }
      }

      int ans = 0;
      for (int i = 1; i <= 2 * OFFSET; i++) {
        px[i] += px[i - 1];
        py[i] += py[i - 1];
        ans = Math.max(ans, Math.max(px[i], py[i]));
      }
      pw.println(ans);
    }
  }
}
