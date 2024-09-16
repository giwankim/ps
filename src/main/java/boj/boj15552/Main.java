package boj.boj15552;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      fastAPlusB(r, pw);
    }
  }

  public static void fastAPlusB(BufferedReader r, PrintWriter pw) throws IOException {
    int T = Integer.parseInt(r.readLine());
    while (T-- > 0) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int a = Integer.parseInt(st.nextToken());
      int b = Integer.parseInt(st.nextToken());
      pw.println(a + b);
    }
  }
}
