package com.example.macbookair.firebaseblog.Models;

/**
 * Created by macbookair on 01/07/2017.
 */

public class Blog {
    private String Title,Description,Image;

    public Blog() {
    }

    public Blog(String title, String description, String image) {
        Title = title;
        Description = description;
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
