package boj.boj23351;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int k = Integer.parseInt(st.nextToken());
      int a = Integer.parseInt(st.nextToken());
      int b = Integer.parseInt(st.nextToken());

      int[] groups = new int[n / a];
      Arrays.fill(groups, k);

      int result = 1;
      while (true) {
        groups[argmin(groups)] += b;
        for (int i = 0; i < groups.length; i++) {
          groups[i]--;
          if (groups[i] == 0) {
            pw.println(result);
            return;
          }
        }
        result++;
      }
    }
  }

  private static int argmin(int[] groups) {
    int result = 0;
    for (int i = 1; i < groups.length; i++) {
      if (groups[i] < groups[result]) {
        result = i;
      }
    }
    return result;
  }
}
