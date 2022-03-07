package com.saf.Dan;

public class itemsModel {
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public itemsModel(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
