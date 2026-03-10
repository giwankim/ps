package boj.boj2557;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      helloWorld(r, pw);
    }
  }

  public static void helloWorld(BufferedReader r, PrintWriter pw) {
    pw.println("Hello World!");
  }
}
