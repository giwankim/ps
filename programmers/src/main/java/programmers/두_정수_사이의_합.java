package programmers;

public class 두_정수_사이의_합 {
  public long solution(int a, int b) {
    long ans = 0;
    int min = Math.min(a, b);
    int max = Math.max(a, b);
    for (int i = min; i <= max; i++) {
      ans += i;
    }
    return ans;
  }
}
