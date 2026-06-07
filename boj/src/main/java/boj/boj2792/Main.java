package boj.boj2792;

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
      int m = Integer.parseInt(st.nextToken());
      int[] jewels = new int[m];
      for (int i = 0; i < m; i++) {
        jewels[i] = Integer.parseInt(r.readLine());
      }

      int ans = Arrays.stream(jewels).max().orElse(-1);
      int lo = 1;
      int hi = ans;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        int cnt = 0;
        for (int jewel : jewels) {
          cnt += jewel / mid;
          cnt += jewel % mid > 0 ? 1 : 0;
        }
        if (cnt <= n) {
          ans = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }
      pw.println(ans);
    }
  }
}
