package boj.boj2304;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int n = Integer.parseInt(r.readLine());
    int[] maxFromLeft = new int[1001];
    int[] maxFromRight = new int[1001];
    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int l = Integer.parseInt(st.nextToken());
      int h = Integer.parseInt(st.nextToken());
      maxFromLeft[l] = h;
      maxFromRight[l] = h;
    }

    for (int i = 1; i < 1001; i++) {
      maxFromLeft[i] = Math.max(maxFromLeft[i], maxFromLeft[i - 1]);
    }
    for (int i = 999; i > 0; i--) {
      maxFromRight[i] = Math.max(maxFromRight[i], maxFromRight[i + 1]);
    }

    int result = 0;
    for (int i = 1; i < 1001; i++) {
      result += Math.min(maxFromLeft[i], maxFromRight[i]);
    }
    pw.println(result);
    pw.close();
  }
}
