package learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class CapitalTour {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      capitalTour(r, pw);
    }
  }

  public static void capitalTour(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int n = Integer.parseInt(st.nextToken());
    int k = Integer.parseInt(st.nextToken());
    int[] line1 = new int[n];
    int[] line2 = new int[n];

    st = new StringTokenizer(r.readLine());
    for (int i = 0; i < n; i++) {
      line1[i] = Integer.parseInt(st.nextToken());
    }

    st = new StringTokenizer(r.readLine());
    for (int i = 0; i < n; i++) {
      line2[i] = Integer.parseInt(st.nextToken());
    }

    int i = 0;
    int j = n - 1;
    while (i < n && j >= 0) {
      int sum = line1[i] + line2[j];
      if (sum == k) {
        pw.println(line1[i] + " " + line2[j]);
        return;
      } else if (sum < k) {
        i += 1;
      } else {
        j -= 1;
      }
    }
  }
}
