package com.giwankim.algospot.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  private static int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
  private static int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
  private static char[][] board = new char[5][5];

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int C = Integer.parseInt(br.readLine());
    while (C-- > 0) {
      for (int i = 0; i < 5; i++) {
        board[i] = br.readLine().toCharArray();
      }

      int N = Integer.parseInt(br.readLine());
      while (N-- > 0) {
        String word = br.readLine();
        if (hasWord(word)) {
          pw.println(word + " YES");
        } else {
          pw.println(word + " NO");
        }
      }
    }

    br.close();
    pw.close();
  }

  private static boolean hasWord(String word) {
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (hasWord(y, x, word)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean hasWord(int y, int x, String word) {
    if (y < 0 || y >= 5 || x < 0 || x >= 5) {
      return false;
    }
    if (board[y][x] != word.charAt(0)) {
      return false;
    }
    if (word.length() == 1) {
      return true;
    }
    for (int d = 0; d < 8; d++) {
      int ny = y + dy[d];
      int nx = x + dx[d];
      if (hasWord(ny, nx, word.substring(1))) {
        return true;
      }
    }
    return false;
  }
}
