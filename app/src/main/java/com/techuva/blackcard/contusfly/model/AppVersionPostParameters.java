package com.techuva.blackcard.contusfly.model;

public class AppVersionPostParameters {

    private String device_id;
    private String title;

    public AppVersionPostParameters(String title, String complaint, String created_by, String image_url) {
        this.title = title;
        this.complaint = complaint;
        this.created_by = created_by;
        this.image_url = image_url;
    }

    private String complaint;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String created_by;
    private String image_url;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private int user_id;

    public AppVersionPostParameters(String device_id) {
        this.device_id = device_id;
    }

    public AppVersionPostParameters(int user_id) {
        this.user_id = user_id;
    }
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }



}
