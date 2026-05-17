package boj.boj2309;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int total = 0;
    int[] heights = new int[9];
    for (int i = 0; i < 9; i++) {
      heights[i] = sc.nextInt();
      total += heights[i];
    }

    Arrays.sort(heights);

    for (int i = 0; i < 9; i++) {
      for (int j = i + 1; j < 9; j++) {
        if (total - heights[i] - heights[j] == 100) {
          for (int k = 0; k < 9; k++) {
            if (k != i && k != j) {
              System.out.println(heights[k]);
            }
          }
          return;
        }
      }
    }
  }
}
