package leetcode;

public class WeightedWordMapping {
  public String mapWordWeights(String[] words, int[] weights) {
    StringBuilder result = new StringBuilder();
    for (String word : words) {
      int sum = 0;
      for (char c : word.toCharArray()) {
        sum += weights[c - 'a'];
        sum %= 26;
      }
      char c = (char) ('z' - sum);
      result.append(c);
    }
    return result.toString();
  }
}
