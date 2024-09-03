package learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class CapitalTour {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out); ) {
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

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (line1[i] + line2[j] == k) {
          pw.println(line1[i] + " " + line2[j]);
        }
      }
    }
  }
}
