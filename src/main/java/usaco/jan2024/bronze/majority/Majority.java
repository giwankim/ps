package usaco.jan2024.bronze.majority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Majority {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      majority(r, pw);
    }
  }

  public static void majority(BufferedReader r, PrintWriter pw) throws IOException {
    int T = Integer.parseInt(r.readLine());
    while (T-- > 0) {
      int N = Integer.parseInt(r.readLine());

      int[] hay = new int[N];
      StringTokenizer st = new StringTokenizer(r.readLine());
      for (int i = 0; i < N; i++) {
        hay[i] = Integer.parseInt(st.nextToken());
      }

      SortedSet<Integer> result = new TreeSet<>();

      for (int i = 0; i + 1 < N; i++) {
        if (hay[i] == hay[i + 1]) {
          result.add(hay[i]);
        }
      }

      for (int i = 0; i + 2 < N; i++) {
        if (hay[i] == hay[i + 2]) {
          result.add(hay[i]);
        }
      }

      if (result.isEmpty()) {
        pw.println(-1);
      } else {
        pw.println(result.stream().sorted().map(String::valueOf).collect(Collectors.joining(" ")));
      }
    }
  }
}
