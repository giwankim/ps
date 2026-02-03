package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withTests
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class GroupAnagramsTest :
    FunSpec({
        val sut = GroupAnagrams()

        context("group anagrams") {
            withTests(
                nameFn = { (strs, expected) -> "strs=${strs.contentToString()}, expected=$expected" },
                GroupAnagramsCase(
                    arrayOf("eat", "tea", "tan", "ate", "nat", "bat"),
                    listOf(listOf("bat"), listOf("nat", "tan"), listOf("ate", "eat", "tea")),
                ),
                GroupAnagramsCase(arrayOf(""), listOf(listOf(""))),
                GroupAnagramsCase(arrayOf("a"), listOf(listOf("a"))),
            ) { (strs, expected) ->
                val actual = sut.groupAnagrams(strs).map { it.sorted() }
                actual shouldContainExactlyInAnyOrder expected.map { it.sorted() }
            }
        }
    })

private data class GroupAnagramsCase(
    val strs: Array<String>,
    val expected: List<List<String>>,
)
