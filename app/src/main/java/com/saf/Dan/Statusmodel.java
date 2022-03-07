package com.saf.Dan;
public class Statusmodel {
    private String date;
    private String nopeople;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
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



    public Statusmodel() {
    }



    public Statusmodel(String date, String nopeople, String amount) {
        this.date = date;
        this.nopeople = nopeople;
        this.amount = amount;
    }

}