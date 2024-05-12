package com.example.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "社保局VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialSecurityBureauVO {
    String bureauCity;      // 社保局城市
    String bureauAddress;  // 社保局地址
    String signSecurity;    //密钥
    boolean isAuthorized;      // 是否允许接收社保转移授权
    boolean hasReceived;       // 是否已接收

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