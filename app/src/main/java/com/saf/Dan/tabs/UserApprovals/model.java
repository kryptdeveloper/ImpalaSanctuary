package com.saf.Dan.tabs.UserApprovals;

public class model
{

  String email;
  String name;


    String purl;

    String phone;
    String unique;

    public model() {
    }

    public model(String name, String email, String phone,String purl) {

        this.email = email;
        this.name = name;
        this.purl = purl;
        this.phone=phone;

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
