package boj.boj1417;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int[] votes = new int[n];
    for (int i = 0; i < n; i++) {
      votes[i] = sc.nextInt();
    }

    int result = 0;
    while (!isStrictlyAhead(votes)) {
      int maxIndex = getMaxIndex(votes);
      votes[maxIndex]--;
      votes[0]++;
      result++;
    }
    System.out.println(result);
  }

  private static boolean isStrictlyAhead(int[] nums) {
    for (int i = 1; i < nums.length; i++) {
      if (nums[0] <= nums[i]) {
        return false;
      }
    }
    return true;
  }

  private static int getMaxIndex(int[] nums) {
    int result = -1;
    for (int i = 1; i < nums.length; i++) {
      if (result == -1 || nums[i] > nums[result]) {
        result = i;
      }
    }
    return result;
  }
}
