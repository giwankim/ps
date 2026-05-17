package boj.boj19532;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    StringTokenizer st = new StringTokenizer(r.readLine());
    int a = Integer.parseInt(st.nextToken());
    int b = Integer.parseInt(st.nextToken());
    int c = Integer.parseInt(st.nextToken());
    int d = Integer.parseInt(st.nextToken());
    int e = Integer.parseInt(st.nextToken());
    int f = Integer.parseInt(st.nextToken());
    for (int x = -999; x <= 999; x++) {
      for (int y = -999; y <= 999; y++) {
        if (a * x + b * y == c && d * x + e * y == f) {
          pw.println(x + " " + y);
          break;
        }
      }
    }
    pw.close();
  }
}
