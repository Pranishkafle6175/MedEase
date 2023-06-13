package com.example.medease.Model;

public class NormalUsers {

    private String Age;
    private  String Gender;
    private String Id;
    private String MobileNo;
    private String Username;
    private String email;
    private String Image;

    public NormalUsers() {
    }

    public NormalUsers(String age, String gender, String id, String mobileNo, String username, String email, String image) {
        Age = age;
        Gender = gender;
        Id = id;
        MobileNo = mobileNo;
        Username = username;
        this.email = email;
        Image = image;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}