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

  private static String[] names = new String[100];
  private static int[] volumes = new int[100];
  private static int[] priorities = new int[100];
  private static int[][] cache = new int[1001][100];

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {

      int cases = Integer.parseInt(br.readLine());
      while (cases-- > 0) {

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int capacity = Integer.parseInt(st.nextToken());

        // read input data
        for (int i = 0; i < n; i++) {
          st = new StringTokenizer(br.readLine());
          names[i] = st.nextToken();
          volumes[i] = Integer.parseInt(st.nextToken());
          priorities[i] = Integer.parseInt(st.nextToken());
        }

        // initialize cache
        for (int i = 1; i <= capacity; i++) {
          Arrays.fill(cache[i], -1);
        }

        int totalPriority = pack(capacity, 0, n);

        List<String> picked = new ArrayList<>();
        reconstruct(capacity, 0, n, picked);

        pw.printf("%d %d%n", totalPriority, picked.size());
        picked.forEach(pw::println);
      }
    }
  }

  public static int pack(int capacity, int item, int n) {
    if (item == n) {
      return 0;
    }
    if (cache[capacity][item] != -1) {
      return cache[capacity][item];
    }

    int result = pack(capacity, item + 1, n);
    if (capacity >= volumes[item]) {
      result =
          Math.max(result, pack(capacity - volumes[item], item + 1, n) + priorities[item]);
    }
    cache[capacity][item] = result;

    return result;
  }

  public static void reconstruct(
      int capacity, int item, int n, List<String> picked) {
    if (item == n) {
      return;
    }
    if (pack(capacity, item, n) == pack(capacity, item + 1, n)) {
      // item is not picked
      reconstruct(capacity, item + 1, n, picked);
      return;
    }
    // item is picked
    picked.add(names[item]);
    reconstruct(capacity - volumes[item], item + 1, n, picked);
  }
}
