package com.wanshare.wscomponent.update.model;

/**
 * Created by Venn on 2017/7/27.
 */

public class VersionEntity {


    //平台
    private String equip;
    //版本
    private int remoteVersionCode;
    //升级url
    private String addressUrl;
    //强制升级
    private boolean isCompel;
    //更新时间
    private String updatedAt;
    //创建时间
    private String createdAt;
    //描述
    private String describe;
    //安装包大小 单位字节
    public String fileSize = "0";

    public int localVersionCode;

    public String versionName;

    public VersionEntity() {
    }

    public VersionEntity(int localVersionCode, int remoteVersionCode,String versionName ,String addressUrl, boolean isCompel, String describe, String fileSize) {
        this.localVersionCode = localVersionCode;
        this.remoteVersionCode = remoteVersionCode;
        this.versionName = versionName;
        this.addressUrl = addressUrl;
        this.isCompel = isCompel;
        this.describe = describe;
        this.fileSize = fileSize;
    }

    public VersionEntity(int remoteVersionCode, int localVersionCode) {
        this.remoteVersionCode = remoteVersionCode;
        this.localVersionCode = localVersionCode;
    }

    public boolean getIsCompel(){
        return isCompel;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public void setLocalVersionCode(int localVersionCode) {
        this.localVersionCode = localVersionCode;
    }

    public int getLocalVersionCode() {
        return localVersionCode;
    }

    public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    public boolean getMustUpdate() {
        return isCompel;
    }

    public void setIsCompel(boolean isCompel) {
        this.isCompel = isCompel;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


    public String getFileSize() {
        return fileSize;
    }

    public void setRemoteVersionCode(int remoteVersionCode) {
        this.remoteVersionCode = remoteVersionCode;
    }

    public int getRemoteVersionCode() {
        return remoteVersionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }
}
