package usaco.jan2016.bronze.promote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Promote {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("promote.in"));
        PrintWriter pw = new PrintWriter("promote.out"); ) {
      promote(r, pw);
    }
  }

  public static void promote(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int b0 = Integer.parseInt(st.nextToken());
    int b1 = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(r.readLine());
    int s0 = Integer.parseInt(st.nextToken());
    int s1 = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(r.readLine());
    int g0 = Integer.parseInt(st.nextToken());
    int g1 = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(r.readLine());
    int p0 = Integer.parseInt(st.nextToken());
    int p1 = Integer.parseInt(st.nextToken());

    int g2p = p1 - p0;
    int s2g = g1 - g0 + g2p;
    int b2s = s1 - s0 + s2g;

    pw.println(b2s);
    pw.println(s2g);
    pw.println(g2p);
  }
}
