package boj.boj2110;

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
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int c = Integer.parseInt(st.nextToken());
      int[] houses = new int[n];
      for (int i = 0; i < n; i++) {
        houses[i] = Integer.parseInt(r.readLine());
      }

      Arrays.sort(houses);

      int ans = -1;
      int lo = 1;
      int hi = houses[n - 1];
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        int cnt = count(houses, mid);
        if (cnt >= c) {
          ans = mid;
          lo = mid + 1;
        } else {
          hi = mid - 1;
        }
      }
      pw.println(ans);
    }
  }

  private static int count(int[] houses, int minDistance) {
    int result = 1;
    int last = houses[0];
    for (int i = 1; i < houses.length; i++) {
      if (houses[i] - last >= minDistance) {
        result++;
        last = houses[i];
      }
    }
    return result;
  }
}
