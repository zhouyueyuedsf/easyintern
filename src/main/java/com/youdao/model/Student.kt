package com.youdao.model

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Student(@XmlElement val id: String,
              @XmlElement val name: String,
              @XmlElement val age: Int) {

    constructor() : this("", "", 2)
}