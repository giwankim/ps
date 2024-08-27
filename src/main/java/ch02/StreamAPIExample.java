package ch02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StreamAPIExample {
  static class Member {
    String name;
    int age;

    public Member(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString() {
      return "Member{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
  }

  public static void main(String[] args) {
    List<Member> members1 = new ArrayList<>();
    members1.add(new Member("카리나", 24));
    members1.add(new Member("윈터", 23));
    members1.add(new Member("지젤", 24));
    members1.add(new Member("닝닝", 22));

    // 스트림 API를 사용하지 않은 예전 방식
    List<String> chosenMembers = new ArrayList<>();
    // 나이가 24세인 멤버의 이름을 chosenMembers 리스트에 삽입
    for (Member member : members1) {
      if (member.age == 24) {
        chosenMembers.add(member.name);
      }
    }

    Collections.sort(chosenMembers);
    for (String name : chosenMembers) {
      System.out.println("name = " + name);
    }

    // ---

    List<Member> members2 = new ArrayList<>();
    members2.add(new Member("카리나", 24));
    members2.add(new Member("윈터", 23));
    members2.add(new Member("지젤", 24));
    members2.add(new Member("닝닝", 22));

    members2.stream()
        .filter(m -> m.age == 24)
        .map(m -> m.name)
        .sorted()
        .toList()
        .forEach(System.out::println);

    // ---

    // 숫자 리스트
    List<Double> numbers1 = Arrays.asList(49.1, 25.5, 9.9);

    // 람다 표현식
    numbers1.stream().map(n -> Math.sqrt(n)).forEach(n -> System.out.println(n));

    // ---

    // 숫자 리스트
    List<Double> numbers2 = Arrays.asList(49.1, 25.5, 9.9);

    // 메서드 참조
    numbers2.stream().map(Math::sqrt).forEach(System.out::println);
  }
}
