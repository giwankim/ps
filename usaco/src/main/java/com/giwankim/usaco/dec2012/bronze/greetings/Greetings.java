package com.giwankim.usaco.dec2012.bronze.greetings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Greetings {
  public static final int MAX = 1_000_000 + 1;
  private static int[] bessie = new int[MAX];
  private static int[] elsie = new int[MAX];

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("greetings.in"));
        PrintWriter pw = new PrintWriter("greetings.out")) {
      greetings(r, pw);
    }
  }

  public static void greetings(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int B = Integer.parseInt(st.nextToken());
    int E = Integer.parseInt(st.nextToken());

    bessie[0] = 0;
    int i = 1;
    while (B-- > 0) {
      st = new StringTokenizer(r.readLine());
      int T = Integer.parseInt(st.nextToken());
      int dx = st.nextToken().charAt(0) == 'L' ? -1 : 1;
      while (T-- > 0) {
        bessie[i] = bessie[i - 1] + dx;
        i += 1;
      }
    }

    elsie[0] = 0;
    int j = 1;
    while (E-- > 0) {
      st = new StringTokenizer(r.readLine());
      int T = Integer.parseInt(st.nextToken());
      int dx = st.nextToken().charAt(0) == 'L' ? -1 : 1;
      while (T-- > 0) {
        elsie[j] = elsie[j - 1] + dx;
        j += 1;
      }
    }

    int ans = 0;
    for (int k = 1; k < Math.min(i, j); k++) {
      if (bessie[k] == elsie[k] && bessie[k - 1] != elsie[k - 1]) {
        ans += 1;
      }
    }
    // elsie stopped
    for (int k = Math.min(i, j); k < i; k++) {
      if (bessie[k] == elsie[j - 1] && bessie[k - 1] != elsie[j - 1]) {
        ans += 1;
      }
    }
    // bessie stopped
    for (int k = Math.min(i, j); k < j; k++) {
      if (bessie[i - 1] == elsie[k] && bessie[i - 1] != elsie[k - 1]) {
        ans += 1;
      }
    }

    pw.println(ans);
  }
}
