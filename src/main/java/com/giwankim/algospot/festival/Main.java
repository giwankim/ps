package com.giwankim.algospot.festival;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int C = Integer.parseInt(br.readLine());
    while (C-- > 0) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int L = Integer.parseInt(st.nextToken());
      List<Integer> costs = new ArrayList<>();
      st = new StringTokenizer(br.readLine());
      for (int i = 0; i < N; i++) {
        costs.add(Integer.parseInt(st.nextToken()));
      }
      pw.printf("%.11f%n", festival(N, L, costs));
    }

    br.close();
    pw.close();
  }

  private static double festival(int N, int L, List<Integer> costs) {
    double minCost = Double.MAX_VALUE;
    List<Integer> sums = new ArrayList<>();
    sums.add(0);
    for (int i = 0; i < N; i++) {
      sums.add(costs.get(i) + sums.get(i));
    }
    for (int i = 0; i <= N - L; i++) {
      for (int j = i; j < N; j++) {
        int sum = sums.get(j + 1) - sums.get(i);
        int len = j - i + 1;
        if (len < L) {
          continue;
        }
        minCost = Math.min(minCost, (double) sum / len);
      }
    }
    return minCost;
  }
}
