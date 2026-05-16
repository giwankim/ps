package boj.boj14568;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    int n = Integer.parseInt(r.readLine());
    int result = 0;
    for (int i = 1; i <= 100; i++) {
      for (int j = 1; j <= 100; j++) {
        for (int k = 1; k <= 100; k++) {
          if (i % 2 == 1) {
            continue;
          }
          if (i + j + k != n) {
            continue;
          }
          if (k < j + 2) {
            continue;
          }
          result++;
        }
      }
    }
    pw.println(result);
    pw.close();
  }
}
