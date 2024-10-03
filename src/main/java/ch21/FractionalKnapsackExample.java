package ch21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FractionalKnapsackExample {
  static class Cargo {
    // 가치($)
    int price;
    // 무게(kg)
    int weight;
    // 단가($/kg)
    float unitPrice;

    public Cargo(int price, int weight) {
      this.price = price;
      this.weight = weight;
      this.unitPrice = (float) price / weight;
    }
  }

  public static float fractionalKnapsack(List<Cargo> cargos) {
    // 용량
    int capacity = 15;
    // 담을 수 있는 최댓값
    float totalValue = 0;

    cargos.sort(Collections.reverseOrder(Comparator.comparingDouble(c -> c.unitPrice)));
    System.out.println("cargos = " + cargos);

    for (Cargo cargo : cargos) {
      // 짐을 쪼개지 않아도 되는 경우 전체 가격 증가
      if (cargo.weight <= capacity) {
        capacity -= cargo.weight;
        totalValue += cargo.price;
      } else { // 짐을 쪼개야 하는 경우 쪼갠만큼 가격 증가
        totalValue += cargo.unitPrice * capacity;
        break;
      }
    }

    return totalValue;
  }

  public static void main(String[] args) {
    List<Cargo> cargos = new ArrayList<>();
    cargos.add(new Cargo(4, 12));
    cargos.add(new Cargo(2, 1));
    cargos.add(new Cargo(10, 4));
    cargos.add(new Cargo(1, 1));
    cargos.add(new Cargo(2, 2));

    float result = fractionalKnapsack(cargos); // 17.333334
    System.out.println("result = " + result);
  }
}
