package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.SponsorDTO;
import com.example.model.Result;
import com.example.model.bo.InsuranceAccountBo;
import com.example.model.vo.InsuranceAccount;
import com.example.model.vo.ResultVO;
import com.example.service.IQueryService;
import com.example.service.SponsorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

/**
 * @author zhexueqi
 * @ClassName SponsorController
 * @since 2024/4/12    16:51
 */
@RestController
@Slf4j
@Api(value = "雇主相关接口", tags = "雇主相关接口")
@RequestMapping("/sponsor")
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    @ApiOperation(value = "注册雇主",notes = "注册雇主")
    @PostMapping("/addSponsorAccount")
    public Result addSponsorAccount(@RequestBody SponsorDTO sponsor){
        return sponsorService.addSponsorAccount(sponsor);
    }

    @ApiOperation(value = "查询雇主公司列表", notes = "查询雇主公司列表")
    @PostMapping(value = "/SponsorCompanys")
    public Result sponsorcompanys(@RequestBody JSONObject jsonObject) {
        String userAddress = (String) jsonObject.get("userAddress");
        if (StrUtil.isEmpty(userAddress)){
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        return sponsorService.listSponsorCompanys(userAddress);
    }

    @ApiOperation(value = "查询公司信息")
    @PostMapping("/getsponsorDetails")
    public Result getsponsorDetails(@RequestBody JSONObject jsonObject){
        String sponsorAddress = jsonObject.getString("sponsorAddress");
        InsuranceAccount insuranceAccountDetail = sponsorService.getInsuranceAccountDetail(sponsorAddress, sponsorAddress);
        return Result.success(insuranceAccountDetail);
    }

    @ApiOperation(value = "给雇主充值", notes = "给雇主充值")
    @PostMapping("/depositSponsor")
    public Result<String> depositSposor(@RequestBody InsuranceAccountDTO insuranceAccountDTO) {
        return sponsorService.depositSposor(insuranceAccountDTO);
    }
}
