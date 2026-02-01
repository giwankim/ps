package com.giwankim.usaco.feb2017.bronze.cowqueue;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class CowQueue {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("cowqueue.in"));
        PrintWriter pw = new PrintWriter("cowqueue.out")) {
      cowQueue(r, pw);
    }
  }

  public static void cowQueue(BufferedReader r, PrintWriter pw) throws IOException {
    int N = Integer.parseInt(r.readLine());
    List<Cow> cows = new ArrayList<>();
    while (N-- > 0) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int arrivalTime = Integer.parseInt(st.nextToken());
      int duration = Integer.parseInt(st.nextToken());
      cows.add(new Cow(arrivalTime, duration));
    }
    cows.sort(Comparator.comparingInt(Cow::getArrivalTime));
    int totalTime = 0;
    for (Cow cow : cows) {
      totalTime = Math.max(totalTime, cow.getArrivalTime()) + cow.getDuration();
    }
    pw.println(totalTime);
  }

  public static class Cow {
    private int arrivalTime;
    private int duration;

    public Cow(int arrivalTime, int duration) {
      this.arrivalTime = arrivalTime;
      this.duration = duration;
    }

    public int getArrivalTime() {
      return arrivalTime;
    }

    public int getDuration() {
      return duration;
    }
  }
}
