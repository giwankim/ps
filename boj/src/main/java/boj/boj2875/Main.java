package boj.boj2875;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int m = sc.nextInt();
    int k = sc.nextInt();
    int result = 0;
    for (int t = 1; t <= 50; t++) {
      if (2 * t > n || t > m || n + m - 3 * t < k) {
        continue;
      }
      result = t;
    }
    System.out.println(result);
  }
}
