package com.youdao.util

import com.youdao.model.AndroidStringXmlModel
import javax.xml.bind.JAXBContext


object XmlUtil {
    fun readStringsXml(path: String): AndroidStringXmlModel {
        val mJaxb = JAXBContext.newInstance(AndroidStringXmlModel::class.java)
        val unmarshaller = mJaxb.createUnmarshaller()
        val stream = XmlUtil::class.java.classLoader.getResourceAsStream(path)
        val model = unmarshaller.unmarshal(stream) as AndroidStringXmlModel
        return model
    }
}

fun main() {
    XmlUtil.readStringsXml("strings.xml")
}
