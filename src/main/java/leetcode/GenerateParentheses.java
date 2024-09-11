package leetcode;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
  public List<String> generateParenthesis(int n) {
    List<String> result = new ArrayList<>();
    generateParenthesis(n, n, result, new StringBuilder());
    return result;
  }

  private void generateParenthesis(int left, int right, List<String> result, StringBuilder sb) {
    if (left == 0 && right == 0) {
      result.add(sb.toString());
    }
    if (left - right > 0) {
      return;
    }
    if (left > 0) {
      sb.append('(');
      generateParenthesis(left - 1, right, result, sb);
      sb.deleteCharAt(sb.length() - 1);
    }
    if (right > 0) {
      sb.append(')');
      generateParenthesis(left, right - 1, result, sb);
      sb.deleteCharAt(sb.length() - 1);
    }
  }
}
