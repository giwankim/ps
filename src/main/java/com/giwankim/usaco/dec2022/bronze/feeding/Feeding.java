package com.giwankim.usaco.dec2022.bronze.feeding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Feeding {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      feeding(r, pw);
    }
  }

  public static void feeding(BufferedReader r, PrintWriter pw) throws IOException {
    int T = Integer.parseInt(r.readLine());
    while (T-- > 0) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int k = Integer.parseInt(st.nextToken());
      char[] cows = r.readLine().toCharArray();

      char[] grass = new char[cows.length];
      Arrays.fill(grass, '.');

      int numPatches = 0;

      int gLast = -1;
      int hLast = -1;
      for (int i = 0; i < n; i++) {
        if (cows[i] == 'G') {
          if (i <= gLast) {
            continue;
          }
          numPatches += 1;
          int j = Math.min(i + k, n - 1);
          if (grass[j] != '.') {
            j = Math.max(0, n - 2);
          }
          grass[j] = 'G';
          //          gLast = i + 2 * k;
          gLast = j + k;
        } else {
          if (i <= hLast) {
            continue;
          }
          numPatches += 1;
          int j = Math.min(i + k, n - 1);
          if (grass[j] != '.') {
            j = Math.max(0, n - 2);
          }
          grass[j] = 'H';
          //          hLast = i + 2 * k;
          hLast = j + k;
        }
      }

      pw.println(numPatches);
      pw.println(grass);
    }
  }
}
