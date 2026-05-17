package boj.boj2435;

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

    int[] a = new int[n];
    st = new StringTokenizer(r.readLine());
    for (int i = 0; i < n; i++) {
      a[i] = Integer.parseInt(st.nextToken());
    }

    int result = Integer.MIN_VALUE;
    for (int i = 0; i < n - k + 1; i++) {
      int sum = 0;
      for (int j = 0; j < k; j++) {
        sum += a[i + j];
      }
      result = Math.max(result, sum);
    }

    pw.println(result);

    pw.close();
  }
}
