package com.example.sdsd;

import android.widget.EditText;

public class MemberInfo {
    private String name;
    private String phoneNum;
    private String birth;
    private String address;

    public MemberInfo(String name, String phoneNum, String birth, String address) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.birth = birth;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }
}
