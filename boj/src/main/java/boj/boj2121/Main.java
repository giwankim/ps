package boj.boj2121;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int a = Integer.parseInt(st.nextToken());
      int b = Integer.parseInt(st.nextToken());
      int[][] points = new int[n][2];
      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(r.readLine());
        points[i][0] = Integer.parseInt(st.nextToken());
        points[i][1] = Integer.parseInt(st.nextToken());
      }

      Arrays.sort(points, Arrays::compare);

      int ans = 0;
      for (int[] point : points) {
        int x = point[0];
        int y = point[1];
        if (Arrays.binarySearch(points, new int[] {x + a, y}, Arrays::compare) < 0) {
          continue;
        }
        if (Arrays.binarySearch(points, new int[] {x, y + b}, Arrays::compare) < 0) {
          continue;
        }
        if (Arrays.binarySearch(points, new int[] {x + a, y + b}, Arrays::compare) < 0) {
          continue;
        }
        ans++;
      }
      pw.println(ans);
    }
  }
}
