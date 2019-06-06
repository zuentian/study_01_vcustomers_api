package com.example.zuer02.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String userName;
    private String numberPhone;
    private String email;
    private String education;
    private String graduationSchool;
    private String profession;
    private String profile;

    private String userCrtDate;
    private String userAltDate;

    public String getUserAltDate() {
        return userAltDate;
    }

    public void setUserAltDate(String userAltDate) {
        this.userAltDate = userAltDate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGraduationSchool() {
        return graduationSchool;
    }

    public void setGraduationSchool(String graduationSchool) {
        this.graduationSchool = graduationSchool;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCrtDate() {
        return userCrtDate;
    }

    public void setUserCrtDate(String userCrtDate) {
        this.userCrtDate = userCrtDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", email='" + email + '\'' +
                ", education='" + education + '\'' +
                ", graduationSchool='" + graduationSchool + '\'' +
                ", profession='" + profession + '\'' +
                ", profile='" + profile + '\'' +
                ", userCrtDate='" + userCrtDate + '\'' +
                ", userAltDate='" + userAltDate + '\'' +
                '}';
    }
}
