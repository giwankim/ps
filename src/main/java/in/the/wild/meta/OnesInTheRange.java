package in.the.wild.meta;

public class OnesInTheRange {
  private final int[] counts;

  public OnesInTheRange(int[] nums) {
    counts = new int[nums.length + 2];
    for (int i = 0; i < nums.length; i++) {
      counts[i + 1] = counts[i];
      counts[i + 1] += nums[i] == 1 ? 1 : 0;
    }
    counts[nums.length + 1] = counts[nums.length];
  }

  public int numOfOnes(int s, int e) {
    return counts[e + 1] - counts[s];
  }
}
