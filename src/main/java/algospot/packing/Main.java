package algospot.packing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {

      int cases = Integer.parseInt(br.readLine());
      while (cases-- > 0) {

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int capacity = Integer.parseInt(st.nextToken());

        String[] names = new String[n];
        int[] volumes = new int[n];
        int[] priorities = new int[n];

        // read input data
        for (int i = 0; i < n; i++) {
          st = new StringTokenizer(br.readLine());
          names[i] = st.nextToken();
          volumes[i] = Integer.parseInt(st.nextToken());
          priorities[i] = Integer.parseInt(st.nextToken());
        }

        // initialize cache
        int[][] cache = new int[capacity + 1][n];
        for (int[] row : cache) {
          Arrays.fill(row, -1);
        }

        int totalPriority = pack(capacity, 0, n, volumes, priorities, cache);

        List<String> picked = new ArrayList<>();
        reconstruct(capacity, 0, n, picked, names, volumes, priorities, cache);

        pw.printf("%d %d%n", totalPriority, picked.size());
        picked.forEach(pw::println);
      }
    }
  }

  public static int pack(
      int capacity, int item, int n, int[] volumes, int[] priorities, int[][] cache) {
    if (item == n) {
      return 0;
    }

    if (cache[capacity][item] != -1) {
      return cache[capacity][item];
    }

    int result = pack(capacity, item + 1, n, volumes, priorities, cache);

    if (capacity >= volumes[item]) {
      result =
          Math.max(
              result,
              pack(capacity - volumes[item], item + 1, n, volumes, priorities, cache)
                  + priorities[item]);
    }

    cache[capacity][item] = result;

    return result;
  }

  public static void reconstruct(
      int capacity,
      int item,
      int n,
      List<String> picked,
      String[] names,
      int[] volumes,
      int[] priorities,
      int[][] cache) {
    if (item == n) {
      return;
    }
    if (pack(capacity, item, n, volumes, priorities, cache)
        == pack(capacity, item + 1, n, volumes, priorities, cache)) {
      // item is not picked
      reconstruct(capacity, item + 1, n, picked, names, volumes, priorities, cache);
      return;
    }
    // item is picked
    picked.add(names[item]);
    reconstruct(capacity - volumes[item], item + 1, n, picked, names, volumes, priorities, cache);
  }
}
