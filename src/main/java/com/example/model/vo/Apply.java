package com.example.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "申请转移Vo")
@Data
public class Apply {
    String applicant; // 申请人地址
    String currentBureauAddress;  //申请人当前社保局地址
    String fromCity;   // 原城市
    String toCity;     // 目标城市
    String toCityBureauAddress; //目标城市社保局地址
    boolean isStopped;    // 是否停缴  true === 停止缴费
    boolean isApproved;   // 是否批准
    boolean hasReceived; // 是否接收
    int personalFund;     // 个人账户基金
    int overallFund;      // 统筹账户基金
    int pensionInsurance; // 养老保险账户


}