package com.giwankim.algospot.packing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int c = Integer.parseInt(br.readLine());
      while (c-- > 0) {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int w = Integer.parseInt(st.nextToken());

        String[] names = new String[n];
        int[] volumes = new int[n];
        int[] priorities = new int[n];

        for (int i = 0; i < n; i++) {
          st = new StringTokenizer(br.readLine());
          names[i] = st.nextToken();
          volumes[i] = Integer.parseInt(st.nextToken());
          priorities[i] = Integer.parseInt(st.nextToken());
        }

        int[][] dp = new int[n][w + 1];

        for (int j = volumes[0]; j <= w; j++) {
          dp[0][j] = priorities[0];
        }

        for (int i = 1; i < n; i++) {
          for (int j = 1; j <= w; j++) {
            dp[i][j] = dp[i - 1][j];
            if (volumes[i] <= j) { // item fits
              dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - volumes[i]] + priorities[i]);
            }
          }
        }

        // reconstruct bag
        List<String> picked = new ArrayList<>();
        int volume = w;
        for (int i = n - 1; i > 0; i--) {
          if (dp[i][volume] > dp[i - 1][volume]) { // picked
            picked.add(names[i]);
            volume -= volumes[i];
          }
        }
        if (dp[0][volume] > 0) {
          picked.add(names[0]);
          volume -= volumes[0];
        }

        Collections.reverse(picked);

        pw.printf("%d %d%n", dp[n - 1][w], picked.size());
        picked.forEach(pw::println);
      }
    }
  }
}
