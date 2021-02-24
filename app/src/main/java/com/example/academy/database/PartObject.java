package com.example.academy.database;

public class PartObject {
    String course_name, name, number, youtube, music, pdf, language;

    public PartObject(String course_name, String name, String number, String youtube, String music, String pdf, String language) {
        this.course_name = course_name;
        this.name = name;
        this.number = number;
        this.youtube = youtube;
        this.music = music;
        this.pdf = pdf;
        this.language = language;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
