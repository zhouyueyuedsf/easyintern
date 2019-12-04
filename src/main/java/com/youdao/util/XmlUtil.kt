package com.youdao.util

import com.youdao.model.AndroidStringXmlModel
import com.youdao.model.Order
import com.youdao.model.Student
import cucumber.api.java.en.And
import java.io.FileInputStream
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller


object XmlUtil {
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
//    XmlUtil.readStringsXml("D:\\JavaProjects\\easyintern-gradle\\src\\main\\resources\\META-INF\\strings.xml")
//    print(String.format("%.1f", 1.2))
}
