package com.saf.Dan.updateAnimals;

public class ImageUploadInfo {

    public String imageName;
    public String animalType;
    public String imageURL;
    public String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String imageName, String animalType, String imageURL, String desc) {
        this.imageName = imageName;
        this.animalType = animalType;
        this.imageURL = imageURL;
        this.desc=desc;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageName() {
        return imageName;
    }

    public String getAnimalType() {
        return animalType;
    }

    public String getImageURL() {
        return imageURL;
    }

}
