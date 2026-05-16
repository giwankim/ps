package boj.boj6131;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int result = 0;
    for (int a = 1; a <= 500; a++) {
      for (int b = 1; b <= a; b++) {
        if (a * a == b * b + n) {
          result++;
        }
      }
    }
    System.out.println(result);
  }
}
