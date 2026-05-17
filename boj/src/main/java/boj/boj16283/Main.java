package boj.boj16283;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt();
    int b = sc.nextInt();
    int n = sc.nextInt();
    int w = sc.nextInt();

    int sheep = -1;
    int goat = -1;
    int count = 0;

    for (int i = 1; i < n; i++) {
      int j = n - i;
      if (w != a * i + b * j) {
        continue;
      }
      sheep = i;
      goat = j;
      count++;
    }

    if (count != 1) {
      System.out.println(-1);
    } else {
      System.out.println(sheep + " " + goat);
    }
  }
}
