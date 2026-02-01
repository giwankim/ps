package com.giwankim.usaco.open2017.bronze.lostcow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class LostCow {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("lostcow.in"));
        PrintWriter pw = new PrintWriter("lostcow.out")) {
      lostCow(r, pw);
    }
  }

  public static void lostCow(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int x = Integer.parseInt(st.nextToken());
    int y = Integer.parseInt(st.nextToken());
    int dist = 0;
    int steps = 1;
    int dir = 1;
    while (true) {
      if ((dir > 0 && x <= y && x + steps >= y)
          || dir < 0 && y <= x && x - steps <= y) { // found bessie
        dist += Math.abs(x - y);
        break;
      } else {
        dist += 2 * steps;
        steps *= 2;
        dir *= -1;
      }
    }
    pw.println(dist);
  }
}
