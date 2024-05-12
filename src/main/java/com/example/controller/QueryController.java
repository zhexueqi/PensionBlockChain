package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.model.DTO.QueryDTO;
import com.example.model.Result;
import com.example.model.vo.PersonVO;
import com.example.service.IQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "查询相关接口", tags = "查询相关接口")
@RestController
@RequestMapping("/query")
public class QueryController {
    @Autowired
    IQueryService queryService;

    /**
     * 查询员工列表(管理员)
     * @param userAddress
     * @return
     */
    @ApiOperation(value = "查询员工列表", notes = "注册接口")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result employees(@RequestBody String userAddress) {
        return queryService.listAllEmployees(userAddress);
    }

    /**
     * 查询公司列表(管理员)
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询公司列表", notes = "查询公司列表")
    @RequestMapping(value = "companys", method = RequestMethod.POST)
    public Result companys(@RequestBody JSONObject jsonObject) {
        String userAddress = jsonObject.getString("userAddress");
        return queryService.listCompanys(userAddress);
    }


    /**
     * 查询社保局列表(管理员)
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询社保局列表", notes = "查询社保局列表")
    @RequestMapping(value = "searchBureaus", method = RequestMethod.POST)
    public Result searchBureaus(@RequestBody JSONObject jsonObject) {
        String userAddress = jsonObject.getString("userAddress");
        System.out.println(userAddress);
        return queryService.listBureaus(userAddress);
    }

    /**
     * 查询社保转移申请列表(管理员)
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询社保转移申请列表", notes = "查询社保转移申请列表")
    @RequestMapping(value = "getAllApplications", method = RequestMethod.POST)
    public Result getAllApplications(@RequestBody JSONObject jsonObject) {
        String userAddress = jsonObject.getString("userAddress");
        return queryService.getAllApplications(userAddress);
    }

    /**
     * 查询员工详细信息(管理员)
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询员工详细信息", notes = "查询员工详细信息")
    @PostMapping("/getEmployeeDetails")
    public Result getEmployeeDetails(@RequestBody JSONObject jsonObject){
        String userAddress = jsonObject.getString("userAddress");
        PersonVO personVODetail = queryService.getPersonDetail(userAddress, userAddress);
        return Result.success(personVODetail);
    }

    @ApiOperation(value = "分页查询养老保险参保登记开通地区", notes = "分页查询养老保险参保登记开通地区")
    @PostMapping("/getPensionOpenArea")
    public Result<List<>> getPensionOpenArea(@RequestBody QueryDTO queryDTO) {

        return Result.success(queryService.getPensionOpenArea(queryDTO));
    }
}
