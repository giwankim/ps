package boj.boj14697;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    int b = sc.nextInt();
    int c = sc.nextInt();
    int n = sc.nextInt();
    for (int i = 0; i <= 300; i++) {
      for (int j = 0; j <= 300; j++) {
        for (int k = 0; k <= 300; k++) {
          if (a * i + b * j + c * k == n) {
            System.out.println(1);
            return;
          }
        }
      }
    }
    System.out.println(0);
  }
}
