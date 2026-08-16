package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class 정수_내림차순으로_배치하기Test {
  정수_내림차순으로_배치하기 sut = new 정수_내림차순으로_배치하기();

  // Step 1: 제약 하한 — n은 1 이상의 자연수라 가장 작은 입력은 한 자리 수다.
  //         정렬할 것이 없어도 그 자리 숫자를 그대로 돌려줘야 하며, 0을 반환하면 안 된다
  @Test
  void 한_자리_수는_그대로_반환된다() {
    assertThat(sut.solution(1)).isEqualTo(1L);
  }

  // Step 2: 처음으로 자리 이동을 요구하는 입력 — n을 그대로 돌려주는 구현은 여기서 12를 낸다
  @Test
  void 오름차순인_두_자리_수는_내림차순으로_뒤집힌다() {
    assertThat(sut.solution(12)).isEqualTo(21L);
  }

  // Step 3: 이미 내림차순인 수 — Step 2를 "자릿수를 뒤집는다"로 읽은 구현은 여기서 12를 낸다.
  //         답이 입력과 같아지는 입력이 존재한다는 사실도 함께 고정한다
  @Test
  void 이미_내림차순인_두_자리_수는_그대로_유지된다() {
    assertThat(sut.solution(21)).isEqualTo(21L);
  }

  // Step 4: 오름차순도 내림차순도 아닌 첫 입력 — 뒤집기도 양 끝 맞바꾸기도 312를 내므로
  //         자릿수 전체를 실제로 정렬해야만 통과한다
  @Test
  void 뒤섞인_세_자리_수는_자릿수_전체가_정렬된다() {
    assertThat(sut.solution(213)).isEqualTo(321L);
  }

  // Step 5: official example — 118372의 답은 873211이다.
  //         Step 4의 정렬을 자릿수가 늘어난 상태에서 다시 확인한다
  @Test
  void 공식_예제_118372는_873211이_된다() {
    assertThat(sut.solution(118372)).isEqualTo(873211L);
  }

  // Step 6: 같은 숫자가 떨어져 나타나는 수 — 정렬하면 한데 모이되 나온 횟수만큼 남아야 한다.
  //         distinct()를 끼우거나 Set에 모으는 구현은 자릿수가 줄어 21이 된다
  @Test
  void 중복된_자릿수는_나온_횟수만큼_남는다() {
    assertThat(sut.solution(1212)).isEqualTo(2211L);
  }

  // Step 7: 0이 섞인 첫 입력 — 0은 가장 작은 자릿수라 맨 뒤로 밀린다.
  //         0을 "값이 없음"으로 보고 건너뛰는 구현은 자리를 하나 잃어 21을 낸다
  @Test
  void 영은_가장_작은_자릿수라_맨_뒤에_놓인다() {
    assertThat(sut.solution(102)).isEqualTo(210L);
  }

  // Step 8: 0이 여럿인 수 — Step 7의 0이 하나가 아닌 경우다. 결과의 자릿수는 언제나 n과 같으므로
  //         0을 전부 걸러내는 구현은 1로, 하나만 흘리는 구현은 100으로 짧아진다
  @Test
  void 영이_여러_개면_그_개수만큼_뒤에_남는다() {
    assertThat(sut.solution(1000)).isEqualTo(1000L);
  }

  // Step 9: 답이 int를 넘는 첫 입력 — n은 int 최댓값이라 아직 int로 버티지만, 답 8,776,444,321은
  //         int는 물론 입력 상한(8,000,000,000)까지 넘는다. 답을 int에 누적하는 구현은 래핑된다
  @Test
  void 답이_int_범위를_넘으면_8776444321을_반환한다() {
    assertThat(sut.solution(2_147_483_647L)).isEqualTo(8_776_444_321L);
  }

  // Step 10: 제약 상한 80억 — 입력 자체가 int 범위를 넘는 유일한 구간이다.
  //          `(int) n % 10`은 `((int) n) % 10`으로 묶이므로(캐스팅이 %보다 먼저다)
  //          래핑된 음수에서 엉뚱한 자릿수를 읽어낸다. Step 8의 연속된 0과도 겹친다
  @Test
  void 제약_상한_80억은_8_뒤에_0이_아홉_개_붙는다() {
    assertThat(sut.solution(8_000_000_000L)).isEqualTo(8_000_000_000L);
  }

  // Step 11: 제약 안에서 답이 가장 커지는 입력 — 9,999,999,997을 낸다.
  //          Step 10은 답이 입력과 같아 "상한이면 n을 그대로" 같은 구현이 묻어갈 수 있으므로,
  //          최대 규모에서 입력과 다른 답을 요구해 그 틈을 막는다
  @Test
  void 답이_최대인_7999999999는_9999999997을_반환한다() {
    assertThat(sut.solution(7_999_999_999L)).isEqualTo(9_999_999_997L);
  }

  // Step 12: 최대 자릿수에 0이 흩어져 있는 경우 — Step 8(여러 개의 0)과 Step 9(int 초과)가
  //          한 입력에서 만난다. 0 다섯 개가 뒤로 밀려도 자릿수는 열 개 그대로다
  @Test
  void 열_자리_수에_흩어진_0들은_모두_뒤로_밀린다() {
    assertThat(sut.solution(1_020_304_050L)).isEqualTo(5_432_100_000L);
  }
}
