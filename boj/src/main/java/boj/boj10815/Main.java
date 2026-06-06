package boj.boj10815;

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
      int n = Integer.parseInt(r.readLine());
      int[] arr = new int[n];
      StringTokenizer st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        arr[i] = Integer.parseInt(st.nextToken());
      }

      Arrays.sort(arr);

      int m = Integer.parseInt(r.readLine());
      st = new StringTokenizer(r.readLine());

      StringBuilder result = new StringBuilder();
      while (m-- > 0) {
        int num = Integer.parseInt(st.nextToken());
        int ans = 0;
        int lo = 0;
        int hi = n - 1;
        while (lo <= hi) {
          int mid = lo + (hi - lo) / 2;
          if (arr[mid] == num) {
            ans = 1;
            break;
          } else if (arr[mid] < num) {
            lo = mid + 1;
          } else {
            hi = mid - 1;
          }
        }
        result.append(ans).append(' ');
      }
      pw.println(result);
    }
  }
}
