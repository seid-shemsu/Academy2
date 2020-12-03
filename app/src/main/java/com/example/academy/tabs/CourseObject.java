package com.example.academy.tabs;

public class CourseObject {
    String course_name, progress, img_url;
    int rating;

    public CourseObject(String course_name, String progress, String img_url, int rating) {
        this.course_name = course_name;
        this.progress = progress;
        this.img_url = img_url;
        this.rating = rating;
    }

    public CourseObject(String course_name, String img_url, int rating) {
        this.course_name = course_name;
        this.img_url = img_url;
        this.rating = rating;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
