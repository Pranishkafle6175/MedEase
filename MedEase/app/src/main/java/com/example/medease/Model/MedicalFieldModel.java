package com.example.medease.Model;

public class MedicalFieldModel {
    int image;
    String description;


    public MedicalFieldModel(int image, String description) {
        this.image = image;
        this.description = description;
    }

    public MedicalFieldModel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
