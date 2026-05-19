package boj.boj16955;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, -1}, {1, 1}}; // D, R, DL, DR

  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    char[][] board = new char[10][10];
    for (int i = 0; i < 10; i++) {
      board[i] = r.readLine().toCharArray();
    }

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] != '.') {
          continue;
        }
        board[i][j] = 'X';
        if (hasFiveInARow(board)) {
          pw.println(1);
          pw.close();
          return;
        }
        board[i][j] = '.';
      }
    }
    pw.println(0);
    pw.close();
  }

  private static boolean hasFiveInARow(char[][] board) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (board[i][j] != 'X') {
          continue;
        }
        for (int[] direction : DIRECTIONS) {
          if (hasFiveInARow(board, i, j, direction)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean hasFiveInARow(char[][] board, int row, int col, int[] direction) {
    int count = 0;
    while (row >= 0
        && row < board.length
        && col >= 0
        && col < board[row].length
        && board[row][col] == 'X') {
      count++;
      row += direction[0];
      col += direction[1];
    }
    return count >= 5;
  }
}
