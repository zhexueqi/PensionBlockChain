package com.example.controller;


import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.InsuranceAccountBo;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhexueqi
 * @ClassName EmployeeController
 * @since 2024/4/12    17:31
 */
@RestController
@Slf4j
@RequestMapping("/employee")
@Api(value = "员工相关接口", tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "申请养老保险转移", notes = "申请养老保险转移")
    @PostMapping("/applyForTransfer")
    public Result<String> applyForTransfer(@RequestBody ApplyBo applyBo) {
        return userService.applyForTransfer(applyBo);
    }
}
