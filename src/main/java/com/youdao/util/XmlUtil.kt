package com.youdao.util

import com.youdao.model.AndroidStringXmlModel
import com.youdao.model.Student
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.StringReader
import java.lang.Boolean
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import kotlin.math.max
import kotlin.math.min


object XmlUtil {
    private const val BASE_LIKE_RATIO_OFFSET = 0.1f
    private const val BASE_LIKE_RATIO = 1 - BASE_LIKE_RATIO_OFFSET

    fun writeModelToXml(model: AndroidStringXmlModel?, path: String, append: kotlin.Boolean) { // 获取JAXB的上下文环境，需要传入具体的 Java bean -> 这里使用Student
        val context = JAXBContext.newInstance(AndroidStringXmlModel::class.java)
        // 创建 Marshaller 实例
        val marshaller = context.createMarshaller()
        // 设置转换参数 -> 这里举例是告诉序列化器是否格式化输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE)
        // 构建输出环境
        val out = FileOutputStream(path, append)
        // 将所需对象序列化 -> 该方法没有返回值
        marshaller.marshal(model, out)
    }
    fun readStringsXmlByPath(path: String): AndroidStringXmlModel {
        val mJaxb = JAXBContext.newInstance(AndroidStringXmlModel::class.java)
        val unmarshaller = mJaxb.createUnmarshaller()
        val stream = FileInputStream(path)
        val model = unmarshaller.unmarshal(stream) as AndroidStringXmlModel
        return model
    }

    fun readStringsXmlByString(stringXml: String): AndroidStringXmlModel {
        val mJaxb = JAXBContext.newInstance(AndroidStringXmlModel::class.java)
        val unmarshaller = mJaxb.createUnmarshaller()
        val stream = StringReader(stringXml)
        val model = unmarshaller.unmarshal(stream) as AndroidStringXmlModel
        return model
    }
    fun String.toTrim() = replace("&", "&amp;")

    fun syncXmlModelAndReturnUnAppendData(newStringXmlModel: AndroidStringXmlModel, oldStringXmlModel: AndroidStringXmlModel, outFile: File): Array<ArrayList<Pair<Int, Int>>> {
        // 记录未append的数据的位置信息，第一个为newPosition，第二个个oldPos
        val stringPosPairList = arrayListOf<Pair<Int, Int>>()
        val stringArrayPosPairList = arrayListOf<Pair<Int, Int>>()
        // 首先比较string标签，需求是比较键(key)和值（value）的相似度，如果相似则判断是更新的string标签，返回给调用者，继续操作即可
        val newStringMapModelList = newStringXmlModel.stringMapModelList
        val oldStringMapModelList = oldStringXmlModel.stringMapModelList
        val tempStringModelList = arrayListOf<AndroidStringXmlModel.StringMapModel>()
        for (newPos in newStringMapModelList.indices) {
            val newName = newStringMapModelList[newPos].name
            val newValue = newStringMapModelList[newPos].value
            for (oldPos in oldStringMapModelList.indices) {
                val oldName = oldStringMapModelList[oldPos].name
                val oldValue = oldStringMapModelList[oldPos].value
                var ratio = 0f
                // 如果值一样，就采用old的name
                val valueLikeRatio = like(newValue, oldValue)
                if (valueLikeRatio == 1f) {
                    continue
                }
                if (like(newName, oldName).also { ratio = it } > BASE_LIKE_RATIO) {
                    stringPosPairList.add(Pair(newPos, oldPos))
                } else {
                    // value和key的相似度要求成反关系
                    if (valueLikeRatio > max(1 - ratio, BASE_LIKE_RATIO)) {
                        stringPosPairList.add(Pair(newPos, oldPos))
                    } else {
                        tempStringModelList.add(newStringMapModelList[newPos])
                    }
                }
            }
        }
        tempStringModelList.forEach {
            newStringMapModelList.add(it)
        }
        return arrayOf(stringPosPairList, stringArrayPosPairList)
    }

    /**
     * 该函数返回相似度
     */
    fun like(newWord: String, oldWord: String): Float {
        if (newWord == oldWord) return 1f
        val distance = minDistance(newWord, oldWord).toFloat()
        return 1 - (distance / oldWord.length)
    }

    /**
     * 编辑距离算法，用于模糊搜索
     */
    private fun minDistance(word1: String, word2: String): Int {
        val n = word1.length
        val m = word2.length

        if (n * m == 0)
            return n + m
        val d = Array(n + 1) { IntArray(m + 1) }

        for (i in 0 until n + 1) {
            d[i][0] = 1
        }

        for (j in 0 until m + 1) {
            d[0][j] = j
        }
        for (i in 1 until n + 1) {
            for (j in 1 until m + 1) {
                val left = d[i - 1][j] + 1
                val down = d[i][j - 1] + 1
                var leftDown = d[i - 1][j - 1]
                if (word1[i - 1] != word2[j - 1]) {
                    leftDown += 1
                }
                d[i][j] = min(left, min(down, leftDown))
            }
        }
        return d[n][m]
    }


//    fun readTestXml(path: String) { // 获取JAXB的上下文环境，需要传入具体的 Java bean -> 这里使用Student
//        val context = JAXBContext.newInstance(Order::class.java)
//        // 创建 UnMarshaller 实例
//        val unmarshaller: Unmarshaller = context.createUnmarshaller()
//        // 加载需要转换的XML数据 -> 这里使用InputStream，还可以使用File，Reader等
//        val stream = FileInputStream(path)
//        // 将XML数据序列化 -> 该方法的返回值为Object基类，需要强转类型
//        val stu = unmarshaller.unmarshal(stream) as Order
//        // 将结果打印到控制台
//        println(stu)
//    }
}

fun main() {
    val testString = "<resources><string name = \"Synonyms_&amp;_Antonyms\">Synonyms&#160;&amp;&#160;Antonyms</string>\n" +
            "<string name = \"Synonyms\">Synonyms</string>\n" +
            "<string name = \"Antonyms\">Antonyms</string>\n" +
            "<string name = \"for_the_meaning_of\">for&#160;the&#160;meaning&#160;of&#160;“good”</string>\n" +
            "<string name = \"All\">All</string>\n" +
            "<string name = \"Detect_language\">Detect&#160;language</string>\n" +
            "<string name = \"Copy_to_Translate_is\">Copy&#160;to&#160;Translate&#160;is&#160;on</string>\n" +
            "<string name = \"More_options\">More&#160;options</string>\n" +
            "<string name = \"New_Version_Detected\">New&#160;Version&#160;Detected</string>\n" +
            "<string name = \"Concise（the_tab_name_of\">Concise（the&#160;tab&#160;name&#160;of&#160;Concise&#160;Definition）</string>\n" +
            "<string name = \"Larousse_Spanish（the_tab_name\">Larousse&#160;Spanish（the&#160;tab&#160;name&#160;of&#160;Larousse&#160;Spanish&#160;Dictionary）</string>\n" +
            "<string name = \"Oxford（the_tab_name_of\">Oxford（the&#160;tab&#160;name&#160;of&#160;Oxford&#160;Dictionary）</string>\n" +
            "<string name = \"Indonesian（the_tab_name_of\">Indonesian（the&#160;tab&#160;name&#160;of&#160;Indonesian&#160;Dictionary）</string>\n" +
            "<string name = \"Collins（the_tab_name_of\">Collins（the&#160;tab&#160;name&#160;of&#160;Collins&#160;Dictionary）</string>\n" +
            "<string name = \"WordNet（the_tab_name_of\">WordNet（the&#160;tab&#160;name&#160;of&#160;WordNet&#160;Dictionary）</string>\n" +
            "<string name = \"Your_native_language\">Your&#160;native&#160;language</string>\n" +
            "<string name = \"1._Upgraded_the_feature\">1.&#160;Upgraded&#160;the&#160;feature&#160;for&#160;Synonyms&#160;&amp;&#160;Antonyms,&#160;and&#160;Bilingual&#160;Sentences&#160;to&#160;help&#160;you&#160;learn&#160;English&#160;more&#160;comprehensively.\n" +
            "2.&#160;Optimized&#160;the&#160;search&#160;results&#160;of&#160;your&#160;native&#160;language,&#160;which&#160;will&#160;help&#160;you&#160;find&#160;the&#160;English&#160;words&#160;you&#160;want&#160;to&#160;use&#160;quickly.\n" +
            "3.&#160;Fixed&#160;the&#160;bug&#160;of&#160;Copy&#160;to&#160;Translate&#160;by&#160;adding&#160;a&#160;notification&#160;for&#160;this&#160;feature.</string>\n" +
            "<string name = \"Clear_all_the_search\">Clear&#160;all&#160;the&#160;search&#160;history&#160;below&#160;the&#160;search&#160;box.</string>\n" +
            "<string name = \"Copy_a_word_or\">Copy&#160;a&#160;word&#160;or&#160;sentence&#160;in&#160;any&#160;app&#160;to&#160;get&#160;the&#160;translation.</string>\n" +
            "<resources/>"
    XmlUtil.readStringsXmlByString(testString)
}
