package com.example.service;


import com.example.model.DTO.SocialSecurityBureauDTO;
import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.SocialSecurityBureauBo;

/**
 * @author zhexueqi
 * @ClassName SocialService
 * @since 2024/4/12    12:46
 */
public interface SocialService {

    /**
     * 添加社保局
     * @param socialSecurityBureauDTO
     * @return
     */
    Result<SocialSecurityBureauDTO> addBureaus(SocialSecurityBureauDTO socialSecurityBureauDTO);


    Result<String> updateAuthorized(SocialSecurityBureauBo socialSecurityBureauBo);

    Result<String> authorizeTransfer(ApplyBo applyBo);

    Result<String> receiveTransfer(ApplyBo applyBo);
}
