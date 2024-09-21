package usaco.dec2017.bronze.shuffle;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Shuffle {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("shuffle.in"));
        PrintWriter pw = new PrintWriter("shuffle.out")) {
      shuffle(r, pw);
    }
  }

  public static void shuffle(BufferedReader r, PrintWriter pw) throws IOException {
    int N = Integer.parseInt(r.readLine());

    int[] a = new int[N + 1];
    StringTokenizer st = new StringTokenizer(r.readLine());
    for (int i = 1; i <= N; i++) {
      a[i] = Integer.parseInt(st.nextToken());
    }

    st = new StringTokenizer(r.readLine());
    String[] finalPositions = new String[N + 1];
    for (int i = 1; i <= N; i++) {
      finalPositions[i] = st.nextToken();
    }

    int[] cows = new int[N + 1];
    for (int i = 1; i <= N; i++) {
      cows[i] = i;
    }
    for (int i = 0; i < 3; i++) {
      int[] nextPositions = new int[N + 1];
      for (int j = 1; j <= N; j++) {
        int nextIdx = a[j];
        nextPositions[nextIdx] = cows[j];
      }
      cows = Arrays.copyOf(nextPositions, N + 1);
    }

    String[] initialPositions = new String[N + 1];
    for (int i = 1; i <= N; i++) {
      initialPositions[cows[i]] = finalPositions[i];
    }

    for (int i = 1; i <= N; i++) {
      pw.println(initialPositions[i]);
    }
  }
}
