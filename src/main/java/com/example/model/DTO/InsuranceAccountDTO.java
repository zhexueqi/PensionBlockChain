package com.example.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName InsuranceAccountDTO
 * @since 2024/4/12    17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceAccountDTO {

    //养老保险账户信息
    Long paymentTimestamp;//缴费时间

    int salary;//员工薪水
    String address;// 员工账户地址
    String sponsorAddress;// 雇主账户地址
    String sponsorName;// 雇主名称
    int amount;//充值存入的金额
}
