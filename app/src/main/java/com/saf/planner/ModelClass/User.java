package com.saf.planner.ModelClass;

public class User {
    private String TokenID,UID,FirstName,LastName,EmailID,PhoneNo,Password,ProfileURL;

    public User(String TokenID,String UID,String FirstName, String LastName, String EmailID, String PhoneNo, String Password, String ProfileURL) {
        this.TokenID = TokenID;
        this.UID = UID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.EmailID = EmailID;
        this.PhoneNo = PhoneNo;
        this.Password = Password;
        this.ProfileURL = ProfileURL;
    }
    public User(String FirstName, String LastName, String EmailID, String PhoneNo, String Password, String ProfileURL) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.EmailID = EmailID;
        this.PhoneNo = PhoneNo;
        this.Password = Password;
        this.ProfileURL = ProfileURL;
    }

    public String getTokenID() {
        return TokenID;
    }

    public String getUID() {
        return UID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getPassword() {
        return Password;
    }

    public String getProfileURL() {
        return ProfileURL;
    }

   /* public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setProfileURL(String profileURL) {
        ProfileURL = profileURL;
    }*/
}
