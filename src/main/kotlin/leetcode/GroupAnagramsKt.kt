package leetcode

class GroupAnagramsKt {
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        val groups: MutableMap<String, MutableList<String>> = mutableMapOf()
        for (str in strs) {
            val key = str.toCharArray().sorted().joinToString("")
            groups.getOrPut(key) { mutableListOf() }
            groups[key]?.add(str)
        }
        return groups.values.map { it.toList() }
    }
}
