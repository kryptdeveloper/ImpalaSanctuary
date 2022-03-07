package com.saf.Dan.accomodation;

public class ImageUploadInfo {
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String desc, price;

    public ImageUploadInfo(String desc, String price, String imageName, String status, String houseType, String imageURL) {
        this.desc = desc;
        this.price = price;
        this.imageName = imageName;
        this.status = status;
        this.houseType = houseType;
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String imageName,status;
    public String houseType;
    public String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String imageName, String houseType, String imageURL, String price, String desc) {
        this.imageName = imageName;
        this.houseType = houseType;
        this.imageURL = imageURL;
        this.price = price;
        this.desc = desc;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void sethouseType(String houseType) {
        this.houseType = houseType;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public String gethouseType() {
        return houseType;
    }

    public String getImageURL() {
        return imageURL;
    }

}
