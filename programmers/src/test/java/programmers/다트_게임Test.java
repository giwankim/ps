package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 다트_게임Test {
  다트_게임 sut = new 다트_게임();

  // Step 1: 가장 단순한 입력 — 기회는 항상 3번이고, 세 점수를 모두 더한 값이 총점이다.
  //         한 번만 읽고 끝내는 구현은 1, 두 번만 읽는 구현은 2를 낸다
  @Test
  void 세_번의_기회_점수를_모두_더한다() {
    assertThat(sut.solution("1S1S1S")).isEqualTo(3);
  }

  // Step 2: 각 기회는 자기 숫자를 쓴다 — Step 1은 세 점수가 같아서 첫 숫자를 세 번 쓰는
  //         구현도 통과한다. 서로 다른 숫자를 주면 그런 구현은 3을 낸다
  @Test
  void 각_기회는_자기_자신의_숫자를_점수로_쓴다() {
    assertThat(sut.solution("1S2S3S")).isEqualTo(6);
  }

  // Step 3: D는 제곱이다 — 2를 쓰면 2² 과 2×2 가 모두 4라서 구분이 안 된다.
  //         3을 써야 제곱(9)과 2배(6)가 갈린다
  @Test
  void D는_점수를_제곱한다() {
    assertThat(sut.solution("3D1S1S")).isEqualTo(11);
  }

  // Step 4: T는 세제곱이다 — 3배(9)나 제곱(9)으로 계산하는 구현은 11을 내지만
  //         세제곱은 27이라 총점이 29가 된다
  @Test
  void T는_점수를_세제곱한다() {
    assertThat(sut.solution("3T1S1S")).isEqualTo(29);
  }

  // Step 5: 보너스는 기회마다 따로 읽는다 — 세 기회의 보너스가 모두 다르다.
  //         첫 보너스를 세 기회에 그대로 적용하는 구현은 2+3+4=9를 낸다
  @Test
  void 보너스는_기회마다_따로_적용된다() {
    assertThat(sut.solution("2S3D4T")).isEqualTo(75);
  }

  // Step 6: 10은 두 자리다 — 이 문제의 유일한 두 자리 점수. 문자를 하나씩 끊어 읽는
  //         구현은 "1"과 "0S"로 쪼개져 파싱 자체가 어긋난다
  @Test
  void 두_자리_점수_10을_한_숫자로_읽는다() {
    assertThat(sut.solution("10S1S1S")).isEqualTo(12);
  }

  // Step 7: 10이 중간에 와도 같다 — 첫 기회만 두 자리를 특별 취급하는 구현이 걸린다.
  //         제곱의 대상도 10이어야 하므로 100이며, 0² 이나 1² 로 읽으면 어긋난다
  @Test
  void 중간_기회의_10도_두_자리로_읽고_제곱한다() {
    assertThat(sut.solution("1S10D1S")).isEqualTo(102);
  }

  // Step 8: 한 기회가 낼 수 있는 최대 점수 10³ = 1000 — 두 자리 파싱과 세제곱이
  //         한 기회에서 동시에 요구된다
  @Test
  void 한_기회의_최대_점수는_10의_세제곱인_1000이다() {
    assertThat(sut.solution("10T1S1S")).isEqualTo(1002);
  }

  // Step 9: 0과 10의 0을 혼동하지 않는다 — 숫자만 훑어 모으는 구현은 "10", "0", "10"을
  //         "1", "0", "0", "1", "0"처럼 흩어 읽어 기회 수부터 틀린다
  @Test
  void 점수_0과_10에_들어있는_0을_구분한다() {
    assertThat(sut.solution("10S0S10S")).isEqualTo(20);
  }

  // Step 10: 0점도 유효한 점수다 — 0의 제곱과 세제곱은 모두 0이다.
  //          거듭제곱을 반복 곱셈의 초깃값 1로 시작해 지수를 잘못 세면 1이 새어 나온다
  @Test
  void 점수_0은_어떤_보너스에서도_0으로_남는다() {
    assertThat(sut.solution("0T0D1S")).isEqualTo(1);
  }

  // Step 11: 아차상(#)은 해당 점수를 마이너스로 만든다 — 옵션이 마지막 기회에 있어
  //          앞뒤 간섭이 없는 가장 단순한 사례다. 총점이 음수가 될 수 있다는 것도 함께 확정한다
  @Test
  void 아차상은_해당_기회의_점수를_음수로_만든다() {
    assertThat(sut.solution("1S1S3D#")).isEqualTo(-7);
  }

  // Step 12: 아차상의 효과는 자기 기회에서 끝난다 — Step 11의 대칭 사례.
  //          부호를 뒤집은 뒤 이후 기회까지 계속 음수로 누적하는 구현은 -11을 낸다
  @Test
  void 아차상은_뒤따르는_기회에는_영향을_주지_않는다() {
    assertThat(sut.solution("3D#1S1S")).isEqualTo(-7);
  }

  // Step 13: 스타상(*)은 자기 점수와 바로 전 점수를 각각 2배로 만든다 — 이 문제에서
  //          유일하게 뒤를 거슬러 올라가는 규칙이다. 자기 점수만 2배 하면 20이 된다
  @Test
  void 스타상은_자기_점수와_직전_점수를_각각_2배로_만든다() {
    assertThat(sut.solution("1S1S3D*")).isEqualTo(21);
  }

  // Step 14: 첫 기회의 스타상은 자기 점수만 2배로 만든다 — 이전 기회가 없다.
  //          무조건 이전 인덱스를 건드리는 구현은 여기서 인덱스 -1로 터진다
  @Test
  void 첫_기회의_스타상은_자기_점수만_2배로_만든다() {
    assertThat(sut.solution("3D*1S1S")).isEqualTo(20);
  }

  // Step 15: 스타상은 뒤가 아니라 앞을 본다 — 두 번째 기회의 *가 세 번째 기회까지
  //          2배로 만드는 구현은 12를 낸다
  @Test
  void 스타상은_뒤따르는_기회에는_영향을_주지_않는다() {
    assertThat(sut.solution("1S2S*3S")).isEqualTo(9);
  }

  // Step 16: 스타상끼리 중첩되면 4배가 된다 — 첫 점수는 자기 *로 한 번, 두 번째 기회의
  //          *로 다시 한 번 2배가 된다. 이미 2배된 값 위에 곱해야 하므로 (1×2)×2 = 4다.
  //          원래 점수를 기준으로 2배씩 더하는 구현은 첫 점수를 3으로 계산한다
  @Test
  void 스타상이_연달아_나오면_첫_점수는_4배가_된다() {
    assertThat(sut.solution("1S*2S*3S")).isEqualTo(11);
  }

  // Step 17: 스타상은 아차상이 뒤집어 놓은 값을 그대로 2배로 만든다 — 즉 -2배가 된다.
  //          부호를 무시하고 절댓값에 2를 곱하는 구현은 두 번째 점수를 +4로 만든다
  @Test
  void 스타상은_아차상으로_음수가_된_직전_점수를_그대로_2배로_만든다() {
    assertThat(sut.solution("1S2S#3S*")).isEqualTo(3);
  }

  // Step 18: 0점 기회의 스타상도 직전 점수는 2배로 만든다 — 자기 점수가 0이라 총점에
  //          기여하지 않지만 뒤를 거스르는 효과는 살아 있다. 점수가 0인 기회의 옵션을
  //          건너뛰는 구현은 -8을 낸다
  @Test
  void 점수가_0인_기회의_스타상도_직전_점수를_2배로_만든다() {
    assertThat(sut.solution("3D#0S*1S")).isEqualTo(-17);
  }

  // Step 19: official example 1 — 중간 기회의 스타상과 세제곱이 함께 나오는 대표 사례
  @Test
  void 공식_예제1_1S2D별3T는_37이다() {
    assertThat(sut.solution("1S2D*3T")).isEqualTo(37);
  }

  // Step 20: official example 2 — 아차상과 두 자리 점수 10이 한 입력에 함께 있다
  @Test
  void 공식_예제2_1D2S샵10S는_9이다() {
    assertThat(sut.solution("1D2S#10S")).isEqualTo(9);
  }

  // Step 21: official example 3 — 옵션이 하나도 없고 0점 기회가 섞인 사례
  @Test
  void 공식_예제3_1D2S0T는_3이다() {
    assertThat(sut.solution("1D2S0T")).isEqualTo(3);
  }

  // Step 22: official example 4 — 첫 기회의 스타상과 스타상 중첩이 동시에 나온다
  @Test
  void 공식_예제4_1S별2T별3S는_23이다() {
    assertThat(sut.solution("1S*2T*3S")).isEqualTo(23);
  }

  // Step 23: official example 5 — 아차상 위에 스타상이 겹쳐 -2배가 되는 사례
  @Test
  void 공식_예제5_1D샵2S별3S는_5이다() {
    assertThat(sut.solution("1D#2S*3S")).isEqualTo(5);
  }

  // Step 24: official example 6 — 총점이 음수가 되는 사례
  @Test
  void 공식_예제6_1T2D3D샵은_마이너스_4이다() {
    assertThat(sut.solution("1T2D3D#")).isEqualTo(-4);
  }

  // Step 25: official example 7 — 마지막 기회의 스타상이 직전 기회까지 2배로 만든다
  @Test
  void 공식_예제7_1D2S3T별은_59이다() {
    assertThat(sut.solution("1D2S3T*")).isEqualTo(59);
  }

  // Step 26: 제약 안에서 가능한 최고 점수 — 첫 점수는 두 번의 스타상을 거쳐 4000,
  //          두 번째도 4000, 세 번째는 2000이다. 값이 커지므로 중간 계산을 좁은 타입에
  //          담는 구현이 있다면 여기서 드러난다
  @Test
  void 최고_점수는_10000이다() {
    assertThat(sut.solution("10T*10T*10T*")).isEqualTo(10000);
  }

  // Step 27: 제약 안에서 가능한 최저 점수 — 세 기회 모두 10T에 아차상이 붙는다.
  //          Step 11의 부호 뒤집기를 가장 큰 점수에서 세 번 연달아 확인한다
  @Test
  void 최저_점수는_마이너스_3000이다() {
    assertThat(sut.solution("10T#10T#10T#")).isEqualTo(-3000);
  }
}
