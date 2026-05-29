package boj.boj23032;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {

      int n = Integer.parseInt(r.readLine());
      StringTokenizer st = new StringTokenizer(r.readLine());
      int[] w = new int[n];
      for (int i = 0; i < n; i++) {
        w[i] = Integer.parseInt(st.nextToken());
      }

      int ans = 0;
      int diff = Integer.MAX_VALUE;
      for (int mid = 0; mid < n - 1; mid++) {
        // expand s and e
        // w[s:mid] vs w[mid+1:e]
        int s = mid;
        int e = mid + 1;
        int L = w[mid];
        int R = w[mid + 1];
        while (s >= 0 && e < n) {
          if (Math.abs(L - R) < diff) {
            diff = Math.abs(L - R);
            ans = L + R;
          } else if (Math.abs(L - R) == diff) {
            ans = Math.max(ans, L + R);
          }
          if (L < R) {
            if (s-- == 0) {
              break;
            }
            L += w[s];
          } else {
            if (e++ == n - 1) {
              break;
            }
            R += w[e];
          }
        }
      }
      pw.println(ans);
    }
  }
}
