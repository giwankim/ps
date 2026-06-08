package boj.boj20033;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] heights = new int[n];
      for (int i = 0; i < n; i++) {
        heights[i] = Integer.parseInt(st.nextToken());
      }

      int ans = 0;
      int lo = 1;
      int hi = 1_000_000_000;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (can(heights, mid)) {
          ans = mid;
          lo = mid + 1;
        } else {
          hi = mid - 1;
        }
      }
      pw.println(ans);
    }
  }

  private static boolean can(int[] heights, int side) {
    int cnt = 0;
    for (int height : heights) {
      if (height >= side) {
        cnt++;
        if (cnt == side) {
          return true;
        }
      } else {
        cnt = 0;
      }
    }
    return false;
  }
}
