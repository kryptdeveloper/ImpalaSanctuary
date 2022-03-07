package com.saf.Dan;


public class person
{
    // Variable to store data corresponding
    // to firstname keyword in database
    private String name;

    // Variable to store data corresponding
    // to email keyword in database
    private String email;

    // Variable to store data corresponding
    // to phone keyword in database
    private String phone;
    private String date;

    public String getMpesa() {
        return mpesa;
    }

    public void setMpesa(String mpesa) {
        this.mpesa = mpesa;
    }

    public String getNopeople() {
        return nopeople;
    }

    public void setNopeople(String nopeople) {
        this.nopeople = nopeople;
    }

    private  String mpesa;

    private String nopeople;
    // Mandatory empty constructor
    // for use of FirebaseUI
    public person() {}




    // Getter and setter method
    public String getname()
    {
        return name;
    }
    public void setFirstname(String firstname)
    {
        this.name = firstname;
    }
    public String getemail()
    {
        return email;
    }
    public void setemail(String email)
    {
        this.email = email;
    }
    public String getphone()
    {
        return phone;
    }
    public void setphone(String phone)
    {
        this.phone = phone;
    }
    public String getdate()
    {
        return date;
    }
    public void setdate(String date)
    {
        this.date = date;
    }
}
