package com.saf.Dan.finance;

public class model
{
  String date;
  String email;
  String name;

    public String getunique() {
        return unique;
    }

    public void setunique(String unique) {
        this.unique = unique;
    }

    String purl;
    String mpesa;
    String phone;
    String unique;

    public model() {
    }

    public model(String name, String date, String email, String phone,String purl, String mpesa,String unique) {
        this.date = date;
        this.email = email;
        this.name = name;
        this.purl = purl;
        this.phone=phone;
        this.mpesa=mpesa;
        this.unique=unique;
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

    public String getMpesa() {
        return mpesa;
    }
    public void setMpesa(String mpesa) {
        this.mpesa = mpesa;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
