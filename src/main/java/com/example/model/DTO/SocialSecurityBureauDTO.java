package com.example.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName SocialSecurityBureauDTO
 * @since 2024/4/12    10:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocialSecurityBureauDTO {

    private String bureauCity;      // 社保局城市
    private String bureauAddress;  // 社保局地址
    private String signSecurity;    //签名密钥
}
