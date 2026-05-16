package boj.boj2503;

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
    int[] numbers = new int[n];
    int[] strikes = new int[n];
    int[] balls = new int[n];
    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      numbers[i] = Integer.parseInt(st.nextToken());
      strikes[i] = Integer.parseInt(st.nextToken());
      balls[i] = Integer.parseInt(st.nextToken());
    }

    int result = 0;
    for (int pitch = 123; pitch <= 987; pitch++) {
      boolean valid = true;
      for (int i = 0; i < numbers.length; i++) {
        if (!matches(pitch, numbers[i], strikes[i], balls[i])) {
          valid = false;
          break;
        }
      }
      if (valid) {
        result++;
      }
    }

    pw.println(result);
    pw.close();
  }

  private static boolean matches(int pitch, int number, int strikes, int balls) {
    int x = pitch / 100;
    int y = (pitch / 10) % 10;
    int z = pitch % 10;
    if (x == 0 || y == 0 || z == 0) {
      return false;
    }
    if (x == y || y == z || z == x) {
      return false;
    }
    int a = number / 100;
    int b = (number / 10) % 10;
    int c = number % 10;

    // strikes
    int myStrikes = 0;
    if (x == a) {
      myStrikes++;
    }
    if (y == b) {
      myStrikes++;
    }
    if (z == c) {
      myStrikes++;
    }
    if (strikes != myStrikes) {
      return false;
    }

    // balls
    int myBalls = 0;
    if (x == b || x == c) {
      myBalls++;
    }
    if (y == a || y == c) {
      myBalls++;
    }
    if (z == a || z == b) {
      myBalls++;
    }
    if (balls != myBalls) {
      return false;
    }

    return true;
  }
}
