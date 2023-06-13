package com.example.medease.Model;

public class Doctors {
    private String Age;
    private String CurrentPractiseSiteLocation;
    private  String Experience;
    private  String Gender;
    private String Id;
    private String MobileNo;
    private String Qualification;
    private String Speciality;
    private String Username;
    private String email;

    public Doctors() {
    }

    public Doctors(String age, String currentPractiseSiteLocation, String experience, String gender, String id, String mobileNo, String qualification, String speciality, String username, String email) {
        Age = age;
        CurrentPractiseSiteLocation = currentPractiseSiteLocation;
        Experience = experience;
        Gender = gender;
        Id = id;
        MobileNo = mobileNo;
        Qualification = qualification;
        Speciality = speciality;
        Username = username;
        this.email = email;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getCurrentPractiseSiteLocation() {
        return CurrentPractiseSiteLocation;
    }

    public void setCurrentPractiseSiteLocation(String currentPractiseSiteLocation) {
        CurrentPractiseSiteLocation = currentPractiseSiteLocation;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
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

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
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
}
