package com.youdao.util

import com.youdao.model.Resource
import java.io.FileInputStream
import javax.xml.bind.JAXBContext


object XmlUtil {
    fun readStringsXml(path: String): Resource {
        val mJaxb = JAXBContext.newInstance(Resource::class.java)
        val unmarshaller = mJaxb.createUnmarshaller()
        val stream = FileInputStream(path)
        val model = unmarshaller.unmarshal(stream) as Resource
        return model
    }
}

fun main() {
    XmlUtil.readStringsXml("strings.xml")
}
