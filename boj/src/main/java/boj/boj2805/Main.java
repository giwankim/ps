package boj.boj2805;

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
      int m = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] trees = new int[n];
      for (int i = 0; i < n; i++) {
        trees[i] = Integer.parseInt(st.nextToken());
      }

      int ans = 1_000_000_000;
      int lo = 0;
      int hi = 1_000_000_000;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (cut(trees, mid) >= m) {
          ans = mid;
          lo = mid + 1;
        } else {
          hi = mid - 1;
        }
      }
      pw.println(ans);
    }
  }

  private static long cut(int[] trees, int height) {
    long result = 0;
    for (int tree : trees) {
      if (tree > height) {
        result += tree - height;
      }
    }
    return result;
  }
}
