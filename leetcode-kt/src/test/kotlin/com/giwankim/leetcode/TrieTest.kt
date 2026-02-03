package com.giwankim.leetcode

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrieTest :
    FunSpec({
        lateinit var sut: Trie

        beforeTest {
            sut = Trie()
        }

        context("trie") {
            test("insert") {
                sut.insert("apple")
                sut.search("apple") shouldBe true
            }

            test("search") {
                sut.insert("apple")
                sut.search("apple") shouldBe true
                sut.search("app") shouldBe false
            }

            test("startsWith") {
                sut.insert("apple")
                sut.startsWith("app") shouldBe true
            }

            test("trie") {
                sut.insert("apple")

                sut.search("apple") shouldBe true
                sut.search("app") shouldBe false
                sut.startsWith("app") shouldBe true

                sut.insert("app")

                sut.search("app") shouldBe true
            }
        }
    })
