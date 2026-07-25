package programmers;

public class 숫자_문자열과_영단어 {
  private static final String[] NUMBERS = {
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
  };

  public int solution(String s) {
    int ans = 0;
    int i = 0;
    while (i < s.length()) {
      int digit = 0;
      if (Character.isDigit(s.charAt(i))) {
        digit = s.charAt(i) - '0';
        i++;
      } else {
        for (int j = 0; j < NUMBERS.length; j++) {
          if (s.startsWith(NUMBERS[j], i)) {
            digit = j;
            i += NUMBERS[j].length();
            break;
          }
        }
      }
      ans = ans * 10 + digit;
    }
    return ans;
  }
}
