package com.example.academy.tabs;

public class CertificateObject {
    String course_name, img_url;
    int code;
    double rating;
    public CertificateObject(String course_name, String img_url, double rating) {
        this.course_name = course_name;
        this.img_url = img_url;
        this.rating = rating;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
