package com.giwankim.programmers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ParkingFee {

  public int[] solution(int[] fees, String[] records) {
    Map<String, Integer> parkingByCar = new HashMap<>();
    Map<String, Integer> minutesByCar = new HashMap<>();

    for (String record : records) {
      String[] tokens = record.split(" ");
      int timestamp = getTimestamp(tokens[0]);
      String car = tokens[1];
      String action = tokens[2];

      if ("IN".equals(action)) {
        parkingByCar.put(car, timestamp);
      } else if ("OUT".equals(action)) {
        Integer inTimestamp = parkingByCar.get(car);
        parkingByCar.remove(car);

        int minutesParked = timestamp - inTimestamp;
        minutesByCar.merge(car, minutesParked, Integer::sum);
      }
    }

    // cars parked till 23:59
    int terminalTimestamp = getTimestamp("23:59");
    parkingByCar.forEach(
        (car, timestamp) -> {
          int minutesParked = terminalTimestamp - timestamp;
          minutesByCar.merge(car, minutesParked, Integer::sum);
        });

    // order values by car
    TreeMap<String, Integer> feeByCar = new TreeMap<>();
    minutesByCar.forEach(
        (car, minutes) -> {
          feeByCar.put(car, getFee(minutes, fees));
        });

    return feeByCar.values().stream().mapToInt(Integer::intValue).toArray();
  }

  private static int getTimestamp(String time) {
    String[] timeComponents = time.split(":");
    int hour = Integer.parseInt(timeComponents[0]);
    int minute = Integer.parseInt(timeComponents[1]);
    return 60 * hour + minute;
  }

  private int getFee(int minutes, int[] fees) {
    int baseTime = fees[0];
    int baseFee = fees[1];
    int unitTime = fees[2];
    int unitFare = fees[3];

    int overtime = Math.max(minutes - baseTime, 0);
    int units = (int) Math.ceil((double) overtime / unitTime);
    return baseFee + units * unitFare;
  }
}
