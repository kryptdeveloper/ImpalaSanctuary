package com.saf.Dan;
public class model2 {
    private String name;
    private String phone;
    private String email, date,mpesa;
    String purl;
    public model2(String name, String date, String email, String phone,String purl, String mpesa) {

        this.mpesa = mpesa;
        this.date = date;
 this.email = email;
        this.name = name;
        this.purl = purl;
        this.phone=phone;

    }
public String getdate() {
        return date;
    }
    public void setdate(String date) {
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

