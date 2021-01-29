package com.example.academy.users;

public class UserObject {
    String name, uri, phone;

    public UserObject(String name, String uri, String phone) {
        this.name = name;
        this.uri = uri;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}