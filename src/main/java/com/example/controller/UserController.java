package com.example.controller;


import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.UserDTO;
import com.example.model.Result;
import com.example.model.bo.InsuranceAccountBo;
import com.example.model.bo.PersonBo;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.example.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author zhexueqi
 * @ClassName UserControoler
 * @since 2024/4/12    17:50
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    @CrossOrigin
    public Result<PersonBo> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        PersonBo personBo = userService.login(userDTO,request);
        return Result.success(personBo);
    }

    @ApiOperation(value = "注销",notes = "登出")
    @PostMapping(value = "/logout")
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return Result.success("注销成功");
    }

    @ApiOperation(value = "注册用户", notes = "注册用户")
    @CrossOrigin
    @PostMapping(value = "/addPerson")
    public Result<String> addPerson(@RequestBody PersonBo personBo) {
        return userService.addPerson(personBo);
    }


    @ApiOperation(value = "注册员工", notes = "注册员工")
    @CrossOrigin
    @PostMapping(value = "/addEmployee")
    public Result<String> addEmployee(@RequestBody InsuranceAccountDTO insuranceAccountDTO) {
        return userService.addEmployee(insuranceAccountDTO);
    }

    @ApiOperation(value = "缴纳养老保险", notes = "缴纳养老保险")
    @PostMapping(value = "/makePayment")
    public Result<String> makePayment(@RequestBody InsuranceAccountBo InsuranceAccountBo) {
        return userService.makePayment(InsuranceAccountBo);
    }
}
