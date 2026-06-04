package boj.boj24525;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      String s = r.readLine();

      int n = s.length();
      int[] p = new int[n + 1];
      int[] counts = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        p[i] = p[i - 1];
        char c = s.charAt(i - 1);
        if (c == 'S') {
          p[i] += 2;
        } else if (c == 'K') {
          p[i] -= 1;
        }
        counts[i] = counts[i - 1] + (c == 'S' || c == 'K' ? 1 : 0);
      }

      int ans = -1;
      Map<Integer, Integer> firstIndex = new HashMap<>();
      firstIndex.put(0, 0);
      for (int i = 1; i <= n; i++) {
        if (firstIndex.containsKey(p[i])) {
          if (counts[i] != counts[firstIndex.get(p[i])]) {
            ans = Math.max(ans, i - firstIndex.get(p[i]));
          }
        } else {
          firstIndex.put(p[i], i);
        }
      }
      pw.println(ans);
    }
  }
}
