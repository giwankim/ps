package boj.boj14400;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
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

    Arrays.sort(xs);
    Arrays.sort(ys);

    long midX = xs[(xs.length - 1) / 2];
    long midY = ys[(ys.length - 1) / 2];

    long result = 0L;
    for (int i = 0; i < n; i++) {
      result += Math.abs(midX - xs[i]) + Math.abs(midY - ys[i]);
    }

    pw.println(result);
    pw.close();
  }
}
