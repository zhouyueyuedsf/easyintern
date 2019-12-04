package com.youdao.model;


import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Order")
public class Order {

    @XmlElement(name = "string-array")
    private List<Product> Product;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Product {
        @XmlAttribute(name = "id")
        private String id;
        private String name;
        //setters, getters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public List<Product> getProduct() {
        return Product;
    }

    public void setProduct(List<Product> product) {
        this.Product = product;
    }
}
