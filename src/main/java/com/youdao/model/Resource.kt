package com.youdao.model

import javax.xml.bind.annotation.*

//@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class Resource(
        @XmlList
        var stringMapModel: List<StringMapModel>,
        @XmlList
        var stringArrayMapModel: List<StringArrayMapModel>
)

//@XmlRootElement(name = "string")
@XmlAccessorType(XmlAccessType.FIELD)
data class StringMapModel(
        @XmlAttribute
        var name: String,
        @XmlValue
        var text: String)

//@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
data class Item(
        @XmlAttribute
        var name: String,
        @XmlValue
        var text: String)

//@XmlRootElement(name = "string-array")
@XmlAccessorType(XmlAccessType.FIELD)
data class StringArrayMapModel(
//        @XmlAttribute(name = "name")
        var key: String,

        var itemList: List<Item>)