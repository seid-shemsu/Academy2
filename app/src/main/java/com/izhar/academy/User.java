package com.izhar.academy;

public class User {
    private String name;
    private String phone;
    private String email;
    private String country;
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User(String name, String phone, String email, String country){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.country = country;
    }

    public User(String name, String phone, String email, String country, String gender) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.country = country;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
