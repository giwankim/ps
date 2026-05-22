package boj.boj2003;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int m = Integer.parseInt(st.nextToken());
      int[] a = new int[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }

      int count = 0;
      int current = 0;
      int i = 0;
      for (int j = 0; j < n; j++) {
        current += a[j];
        while (current > m && i < j) {
          current -= a[i];
          i++;
        }
        if (current == m) {
          count++;
        }
      }
      pw.println(count);
    }
  }
}
