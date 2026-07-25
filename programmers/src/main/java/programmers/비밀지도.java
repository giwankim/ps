package programmers;

public class 비밀지도 {
  public String[] solution(int n, int[] arr1, int[] arr2) {
    String[] ans = new String[n];
    for (int i = 0; i < n; i++) {
      int v = arr1[i] | arr2[i];
      char[] row = new char[n];
      for (int j = 0; j < n; j++) {
        row[j] = ((v >> (n - 1 - j)) & 1) == 1 ? '#' : ' ';
      }
      ans[i] = new String(row);
    }
    return ans;
  }
}
