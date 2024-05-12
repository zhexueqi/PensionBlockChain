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
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @CrossOrigin
    public Result<PersonBo> login(@RequestBody UserDTO userDTO) {
        PersonBo personBo = userService.login(userDTO);
        return Result.success(personBo);
    }

    @ApiOperation(value = "注册用户", notes = "注册用户")
    @CrossOrigin
    @RequestMapping(value = "addPerson", method = RequestMethod.POST)
    public Result<String> addPerson(@RequestBody PersonBo personBo) {
        return userService.addPerson(personBo);
    }


    @ApiOperation(value = "注册员工", notes = "注册员工")
    @CrossOrigin
    @RequestMapping(value = "addEmployee", method = RequestMethod.POST)
    public Result<String> addEmployee(@RequestBody InsuranceAccountDTO insuranceAccountDTO) {
        return userService.addEmployee(insuranceAccountDTO);
    }

    @ApiOperation(value = "缴纳养老保险", notes = "缴纳养老保险")
    @RequestMapping(value = "makePayment", method = RequestMethod.POST)
    public Result<String> makePayment(@RequestBody InsuranceAccountBo InsuranceAccountBo) {
        return userService.makePayment(InsuranceAccountBo);
    }
}
