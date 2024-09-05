package learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class WalkAroundLake {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      walkAroundLake(r, pw);
    }
  }

  public static void walkAroundLake(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int D = Integer.parseInt(st.nextToken());
    int N = Integer.parseInt(st.nextToken());
    int[] d = new int[N];
    st = new StringTokenizer(r.readLine());
    for (int i = 0; i < N; i++) {
      d[i] = Integer.parseInt(st.nextToken());
    }
    st = new StringTokenizer(r.readLine());
    int[] e = new int[N];
    for (int i = 0; i < N; i++) {
      e[i] = Integer.parseInt(st.nextToken());
    }

    int bessie = 0;
    int elsie = D;
    for (int i = 0; i < N; i++) {
      bessie += d[i];
      elsie += e[i];
      if (bessie >= elsie) {
        pw.println(i + 1);
        return;
      }
    }
    pw.println(-1);
  }
}
