package com.saf.Dan.accomodation;
public class User {
    String phone;
    String name;
    String date;
public User(){

}
    public User( String amount,String date,String email, String mpesa,String name, String nopeople,String phone, String purl) {
        this.phone = phone;
        this.name = name;
        this.date = date;
        this.nopeople = nopeople;
        this.amount = amount;
        this.email = email;
        this.mpesa = mpesa;
        this.purl = purl;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNopeople() {
        return nopeople;
    }

    public void setNopeople(String nopeople) {
        this.nopeople = nopeople;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMpesa() {
        return mpesa;
    }

    public void setMpesa(String mpesa) {
        this.mpesa = mpesa;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    String nopeople;
    String amount;
    String email;
    String mpesa;
    String purl;

}