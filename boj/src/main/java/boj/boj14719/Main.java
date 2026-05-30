package boj.boj14719;

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
      int h = Integer.parseInt(st.nextToken());
      int w = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] blocks = new int[w];
      for (int i = 0; i < w; i++) {
        blocks[i] = Integer.parseInt(st.nextToken());
      }

      int[] left = new int[w];
      left[0] = blocks[0];
      for (int i = 1; i < w; i++) {
        left[i] = Math.max(left[i - 1], blocks[i]);
      }

      int[] right = new int[w];
      right[w - 1] = blocks[w - 1];
      for (int i = w - 2; i >= 0; i--) {
        right[i] = Math.max(right[i + 1], blocks[i]);
      }

      int ans = 0;
      for (int i = 0; i < w; i++) {
        ans += Math.min(left[i], right[i]) - blocks[i];
      }
      pw.println(ans);
    }
  }
}
