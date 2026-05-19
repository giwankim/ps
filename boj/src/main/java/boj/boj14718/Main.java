package boj.boj14718;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    StringTokenizer st = new StringTokenizer(r.readLine());
    int n = Integer.parseInt(st.nextToken());
    int k = Integer.parseInt(st.nextToken());
    int[][] enemies = new int[n][3];
    for (int i = 0; i < n; i++) {
      st = new StringTokenizer(r.readLine());
      enemies[i][0] = Integer.parseInt(st.nextToken());
      enemies[i][1] = Integer.parseInt(st.nextToken());
      enemies[i][2] = Integer.parseInt(st.nextToken());
    }

    int result = Integer.MAX_VALUE;
    for (int si = 0; si < n; si++) {
      for (int di = 0; di < n; di++) {
        for (int ii = 0; ii < n; ii++) {
          int str = enemies[si][0];
          int dex = enemies[di][1];
          int intl = enemies[ii][2];

          int count = 0;
          for (int[] enemy : enemies) {
            if (str >= enemy[0] && dex >= enemy[1] && intl >= enemy[2]) {
              count++;
            }
          }
          if (count >= k) {
            result = Math.min(result, str + dex + intl);
          }
        }
      }
    }
    pw.print(result);
    pw.close();
  }
}
