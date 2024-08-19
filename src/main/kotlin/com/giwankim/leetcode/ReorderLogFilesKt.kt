package com.giwankim.leetcode

class ReorderLogFilesKt {
    fun reorderLogFiles(logs: Array<String>): Array<String> {
        val letterLogs = mutableListOf<String>()
        val digitLogs = mutableListOf<String>()

        for (log in logs) {
            if (Character.isDigit(log.split(" ")[1][0])) {
                digitLogs.add(log)
            } else {
                letterLogs.add(log)
            }
        }

        letterLogs.sortWith { s1, s2 ->
            val tokens1 = s1.split(" ", limit = 2)
            val tokens2 = s2.split(" ", limit = 2)
            val compareTo = tokens1[1].compareTo(tokens2[1])
            if (compareTo == 0) {
                tokens1[0].compareTo(tokens2[0])
            } else {
                compareTo
            }
        }

//        letterLogs.sortWith(compareBy<String> { it.split(" ", limit = 2)[1] }.thenBy { it.split(" ", limit = 2)[0] })

        return (letterLogs + digitLogs).toTypedArray()
    }
}
