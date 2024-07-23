package com.giwankim.programmers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompareNumbersKtTest {
    @Test
    fun compareNumbers() {
        val compareNumbers = CompareNumbersKt()
        assertThat(compareNumbers.solution(2, 3)).isEqualTo(-1)
        assertThat(compareNumbers.solution(11, 11)).isEqualTo(1)
        assertThat(compareNumbers.solution(7, 99)).isEqualTo(-1)
    }
}
