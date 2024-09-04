package usaco.jan2023.bronze.leaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

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
      E[i] -= 1;
    }

    int firstG = IntStream.range(0, N).filter(i -> cows[i] == 'G').findFirst().orElse(-1);
    int lastG =
        IntStream.range(0, N)
            .map(i -> N - 1 - i)
            .filter(i -> cows[i] == 'G')
            .findFirst()
            .orElse(-1);
    int firstH = IntStream.range(0, N).filter(i -> cows[i] == 'H').findFirst().orElse(-1);
    int lastH =
        IntStream.range(0, N)
            .map(i -> N - 1 - i)
            .filter(i -> cows[i] == 'H')
            .findFirst()
            .orElse(-1);

    long ans = 0;

    // first Guernsey is leader
    if (lastG <= E[firstG]) {
      for (int i = 0; i < firstG; i++) {
        if (i == firstH) {
          continue;
        }
        // ith cow is Holstein and its list contains earliest Guernsey
        if (cows[i] == 'H' && firstG <= E[i]) {
          ans += 1;
        }
      }
    }

    // first Holstein is leader
    if (lastH <= E[firstH]) {
      for (int i = 0; i < firstH; i++) {
        if (i == firstG) {
          continue;
        }
        // ith cow is Guernsey and its list contains earliest Holstein
        if (cows[i] == 'G' && firstH <= E[i]) {
          ans += 1;
        }
      }
    }

    // check whether firstG and firstH can be leaders together
    if ((lastG <= E[firstG] || (firstG <= firstH && firstH <= E[firstG]))
        && (lastH <= E[firstH] || (firstH <= firstG && firstG <= E[firstH]))) {
      ans += 1;
    }

    pw.println(ans);
  }
}
