package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GroupAnagramsTest {
    @ParameterizedTest
    @MethodSource
    fun groupAnagrams(
        strs: Array<String>,
        expected: List<List<String>>,
    ) {
        val actual = GroupAnagrams().groupAnagrams(strs)
        assertThat(actual.map { it.sorted() }).containsExactlyInAnyOrderElementsOf(expected.map { it.sorted() })
    }

    companion object {
        @JvmStatic
        fun groupAnagrams(): List<Arguments> = listOf(
            Arguments.of(
                arrayOf("eat", "tea", "tan", "ate", "nat", "bat"),
                listOf(listOf("bat"), listOf("nat", "tan"), listOf("ate", "eat", "tea")),
            ),
            Arguments.of(arrayOf(""), listOf(listOf(""))),
            Arguments.of(arrayOf("a"), listOf(listOf("a"))),
        )
    }
}
