package boj.boj5875;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      char[] s = r.readLine().toCharArray();

      int height = 0;
      for (char c : s) {
        if (c == '(') {
          height++;
        } else {
          height--;
        }
      }

      int ans = 0;
      int opens = 0;
      int closes = 0;
      if (height < 0) {
        height = 0;
        for (char c : s) {
          height += (c == '(') ? 1 : -1;
          if (c == '(') {
            opens++;
          } else {
            closes++;
          }
          if (height < 0) {
            ans = closes;
            break;
          }
        }
      } else if (height > 0) {
        height = 0;
        for (char c : s) {
          height += (c == '(') ? 1 : -1;
          if (c == '(') {
            opens++;
          }
          if (height == 1) {
            opens = 0;
          }
        }
        ans = opens;
      }
      pw.println(ans);
    }
  }
}
