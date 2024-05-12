package com.example.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName UserDTO
 * @since 2024/4/11    21:05
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    //用户地址
    private String userAddress;
    //用户名
    private String name;
    //用户类型
    private String type;
    //用户密钥
    private String signSecurity;
}
