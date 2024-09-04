package cses.missing.number;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class MissingNumber {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      missingNumber(r, pw);
    }
  }

  public static void missingNumber(BufferedReader r, PrintWriter pw) throws IOException {
    int N = Integer.parseInt(r.readLine());
    StringTokenizer st = new StringTokenizer(r.readLine());

    long sum = (long) N * (N + 1) / 2;
    long runningSum = 0L;
    for (int i = 0; i + 1 < N; i++) {
      runningSum += Integer.parseInt(st.nextToken());
    }
    pw.println(sum - runningSum);
  }
}
