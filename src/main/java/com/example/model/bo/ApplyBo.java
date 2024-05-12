package com.example.model.bo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "申请转移Vo")
public class ApplyBo {
    String applicant; // 申请人地址
    String currentBureauAddress;  //申请人当前社保局地址
    String fromCity;   // 原城市
    String toCity;     // 目标城市
    boolean isStopped;    // 是否停缴  true === 停止缴费
    boolean isApproved;   // 是否批准

    int personalFund;     // 个人账户基金
    int overallFund;      // 统筹账户基金
    int pensionInsurance; // 养老保险账户

    String toBureauAddress; //目标社保局地址

    String senderAddress; // 交易发起者

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getToBureauAddress() {
        return toBureauAddress;
    }

    public void setToBureauAddress(String toBureauAddress) {
        this.toBureauAddress = toBureauAddress;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getCurrentBureauAddress() {
        return currentBureauAddress;
    }

    public void setCurrentBureauAddress(String currentBureauAddress) {
        this.currentBureauAddress = currentBureauAddress;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getPersonalFund() {
        return personalFund;
    }

    public void setPersonalFund(int personalFund) {
        this.personalFund = personalFund;
    }

    public int getOverallFund() {
        return overallFund;
    }

    public void setOverallFund(int overallFund) {
        this.overallFund = overallFund;
    }

    public int getPensionInsurance() {
        return pensionInsurance;
    }

    public void setPensionInsurance(int pensionInsurance) {
        this.pensionInsurance = pensionInsurance;
    }
}