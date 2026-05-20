package boj.boj17286;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    StringTokenizer st = new StringTokenizer(r.readLine());
    int x = Integer.parseInt(st.nextToken());
    int y = Integer.parseInt(st.nextToken());
    int[] xs = new int[3];
    int[] ys = new int[3];
    for (int i = 0; i < 3; i++) {
      st = new StringTokenizer(r.readLine());
      xs[i] = Integer.parseInt(st.nextToken());
      ys[i] = Integer.parseInt(st.nextToken());
    }

    double result = Double.MAX_VALUE;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i == j) {
          continue;
        }
        for (int k = 0; k < 3; k++) {
          if (k == i || k == j) {
            continue;
          }
          double tourDistance = hypot(x, y, xs[i], ys[i])
              + hypot(xs[i], ys[i], xs[j], ys[j])
              + hypot(xs[j], ys[j], xs[k], ys[k]);
          result = Math.min(result, tourDistance);
        }
      }
    }
    pw.print((int) result);
    pw.close();
  }

  private static double hypot(int x1, int y1, int x2, int y2) {
    int dx = x2 - x1;
    int dy = y2 - y1;
    return Math.sqrt(dx * dx + dy * dy);
  }
}
