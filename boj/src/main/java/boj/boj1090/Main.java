package boj.boj1090;

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

    int[] ys = new int[n];
    int[] xs = new int[n];
    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      xs[i] = Integer.parseInt(st.nextToken());
      ys[i] = Integer.parseInt(st.nextToken());
    }

    long[] result = new long[n];
    Arrays.fill(result, Long.MAX_VALUE);

    for (int x : xs) {
      for (int y : ys) {
        // (x, y)로 이동하는 거리 계산
        long[] dists = new long[n];
        for (int i = 0; i < n; i++) {
          dists[i] = Math.abs((long) x - xs[i]) + Math.abs((long) y - ys[i]);
        }

        Arrays.sort(dists);

        // 결과 업데이트
        long sum = 0;
        for (int i = 0; i < n; i++) {
          sum += dists[i];
          result[i] = Math.min(result[i], sum);
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(result[i]).append(' ');
    }
    pw.println(sb);
    pw.close();
  }
}
