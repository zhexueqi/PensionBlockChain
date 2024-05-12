package com.example.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.mapper.UserMapper;
import com.example.model.Result;
import com.example.model.bo.PersonBo;
import com.example.model.bo.SocialSecurityBureauBo;
import com.example.model.vo.*;
import com.example.service.IQueryService;
import com.example.utils.WeBASEUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.constant.ContractConstant.*;


@Service
public class QueryServiceImpl implements IQueryService {
    @Autowired
    WeBASEUtils weBASEUtils;
    @Autowired
    private UserMapper userMapper;

    @Value("${system.contract.owner}")
    private String owner;

    /**
     * 获取所有的职工列表
     * @return
     */
    @Override
    public Result listAllEmployees(String userAddress) {
        if (userAddress == null) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        //获取所有的职工地址
        String _result = weBASEUtils.funcPost(userAddress,GET_PERSON_LIST,new ArrayList());;
        JSONArray _resultJson = JSONUtil.parseArray(_result);
        String a = _resultJson.get(0).toString();
        JSONArray addressArray = JSONUtil.parseArray(a);
        //根据地址获取职工详情
        List<PersonVO> personVOList = new ArrayList<>();
        for(Object obj :addressArray) {
            PersonVO personVO = getPersonDetail(userAddress, (String) obj);
            personVOList.add(personVO);
        }
        return Result.success(personVOList);
    }

    /**
     * 查询所有雇主账户信息
     * @param userAddress
     * @return
     */
    @Override
    public Result listCompanys(String userAddress) {
        //获取所有的雇主账户地址
        String _result = weBASEUtils.funcPost(userAddress,GET_SPONSORS_LIST,new ArrayList());
        JSONArray _resultJson = JSONUtil.parseArray(_result);
        String a = _resultJson.get(0).toString();
        JSONArray addressArray = JSONUtil.parseArray(a);
        //根据地址获取雇主详情
        List<InsuranceAccount> insuranceAccounts = new ArrayList<>();
        for(Object obj: addressArray) {
            InsuranceAccount insuranceAccount = getInsuranceAccountDetail(userAddress, (String) obj);
            insuranceAccounts.add(insuranceAccount);
        }
        return Result.success(insuranceAccounts);
    }

    /**
     * 查询所有社保局信息
     * @param userAddress
     * @return
     */
    @Override
    public Result listBureaus(String userAddress) {
        System.out.println(userAddress);
        //获取所有的社保局地址
        String _result = weBASEUtils.funcPost(userAddress,GET_ALL_SOCIAL_SECURITY_BUREAUS,new ArrayList());
        JSONArray _resultJson = JSONUtil.parseArray(_result);
        String a = _resultJson.get(0).toString();
        JSONArray addressArray = JSONUtil.parseArray(a);
        //根据地址获取社保局详情
        List<SocialSecurityBureauVO> socialSecurityBureaus = new ArrayList<>();
        for(Object obj: addressArray) {
            SocialSecurityBureauVO socialSecurityBureau = getSocialSecurityBureauDetail(userAddress, (String) obj);
            socialSecurityBureaus.add(socialSecurityBureau);
        }
        return Result.success(socialSecurityBureaus);
    }

    /**
     * 查询申请人列表信息
     * @param userAddress
     * @return
     */
    @Override
    public Result getAllApplications(String userAddress) {
        //获取所有的申请人地址
        String _result = weBASEUtils.funcPost(userAddress,GET_ALL_APPLICATIONS,new ArrayList());
        JSONArray _resultJson = JSONUtil.parseArray(_result);
        String a = _resultJson.get(0).toString();
        JSONArray addressArray = JSONUtil.parseArray(a);
        //根据地址获取申请详情
        List<Apply> applys = new ArrayList<>();
        for(Object obj: addressArray) {
            Apply apply = getApplication(userAddress, (String) obj);
            SocialSecurityBureauBo socialSecurityBureauBo = searchBureaus(apply.getToCity());

            apply.setToCityBureauAddress(socialSecurityBureauBo.getBureauAddress());
            applys.add(apply);
        }
        List<Apply> applyList = new ArrayList<>();
        for (Apply apply : applys) {
            if (apply.getCurrentBureauAddress().equals(userAddress) || apply.getToCityBureauAddress().equals(userAddress)){
                SocialSecurityBureauVO socialSecurityBureauDetail = getSocialSecurityBureauDetail(owner, userAddress);
                apply.setHasReceived(socialSecurityBureauDetail.isHasReceived());
                applyList.add(apply);
            }
        }

        return Result.success(applys);
    }

    @Override
    public SocialSecurityBureauBo searchBureaus(String bureauCityName){
        List funcParam = new ArrayList();
        funcParam.add(bureauCityName);

        String _result = weBASEUtils.funcPost(owner,SEARCH_BUREAUS,funcParam);
        JSONArray socialSecurityBureauArray = JSONUtil.parseArray(_result);

        SocialSecurityBureauBo socialSecurityBureauBo = new SocialSecurityBureauBo();
        socialSecurityBureauBo.setBureauCity(socialSecurityBureauArray.getStr(0));
        socialSecurityBureauBo.setBureauAddress(socialSecurityBureauArray.getStr(1));
        socialSecurityBureauBo.setAuthorized(socialSecurityBureauArray.getBool(2));
        socialSecurityBureauBo.setHasReceived(socialSecurityBureauArray.getBool(3));
        return socialSecurityBureauBo;
    }



    /**
     * 根据员工地址查询员工信息
     * @param userAddress
     * @param
     * @return
     */
    private Apply getApplication(String userAddress,String applicationAddress){
        List funcParam = new ArrayList();
        funcParam.add(applicationAddress);

        String _result = weBASEUtils.funcPost(userAddress,GET_APPLICATION,funcParam);
        JSONArray employeeArray = JSONUtil.parseArray(_result);

        Apply apply = new Apply();
        apply.setApplicant((String)employeeArray.get(0));
        apply.setCurrentBureauAddress((String)employeeArray.get(1));
        apply.setFromCity((String)employeeArray.get(2));
        apply.setToCity((String)employeeArray.get(3));
        apply.setStopped(Boolean.parseBoolean(employeeArray.get(4).toString()));
        apply.setApproved(Boolean.parseBoolean(employeeArray.get(5).toString()));
        return apply;
    }




    /**
     * 根据员工地址查询员工信息
     * @param userAddress
     * @param employeeAddress
     * @return
     */
    @Override
    public PersonVO getPersonDetail(String userAddress, String employeeAddress){
        List funcParam = new ArrayList();
        funcParam.add(employeeAddress);
        PersonBo person = userMapper.getPersonByAddress(employeeAddress);

        String _result = weBASEUtils.funcPost(owner,GET_PERSON,funcParam);
        JSONArray employeeArray = JSONUtil.parseArray(_result);

        PersonVO employee = new PersonVO();
        employee.setName((String)employeeArray.get(0));
        employee.setAge(Integer.parseInt(employeeArray.get(1).toString()));
        employee.setIdNumber((String)employeeArray.get(2));
        employee.setEmployer((String)employeeArray.get(3));
        employee.setStartDate(Long.parseLong(employeeArray.get(4).toString()));
        employee.setSalary(Integer.parseInt(employeeArray.get(5).toString()));
        employee.setPaymentBase(Integer.parseInt(employeeArray.get(6).toString()));
        employee.setSecurity(person.getSignSecurity());
        //查询员工账户余额
        String _result2 = weBASEUtils.funcPost(owner,GET_INSURANCE_ACCOUNT,funcParam);
        JSONArray employeeArray2 = JSONUtil.parseArray(_result2);
        employee.setPersonalBalance(Integer.parseInt(employeeArray2.get(0).toString()));

        employee.setEmployeeAddress(employeeAddress);
        return employee;
    }

    /**
     * 根据员工地址查询账户详情
     * @param userAddress
     * @param
     * @return
     */
    private SocialSecurityBureauVO getSocialSecurityBureauDetail(String userAddress,String bureauAddress){
        List funcParam = new ArrayList();
        funcParam.add(bureauAddress);
        String _result = weBASEUtils.funcPost(userAddress,GET_CURRENT_BUREAUS,funcParam);
        JSONArray employeeArray = JSONUtil.parseArray(_result);
        SocialSecurityBureauVO socialSecurityBureau = new SocialSecurityBureauVO();
        socialSecurityBureau.setBureauCity((String) employeeArray.get(0));
        socialSecurityBureau.setBureauAddress((String) employeeArray.get(1));
        socialSecurityBureau.setAuthorized(employeeArray.getBool(2));
        socialSecurityBureau.setHasReceived(employeeArray.getBool(3));
        return socialSecurityBureau;
    }


    /**
     * 根据员工地址查询账户详情
     * @param userAddress
     * @param
     * @return
     */
    @Override
    public InsuranceAccount getInsuranceAccountDetail(String userAddress, String accountAddress){
        List funcParam = new ArrayList();
        //选手填写部分
        funcParam.add(accountAddress);

        InsuranceAccount insuranceAccount = new InsuranceAccount();
        try{
            String _result = weBASEUtils.funcPost(owner,GET_INSURANCE_ACCOUNT,funcParam);
            JSONArray employeeArray = JSONUtil.parseArray(_result);
            String _result2 = weBASEUtils.funcPost(owner,GET_SPONSOR,funcParam);
            JSONArray sponsor = JSONUtil.parseArray(_result2);
            //封装员工信息
            insuranceAccount.setPersonalBalance(Integer.parseInt(employeeArray.get(0).toString()));
            System.out.println(employeeArray.get(0).toString());
            insuranceAccount.setOverallBalance(Integer.parseInt(employeeArray.get(1).toString()));
            insuranceAccount.setSponsor(Boolean.parseBoolean(employeeArray.get(2).toString()));
            insuranceAccount.setPaymentTimestamp(Long.valueOf(employeeArray.get(3).toString()));
            insuranceAccount.setSponsorName(sponsor.get(0).toString());
            insuranceAccount.setSponsorAddress(accountAddress);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return insuranceAccount;
    }



}
