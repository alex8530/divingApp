package com.diverapp.diverapp;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modle.Request;

public class User {
    private String name;

    public String getMaster_diving() {
        return master_diving;
    }

    public void setMaster_diving(String master_diving) {
        this.master_diving = master_diving;
    }

    String master_diving;
    int type;
    private String createionDate;
    private String imageUrl;
   private String userClass;

   private String profileImage;
    private String Bio;

    private Boolean completedInfo;
   private String berthday;
    private String mobilNumber;
 private String bankAccount;
  private   String tokenId;
private String Diving_Prenc;

    public String getDiving_Prenc() {
        return Diving_Prenc;
    }

    public void setDiving_Prenc(String diving_Prenc) {
        Diving_Prenc = diving_Prenc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateionDate() {
        return createionDate;
    }

    public void setCreateionDate(String createionDate) {
        this.createionDate = createionDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public Boolean getCompletedInfo() {
        return completedInfo;
    }

    public void setCompletedInfo(Boolean completedInfo) {
        this.completedInfo = completedInfo;
    }

    public String getBerthday() {
        return berthday;
    }

    public void setBerthday(String berthday) {
        this.berthday = berthday;
    }

    public String getMobilNumber() {
        return mobilNumber;
    }

    public void setMobilNumber(String mobilNumber) {
        this.mobilNumber = mobilNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
