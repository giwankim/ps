package com.giwankim.leetcode

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CountBinarySubstringsTest {
    private val sut = CountBinarySubstrings()

    @Test
    fun countBinarySubstrings() {
        assertSoftly {
            sut.countBinarySubstrings("00110011") shouldBe 6
            sut.countBinarySubstrings("10101") shouldBe 4
        }
    }
}
