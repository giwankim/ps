package boj.boj15593;

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
    int[] a = new int[n];
    int[] b = new int[n];
    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      a[i] = Integer.parseInt(st.nextToken());
      b[i] = Integer.parseInt(st.nextToken());
    }

    int result = 0;
    for (int i = 0; i < n; i++) {
      result = Math.max(result, countCovered(a, b, i));
    }
    pw.println(result);

    pw.close();
  }

  private static int countCovered(int[] a, int[] b, int index) {
    boolean[] covered = new boolean[1000];
    for (int i = 0; i < a.length; i++) {
      if (i == index) {
        continue;
      }
      for (int j = a[i]; j < b[i]; j++) {
        covered[j] = true;
      }
    }

    int result = 0;
    for (int i = 0; i < 1000; i++) {
      if (covered[i]) {
        result++;
      }
    }
    return result;
  }
}
