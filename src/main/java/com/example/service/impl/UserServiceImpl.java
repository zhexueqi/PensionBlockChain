package com.example.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.constant.EnumConstant;
import com.example.exception.BusinessException;
import com.example.mapper.SponsorMapper;
import com.example.mapper.UserMapper;
import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.UserDTO;
import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.InsuranceAccountBo;
import com.example.model.bo.PersonBo;
import com.example.model.vo.InsuranceAccount;
import com.example.model.vo.ResultVO;
import com.example.model.vo.SponsorVO;
import com.example.service.UserService;
import com.example.utils.WeBASEUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.constant.ContractConstant.*;
import static com.example.constant.UserConstant.USER_LOGIN_STATE;
import static com.example.utils.SignUtils.generateSigantureWithSecp256k1;

/**
 * @author zhexueqi
 * @ClassName UserServiceImpl
 * @since 2024/4/11    21:07
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${system.contract.owner}")
    String ownerAddress;
    @Autowired
    WeBASEUtils weBASEUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SponsorMapper sponsorMapper;

    /**
     * 用户登录
     * @param userDTO 用户数据传输对象，包含用户地址、名称、签名安全信息和用户类型
     * @return PersonBo 个人业务对象，包含用户详细信息
     * @throws BusinessException 如果用户数据有问题或用户不存在，抛出业务异常
     */
    @Override
    public PersonBo login(UserDTO userDTO, HttpServletRequest request) {
        // 校验传入的用户信息是否为空
        if (StrUtil.isEmpty(userDTO.getUserAddress()) || StrUtil.isEmpty(userDTO.getName())
                || StrUtil.isEmpty(userDTO.getSignSecurity())
                || StrUtil.isEmpty(userDTO.getType())) {
            throw new BusinessException(ResultVO.PARAM_EMPTY);
        }

        // 根据用户地址查询用户信息
        PersonBo personBo;
        personBo = userMapper.getPersonByAddress(userDTO.getUserAddress());
        if (personBo == null) {
            log.info("用户不存在");
            throw new BusinessException(ResultVO.ACCOUNTS_NOT_EXIST);
        }

        // 验证用户名称是否匹配
        if (!personBo.getName().equals(userDTO.getName())) {
            throw new BusinessException(ResultVO.NAME_ERROR);
        }

        // 验证用户类型是否匹配
        if (personBo.getUserType() != Integer.parseInt(userDTO.getType())) {
            throw new BusinessException(ResultVO.USER_TYPE_ERROR);
        }

        // 验证签名是否有效
        if (!personBo.getSignSecurity().equals(userDTO.getSignSecurity())) {
            throw new BusinessException(ResultVO.SIGN_INVALID);
        }

        // 验证用户地址是否匹配
        if (!personBo.getEmployeeAddress().equals(userDTO.getUserAddress())) {
            throw new BusinessException(ResultVO.ADDRESS_INVALID);
        }

        // 如果名称不匹配，抛出异常（此段代码重复，可能需要检查）
        if (!personBo.getName().equals(userDTO.getName())) {
            throw new BusinessException(ResultVO.NAME_ERROR);
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, personBo);

        return personBo;
    }

    /**
     * 注册雇员账户
     * @param insuranceAccountDTO
     * @return
     */
    @Override
    public Result<String> addEmployee(InsuranceAccountDTO insuranceAccountDTO) {

        if (StrUtil.isEmpty(insuranceAccountDTO.getAddress())
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List funcParam = new ArrayList();
        funcParam.add(insuranceAccountDTO.getAddress());
        funcParam.add(insuranceAccountDTO.getSalary());
        funcParam.add(insuranceAccountDTO.getSponsorAddress());

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getKeyPairFactory();

        ECDSASignatureResult ecdsaSignatureResult = generateSigantureWithSecp256k1(cryptoSuite,cryptoKeyPair,insuranceAccountDTO.getAddress());
        String signStr = ecdsaSignatureResult.convertToString();

        String _result = weBASEUtils.funcPost(ownerAddress,ADD_EMPLOYEE,funcParam);
        JSONObject obj= JSONUtil.parseObj(_result);
        String statusOK= obj.get("message").toString();
        if (!statusOK.equals(EnumConstant.STATUS_OK)){
            return Result.error(ResultVO.CONTRACT_ERROR);
        }
        userMapper.updateUser(EnumConstant.EMPLOYEE,insuranceAccountDTO.getAddress(),signStr);
        return Result.success(signStr);
    }


    /**
     * 注册用户
     * @param personBo
     * @return
     */
    @Override
    public Result<String> addPerson(PersonBo personBo) {
        SponsorVO sponsor = sponsorMapper.getSponsorByName(personBo.getEmployer());
        PersonBo personByIdNumber = userMapper.getPersonByIdNumber(personBo.getIdNumber());
        JSONObject out_put = new JSONObject();

        //校验
        Result<String> stringResult = validData(sponsor, personByIdNumber, personBo);
        if (stringResult.getCode() != 200){
            return stringResult;//如果返回码不是200，则说明校验失败。
        }

        List<Object> funcParam = new ArrayList<>();

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getKeyPairFactory();
        //生成用户地址
        String address = cryptoKeyPair.getAddress();

        funcParam.add(address);
        PersonBo person;
        person = userMapper.getPersonByIdNumber(personBo.getIdNumber());
        if (person != null){
            return Result.error(ResultVO.ACCOUNT_EXIST);
        }
        //根据用户地址
        ECDSASignatureResult ecdsaSignatureResult = generateSigantureWithSecp256k1(cryptoSuite,cryptoKeyPair,address);
        String signStr = ecdsaSignatureResult.convertToString();

        //调用智能合约
        String _result = weBASEUtils.funcPost(ownerAddress,ADD_PERSON,funcParam);
        JSONObject obj= JSONUtil.parseObj(_result);
        String statusOK=obj.get("message").toString();
        if (!statusOK.equals(EnumConstant.STATUS_OK)){
            return Result.error(ResultVO.CONTRACT_ERROR);
        }else {
            personBo.setCreateTime(LocalDateTime.now());
            personBo.setUpdateTime(LocalDateTime.now());
            personBo.setSignSecurity(signStr);
            personBo.setEmployeeAddress(address);
            userMapper.addPerson(personBo);
            out_put.set("signSecurity",signStr);
            out_put.set("employeeAddress",address);
            return Result.success(out_put.toString());
        }
    }

    public Result<String> validData(SponsorVO sponsor, PersonBo personByIdNumber, PersonBo personBo){
        if (sponsor == null){
            return Result.error(ResultVO.SPONSOR_NOT_EXIST);
        }
        if (personByIdNumber !=null){
            return Result.error(ResultVO.ACCOUNT_EXIST);
        }
        if (personBo.getIdNumber().length() != 18){
            return Result.error(ResultVO.IDNUMBER_LENGTH_ERROR);
        }
        if (StrUtil.isBlank(personBo.getIdNumber())){
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        if (personBo.getAge()<0){
            return Result.error(ResultVO.AGE_ERROR);
        }
        if (personBo.getAge()>120){
            return Result.error(ResultVO.AGE_ERROR);
        }
        if (personBo.getSalary()<0){
            return Result.error(ResultVO.SALARY_ERROR);
        }
        if (personBo.getSalary()>1000000){
            return Result.error(ResultVO.SALARY_ERROR);
        }
        if (StrUtil.isBlank(personBo.getName())){
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        if (personBo.getSex()!=1&&personBo.getSex()!=2){
            return Result.error(ResultVO.SEX_ERROR);
        }
        return Result.success("ok");
    }


    /**
     * 申请社保转移
     * @param applyBo
     * @return
     */
    @Override
    public Result<String> applyForTransfer(ApplyBo applyBo) {

        if (StrUtil.isEmpty(applyBo.getApplicant())||StrUtil.isEmpty(applyBo.getCurrentBureauAddress())
                ||StrUtil.isEmpty(applyBo.getToBureauAddress())
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List<String> funcParam = new ArrayList<>();
        funcParam.add(applyBo.getFromCity());
        funcParam.add(applyBo.getToCity());
        //申请人
        funcParam.add(applyBo.getApplicant());
        funcParam.add(applyBo.getCurrentBureauAddress());
        funcParam.add(applyBo.getToBureauAddress());

        String _result = weBASEUtils.funcPost(ownerAddress,APPLY_FOR_TRANSFER,funcParam);
        JSONObject obj=JSONUtil.parseObj(_result);
        String statusOK=obj.get("message").toString();
        return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);

    }

    /**
     * 社保缴费
     * @param insuranceAccountBo
     * @return
     */
    @Override
    public Result<String> makePayment(InsuranceAccountBo insuranceAccountBo) {

        if (StrUtil.isEmpty(insuranceAccountBo.getAddress()))
        {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        try {
            //获取所有的雇主账户地址
            String sponsorResult = weBASEUtils.funcPost(ownerAddress,GET_SPONSORS_LIST,new ArrayList());
            JSONArray sponsorResultJson = JSONUtil.parseArray(sponsorResult);
            if (sponsorResultJson.isEmpty()){
                return Result.error(ResultVO.COMPANY_NOT_EXIST);//提前返回，防止空指针异常
            }
            String sponsorAddressString = sponsorResultJson.get(0).toString();
            JSONArray sponsorAddressArray = JSONUtil.parseArray(sponsorAddressString);
            //根据传入的用户地址查询对应的雇主名字和雇主地址
            String sponsorName ="";
            String sponsorAddress= "";
            for(Object address: sponsorAddressArray) {
                if (!(address instanceof String)){
                    continue;
                }
                //通过方法获取到该地址的账号详细
                InsuranceAccount insuranceAccount = getInsuranceAccountDetail((String) address, (String) address);

                sponsorAddress = insuranceAccount.getSponsorAddress();
                if (sponsorAddress !=null && !sponsorAddress.equals(address)){
                    sponsorName = insuranceAccount.getSponsorName();
                    break;//找到立马跳出循环
                }
            }
            //如果sponsorName等于空，或者 sponsorName不等于传进来中的sponsorName，则返回错误
            if (StrUtil.isEmpty(sponsorName) || !sponsorName.equals(insuranceAccountBo.getSponsorName())){
                return Result.error(ResultVO.COMPANY_NOT_EXIST);
            }

            List<Object> funcParam = new ArrayList<>();
            funcParam.add(insuranceAccountBo.getAddress());
            funcParam.add(insuranceAccountBo.getSalary());
            funcParam.add(sponsorAddress);

            String _result = weBASEUtils.funcPost(ownerAddress,MAKE_PAYMENT,funcParam);
            JSONObject obj=JSONUtil.parseObj(_result);
            String statusOK= obj.get("message").toString();

            return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);
        } catch (Exception e) {
            // 可以根据实际需要记录日志或处理异常
            return Result.error("An error occurred during the payment process.");
        }
    }

    /**
     * 获取社保账户详细
     * @param userAddress
     * @param accountAddress
     * @return
     */
    public InsuranceAccount getInsuranceAccountDetail(String userAddress, String accountAddress){
        List funcParam = new ArrayList();
        funcParam.add(accountAddress);

        InsuranceAccount insuranceAccount = new InsuranceAccount();
        try{
            String _result = weBASEUtils.funcPost(ownerAddress,GET_INSURANCE_ACCOUNT,funcParam);
            JSONArray employeeArray = JSONUtil.parseArray(_result);
            String _result2 = weBASEUtils.funcPost(ownerAddress,GET_SPONSOR,funcParam);
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
