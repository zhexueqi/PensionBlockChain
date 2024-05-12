package com.example.model.bo;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@ApiModel(value = "员工VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBo {
    String name; // 姓名
    int age; // 年龄
    int sex;//性别
    int userType;//用户类型
    String signSecurity;
    String phone;//手机号码
    String idNumber; // 身份证号码
    String employer; // 雇主
    Long startDate; // 缴费开始日期
    int salary; // 工资
    Long paymentBase; // 缴费基数
    String employeeAddress; //员工地址
    String senderAddress;//交易发起者
    LocalDateTime createTime;//创建时间
    LocalDateTime updateTime;//更新时间


}