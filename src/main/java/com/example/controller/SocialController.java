package com.example.controller;


import com.example.model.DTO.SocialSecurityBureauDTO;
import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.SocialSecurityBureauBo;
import com.example.service.SocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhexueqi
 * @ClassName SocialController
 * @since 2024/4/12    17:26
 */
@RestController
@Slf4j
@RequestMapping("/social")
@Api(value = "社保局相关接口", tags = "社保局相关接口")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @ApiOperation(value = "注册社保局", notes = "注册接口")
    @CrossOrigin
    @PostMapping(value = "/addBureaus")
    public Result<SocialSecurityBureauDTO> addBureaus(@RequestBody SocialSecurityBureauDTO socialSecurityBureauDTO) {
        return socialService.addBureaus(socialSecurityBureauDTO);
    }

    @ApiOperation(value = "修改接收社保局的权限", notes = "修改接收社保局的权限 ")
    @PostMapping(value = "/updateAuthorized")
    public Result<String> updateAuthorized(@RequestBody SocialSecurityBureauBo socialSecurityBureauBo) {
        return socialService.updateAuthorized(socialSecurityBureauBo);
    }

    @ApiOperation(value = "社保局审核申请", notes = "fromCity社保局审核申请")
    @PostMapping(value = "/authorizeTransfer")
    public Result<String> authorizeTransfer(@RequestBody ApplyBo applyBo) {
        return socialService.authorizeTransfer(applyBo);
    }

    /**
     * 新的社保局接受申请
     * @param applyBo
     * @return
     */
    @ApiOperation(value = "新的社保局接受申请", notes = "toCity 社保局接收账户转移接口 ")
    @PostMapping(value = "/receiveTransfer")
    public Result<String> receiveTransfer(@RequestBody ApplyBo applyBo) {
        return socialService.receiveTransfer(applyBo);
    }
}
