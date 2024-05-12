package com.example.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.example.model.vo.ResultVO;
import lombok.NoArgsConstructor;

@ApiModel(value = "返回类")
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty(value = "返回值")
    private int code;
    @ApiModelProperty(value = "返回消息")
    private String msg;
    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 成功时候的调用
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>(data);
        result.code = 200;
        return result;
    }
    public static <T> Result<T> success(T data,T message) {
        Result<T> result = new Result<T>(data);
        result.code = 200;
        result.msg = message.toString();
        return result;
    }

    /**
     * 失败时候的调用
     */
    public static <T> Result<T> error(ResultVO resultVO) {
        return new Result<T>(resultVO);
    }
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(ResultVO resultVO) {
        if (resultVO != null) {
            this.code = resultVO.getCode();
            this.msg = resultVO.getMsg();
            this.data = (T) resultVO.getData();
        }
    }

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
