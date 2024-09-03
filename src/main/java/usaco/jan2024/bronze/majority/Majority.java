package usaco.jan2024.bronze.majority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Majority {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      majority(r, pw);
    }
  }

  public static void majority(BufferedReader r, PrintWriter pw) throws IOException {
    int T = Integer.parseInt(r.readLine());
    while (T-- > 0) {
      int N = Integer.parseInt(r.readLine());

      boolean[] result = new boolean[N + 1];

      int[] hay = new int[N];
      StringTokenizer st = new StringTokenizer(r.readLine());
      for (int i = 0; i < N; i++) {
        hay[i] = Integer.parseInt(st.nextToken());
      }

      for (int i = 0; i + 1 < N; i++) {
        if (hay[i] == hay[i + 1]) {
          result[hay[i]] = true;
        }
      }
      for (int i = 0; i + 2 < N; i++) {
        if (hay[i] == hay[i + 2]) {
          result[hay[i]] = true;
        }
      }

      boolean printed = false;
      for (int i = 1; i <= N; i++) {
        if (result[i]) {
          if (printed) {
            pw.print(" ");
          }
          pw.print(i);
          printed = true;
        }
      }
      if (!printed) {
        pw.print(-1);
      }
      pw.println();
    }
  }
}
