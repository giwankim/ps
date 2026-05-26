package boj.boj1484;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int g = Integer.parseInt(r.readLine());

      List<Integer> ans = new ArrayList<>();
      int lo = 1;
      int hi = 2;
      int limit = (g + 1) / 2;
      while (hi <= limit) {
        long diff = (long) hi * hi - (long) lo * lo;
        if (diff == g) {
          ans.add(hi);
          hi++;
          lo++;
        } else if (diff < g) {
          hi++;
        } else {
          lo++;
        }
      }

      if (ans.isEmpty()) {
        pw.println(-1);
        return;
      }
      for (int x : ans) {
        pw.println(x);
      }
    }
  }
}
