package boj.boj9417;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    int tc = Integer.parseInt(r.readLine());
    while (tc-- > 0) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int m = st.countTokens();
      int[] nums = new int[m];
      for (int i = 0; i < m; i++) {
        nums[i] = Integer.parseInt(st.nextToken());
      }
      pw.println(maxGcd(nums));
    }
    pw.close();
  }

  private static int maxGcd(int[] nums) {
    int result = 0;
    for (int i = 0; i < nums.length; i++) {
      for (int j = i + 1; j < nums.length; j++) {
        result = Math.max(result, gcd(nums[i], nums[j]));
      }
    }
    return result;
  }

  private static int gcd(int a, int b) {
    return b == 0 ? Math.abs(a) : gcd(b, a % b);
  }
}
