package com.example.model.vo;

public class ResultVO<T> {

    private int code;
    private String msg;
    private T data;

    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 统一返回内容
     * */
    //成功:200
    public static ResultVO SUCCESS = new ResultVO(200, "正常", "正常");
    //请求参数缺失:404001
    public static ResultVO PARAM_EMPTY = new ResultVO(404001, "请求参数缺失", "请求参数缺失");
    //查询内容缺失:404002
    public static ResultVO QUERY_EMPTY = new ResultVO(404002, "查询内容不存在", "查询内容不存在");

    public static final ResultVO SIGN_INVALID = new ResultVO(40003,"密钥错误","密钥错误");
    public static ResultVO SENDER_ADDRESS_OR_NAME_ERROR = new ResultVO(40004, "地址或姓名不正确","地址或姓名不正确");
    public static ResultVO IDNUMBER_LENGTH_ERROR = new ResultVO(40005,"身份证号码长度不正确","身份证号码长度不正确");
    public static ResultVO PARAM_ERROR = new ResultVO(40006,"参数错误","参数错误");
    public static ResultVO SALARY_ERROR = new ResultVO(40007,"工资错误","工资错误");
    public static ResultVO SEX_ERROR = new ResultVO(40008,"性别错误","性别错误");
    public static final ResultVO NAME_ERROR = new ResultVO(40002, "姓名错误", "姓名错误");

    //信息已存在:500001
    public static ResultVO QUERY_EXISTS = new ResultVO(500001, "信息已存在！", "信息已存在！");

    public static ResultVO CONTRACT_ERROR = new ResultVO(500002, "智能合约请求存在问题","");

    public static ResultVO TOKEN_EMPTY = new ResultVO(500003, "Token已失效","Token已失效");

    public static ResultVO ADDRESS_INVALID = new ResultVO(500004, "操作地址不合法","操作地址不合法");

    public static ResultVO COMPANY_NOT_EXIST = new ResultVO(500005, "公司不存在","公司不存在");
    public static ResultVO ACCOUNTS_NOT_EXIST =  new ResultVO(500006, "用户不存在","用户不存在");
    public static ResultVO AMOUNT_NOT_ENOUGH = new ResultVO(500007, "金额不足","金额不足");
    public static ResultVO ACCOUNT_EXIST = new ResultVO(500008, "用户已存在","用户已存在");
    public static ResultVO PASSWORD_ERROR = new ResultVO(500009, "密码错误","密码错误");
    public static ResultVO USER_TYPE_ERROR = new ResultVO(500010, "用户类型错误","用户类型错误");
    public static ResultVO ID_NUMBER_IS_EXIST = new ResultVO(500011, "身份证号已存在","身份证号已存在");
    public static ResultVO AGE_ERROR = new ResultVO(500012, "年龄错误","年龄错误");
    public static ResultVO Bureaus_NAME_EXIST = new ResultVO(500013, "社保局名字已被注册","社保局名字已被注册");
    public static final ResultVO SQL_ERROR = new ResultVO(500014, "数据库错误","数据库错误");
    public static final ResultVO SPONSOR_EXIST = new ResultVO(500015, "工作单位已存在","工作单位已存在");
    public static final ResultVO SPONSOR_NOT_EXIST = new ResultVO(500016, "工作单位不存在","工作单位不存在");
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
