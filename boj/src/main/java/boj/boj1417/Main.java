package boj.boj1417;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int dasom = sc.nextInt();
    int[] votes = new int[n - 1];
    for (int i = 0; i + 1 < n; i++) {
      votes[i] = sc.nextInt();
    }

    if (n == 1) {
      System.out.println(0);
      return;
    }
    Arrays.sort(votes);
    int result = 0;
    while (dasom <= votes[n - 2]) {
      dasom++;
      votes[n - 2]--;
      result++;
      Arrays.sort(votes);
    }
    System.out.println(result);
  }
}
