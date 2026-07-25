package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class 두_정수_사이의_합Test {
  두_정수_사이의_합 sut = new 두_정수_사이의_합();

  // Step 1: smallest possible interval — a와 b가 같으면 구간에 원소가 하나뿐이므로
  //         "둘 중 아무 수나" 반환하라는 조건은 결국 그 수 자체를 뜻한다
  @Test
  void 두_수가_같으면_그_수를_그대로_반환한다() {
    assertThat(sut.solution(3, 3)).isEqualTo(3);
  }

  // Step 2: 원소가 둘인 최소 구간 — 양 끝을 모두 포함(closed interval)한다는 것을 고정한다
  @Test
  void 연속한_두_정수는_양_끝을_모두_더한다() {
    assertThat(sut.solution(3, 4)).isEqualTo(7);
  }

  // Step 3: official example 1 — 처음으로 내부 원소(4)가 생기는 구간.
  //         등차수열 공식을 쓸 때 개수(3)를 먼저 2로 나누면 8이 나와 여기서 걸린다
  @Test
  void 공식_예제1_3과_5_사이의_합은_12다() {
    assertThat(sut.solution(3, 5)).isEqualTo(12);
  }

  // Step 4: official example 3 — 대소관계가 정해져 있지 않으므로 a > b도 같은 구간으로 본다.
  //         정렬 없이 a부터 b까지 올라가는 루프는 여기서 0을 반환한다
  @Test
  void 공식_예제3_a가_b보다_커도_같은_구간으로_보고_12를_반환한다() {
    assertThat(sut.solution(5, 3)).isEqualTo(12);
  }

  // Step 5: 구간 전체가 음수 — 합도 음수가 되며, 절댓값만 다루는 구현은 +12를 낸다
  @Test
  void 두_수가_모두_음수면_합도_음수다() {
    assertThat(sut.solution(-5, -3)).isEqualTo(-12);
  }

  // Step 6: 0을 가로지르는 비대칭 구간 — -3..3이 상쇄되고 4만 남는다.
  //         음수 쪽을 빠뜨리면 10(=0+1+2+3+4)이 나온다
  @Test
  void 영을_가로지르는_구간은_음수_구간도_함께_더한다() {
    assertThat(sut.solution(-3, 4)).isEqualTo(4);
  }

  // Step 7: 대칭 구간은 완전히 상쇄된다 — 합이 0인 것은 "계산하지 않음"이 아니라
  //         실제 결과다. Step 6과 짝을 이뤄 상쇄 경계를 고정한다
  @Test
  void 영을_중심으로_대칭인_구간은_모두_상쇄되어_0이다() {
    assertThat(sut.solution(-3, 3)).isEqualTo(0);
  }

  // Step 8: 반대 방향 상쇄 — 원소 하나(-4)만 남아 음수가 된다
  @Test
  void 대칭_구간에_음수_하나가_더_붙으면_그_수만_남는다() {
    assertThat(sut.solution(-4, 3)).isEqualTo(-4);
  }

  // Step 9: 누적값이 int를 넘는 첫 구간 — 정답 50,000,005,000,000은 int 범위의 2만 배다.
  //         IntStream.rangeClosed(..).sum()처럼 int로 누적하면 여기서 무너진다
  @Test
  void 합이_int_범위를_넘으면_long으로_누적해_50000005000000을_반환한다() {
    assertThat(sut.solution(1, 10_000_000)).isEqualTo(50_000_005_000_000L);
  }

  // Step 10: 등차수열 공식의 중간항 오버플로 — 정답 1,500,013,378은 int에 들어가지만
  //          (b-a+1)*(a+b) = 3,000,026,756은 넘친다. long 캐스팅 없이 곱하면
  //          래핑되어 -647,470,270이 나오므로 Step 9로는 잡히지 않는 결함을 잡는다
  @Test
  void 정답은_int에_들어가도_공식의_중간_곱이_넘치면_안_된다() {
    assertThat(sut.solution(1, 54_772)).isEqualTo(1_500_013_378L);
  }

  // Step 11: 음수 방향 최대 크기 + 역순 인자 — Step 4와 Step 9의 결합
  @Test
  void 인자가_역순인_음수_최대_구간은_마이너스50000005000000을_반환한다() {
    assertThat(sut.solution(0, -10_000_000)).isEqualTo(-50_000_005_000_000L);
  }

  // Step 12: 제약 하한에서의 a == b — Step 1을 최소 경계값에서 다시 확인한다
  @Test
  void 최솟값끼리_같으면_마이너스10000000을_반환한다() {
    assertThat(sut.solution(-10_000_000, -10_000_000)).isEqualTo(-10_000_000L);
  }

  // Step 13: 가능한 가장 넓은 구간(20,000,001개) — 결과는 0이지만 누적 도중
  //          부분합이 -50조까지 내려가므로 int 누적기는 여기서도 깨진다.
  //          O(n) 루프 구현이 시간 안에 끝나는지도 함께 확인한다
  @Test
  void 제약_전체_구간은_완전히_상쇄되어_0이다() {
    assertThat(sut.solution(-10_000_000, 10_000_000)).isEqualTo(0L);
  }

  // Step 14: Step 13에서 상한만 1 줄인 구간 — 최대 규모에서 0이 아닌 답을 요구해
  //          "무조건 0을 반환하는" 구현이 Step 13에 묻어가는 것을 막는다
  @Test
  void 제약_전체_구간에서_상한만_하나_줄이면_마이너스10000000을_반환한다() {
    assertThat(sut.solution(-10_000_000, 9_999_999)).isEqualTo(-10_000_000L);
  }
}
