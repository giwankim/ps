package usaco.jan2023.bronze.leaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Leaders {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      leaders(r, pw);
    }
  }

  public static void leaders(BufferedReader r, PrintWriter pw) throws IOException {
    int N = Integer.parseInt(r.readLine());
    char[] cows = r.readLine().toCharArray();
    int[] E = new int[N];
    StringTokenizer st = new StringTokenizer(r.readLine());
    for (int i = 0; i < N; i++) {
      E[i] = Integer.parseInt(st.nextToken());
    }

    long ans = 0;
    for (int i = 0; i + 1 < N; i++) {
      for (int j = i + 1; j < N; j++) {
        if (canBeLeaders(i, j, cows, E)) {
          ans += 1;
        }
      }
    }

    pw.println(ans);
  }

  private static boolean canBeLeaders(int i, int j, char[] cows, int[] E) {
    return canBeLeader(i, j, cows, E) && canBeLeader(j, i, cows, E);
  }

  private static boolean canBeLeader(int i, int j, char[] cows, int[] E) {
    if (i <= j && j < E[i]) {
      return true;
    }
    for (int k = 0; k < cows.length; k++) {
      if (cows[k] == cows[i] && !(i <= k && k < E[i])) {
        return false;
      }
    }
    return true;
  }
}
