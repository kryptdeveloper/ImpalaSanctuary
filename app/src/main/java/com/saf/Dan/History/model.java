package com.saf.Dan.History;

public class model
{
  String date,email,name,purl,mpesa,phone,amount,nopeople,unique, room_price,room_name;

    public String getRname() {
        return room_name;
    }
    public void setRname(String room_name) {
        this.room_name = room_name;
    }

    public String getRprice() {
        return room_price;
    }
    public void setRprice(String room_price) {
        this.room_price = room_price;
    }



    public String getunique() {
        return unique;
    }
    public void setunique(String unique) {
        this.unique = unique;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNopeople() {
        return nopeople;
    }

    public void setNopeople(String nopeople) {
        this.nopeople = nopeople;
    }

    public model() {
    }

    public model(String name, String date, String email, String phone,String purl, String mpesa,String amount,String nopeople, String unique,String room_price,String room_name) {
        this.date = date;
        this.email = email;
        this.name = name;
        this.purl = purl;
        this.phone=phone;
        this.mpesa=mpesa;
        this.amount=amount;
        this.nopeople=nopeople;
        this.unique=unique;

        this.room_price=room_price;
        this.room_name=room_name;
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
