package leetcode;

public class ValidAnagram {
  public boolean isAnagram(String s, String t) {
    int[] count = new int[128];

    for (char c : s.toCharArray()) {
      count[c] += 1;
    }

    for (char c : t.toCharArray()) {
      count[c] -= 1;
    }

    for (int x : count) {
      if (x != 0) {
        return false;
      }
    }
    return true;
  }
}
