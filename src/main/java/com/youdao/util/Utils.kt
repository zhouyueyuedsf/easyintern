package com.youdao.util

object Utils {
    @JvmStatic
    fun excelColNameToNumber(name: String): Int {
        var res = 0
        name.forEach {
            val num = it - 'A' + 1
            res = res * 26 + num
        }
        return res
    }
}