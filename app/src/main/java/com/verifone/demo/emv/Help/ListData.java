package com.verifone.demo.emv.Help;

public class ListData {
    private int imageResource;
    private String title,helpType;

    public ListData(int imageResource, String title) {
        this.imageResource = imageResource;
        this.title = title;
        this.helpType = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;

    }
    public String getHelpType() {
        return helpType;
    }
}