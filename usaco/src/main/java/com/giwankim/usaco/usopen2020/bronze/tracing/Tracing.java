package com.giwankim.usaco.usopen2020.bronze.tracing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Tracing {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("tracing.in"));
        PrintWriter pw = new PrintWriter("tracing.out")) {
      tracing(r, pw);
    }
  }

  public static void tracing(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int N = Integer.parseInt(st.nextToken());
    int T = Integer.parseInt(st.nextToken());

    boolean[] finalSick = new boolean[N + 1];
    String s = r.readLine();
    for (int i = 1; i <= s.length(); i++) {
      finalSick[i] = s.charAt(i - 1) == '1';
    }

    int[] ts = new int[251];
    int[] xs = new int[251];
    int[] ys = new int[251];
    while (T-- > 0) {
      st = new StringTokenizer(r.readLine());
      int t = Integer.parseInt(st.nextToken());
      ts[t] = 1;
      xs[t] = Integer.parseInt(st.nextToken());
      ys[t] = Integer.parseInt(st.nextToken());
    }

    boolean[] canBeCow0 = new boolean[101];
    int kMin = Integer.MAX_VALUE;
    int kMax = Integer.MIN_VALUE;
    for (int k = 0; k <= 251; k++) {
      for (int cow0 = 1; cow0 <= N; cow0++) {
        // simulate
        boolean[] sick = new boolean[N + 1];
        int[] shakes = new int[N + 1];
        sick[cow0] = true;
        for (int t = 1; t <= 250; t++) {
          if (ts[t] == 0) {
            continue;
          }
          if (sick[xs[t]]) {
            shakes[xs[t]] += 1;
          }
          if (sick[ys[t]]) {
            shakes[ys[t]] += 1;
          }
          if (sick[xs[t]] && shakes[xs[t]] <= k) {
            sick[xs[t]] = true;
            sick[ys[t]] = true;
          }
          if (sick[ys[t]] && shakes[ys[t]] <= k) {
            sick[xs[t]] = true;
            sick[ys[t]] = true;
          }
        }

        if (Arrays.compare(sick, finalSick) == 0) {
          canBeCow0[cow0] = true;
          kMin = Math.min(kMin, k);
          kMax = Math.max(kMax, k);
        }
      }
    }

    int count = IntStream.range(0, canBeCow0.length).map(i -> canBeCow0[i] ? 1 : 0).sum();

    pw.println(count + " " + kMin + " " + (kMax == 251 ? "Infinity" : kMax));
  }
}
