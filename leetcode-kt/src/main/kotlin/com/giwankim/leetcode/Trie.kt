package com.giwankim.leetcode

class TrieNode {
    var isWord: Boolean = false
    val children: Array<TrieNode?> = arrayOfNulls(26)

    fun contains(c: Char): Boolean = children[c - 'a'] != null

    fun child(c: Char): TrieNode? = children[c - 'a']
}

class Trie {
    val root: TrieNode = TrieNode()

    fun insert(word: String) {
        var cur = root
        for (c in word.lowercase()) {
            if (!cur.contains(c)) {
                cur.children[c - 'a'] = TrieNode()
            }
            cur = cur.child(c)!!
        }
        cur.isWord = true
    }

    fun search(word: String): Boolean {
        var cur: TrieNode = root
        for (c in word.lowercase()) {
            if (!cur.contains(c)) {
                return false
            }
            cur = cur.child(c)!!
        }
        return cur.isWord
    }

    fun startsWith(prefix: String): Boolean {
        var cur: TrieNode = root
        for (c in prefix.lowercase()) {
            if (!cur.contains(c)) {
                return false
            }
            cur = cur.child(c)!!
        }
        return true
    }
}
