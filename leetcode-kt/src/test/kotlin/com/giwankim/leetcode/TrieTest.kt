package com.giwankim.leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrieTest {
    private lateinit var trie: Trie

    @BeforeEach
    fun setUp() {
        trie = Trie()
    }

    @Test
    fun insert() {
        trie.insert("apple")
        assertThat(trie.search("apple")).isTrue()
    }

    @Test
    fun search() {
        trie.insert("apple")
        assertThat(trie.search("apple")).isTrue()
        assertThat(trie.search("app")).isFalse()
    }

    @Test
    fun startsWith() {
        trie.insert("apple")
        assertThat(trie.startsWith("app")).isTrue()
    }

    @Test
    fun trie() {
        trie.insert("apple")

        assertThat(trie.search("apple")).isTrue()
        assertThat(trie.search("app")).isFalse()
        assertThat(trie.startsWith("app")).isTrue()

        trie.insert("app")

        assertThat(trie.search("app")).isTrue()
    }
}
