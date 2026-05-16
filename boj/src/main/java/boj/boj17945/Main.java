package boj.boj17945;

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
    StringBuilder sb = new StringBuilder();
    for (int x = -1000; x <= 1000; x++) {
      if (x * x + 2 * a * x + b == 0) {
        sb.append(x).append(" ");
      }
    }
    pw.println(sb);
    pw.close();
  }
}
