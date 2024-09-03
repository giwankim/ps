// package cses.sum.two.values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class SumTwoValues {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      sumTwoValues(r, pw);
    }
  }

  public static void sumTwoValues(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int n = Integer.parseInt(st.nextToken());
    int x = Integer.parseInt(st.nextToken());

    int[][] nums = new int[n][2];
    st = new StringTokenizer(r.readLine());
    for (int i = 0; i < n; i++) {
      nums[i][0] = Integer.parseInt(st.nextToken());
      nums[i][1] = i + 1;
    }

    Arrays.sort(nums, Comparator.comparingInt(a -> a[0]));

    int lo = 0;
    int hi = n - 1;
    while (lo < hi) {
      long sum = nums[lo][0] + nums[hi][0];
      if (sum == x) {
        pw.println(nums[lo][1] + " " + nums[hi][1]);
        return;
      } else if (sum < x) {
        lo += 1;
      } else {
        hi -= 1;
      }
    }
    pw.println("IMPOSSIBLE");
  }
}
