package boj.boj10655;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int[] xs = new int[n];
      int[] ys = new int[n];
      for (int i = 0; i < n; i++) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        xs[i] = Integer.parseInt(st.nextToken());
        ys[i] = Integer.parseInt(st.nextToken());
      }

      int total = 0;
      for (int i = 1; i < n; i++) {
        total += distance(xs[i - 1], ys[i - 1], xs[i], ys[i]);
      }

      int result = total;
      for (int i = 1; i + 1 < n; i++) {
        int gain = distance(xs[i - 1], ys[i - 1], xs[i], ys[i])
            + distance(xs[i], ys[i], xs[i + 1], ys[i + 1])
            - distance(xs[i - 1], ys[i - 1], xs[i + 1], ys[i + 1]);
        result = Math.min(result, total - gain);
      }
      pw.println(result);
    }
  }

  private static int distance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }
}
