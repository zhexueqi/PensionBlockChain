package com.example.model.vo;

//雇主信息
public class InsuranceAccount {
    //养老保险账户信息
    int personalBalance; // 个人账户余额
    int overallBalance; // 总账户余额
    boolean isSponsor; // 雇主是否为职工的管理员
    Long paymentTimestamp;//缴费时间
    String sponsorName;// 雇主名称
    String sponsorAddress;// 雇主账户地址

    public String getSponsorAddress() {
        return sponsorAddress;
    }

    public void setSponsorAddress(String sponsorAddress) {
        this.sponsorAddress = sponsorAddress;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public int getPersonalBalance() {
        return personalBalance;
    }

    public void setPersonalBalance(int personalBalance) {
        this.personalBalance = personalBalance;
    }

    public int getOverallBalance() {
        return overallBalance;
    }

    public void setOverallBalance(int overallBalance) {
        this.overallBalance = overallBalance;
    }

    public boolean isSponsor() {
        return isSponsor;
    }

    public void setSponsor(boolean sponsor) {
        isSponsor = sponsor;
    }

    public Long getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setPaymentTimestamp(Long paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }

}
