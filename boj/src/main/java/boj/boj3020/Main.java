package boj.boj3020;

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
      int h = Integer.parseInt(st.nextToken());

      int[] counts = new int[h + 2];
      for (int i = 0; i < n; i += 2) {
        int a = Integer.parseInt(r.readLine());
        counts[1]++;
        counts[a + 1]--;
        int b = Integer.parseInt(r.readLine());
        counts[h - b + 1]++;
        counts[h + 1]--;
      }

      for (int i = 1; i <= h + 1; i++) {
        counts[i] += counts[i - 1];
      }

      int minCount = n;
      for (int i = 1; i <= h; i++) {
        minCount = Math.min(minCount, counts[i]);
      }

      int count = 0;
      for (int i = 1; i <= h; i++) {
        if (counts[i] == minCount) {
          count++;
        }
      }
      pw.println(minCount + " " + count);
    }
  }
}
