package boj.boj1438;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int[][] points = new int[n][2];
      for (int i = 0; i < n; i++) {
        StringTokenizer st = new StringTokenizer(r.readLine());
        points[i][0] = Integer.parseInt(st.nextToken());
        points[i][1] = Integer.parseInt(st.nextToken());
      }

      int ans = Integer.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int xmin = Math.min(points[i][0], points[j][0]);
          int xmax = Math.max(points[i][0], points[j][0]);

          int[] ys = Arrays.stream(points)
              .filter(p -> p[0] >= xmin && p[0] <= xmax)
              .mapToInt(p -> p[1])
              .sorted()
              .toArray();

          int s = 0;
          for (int e = 0; e < ys.length; e++) {
            while (e - s + 1 >= n / 2) {
              int area = (xmax - xmin + 2) * (ys[e] - ys[s] + 2);
              ans = Math.min(ans, area);
              s++;
            }
          }
        }
      }
      pw.println(ans);
    }
  }
}
