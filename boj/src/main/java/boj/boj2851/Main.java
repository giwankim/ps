package boj.boj2851;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int[] a = new int[10];
    for (int i = 0; i < 10; i++) {
      a[i] = Integer.parseInt(r.readLine());
    }

    int result = 0;
    for (int i = 0; i < 10; i++) {
      result += a[i];
      if (result > 100) {
        if (result - 100 > 100 - (result - a[i])) {
          result -= a[i];
        }
        break;
      }
    }
    pw.println(result);

    pw.close();
  }
}
