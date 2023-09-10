package com.ketty.chinesemedicine.entity;


import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2022-12-11
 */
public class User implements Serializable {
    private Long uId;
    private String uPhone;
    private String uName;
    private String uSex;
    private String uPassword;
    private String uHeadicon;
    private String uBirthday;
    private String uAddress;
    private String uAbout;
    private String uQqId;
    private String uQqName;
    private String backgroundImage;
    private String ipAddress;

    public String getuQqName() {
        return uQqName;
    }

    public void setuQqName(String uQqName) {
        this.uQqName = uQqName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getuQqId() {
        return uQqId;
    }

    public void setuQqId(String uQqId) {
        this.uQqId = uQqId;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuHeadicon() {
        return uHeadicon;
    }

    public void setuHeadicon(String uHeadicon) {
        this.uHeadicon = uHeadicon;
    }

    public String getuBirthday() {
        return uBirthday;
    }

    public void setuBirthday(String uBirthday) {
        this.uBirthday = uBirthday;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getuAbout() {
        return uAbout;
    }

    public void setuAbout(String uAbout) {
        this.uAbout = uAbout;
    }
}
