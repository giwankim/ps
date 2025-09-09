package leetcode;

public class CountCompletePairPairs2 {

  public long countCompleteDayPairs(int[] hours) {
    if (hours.length < 2) {
      return 0L;
    }

    long[] freqs = new long[24];
    long result = 0L;

    for (int hour : hours) {
      int rem = hour % 24;
      int complement = (24 - rem) % 24;
      result += freqs[complement];
      freqs[rem] += 1;
    }
    return result;
  }
}
