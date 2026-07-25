package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class 숫자_문자열과_영단어Test {
  숫자_문자열과_영단어 sut = new 숫자_문자열과_영단어();

  // Step 1: smallest valid input (1 ≦ s의 길이) 이면서 return 값의 하한 1 —
  //         영단어가 하나도 없으므로 s를 그대로 정수로 읽기만 해도 통과한다
  @Test
  void 한_자리_숫자_문자열은_그_숫자를_반환한다() {
    assertThat(sut.solution("1")).isEqualTo(1);
  }

  // Step 2: 영단어를 처음 요구한다 — Step 1을 통과시킨 Integer.parseInt(s) 한 줄은
  //         여기서 NumberFormatException으로 무너진다
  @Test
  void 한_자리_영단어는_대응되는_숫자를_반환한다() {
    assertThat(sut.solution("one")).isEqualTo(1);
  }

  // Step 3: official example 4 — 자릿수는 이어 붙는 것이지 더해지는 것이 아니다.
  //         자리마다 값을 합산하는 구현은 6을 낸다
  @Test
  void 공식_예제4_영단어가_없으면_숫자_문자열을_그대로_읽는다() {
    assertThat(sut.solution("123")).isEqualTo(123);
  }

  // Step 4: 영단어도 자릿수로서 이어 붙는다 — 1과 2를 더해 3을 내는 구현,
  //         또는 찾은 순서를 뒤집어 21을 내는 구현은 여기서 걸린다
  @Test
  void 영단어가_연달아_나오면_자릿수_순서대로_이어_붙는다() {
    assertThat(sut.solution("onetwo")).isEqualTo(12);
  }

  // Step 5: 숫자와 영단어가 한 문자열에 섞이는 최소 사례 — 숫자가 먼저 온다
  @Test
  void 숫자_뒤에_영단어가_오면_원래_자리를_지킨다() {
    assertThat(sut.solution("2three")).isEqualTo(23);
  }

  // Step 6: Step 5의 반대 순서 — 영단어를 따로 모아 앞이나 뒤에 몰아 붙이는 구현은
  //         두 사례 중 하나에서 반드시 어긋난다 (앞에 몰면 여기서 23을 낸다)
  @Test
  void 영단어_뒤에_숫자가_오면_원래_자리를_지킨다() {
    assertThat(sut.solution("three2")).isEqualTo(32);
  }

  // Step 7: 구분자 없이 맞붙은 두 영단어 — 단어 경계를 숫자로 짚을 수 없다.
  //         공식 예제1의 뒷부분이며, 단어 하나만 읽고 멈추는 구현은 7이나 8을 낸다
  @Test
  void 영단어_두_개가_구분자_없이_붙어_있어도_각각_해석한다() {
    assertThat(sut.solution("seveneight")).isEqualTo(78);
  }

  // Step 8: "zero"도 0이라는 자릿수다 — 빈 문자열로 지우거나 건너뛰는 구현은 1을 낸다
  @Test
  void zero는_빈_문자열이_아니라_숫자_0으로_한_자리를_차지한다() {
    assertThat(sut.solution("1zero")).isEqualTo(10);
  }

  // Step 9: 같은 영단어가 두 번 나온다 (문제 설명의 10203 예시) — replaceFirst나
  //         indexOf 한 번으로 끝내는 구현은 뒤쪽 "zero"를 남겨 파싱에서 터진다
  @Test
  void 같은_영단어가_여러_번_나오면_모두_치환된다() {
    assertThat(sut.solution("1zerotwozero3")).isEqualTo(10203);
  }

  // Step 10: 첫 글자가 같고 길이까지 같은 짝(four/five) — 두 번째 글자를 보지 않고
  //          'f'만으로 단어를 고르는 구현은 두 사례 중 하나에서 44나 55를 낸다
  @Test
  void 첫_글자가_f로_같은_four와_five를_구별한다() {
    assertThat(sut.solution("fourfive")).isEqualTo(45);
  }

  // Step 11: Step 10의 대칭 사례 — 순서를 바꿔도 각 단어가 제 숫자로 읽혀야 한다
  @Test
  void four와_five의_순서가_바뀌어도_각각_해석한다() {
    assertThat(sut.solution("fivefour")).isEqualTo(54);
  }

  // Step 12: 첫 글자가 같고 길이는 다른 짝(six 3자, seven 5자) — 첫 글자별로 잘라낼
  //          길이를 고정한 구현은 남거나 모자란 글자 때문에 무너진다
  @Test
  void 첫_글자가_s로_같고_길이가_다른_six와_seven을_구별한다() {
    assertThat(sut.solution("sixseven")).isEqualTo(67);
  }

  // Step 13: Step 12의 대칭 사례 — 긴 단어가 먼저 온다
  @Test
  void six와_seven의_순서가_바뀌어도_각각_해석한다() {
    assertThat(sut.solution("sevensix")).isEqualTo(76);
  }

  // Step 14: 첫 글자가 t로 같은 마지막 짝(two 3자, three 5자) — "th"까지 봐야 갈린다
  @Test
  void 첫_글자가_t로_같은_two와_three를_구별한다() {
    assertThat(sut.solution("twothree")).isEqualTo(23);
  }

  // Step 15: Step 14의 대칭 사례 — 이로써 첫 글자가 겹치는 세 짝을 모두 양방향으로 덮는다
  @Test
  void two와_three의_순서가_바뀌어도_각각_해석한다() {
    assertThat(sut.solution("threetwo")).isEqualTo(32);
  }

  // Step 16: official example 1 — 영단어로 시작하고 영단어로 끝나며 중간에 숫자가 끼어
  //          Step 5·6·7이 한 문자열에서 동시에 요구된다
  @Test
  void 공식_예제1_one4seveneight는_1478이다() {
    assertThat(sut.solution("one4seveneight")).isEqualTo(1478);
  }

  // Step 17: official example 2 — 숫자로 시작하고 숫자로 끝나며 영단어가 사이에 흩어져 있다
  @Test
  void 공식_예제2_23four5six7은_234567이다() {
    assertThat(sut.solution("23four5six7")).isEqualTo(234567);
  }

  // Step 18: official example 3 — 예제2와 자릿수 구성이 다른데도 같은 값이 나와야 한다.
  //          같은 정답을 가리키는 표현이 여러 가지임을 고정한다
  @Test
  void 공식_예제3_2three45sixseven도_예제2와_같은_234567이다() {
    assertThat(sut.solution("2three45sixseven")).isEqualTo(234567);
  }

  // Step 19: 열 개 영단어가 각각 정확히 한 번씩 — 대응표 전체를 한 사례로 검증한다.
  //          "zero"가 맨 끝이므로 제한사항(s는 "zero"로 시작하지 않는다)도 지킨다
  @Test
  void 열_개_영단어가_모두_한_번씩_나오면_1234567890이_된다() {
    assertThat(sut.solution("onetwothreefourfivesixseveneightninezero")).isEqualTo(1_234_567_890);
  }

  // Step 20: return 값의 상한 2,000,000,000을 숫자로만 표현한 사례 —
  //          열 자리 값이 int 범위 안에서 온전히 유지되는지 확인한다
  @Test
  void 상한값_2000000000을_숫자_문자열로_읽는다() {
    assertThat(sut.solution("2000000000")).isEqualTo(2_000_000_000);
  }

  // Step 21: 같은 상한값을 영단어로만 표현한 사례 — "zero" 아홉 개가 남김없이 치환되어야
  //          하므로 Step 9의 반복 치환을 가장 긴 형태로 다시 확인한다
  @Test
  void 상한값_2000000000을_영단어로만_표현해도_같은_값이다() {
    assertThat(sut.solution("two" + "zero".repeat(9))).isEqualTo(2_000_000_000);
  }

  // Step 22: 제한사항이 실제로 허용하는 가장 긴 입력 — 48자.
  //          값이 2,000,000,000 이하라 자릿수는 최대 열이고, 첫 자리가 2면 나머지 아홉
  //          자리가 모두 0이어야 하므로(= 39자) 가장 긴 입력의 첫 단어는 "one"(3자)이다.
  //          남은 아홉 자리를 가장 긴 영단어(5자)로 채운 3 + 45 = 48자가 상한이며,
  //          제한사항에 적힌 길이 50은 어떤 올바른 입력으로도 도달할 수 없다
  @Test
  void 제한사항이_허용하는_가장_긴_48자_입력을_처리한다() {
    String s = "one" + "three".repeat(9);
    assertThat(s).hasSize(48);
    assertThat(sut.solution(s)).isEqualTo(1_333_333_333);
  }
}
