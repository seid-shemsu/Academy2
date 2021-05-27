package com.izhar.academy.tabs;

public class CertificateObject {
    String course_name, img_url, code;
    double rating, mark;

    public CertificateObject(String course_name, String img_url, double rating, String code) {
        this.course_name = course_name;
        this.img_url = img_url;
        this.rating = rating;
        this.code = code;
    }

    public CertificateObject(String course_name, String img_url, double rating, String code, Double mark) {
        this.course_name = course_name;
        this.img_url = img_url;
        this.rating = rating;
        this.code = code;
        this.mark = mark;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
}
