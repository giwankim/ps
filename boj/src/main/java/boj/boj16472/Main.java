package boj.boj16472;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      String s = r.readLine();

      int ans = 0;
      int[] counts = new int[26];
      int count = 0;
      int i = 0;
      for (int j = 0; j < s.length(); j++) {
        // expand window
        if (counts[s.charAt(j) - 'a'] == 0) {
          count++;
        }
        counts[s.charAt(j) - 'a']++;
        // shrink window
        while (count > n) {
          counts[s.charAt(i) - 'a']--;
          if (counts[s.charAt(i) - 'a'] == 0) {
            count--;
          }
          i++;
        }
        // update answer
        ans = Math.max(ans, j - i + 1);
      }
      pw.println(ans);
    }
  }
}
