package com.example.model.bo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "社保局VO")
public class SocialSecurityBureauBo {
    String bureauCity;      // 社保局城市
    String bureauAddress;  // 社保局地址
    boolean isAuthorized;      // 是否允许接收社保转移授权
    boolean hasReceived;       // 是否已接收

    String senderAddress;//交易发起者

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getBureauCity() {
        return bureauCity;
    }

    public void setBureauCity(String bureauCity) {
        this.bureauCity = bureauCity;
    }

    public String getBureauAddress() {
        return bureauAddress;
    }

    public void setBureauAddress(String bureauAddress) {
        this.bureauAddress = bureauAddress;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public boolean isHasReceived() {
        return hasReceived;
    }

    public void setHasReceived(boolean hasReceived) {
        this.hasReceived = hasReceived;
    }
}