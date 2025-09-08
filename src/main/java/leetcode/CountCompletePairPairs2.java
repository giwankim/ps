package leetcode;

public class CountCompletePairPairs2 {

  public long countCompleteDayPairs(int[] hours) {
    if (hours.length < 2) {
      return 0L;
    }

    long result = 0;
    for (int i = 0; i + 1 < hours.length; i++) {
      for (int j = i + 1; j < hours.length; j++) {
        if ((hours[i] + hours[j]) % 24 == 0) {
          result += 1;
        }
      }
    }
    return result;
  }
}
