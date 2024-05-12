package com.example.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.constant.EnumConstant;
import com.example.exception.BusinessException;
import com.example.mapper.SocialMapper;
import com.example.model.DTO.SocialSecurityBureauDTO;
import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.SocialSecurityBureauBo;
import com.example.model.vo.ResultVO;
import com.example.model.vo.SocialSecurityBureauVO;
import com.example.service.SocialService;
import com.example.utils.WeBASEUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.constant.ContractConstant.*;
import static com.example.utils.SignUtils.generateSigantureWithSecp256k1;

/**
 * @author zhexueqi
 * @ClassName SocialServiceImpl
 * @since 2024/4/12    12:46
 */
@Service
@Slf4j
public class SocialServiceImpl implements SocialService {

    @Value("${system.contract.owner}")
    String ownerAddress;
    @Autowired
    private SocialMapper socialMapper;
    @Autowired
    WeBASEUtils weBASEUtils;

    /**
     * 注册社保局
     * @param socialSecurityBureauDTO
     * @return
     */
    @Override
    public Result<SocialSecurityBureauDTO> addBureaus(SocialSecurityBureauDTO socialSecurityBureauDTO) {
        // 数据校验
        if (StrUtil.isEmpty(socialSecurityBureauDTO.getBureauCity())) {
            return Result.error("Bureau city 不能为空"); // 自定义错误信息，根据实际情况调整
        }
        SocialSecurityBureauVO bureau =null;

        try {
            bureau = socialMapper.getSocialByName(socialSecurityBureauDTO);
        } catch (Exception e) {
            // 日志记录异常，便于问题定位
            log.error("没有查询成功");
            throw new BusinessException(ResultVO.PARAM_ERROR);
        }
        if (bureau != null) {
            log.error("社保局名字已被注册");
            return Result.error(ResultVO.Bureaus_NAME_EXIST);
        }

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getKeyPairFactory();

        List<String> funcParam = new ArrayList<>();
        funcParam.add(socialSecurityBureauDTO.getBureauCity());
        String bureauAddress = cryptoKeyPair.getAddress();
        funcParam.add(bureauAddress);

        SocialSecurityBureauVO social = null;
        try {
            social = socialMapper.getSocialByAddress(bureauAddress);
        } catch (Exception e) {
            // 日志记录异常，便于问题定位
            log.error("通过地址获取Person错误", e);
            return Result.error(ResultVO.SENDER_ADDRESS_OR_NAME_ERROR);
        }
        if (social != null){
            return Result.error(ResultVO.SENDER_ADDRESS_OR_NAME_ERROR);
        }
        socialSecurityBureauDTO.setBureauAddress(bureauAddress);

        //通过地址进行签名
        ECDSASignatureResult ecdsaSignatureResult;
        try {
            ecdsaSignatureResult = generateSigantureWithSecp256k1(cryptoSuite, cryptoKeyPair, socialSecurityBureauDTO.getBureauAddress());
        } catch (Exception e) {
            log.error("通过地址进行签名错误：", e);
            return Result.error(ResultVO.SIGN_INVALID); // 根据实际情况调整错误信息
        }
        String signStr = ecdsaSignatureResult.convertToString();

        String _result = weBASEUtils.funcPost(ownerAddress,ADD_BUREAUS,funcParam);
        JSONObject obj = JSONUtil.parseObj(_result);
        String statusOK = obj.get("message").toString();
        if (!statusOK.equals(EnumConstant.STATUS_OK)) {
            // 记录具体错误信息或返回更详细的错误信息
            log.error("合约报错异常，异常信息：" + statusOK);
            return Result.error(ResultVO.CONTRACT_ERROR);
        }
        socialSecurityBureauDTO.setSignSecurity(signStr);
        socialMapper.addSocial(socialSecurityBureauDTO);

        return Result.success(socialSecurityBureauDTO);
    }

    /**
     * 修改社保局权限
     * @param socialSecurityBureauBo
     * @return
     */
    @Override
    public Result<String> updateAuthorized(SocialSecurityBureauBo socialSecurityBureauBo) {
        if (StrUtil.isEmpty(socialSecurityBureauBo.getBureauAddress())
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List funcParam = new ArrayList();
        //社保局地址
        funcParam.add(socialSecurityBureauBo.getBureauAddress());

        String _result = weBASEUtils.funcPost(ownerAddress,CHANGE_BUREAUS_AUTHORIZED,funcParam);
        JSONObject obj=JSONUtil.parseObj(_result);
        String statusOK=obj.get("message").toString();
        return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);
    }


    /**
     * fromCity 社保局查询调用申请
     * @param applyBo
     * @return
     */
    @Override
    public Result<String> authorizeTransfer(ApplyBo applyBo) {
        if (StrUtil.isEmpty(applyBo.getApplicant())
                ||StrUtil.isEmpty(applyBo.getCurrentBureauAddress())
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List funcParam = new ArrayList();
        //申请人
        funcParam.add(applyBo.getApplicant());
        funcParam.add(applyBo.getCurrentBureauAddress());

        String _result = weBASEUtils.funcPost(ownerAddress,AUTHORIZE_TRANSFER,funcParam);
        JSONObject obj=JSONUtil.parseObj(_result);
        String statusOK=obj.get("message").toString();
        return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);

    }

    /**
     * toCity 社保局接受申请
     * @param applyBo
     * @return
     */
    @Override
    public Result<String> receiveTransfer(ApplyBo applyBo) {
        if (StrUtil.isEmpty(applyBo.getApplicant())
                ||StrUtil.isEmpty(applyBo.getToBureauAddress())
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        List funcParam = new ArrayList();
        //申请人
        funcParam.add(applyBo.getApplicant());
        funcParam.add(applyBo.getToBureauAddress());

        String _result = weBASEUtils.funcPost(ownerAddress,RECEIVE_TRANSFER,funcParam);
        JSONObject obj=JSONUtil.parseObj(_result);
        String statusOK=obj.get("message").toString();
        return statusOK.equals(EnumConstant.STATUS_OK)?Result.success(EnumConstant.STATUS_OK):Result.error(ResultVO.CONTRACT_ERROR);
    }
}
