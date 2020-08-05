package com.techuva.blackcard.new_changes.post_parameters;

public class OTPValidatePostParameters {


    private String action;
    private String phone_no;
    private String optcode;
    private String device_token;
    private String os_name;

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }


    public OTPValidatePostParameters(String action, String phone_no, String optcode, String device_token, String os_name) {
        this.action = action;
        this.phone_no = phone_no;
        this.optcode = optcode;
        this.device_token = device_token;
        this.os_name = os_name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getOptcode() {
        return optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }




}
