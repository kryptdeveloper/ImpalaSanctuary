package com.saf.Dan.wadspecs;

public class model
{
  //String date;
  String email;
  String name;


    String purl;
    //String mpesa;
    String phone;
    //String unique;

    public model() {
    }

    public model(String name, String email, String phone) {
      //  this.date = date;
        this.email = email;
        this.name = name;
       // this.purl = purl;
        this.phone=phone;
      //  this.mpesa=mpesa;
        //this.unique=unique;
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




    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
