package com.saf.Dan;

public class ModeHistory {
    public ModeHistory() {}

    public String getDateh() {
        return dateh;
    }

    public void setDateh(String dateh) {
        this.dateh = dateh;
    }

    public String getCodeh() {
        return codeh;
    }

    public void setCodeh(String codeh) {
        this.codeh = codeh;
    }

    public String getNope() {
        return nope;
    }

    public void setNope(String nope) {
        this.nope = nope;
    }

    private String dateh;

    // Variable to store data corresponding
    // to lastname keyword in database
    private String codeh;

    // Variable to store data corresponding
    // to age keyword in database
    private String nope;
}
