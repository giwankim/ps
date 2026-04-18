package learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class WhereIsTheKing {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      whereIsTheKing(r, pw);
    }
  }

  public static void whereIsTheKing(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int K = Integer.parseInt(st.nextToken());

    int[] nums = new int[N + 1];
    st = new StringTokenizer(r.readLine());
    for (int i = 1; i <= N; i++) {
      nums[i] = Integer.parseInt(st.nextToken());
    }

    char[] cups = new char[N + 1];
    cups[0] = ' ';
    for (int i = 1; i <= N; i++) {
      cups[i] = (char) ('A' + i);
    }

    char king = cups[K];

    char[] shuffledCups = new char[N + 1];
    while (M-- > 0) {
      for (int prevIdx = 1; prevIdx <= N; prevIdx++) {
        int nextIdx = nums[prevIdx];
        shuffledCups[nextIdx] = cups[prevIdx];
      }
      // copy shuffledCups to cups
      cups = Arrays.copyOf(shuffledCups, N + 1);
    }

    for (int i = 1; i <= N; i++) {
      if (cups[i] == king) {
        pw.println(i);
        break;
      }
    }
  }
}
