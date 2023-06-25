package lk.ijse.dep10.sas.util;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.sql.Blob;

public class Student implements Serializable {
    private String Id;
    private String name;
    private Blob picture;
    private ImageView imageView;

    public Student() {
    }

    public Student(String id, String name, Blob picture, ImageView imageView) {
        Id = id;
        this.name = name;
        this.picture = picture;
        this.imageView = imageView;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
