package com.example.exception;


import com.example.model.vo.ResultVO;

/**
 * @author zhexueqi
 * @ClassName BussinessException
 * @since 2024/4/11    21:24
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    int code;


    /**
     * 描述
     */
    String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResultVO resultVO){
        super(resultVO.getMsg());
        this.code = resultVO.getCode();
        this.description = resultVO.getData().toString();
    }

    public BusinessException(ResultVO resultVO, String description) {
        super(resultVO.getMsg());
        this.code = resultVO.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
