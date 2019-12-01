package com.youdao.model

import javax.xml.bind.annotation.*

@XmlRootElement(name = "resource")
@XmlAccessorType(XmlAccessType.FIELD)
data class AndroidStringXmlModel(
        @XmlList
        var stringMapModel: List<StringMapModel>,
        @XmlList
        var stringArrayMapModel: List<StringArrayMapModel>
)

@XmlRootElement(name = "string")
@XmlAccessorType(XmlAccessType.FIELD)
data class StringMapModel(
        @XmlAttribute(name = "name")
        var key: String,
        @XmlValue
        var text: String)

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
data class ItemMapModel(
        @XmlAttribute(name = "name")
        var key: String,
        @XmlValue
        var text: String)

@XmlRootElement(name = "string-array")
@XmlAccessorType(XmlAccessType.FIELD)
data class StringArrayMapModel(
        @XmlAttribute(name = "name")
        var key: String,

        var itemList: List<ItemMapModel>)