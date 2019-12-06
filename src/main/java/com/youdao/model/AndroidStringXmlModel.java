package com.youdao.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @XmlRootElement(name = "resources") 根元素为<resources></resource>
 * @XmlAccessorType 指定映射的字段类型
 * XmlAccessType.FIELD：映射这个类中的所有字段到XML
 * XmlAccessType.PROPERTY：映射这个类中的属性（get/set方法）到XML
 * XmlAccessType.PUBLIC_MEMBER：将这个类中的所有public的field或property同时映射到XML（默认）
 * XmlAccessType.NONE：不映射
 *
 * @XmlElement(name = "string") 映射根元素下的标签为<string>的子元素
 * @XmlAttribute(name = "name") 映射元素下的属性为name到xml,<string name = "xxx"></string>
 * @XmlValue 映射元素下的值,如<string name="">xxx</>，xxx表示该标签注释下的字段的值
 */
@XmlRootElement(name = "resources")
@XmlAccessorType(XmlAccessType.FIELD)
public class AndroidStringXmlModel {
    @XmlElement(name = "string")
    private List<StringMapModel> stringMapModelList;
    @XmlElement(name = "string-array")
    private List<StringArrayMapModel> stringArrayMapModels;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class StringMapModel {
        @XmlAttribute(name = "name")
        String name;
        @XmlValue
        String value;
        @XmlAttribute(name = "tools:ignore")
        String toolIgnore;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getToolIgnore() {
            return toolIgnore;
        }

        public void setToolIgnore(String toolIgnore) {
            this.toolIgnore = toolIgnore;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class StringArrayMapModel {
        @XmlAttribute(name = "name")
        String name;
        List<String> item;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getItems() {
            return item;
        }

        public void setItems(List<String> item) {
            this.item = item;
        }
    }

    public List<StringMapModel> getStringMapModelList() {
        return stringMapModelList;
    }

    public void setStringMapModelList(List<StringMapModel> stringMapModelList) {
        this.stringMapModelList = stringMapModelList;
    }

    public List<StringArrayMapModel> getStringArrayMapModels() {
        return stringArrayMapModels;
    }

    public void setStringArrayMapModels(List<StringArrayMapModel> stringArrayMapModels) {
        this.stringArrayMapModels = stringArrayMapModels;
    }

}

