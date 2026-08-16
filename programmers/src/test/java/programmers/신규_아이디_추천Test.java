package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 신규_아이디_추천Test {
  신규_아이디_추천 sut = new 신규_아이디_추천();

  // Step 1: 이미 규칙에 맞는 아이디는 손대지 않는다 — 7단계 중 어느 것도 걸리지 않는
  //         가장 단순한 입력이다. 빈 문자열을 돌려주는 구현이 여기서 걸린다
  @Test
  void 규칙에_맞는_아이디는_그대로_반환한다() {
    assertThat(sut.solution("abc")).isEqualTo("abc");
  }

  // Step 2: 1단계 — 대문자는 소문자가 된다. 2단계를 1단계보다 먼저 돌리는 구현은
  //         대문자를 허용되지 않는 문자로 보고 모두 지워 "aaa"를 낸다
  @Test
  void 대문자를_소문자로_바꾼다() {
    assertThat(sut.solution("ABC")).isEqualTo("abc");
  }

  // Step 3: 1단계는 대문자만 내린다 — 대소문자를 뒤집는 구현은 "AbC"를 낸다
  @Test
  void 소문자는_그대로_두고_대문자만_내린다() {
    assertThat(sut.solution("aBc")).isEqualTo("abc");
  }

  // Step 4: 2단계 — 허용 문자는 소문자, 숫자, 빼기, 밑줄, 마침표 다섯 가지다.
  //         영숫자만 남기는 구현은 "a1bcd"를, 기호를 싸잡아 지우는 구현은 "a1bcd"를 낸다
  @Test
  void 허용된_다섯_종류의_문자는_모두_남는다() {
    assertThat(sut.solution("a1-b_c.d")).isEqualTo("a1-b_c.d");
  }

  // Step 5: 2단계 — 제한사항이 열거한 특수문자를 한 번에 확인한다.
  //         허용 목록이 아니라 몇몇 기호만 골라 지우는 구현이 여기서 걸린다
  @Test
  void 허용되지_않는_특수문자를_모두_제거한다() {
    assertThat(sut.solution("abc~!@#$%^&*()=+[{]}:?,<>/")).isEqualTo("abc");
  }

  // Step 6: 3단계 — 연달아 붙은 마침표는 하나로 줄어든다.
  //         3단계를 건너뛰는 구현은 "a..b"를 그대로 낸다
  @Test
  void 연속된_마침표를_하나로_줄인다() {
    assertThat(sut.solution("a..b")).isEqualTo("a.b");
  }

  // Step 7: 3단계는 몇 개가 붙어 있든 하나로 줄인다 — 두 개씩 짝지어 한 번만 치환하는
  //         구현은 앞의 두 개만 합쳐 "a..b"를 낸다. 홀수 개가 그 구현을 드러낸다
  @Test
  void 세_개_이상_연속된_마침표도_하나로_줄인다() {
    assertThat(sut.solution("a...b")).isEqualTo("a.b");
  }

  // Step 8: 순서 — 2단계가 3단계보다 먼저다. 마침표 사이의 '!'는 2단계에서 사라지고
  //         그제서야 마침표가 맞붙는다. 3단계를 먼저 돌리는 구현은 이때 붙은 마침표를
  //         보지 못해 "a..b"를 낸다
  @Test
  void 특수문자를_지운_뒤에_생긴_연속_마침표도_합친다() {
    assertThat(sut.solution("a.!.b")).isEqualTo("a.b");
  }

  // Step 9: 4단계 — 처음에 오는 마침표는 떨어진다
  @Test
  void 처음에_오는_마침표를_제거한다() {
    assertThat(sut.solution(".abc")).isEqualTo("abc");
  }

  // Step 10: 4단계 — 끝에 오는 마침표도 떨어진다
  @Test
  void 끝에_오는_마침표를_제거한다() {
    assertThat(sut.solution("abc.")).isEqualTo("abc");
  }

  // Step 11: 4단계는 양쪽을 모두 본다 — 한쪽을 떼면 끝내는 if/else 구현은
  //          ".abc." 에서 한쪽만 떼어 "abc." 또는 ".abc"를 낸다
  @Test
  void 처음과_끝의_마침표를_함께_제거한다() {
    assertThat(sut.solution(".abc.")).isEqualTo("abc");
  }

  // Step 12: 4단계는 양 끝만 본다 — 마침표를 전부 지우는 구현은 "ab"까지 줄인 뒤
  //          7단계까지 얹어 "abb"를 낸다
  @Test
  void 가운데_마침표는_제거하지_않는다() {
    assertThat(sut.solution(".a.b.")).isEqualTo("a.b");
  }

  // Step 13: 순서 — 3단계가 4단계보다 먼저다. 앞의 마침표 두 개는 먼저 하나로 합쳐진 뒤
  //          떨어져야 한다. 4단계를 먼저 돌려 마침표를 하나만 떼는 구현은 ".abc"를 낸다
  @Test
  void 처음에_연달아_붙은_마침표도_남김없이_제거한다() {
    assertThat(sut.solution("..abc")).isEqualTo("abc");
  }

  // Step 14: 7단계 — 길이가 2면 마지막 문자를 한 번 덧붙여 3을 채운다.
  //          첫 문자를 덧붙이는 구현은 "aba"를 낸다
  @Test
  void 길이가_2면_마지막_문자를_한_번_덧붙인다() {
    assertThat(sut.solution("ab")).isEqualTo("abb");
  }

  // Step 15: 7단계는 길이가 3이 될 때까지 반복한다 — 한 번만 덧붙이는 구현은 "aa"에서 멈춘다
  @Test
  void 길이가_1이면_마지막_문자를_두_번_덧붙인다() {
    assertThat(sut.solution("a")).isEqualTo("a".repeat(3));
  }

  // Step 16: 7단계가 덧붙이는 것은 어디까지나 "마지막 문자"다 — 5단계의 "a"와 헷갈려
  //          빈자리를 'a'로 메우는 구현은 "a-a"를 낸다
  @Test
  void 덧붙이는_문자는_알파벳이_아니어도_된다() {
    assertThat(sut.solution("a-")).isEqualTo("a--");
  }

  // Step 17: 순서 — 7단계는 4단계로 짧아진 결과를 보고 판단한다. 입력은 2자지만
  //          마침표가 떨어져 1자가 되므로 두 번 덧붙여야 한다. 4단계보다 먼저 길이를
  //          채우는 구현은 "a.."를 만든 뒤 마침표를 떼어 "a"를 낸다
  @Test
  void 마침표를_제거해_짧아진_길이를_기준으로_채운다() {
    assertThat(sut.solution("a.")).isEqualTo("aaa");
  }

  // Step 18: 5단계 — 마침표 하나만 남았다가 4단계에서 빈 문자열이 되면 "a"를 넣고,
  //          그 "a"가 다시 7단계를 거쳐 3자가 된다. 5단계를 건너뛰는 구현은 ""를 낸다
  @Test
  void 빈_문자열이_되면_a로_채워_3자를_만든다() {
    assertThat(sut.solution(".")).isEqualTo("aaa");
  }

  // Step 19: 6단계 — 16자면 앞 15자만 남는다. 6단계를 건너뛰는 구현은 16자를 그대로 낸다
  @Test
  void 길이가_16이면_앞의_15자만_남긴다() {
    assertThat(sut.solution("abcdefghijklmnop")).isEqualTo("abcdefghijklmno");
  }

  // Step 20: 6단계의 경계 — 15자는 자르지 않는다. 조건을 15자 이상으로 잘못 쓰고
  //          14자까지만 남기는 구현이 여기서 걸린다
  @Test
  void 길이가_15면_자르지_않는다() {
    assertThat(sut.solution("abcdefghijklmno")).isEqualTo("abcdefghijklmno");
  }

  // Step 21: 6단계의 뒷부분 — 자른 자리에 마침표가 끝으로 남으면 그것마저 뗀다.
  //          자르기만 하는 구현은 "0123456789abcd."를 낸다
  @Test
  void 자른_뒤_끝에_남은_마침표를_제거한다() {
    assertThat(sut.solution("0123456789abcd.e")).isEqualTo("0123456789abcd");
  }

  // Step 22: 6단계의 마침표 제거는 딱 한 자리만 짧게 만든다 — 14자가 된 결과를 다시
  //          15자로 늘리거나 7단계로 오해해 채우는 구현이 있다면 여기서 드러난다.
  //          Step 21과 같은 규칙을 길이 쪽에서 못박는다
  @Test
  void 끝의_마침표를_뗀_14자를_다시_채우지_않는다() {
    assertThat(sut.solution("0123456789abcd.e")).hasSize(14);
  }

  // Step 23: 순서 — 6단계는 2단계를 지난 길이로 판단한다. 입력은 26자지만 특수문자를
  //          지우고 나면 6자뿐이다. 원본 길이로 먼저 자르는 구현은 느낌표 15개만
  //          남긴 뒤 모두 지워 "aaa"를 낸다
  @Test
  void 자르기는_특수문자를_제거한_뒤의_길이로_판단한다() {
    assertThat(sut.solution("!".repeat(20) + "abcdef")).isEqualTo("abcdef");
  }

  // Step 24: 순서 — 6단계는 3단계를 지난 뒤에 자른다. 마침표를 먼저 합쳐야 15자의
  //          경계가 한 칸 뒤로 밀린다. 합치기 전에 자르는 구현은 한 글자 모자란
  //          "a.bcdefghijklm"을 낸다
  @Test
  void 마침표를_합친_뒤의_문자열을_자른다() {
    assertThat(sut.solution("a..bcdefghijklmnop")).isEqualTo("a.bcdefghijklmn");
  }

  // Step 25: 제한사항의 최대 길이 1,000 — 6단계가 15자로 줄인다
  @Test
  void 최대_길이_1000의_입력도_15자로_줄인다() {
    assertThat(sut.solution("a".repeat(1000))).isEqualTo("a".repeat(15));
  }

  // Step 26: 1,000자가 전부 마침표인 경우 — 3단계가 하나로 줄이고, 4단계가 그것마저
  //          떼어 빈 문자열이 된다. 가장 긴 입력이 가장 짧은 결과로 이어지는 사례다
  @Test
  void 마침표만_1000개인_입력은_aaa가_된다() {
    assertThat(sut.solution(".".repeat(1000))).isEqualTo("aaa");
  }

  // Step 27: official example 1 — 7단계 중 1~4단계와 6단계가 한꺼번에 걸리는 대표 사례
  @Test
  void 공식_예제1은_bat_y_abcdefghi다() {
    assertThat(sut.solution("...!@BaT#*..y.abcdefghijklm")).isEqualTo("bat.y.abcdefghi");
  }

  // Step 28: official example 2 — 특수문자 제거로 생긴 연속 마침표가 합쳐지고,
  //          끝의 마침표가 떨어진 뒤 7단계가 빼기(-)를 덧붙인다
  @Test
  void 공식_예제2는_z빼기빼기다() {
    assertThat(sut.solution("z-+.^.")).isEqualTo("z--");
  }

  // Step 29: official example 3 — 4단계에서 빈 문자열이 되어 5단계와 7단계가 함께 걸린다
  @Test
  void 공식_예제3은_aaa다() {
    assertThat(sut.solution("=.=")).isEqualTo("aaa");
  }

  // Step 30: official example 4 — 7단계 중 어느 것도 걸리지 않아 입력이 그대로 나온다
  @Test
  void 공식_예제4는_입력_그대로다() {
    assertThat(sut.solution("123_.def")).isEqualTo("123_.def");
  }

  // Step 31: official example 5 — 6단계에서 자른 뒤 끝에 남은 마침표까지 떼는 사례
  @Test
  void 공식_예제5는_abcdefghijklmn이다() {
    assertThat(sut.solution("abcdefghijklmn.p")).isEqualTo("abcdefghijklmn");
  }
}
