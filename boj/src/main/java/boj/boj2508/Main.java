package boj.boj2508;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    int t = Integer.parseInt(br.readLine());
    while (t-- > 0) {
      br.readLine();
      StringTokenizer st = new StringTokenizer(br.readLine());
      int r = Integer.parseInt(st.nextToken());
      int c = Integer.parseInt(st.nextToken());
      char[][] candies = new char[r][c];
      for (int i = 0; i < r; i++) {
        candies[i] = br.readLine().toCharArray();
      }
      pw.println(count(candies));
    }
    pw.close();
  }

  private static int count(char[][] candies) {
    int result = 0;
    for (int i = 0; i < candies.length; i++) {
      for (int j = 0; j + 2 < candies[i].length; j++) {
        if (candies[i][j] == '>' && candies[i][j + 1] == 'o' && candies[i][j + 2] == '<') {
          result++;
        }
      }
    }
    for (int i = 1; i + 1 < candies.length; i++) {
      for (int j = 0; j < candies[i].length; j++) {
        if (candies[i - 1][j] == 'v' && candies[i][j] == 'o' && candies[i + 1][j] == '^') {
          result++;
        }
      }
    }
    return result;
  }
}
