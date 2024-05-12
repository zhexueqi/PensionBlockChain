package com.example.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "员工VO")
@Data
public class PersonVO {
    String name; // 姓名
    int age; // 年龄
    String idNumber; // 身份证号码
    String employer; // 雇主
    Long startDate; // 缴费开始日期
    int salary; // 工资
    int paymentBase; // 缴费基数
    String employeeAddress; //员工地址

    int personalBalance;// 账户余额
    String security; //密钥

}