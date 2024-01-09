package com.xw.entity;

import java.io.Serializable;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
public class Goods implements Serializable {

    private Long id; //商品编号
    private String title; //商品标题
    private String price; //商品价格
    private String image; //商品图片
    private String brand; //商品品牌

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }
}
