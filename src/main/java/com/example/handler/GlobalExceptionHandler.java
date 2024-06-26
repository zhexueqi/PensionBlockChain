package com.example.handler;

import com.example.constant.MessageConstant;
import com.example.exception.BaseException;
import com.example.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry '123' for key 'employee.idx_username'
        // 获取异常信息
        String message = ex.getMessage();
        //记录日志
        log.error("异常信息：{}", message);
        //如果有重复的员工，则返回错误信息
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            return Result.error(username + MessageConstant.ALREADY_EXIST);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
