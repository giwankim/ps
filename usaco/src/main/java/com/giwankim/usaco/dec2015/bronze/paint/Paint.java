package com.giwankim.usaco.dec2015.bronze.paint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Paint {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("paint.in"));
        PrintWriter pw = new PrintWriter("paint.out")) {
      paint(r, pw);
    }
  }

  public static void paint(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int a = Integer.parseInt(st.nextToken());
    int b = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(r.readLine());
    int c = Integer.parseInt(st.nextToken());
    int d = Integer.parseInt(st.nextToken());

    int ans = 0;
    for (int i = 0; i < 100; i++) {
      if (a <= i && i + 1 <= b) {
        ans += 1;
      } else if (c <= i && i + 1 <= d) {
        ans += 1;
      }
    }
    pw.println(ans);
  }
}
