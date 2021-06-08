package com.thundersharp.bombaydine.user.core.Model;

public class NamePhone {

    public NamePhone(){}

    private String name,phone;

    public NamePhone(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
