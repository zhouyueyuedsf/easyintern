package com.youdao.model;

import javax.xml.bind.annotation.*;
import java.util.List;
@XmlRootElement(name = "resources")
@XmlAccessorType(XmlAccessType.FIELD)
public class AndroidStringXmlModel {
    @XmlElement(name = "string")
    private List<StringMapModel> stringMapModel;
    @XmlElement(name = "string-array")
    private List<StringArrayMapModel> stringArrayMapModels;

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class StringMapModel {
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
    private static class StringArrayMapModel {
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

    public List<StringMapModel> getStringMapModel() {
        return stringMapModel;
    }

    public void setStringMapModel(List<StringMapModel> stringMapModel) {
        this.stringMapModel = stringMapModel;
    }

    public List<StringArrayMapModel> getStringArrayMapModels() {
        return stringArrayMapModels;
    }

    public void setStringArrayMapModels(List<StringArrayMapModel> stringArrayMapModels) {
        this.stringArrayMapModels = stringArrayMapModels;
    }
}

