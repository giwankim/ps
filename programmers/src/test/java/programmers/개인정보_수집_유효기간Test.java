package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class 개인정보_수집_유효기간Test {
  개인정보_수집_유효기간 sut = new 개인정보_수집_유효기간();

  // Step 1: 가장 단순한 입력 — 약관도 개인정보도 하나뿐이고, 유효기간이 한참 지났다.
  //         빈 배열을 돌려주는 구현이 여기서 걸린다. 날짜를 "." 으로 쪼개는 구현도
  //         마찬가지다. 자바에서 split(".")의 "."은 정규식의 임의 문자라서 빈 배열이 된다
  @Test
  void 유효기간이_지난_개인정보의_번호를_반환한다() {
    assertThat(sut.solution("2022.05.19", new String[] {"A 6"}, new String[] {"2021.05.02 A"}))
        .containsExactly(1);
  }

  // Step 2: 번호는 배열 인덱스가 아니라 1부터 세는 순번이다. 파기 대상을 두 번째에 두면
  //         0부터 세는 구현은 [1]을, 1부터 세는 구현은 [2]를 낸다
  @Test
  void 개인정보_번호는_1부터_센다() {
    assertThat(sut.solution(
            "2022.05.19", new String[] {"A 6"}, new String[] {"2022.05.18 A", "2021.01.01 A"}))
        .containsExactly(2);
  }

  // Step 3: Step 1과 Step 2를 모두 통과하는 "전부 파기" 구현을 걸러낸다.
  //         뒤에 붙은 개인정보는 유효기간이 남아 있으므로 결과에서 빠져야 한다
  @Test
  void 유효기간이_남은_개인정보는_결과에서_빠진다() {
    assertThat(sut.solution(
            "2022.05.19", new String[] {"A 6"}, new String[] {"2021.01.01 A", "2022.05.18 A"}))
        .containsExactly(1);
  }

  // Step 4: 비교는 연-월-일 전체로 한다. 2021년 11월 1일에 수집된 개인정보의 파기
  //         시작일은 2021년 12월 1일이다. 연도를 빼고 월일만 비교하는 구현은
  //         오늘(05.19)이 파기 시작일(12.01)보다 앞선다고 보아 보관 가능하다고 잘못 판단한다
  @Test
  void 월일이_앞서도_연도가_지났으면_파기한다() {
    assertThat(sut.solution("2022.05.19", new String[] {"A 1"}, new String[] {"2021.11.01 A"}))
        .containsExactly(1);
  }

  // Step 5: 경계 — 유효기간 3달짜리 약관으로 2022년 2월 20일에 수집한 개인정보는
  //         2022년 5월 19일까지 보관 가능하다. 오늘이 마지막 보관 가능일이면 아직 파기하지
  //         않는다. 부등호를 하루 밀어 쓴 구현은 1번까지 파기해 [1, 2]를 낸다
  @Test
  void 마지막_보관_가능일에는_파기하지_않는다() {
    assertThat(sut.solution(
            "2022.05.19", new String[] {"C 3"}, new String[] {"2022.02.20 C", "2021.01.01 C"}))
        .containsExactly(2);
  }

  // Step 6: 경계의 반대편 — 하루 앞선 2022년 2월 19일 수집분은 2022년 5월 18일까지만
  //         보관 가능하므로 오늘 파기해야 한다. Step 5를 통과하려고 부등호를 반대로 민
  //         구현은 여기서 빈 배열을 낸다. 두 테스트가 경계를 양쪽에서 못박는다
  @Test
  void 파기_시작일_당일에_파기한다() {
    assertThat(sut.solution("2022.05.19", new String[] {"C 3"}, new String[] {"2022.02.19 C"}))
        .containsExactly(1);
  }

  // Step 7: 유효기간은 개인정보에 적힌 약관 종류로 찾아야 한다. 같은 날 수집된 두 건이
  //         약관 때문에 갈린다. 약관을 하나만 보고 terms[0]의 6달을 모두에게 적용하는
  //         구현은 B약관 건까지 파기해 [1, 2]를 낸다
  @Test
  void 유효기간은_개인정보의_약관_종류로_찾는다() {
    assertThat(sut.solution(
            "2022.05.19", new String[] {"A 6", "B 12"}, new String[] {"2021.07.01 B", "2021.07.01 A"
            }))
        .containsExactly(2);
  }

  // Step 8: 약관은 알파벳 순서로 주어지지 않는다. terms의 i번째가 'A' + i번 약관이라고
  //         가정하는 구현은 D약관을 두 번째 자리에서 찾지 못하거나 Z약관의 3달을 잘못
  //         적용한다. 3달이면 아직 보관 가능하지만 5달이면 파기 대상이므로 결과가 갈린다
  @Test
  void 약관이_알파벳_순서로_주어지지_않아도_찾는다() {
    assertThat(
            sut.solution("2020.01.01", new String[] {"Z 3", "D 5"}, new String[] {"2019.01.01 D"}))
        .containsExactly(1);
  }

  // Step 9: 달을 더하다 12를 넘으면 해가 바뀐다. 2021년 12월에 3달을 더하면 2022년 3월이다.
  //         월을 15로 둔 채 연도를 그대로 두고 비교하는 구현은 2021년 15월을 오늘(2022년)보다
  //         과거로 보아 1번까지 파기해 [1, 2]를 낸다
  @Test
  void 달을_더하다_해가_바뀌어도_마지막_보관일을_지킨다() {
    assertThat(sut.solution(
            "2022.02.28", new String[] {"A 3", "B 1"}, new String[] {"2021.12.01 A", "2021.01.01 B"
            }))
        .containsExactly(2);
  }

  // Step 10: Step 9의 경계 반대편 — 하루 뒤인 2022년 3월 1일이 파기 시작일이다.
  //          해를 넘기는 계산에서도 경계가 하루도 어긋나지 않아야 한다
  @Test
  void 해가_바뀐_파기_시작일_당일에_파기한다() {
    assertThat(sut.solution("2022.03.01", new String[] {"A 3"}, new String[] {"2021.12.01 A"}))
        .containsExactly(1);
  }

  // Step 11: 파기 시작일이 12월에 딱 걸리는 경우 — 2021년 11월 1일 수집분에 1달을 더하면
  //          2021년 12월 1일이다. 1부터 세는 달을 12로 나누고 남겨 정규화하는 구현은
  //          11 + 1 = 12를 "이듬해 0월"로 밀어내고, 그 바람에 이 12월 내내 보관 가능하다고
  //          잘못 판단한다. Step 9, Step 10이 잡는 13월 이상의 올림과 달리, 정확히 12월에
  //          떨어질 때만 어긋나므로 앞선 어떤 테스트에도 걸리지 않는다
  @Test
  void 파기_시작일이_12월이면_그_12월에_파기한다() {
    assertThat(sut.solution("2021.12.01", new String[] {"A 1"}, new String[] {"2021.11.01 A"}))
        .containsExactly(1);
  }

  // Step 12: Step 11의 경계 반대편 — 파기 시작일이 12월 28일이면 12월 27일에는 아직 보관한다.
  //          12월을 이듬해로 밀어내는 구현이 우연히 통과하는 자리다. Step 11을 고치면서
  //          12월 전체를 파기로 뒤집지 않았는지 여기서 못박는다
  @Test
  void 파기_시작일_전이면_12월이어도_보관한다() {
    assertThat(sut.solution(
            "2021.12.27", new String[] {"A 1", "B 1"}, new String[] {"2021.11.28 A", "2021.01.01 B"
            }))
        .containsExactly(2);
  }

  // Step 13: 12월 만료는 유효기간이 12의 배수일 때도 나온다. 12월 수집분에 12달을 더하면
  //          해만 하나 늘고 달은 그대로 12월이다. 유효기간을 12로 나눈 몫은 해에, 나머지는
  //          달에 각각 더하는 구현에서 두 경로가 모두 12월로 모이므로, Step 11과 같은 자리가
  //          다른 입력에서 다시 드러난다
  @Test
  void 유효기간이_12의_배수여도_12월_만료를_놓치지_않는다() {
    assertThat(sut.solution("2022.12.28", new String[] {"A 12"}, new String[] {"2021.12.01 A"}))
        .containsExactly(1);
  }

  // Step 14: "모든 달은 28일"은 달 수를 그대로 더하라는 뜻이지, 실제 달력에 28일씩 더하라는
  //          뜻이 아니다. 2021년 3월 1일 + 2달은 2021년 5월 1일이지만, 실제 달력에서 56일을
  //          더하면 2021년 4월 26일이 된다. 오늘이 4월 28일이면 전자는 보관, 후자는 파기로
  //          갈린다. LocalDate.plusDays(28L * term) 식의 구현이 여기서 [1, 2]를 낸다
  @Test
  void 유효기간은_달_수이지_28일씩_더한_날수가_아니다() {
    assertThat(sut.solution(
            "2021.04.28", new String[] {"A 2", "B 1"}, new String[] {"2021.03.01 A", "2021.01.01 B"
            }))
        .containsExactly(2);
  }

  // Step 15: 같은 날 수집됐어도 약관이 다르면 파기 여부가 갈린다. 날짜를 기준으로 판정을
  //          한 번만 계산해 재사용하는 구현은 두 건을 같은 결과로 묶어버린다
  @Test
  void 같은_날_수집됐어도_약관이_다르면_결과가_갈린다() {
    assertThat(sut.solution(
            "2022.05.19", new String[] {"A 3", "B 12"}, new String[] {"2022.02.01 A", "2022.02.01 B"
            }))
        .containsExactly(1);
  }

  // Step 16: 결과는 번호 오름차순이다. 수집 날짜는 거꾸로 늘어놓았으므로, 파기 시급한
  //          순서나 날짜 순서로 정렬하는 구현은 [3, 2, 1]을 낸다
  @Test
  void 결과는_수집_날짜_순서가_아니라_번호_순서다() {
    assertThat(sut.solution("2022.05.19", new String[] {"A 1"}, new String[] {
          "2021.03.01 A", "2021.02.01 A", "2021.01.01 A"
        }))
        .containsExactly(1, 2, 3);
  }

  // Step 17: 제한사항의 최대 유효기간 100달 — 2000년 1월 1일에 수집한 개인정보는 8년 4달
  //          뒤인 2008년 5월 1일부터 파기 대상이므로 4월 28일에는 아직 보관 가능하다.
  //          달을 12로 나누고 남기는 계산이 여러 해를 건너뛸 때도 맞아야 한다
  @Test
  void 유효기간_100달의_마지막_보관일에는_파기하지_않는다() {
    assertThat(sut.solution("2008.04.28", new String[] {"A 100", "B 1"}, new String[] {
          "2000.01.01 A", "2000.01.01 B"
        }))
        .containsExactly(2);
  }

  // Step 18: Step 17의 경계 반대편 — 2008년 5월 1일이 100달째 파기 시작일이다
  @Test
  void 유효기간_100달이_지나면_파기한다() {
    assertThat(sut.solution("2008.05.01", new String[] {"A 100"}, new String[] {"2000.01.01 A"}))
        .containsExactly(1);
  }

  // Step 19: 제한사항의 최대 크기 — 약관 20개, 개인정보 100건. 모두 22년 전에 수집된
  //          유효기간 1달짜리이므로 100건 전부가 파기 대상이다. 결과 배열의 크기를
  //          약관 수나 26으로 고정한 구현, 앞쪽 몇 건만 담는 구현이 여기서 걸린다
  @Test
  void 약관_20개와_개인정보_100건도_모두_처리한다() {
    String[] terms =
        IntStream.range(0, 20).mapToObj(i -> (char) ('A' + i) + " 1").toArray(String[]::new);
    String[] privacies = IntStream.range(0, 100)
        .mapToObj(i -> "2000.01.01 " + (char) ('A' + i % 20))
        .toArray(String[]::new);

    assertThat(sut.solution("2022.12.28", terms, privacies))
        .containsExactly(IntStream.rangeClosed(1, 100).toArray());
  }

  // Step 20: official example 1 — 약관 세 종류가 섞이고 경계의 양쪽(2월 19일, 2월 20일)이
  //          함께 들어 있는 대표 사례다
  @Test
  void 공식_예제1은_1번과_3번이다() {
    assertThat(sut.solution("2022.05.19", new String[] {"A 6", "B 12", "C 3"}, new String[] {
          "2021.05.02 A", "2021.07.01 B", "2022.02.19 C", "2022.02.20 C"
        }))
        .containsExactly(1, 3);
  }

  // Step 21: official example 2 — 약관이 알파벳 순서가 아니고, 수집 날짜 순서와 번호 순서가
  //          어긋나며, 3번은 오늘이 마지막 보관 가능일이라 살아남는다. 앞선 규칙들이
  //          한 입력에서 동시에 걸리는 사례다
  @Test
  void 공식_예제2는_1번과_4번과_5번이다() {
    assertThat(sut.solution("2020.01.01", new String[] {"Z 3", "D 5"}, new String[] {
          "2019.01.01 D", "2019.11.15 Z", "2019.08.02 D", "2019.07.01 D", "2018.12.28 Z"
        }))
        .containsExactly(1, 4, 5);
  }
}
