package boj.boj2563;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int n = Integer.parseInt(r.readLine());
    int[] xs = new int[n];
    int[] ys = new int[n];
    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      xs[i] = Integer.parseInt(st.nextToken());
      ys[i] = Integer.parseInt(st.nextToken());
    }

    boolean[][] grid = new boolean[100][100];
    for (int k = 0; k < n; k++) {
      for (int i = xs[k]; i < xs[k] + 10; i++) {
        for (int j = ys[k]; j < ys[k] + 10; j++) {
          grid[i][j] = true;
        }
      }
    }

    int result = 0;
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        if (grid[i][j]) {
          result++;
        }
      }
    }
    pw.println(result);
    pw.close();
  }
}
