package leetcode

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MinStepsToMakeTwoStringsAnagramKtTest {
    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        textBlock = """
            bab | aba | 1
            leetcode | practice | 5
            anagram | mangaar | 0""",
    )
    fun minSteps(
        s: String,
        t: String,
        expected: Int,
    ) {
        val sut = MinStepsToMakeTwoStringsAnagramKt()
        sut.minSteps(s, t) shouldBe expected
    }
}
