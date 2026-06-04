package boj.boj24956;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  private static final int MOD = 1_000_000_007;

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      String s = r.readLine();
      int w = 0;
      int wh = 0;
      int whe = 0;
      int whee = 0;
      for (int i = 0; i < n; i++) {
        switch (s.charAt(i)) {
          case 'W' -> {
            w++;
            w %= MOD;
          }
          case 'H' -> {
            wh += w;
            wh %= MOD;
          }
          case 'E' -> {
            whe %= MOD;
            whee *= 2;
            whee %= MOD;
            whee += whe;
            whee %= MOD;
            whe += wh;
          }
          default -> {}
        }
      }
      pw.println(whee);
    }
  }
}
