package usaco.mar2013.bronze.cowrace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class CowRace {
  private static int[] bessie = new int[1_000_000 + 1];
  private static int[] elsie = new int[1_000_000 + 1];

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("cowrace.in"));
        PrintWriter pw = new PrintWriter("cowrace.out")) {
      cowRace(r, pw);
    }
  }

  public static void cowRace(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    Arrays.fill(bessie, 0);
    Arrays.fill(elsie, 0);

    int t = 1;
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(r.readLine());
      int speed = Integer.parseInt(st.nextToken());
      int time = Integer.parseInt(st.nextToken());
      while (time-- > 0) {
        bessie[t] = bessie[t - 1] + speed;
        t += 1;
      }
    }

    t = 1;
    for (int i = 0; i < M; i++) {
      st = new StringTokenizer(r.readLine());
      int speed = Integer.parseInt(st.nextToken());
      int time = Integer.parseInt(st.nextToken());
      while (time-- > 0) {
        elsie[t] = elsie[t - 1] + speed;
        t += 1;
      }
    }

    int ans = 0;
    char leader = ' ';
    for (int i = 0; i < t; i++) {
      if (bessie[i] > elsie[i]) {
        if (leader == 'E') {
          ans += 1;
        }
        leader = 'B';
      } else if (bessie[i] < elsie[i]) {
        if (leader == 'B') {
          ans += 1;
        }
        leader = 'E';
      }
    }
    pw.println(ans);
  }
}
