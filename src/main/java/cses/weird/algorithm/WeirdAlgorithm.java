package cses.weird.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WeirdAlgorithm {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      weirdAlgorithm(r, pw);
    }
  }

  public static void weirdAlgorithm(BufferedReader r, PrintWriter pw) throws IOException {
    long n = Long.parseLong(r.readLine());
    while (n != 1L) {
      pw.print(n + " ");
      if (n % 2L == 0L) {
        n /= 2L;
      } else {
        n *= 3L;
        n += 1L;
      }
    }
    pw.println(1L + " ");
  }
}
