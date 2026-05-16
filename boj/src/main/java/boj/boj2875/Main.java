package boj.boj2875;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int m = sc.nextInt();
    int k = sc.nextInt();
    int result = 0;
    for (int i = 0; i <= k && i <= n; i++) {
      for (int j = 0; j <= k - i && j <= m; j++) {
        if (i + j != k) {
          continue;
        }
        result = Math.max(result, Math.min((n - i) / 2, m - j));
      }
    }
    System.out.println(result);
  }
}
