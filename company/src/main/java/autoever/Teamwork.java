package autoever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Teamwork {

  private static int n;
  private static int k;
  private static int m;
  private static int[] weights;
  private static int[] values;

  private static boolean[] used;
  private static int[][] cache;

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {

      StringTokenizer st = new StringTokenizer(br.readLine());
      n = Integer.parseInt(st.nextToken());
      k = Integer.parseInt(st.nextToken());
      m = Integer.parseInt(st.nextToken());

      weights = new int[n];
      values = new int[n];

      for (int i = 0; i < n; i++) {
        st = new StringTokenizer(br.readLine());
        weights[i] = Integer.parseInt(st.nextToken());
        values[i] = Integer.parseInt(st.nextToken());
      }

      used = new boolean[n];
      cache = new int[n][m + 1];

      int result = 0;

      while (k-- > 0) {
        for (int[] row : cache) {
          Arrays.fill(row, -1);
        }
        result += maximizeValueRecursive(0, m);
        // mark used items
        reconstruct(0, m);
      }

      pw.println(result);

      //      pw.println(maximizeValue());
    }
  }

  private static void reconstruct(int item, int capacity) {
    if (item == n) {
      return;
    }
    if (maximizeValueRecursive(item, capacity) == maximizeValueRecursive(item + 1, capacity)) {
      // did not take item
      reconstruct(item + 1, capacity);
      return;
    }

    used[item] = true; // take item
    reconstruct(item + 1, capacity - weights[item]);
  }

  private static int maximizeValueRecursive(int item, int capacity) {
    if (item == n) {
      return 0;
    }
    if (cache[item][capacity] != -1) { // cache hit
      return cache[item][capacity];
    }

    int result = maximizeValueRecursive(item + 1, capacity); // value if skip item

    if (capacity >= weights[item] && !used[item]) { // can take item
      // value if item taken
      int valueIfTaken = values[item] + maximizeValueRecursive(item + 1, capacity - weights[item]);
      result = Math.max(result, valueIfTaken);
    }

    cache[item][capacity] = result;

    return result;
  }

  private static int maximizeValue() {
    boolean[] used = new boolean[n];

    while (k-- > 0) {
      int[][] dp = new int[n][m + 1];

      for (int j = weights[0]; j <= m; j++) {
        if (!used[0]) {
          dp[0][j] = values[0];
        }
      }

      for (int i = 1; i < n; i++) {
        for (int j = 1; j <= m; j++) {
          dp[i][j] = dp[i - 1][j]; // not pick ith item
          if (j >= weights[i] && !used[i]) {
            dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - weights[i]] + values[i]);
          }
        }
      }

      // reconstruct picked items and set them as used
      int weight = m;
      for (int i = n - 1; i > 0; i--) {
        if (dp[i][weight] > dp[i - 1][weight]) {
          used[i] = true;
          weight -= weights[i];
        }
      }
      if (dp[0][weight] > 0) {
        used[0] = true;
      }
    }

    int result = 0;
    for (int i = 0; i < n; i++) {
      if (used[i]) {
        result += values[i];
      }
    }
    return result;
  }
}
