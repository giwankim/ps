package com.giwankim.algospot.jumpgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {

      int C = Integer.parseInt(br.readLine());
      while (C-- > 0) {
        int n = Integer.parseInt(br.readLine());

        // read board from StdIn
        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
          StringTokenizer st = new StringTokenizer((br.readLine()));
          for (int j = 0; j < n; j++) {
            board[i][j] = Integer.parseInt(st.nextToken());
          }
        }

        // initialize cache
        int[][] cache = new int[n][n];
        for (int[] row : cache) {
          Arrays.fill(row, -1);
        }

        // print result to StdOut
        if (jump(n, board, 0, 0, cache) == 1) {
          pw.println("YES");
        } else {
          pw.println("NO");
        }
      }
    }
  }

  public static int jump(int n, int[][] board, int y, int x, int[][] cache) {
    if (x >= n || y >= n) {
      return 0;
    }

    if (x == n - 1 && y == n - 1) {
      return 1;
    }

    if (cache[y][x] != -1) {
      return cache[y][x];
    }

    int jumpSize = board[y][x];
    if (jump(n, board, y + jumpSize, x, cache) == 1
        || jump(n, board, y, x + jumpSize, cache) == 1) {
      cache[y][x] = 1;
    } else {
      cache[y][x] = 0;
    }
    return cache[y][x];
  }
}
