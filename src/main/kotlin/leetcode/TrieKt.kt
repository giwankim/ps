package leetcode

class TrieNodeKt {
    var isWord: Boolean = false
    val children: Array<TrieNodeKt?> = arrayOfNulls(26)

    fun contains(c: Char): Boolean = children[c - 'a'] != null

    fun child(c: Char): TrieNodeKt? = children[c - 'a']
}

class TrieKt {
    val root: TrieNodeKt = TrieNodeKt()

    fun insert(word: String) {
        var cur = root
        for (c in word.lowercase()) {
            if (!cur.contains(c)) {
                cur.children[c - 'a'] = TrieNodeKt()
            }
            cur = cur.child(c)!!
        }
        cur.isWord = true
    }

    fun search(word: String): Boolean {
        var cur: TrieNodeKt = root
        for (c in word.lowercase()) {
            if (!cur.contains(c)) {
                return false
            }
            cur = cur.child(c)!!
        }
        return cur.isWord
    }

    fun startsWith(prefix: String): Boolean {
        var cur: TrieNodeKt = root
        for (c in prefix.lowercase()) {
            if (!cur.contains(c)) {
                return false
            }
            cur = cur.child(c)!!
        }
        return true
    }
}
