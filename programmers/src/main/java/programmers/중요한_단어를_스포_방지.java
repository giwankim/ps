package programmers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 중요한_단어를_스포_방지 {
  public int solution(String message, int[][] spoiler_ranges) {
    // split words
    List<int[]> words = new ArrayList<>();
    int start = 0;
    for (int end = 0; end < message.length(); end++) {
      if (message.charAt(end) != ' ') {
        continue;
      }
      words.add(new int[] {start, end - 1});
      start = end + 1;
    }
    words.add(new int[] {start, message.length() - 1});

    // non spoiler words
    Set<String> nonSpoilers = new HashSet<>();
    for (int[] word : words) {
      boolean isNonSpoiler = true;
      for (int[] range : spoiler_ranges) {
        if (word[1] < range[0] || word[0] > range[1]) {
          continue;
        }
        isNonSpoiler = false;
        break;
      }
      if (isNonSpoiler) {
        nonSpoilers.add(message.substring(word[0], word[1] + 1));
      }
    }

    Set<String> ans = new HashSet<>();
    for (int[] range : spoiler_ranges) {
      for (String word : getRevealedWords(message, range, words)) {
        if (nonSpoilers.contains(word)) {
          continue;
        }
        ans.add(word);
      }
    }
    return ans.size();
  }

  private List<String> getRevealedWords(String message, int[] range, List<int[]> words) {
    List<String> result = new ArrayList<>();
    for (int[] word : words) {
      if (word[1] < range[0] || word[0] > range[1]) {
        continue;
      }
      result.add(message.substring(word[0], word[1] + 1));
    }
    return result;
  }
}
