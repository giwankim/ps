package programmers;

public class 최대공약수와_최소공배수 {
  public int[] solution(int n, int m) {
    int gcd = gcd(n, m);
    int lcm = n / gcd * m;
    return new int[] {gcd, lcm};
  }

  private int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }
}
