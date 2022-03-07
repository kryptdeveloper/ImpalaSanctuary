package com.saf.Dan.Warden;

public class Model
{
  String date,email,name,purl,phone, key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Model() {
    }

    public Model(String name, String date, String email, String phone, String purl, String key) {
        this.date = date;
        this.email = email;
        this.name = name;
        this.purl = purl;
        this.phone=phone;
        this.key=key;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPurl() {
        return purl;
    }
    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
