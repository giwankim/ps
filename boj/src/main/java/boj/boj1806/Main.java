package boj.boj1806;

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
      int s = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }

      int result = Integer.MAX_VALUE;
      int i = 0;
      int windowSum = 0;
      for (int j = 0; j < n; j++) {
        windowSum += a[j];
        while (windowSum >= s) {
          result = Math.min(result, j - i + 1);
          windowSum -= a[i];
          i++;
        }
      }

      if (result == Integer.MAX_VALUE) {
        pw.println(0);
        return;
      }
      pw.println(result);
    }
  }
}
