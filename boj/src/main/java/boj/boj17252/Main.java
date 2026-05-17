package boj.boj17252;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    if (n == 0) {
      System.out.println("NO");
      return;
    }
    while (n > 0) {
      if (n % 3 == 2) {
        System.out.println("NO");
        return;
      }
      n /= 3;
    }
    System.out.println("YES");
  }
}
