package com.example.model.bo;

//雇主信息
public class InsuranceAccountBo {
    //养老保险账户信息
    int personalBalance; // 个人账户余额
    int overallBalance; // 总账户余额
    boolean isSponsor; // 雇主是否为职工的管理员
    Long paymentTimestamp;//缴费时间

    int salary;//员工薪水
    String address;// 员工账户地址
    String sponsorAddress;// 雇主账户地址
    String sponsorName;// 雇主名称
    String senderAddress;//交易发起着账户地址
    int amount;//充值存入的金额

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorAddress() {
        return sponsorAddress;
    }

    public void setSponsorAddress(String sponsorAddress) {
        this.sponsorAddress = sponsorAddress;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
