package com.example.medease.Model;

public class Products {
    String productName;
    String productPrice;
    String imageUrl;

    String productQuantity;
    String productType;
    String productDescription;
    public Products(String productName, String productPrice, String imageUrl, String productType, String productDescription) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.productDescription = productDescription;
        productQuantity = "0";
    }

    public Products(String productName, String productPrice, String imageUrl, String productDescription) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.productDescription = productDescription;
        productQuantity = "0";
    }

    public Products(){}
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

//    public String getProductQty() {
//        return productQty;
//    }
//
//    public void setProductQty(String productQty) {
//        this.productQty = productQty;
//    }

    public void setProductQuantity(String quantity) {
        productQuantity = quantity;
    }

    public  String getProductQuantity() {
        return productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public Integer getProductid() {
//        return productid;
//    }
//
//    public void setProductid(Integer productid) {
//        this.productid = productid;
//    }
}
