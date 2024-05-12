package com.example.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.constant.EnumConstant;
import com.example.mapper.SponsorMapper;
import com.example.mapper.UserMapper;
import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.SponsorDTO;
import com.example.model.Result;
import com.example.model.vo.InsuranceAccount;
import com.example.model.vo.PersonVO;
import com.example.model.vo.ResultVO;
import com.example.model.vo.SponsorVO;
import com.example.service.SponsorService;
import com.example.utils.WeBASEUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.constant.ContractConstant.ADD_SPONSOR_ACCOUNT;
import static com.example.constant.ContractConstant.DEPOSIT_SPONSOR;
import static com.example.utils.SignUtils.generateSigantureWithSecp256k1;

/**
 * @author zhexueqi
 * @ClassName SponsorSeriveImpl
 * @since 2024/4/6    10:45
 */
@Slf4j
@Service
public class SponsorSeriveImpl implements SponsorService {
    @Value("${system.contract.owner}")
    private String ownerAddress;
    @Autowired
    WeBASEUtils weBASEUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired

    private SponsorMapper sponsorMapper;

    /**
     * 注册雇主
     *
     * @param sponsorDTO
     * @return
     */
    @Transactional
    @Override
    public Result<SponsorVO> addSponsorAccount(SponsorDTO sponsorDTO) {

        if (sponsorMapper.getSponsorByName(sponsorDTO.getSponsorName()) !=null) {
            return Result.error(ResultVO.SPONSOR_EXIST);
        }

        List<String> funcParam = new ArrayList<>();
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getKeyPairFactory();

        //生成地址
        String sponsorAddress = cryptoKeyPair.getAddress();
        //签名
        ECDSASignatureResult ecdsaSignatureResult = generateSigantureWithSecp256k1(cryptoSuite,cryptoKeyPair,sponsorAddress);
        String signStr = ecdsaSignatureResult.convertToString();

        funcParam.add(sponsorAddress);
        funcParam.add(sponsorDTO.getSponsorName());
        String _result = weBASEUtils.funcPost(ownerAddress,ADD_SPONSOR_ACCOUNT,funcParam);
        JSONObject obj= JSONUtil.parseObj(_result);
        String statusOK= obj.get("message").toString();
        if (!statusOK.equals(EnumConstant.STATUS_OK)){
            return Result.error(ResultVO.CONTRACT_ERROR);
        }
        SponsorVO sponsor = new SponsorVO();
        sponsor.setSponsorName(sponsorDTO.getSponsorName());
        sponsor.setSponsorAddress(sponsorAddress);
        sponsor.setSignSecurity(signStr);
        log.info("addSponsorAccount:{}",sponsor);

        sponsorMapper.addSponsor(sponsor);

        return Result.success(sponsor);
    }

    /**
     * 雇主账户充值
     * @param insuranceAccountDTO
     * @return
     */
    @Override
    public Result<String> depositSposor(InsuranceAccountDTO insuranceAccountDTO) {
        if (StrUtil.isEmpty(insuranceAccountDTO.getSponsorAddress()))
        {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List funcParam = new ArrayList();
        //调用合约实现充值
        funcParam.add(insuranceAccountDTO.getSponsorAddress());
        funcParam.add(insuranceAccountDTO.getAmount());
        String _result = weBASEUtils.funcPost(ownerAddress,DEPOSIT_SPONSOR,funcParam);
        JSONObject obj=JSONUtil.parseObj(_result);
        String statusOK= obj.get("message").toString();
        return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);

    }

    @Override
    public Result listSponsorCompanys(String userAddress) {
        if (userAddress == null) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        //获取所有的职工地址
        String _result = weBASEUtils.funcPost(userAddress,"getPersonList",new ArrayList());;
        JSONArray _resultJson = JSONUtil.parseArray(_result);
        String a = _resultJson.get(0).toString();
        JSONArray addressArray = JSONUtil.parseArray(a);
        //获取所有的雇主账户地址
        String sponsor_result = weBASEUtils.funcPost(userAddress,"getSponsorsList",new ArrayList());
        JSONArray sponsor_resultJson = JSONUtil.parseArray(sponsor_result);
        String b = sponsor_resultJson.get(0).toString();
        JSONArray sponsor_addressArray = JSONUtil.parseArray(b);
        //根据地址获取雇主名字
        String sponsorName ="";
        for(Object address: sponsor_addressArray) {
            InsuranceAccount insuranceAccount = getInsuranceAccountDetail(userAddress, (String) address);
            if (insuranceAccount.getSponsorAddress().equals(userAddress)){
                sponsorName = insuranceAccount.getSponsorName();
            }
        }
        //根据地址获取职工雇主
        List<PersonVO> personVOList = new ArrayList<>();
        for(Object obj :addressArray) {
            PersonVO personVO = getPersonDetail(userAddress, (String) obj);
            if (personVO.getEmployer().equals(sponsorName)){
                personVOList.add(personVO);
            }
        }

        return Result.success(personVOList);
    }

    /**
     * 根据员工地址查询员工信息
     * @param userAddress
     * @param employeeAddress
     * @return
     */
    private PersonVO getPersonDetail(String userAddress, String employeeAddress){
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(employeeAddress);

        String _result = weBASEUtils.funcPost(userAddress,"getPerson",funcParam);
        JSONArray employeeArray = JSONUtil.parseArray(_result);

        PersonVO employee = new PersonVO();
        employee.setName((String)employeeArray.get(0));
        employee.setAge(Integer.parseInt(employeeArray.get(1).toString()));
        employee.setIdNumber((String)employeeArray.get(2));
        employee.setEmployer((String)employeeArray.get(3));
        employee.setStartDate(Long.parseLong(employeeArray.get(4).toString()));
        employee.setSalary(Integer.parseInt(employeeArray.get(5).toString()));
        employee.setPaymentBase(Integer.parseInt(employeeArray.get(6).toString()));
        //查询员工账户余额
        String _result2 = weBASEUtils.funcPost(userAddress,"getInsuranceAccount",funcParam);
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
    @Override
    public InsuranceAccount getInsuranceAccountDetail(String userAddress, String accountAddress){
        List funcParam = new ArrayList();
        //选手填写部分
        funcParam.add(accountAddress);

        InsuranceAccount insuranceAccount = new InsuranceAccount();
        try{
            String _result = weBASEUtils.funcPost(ownerAddress,"getInsuranceAccount",funcParam);
            JSONArray employeeArray = JSONUtil.parseArray(_result);
            String _result2 = weBASEUtils.funcPost(ownerAddress,"getSponsor",funcParam);
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
