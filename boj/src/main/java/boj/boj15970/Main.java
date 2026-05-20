package boj.boj15970;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
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

      Arrays.sort(points, Comparator.<int[]>comparingInt(a -> a[1]).thenComparingInt(a -> a[0]));

      long result = 0L;
      for (int i = 0; i < n; i++) {
        result += minDistance(points, i);
      }
      pw.println(result);
    }
  }

  public static long minDistance(int[][] points, int index) {
    long result = Long.MAX_VALUE;
    if (index - 1 >= 0 && points[index - 1][1] == points[index][1]) {
      result = Math.min(result, (long) points[index][0] - points[index - 1][0]);
    }
    if (index + 1 < points.length && points[index + 1][1] == points[index][1]) {
      result = Math.min(result, (long) points[index + 1][0] - points[index][0]);
    }
    return result;
  }
}
