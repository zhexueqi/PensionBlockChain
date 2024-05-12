package com.example.service;


import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.SponsorDTO;
import com.example.model.Result;
import com.example.model.bo.InsuranceAccountBo;
import com.example.model.vo.InsuranceAccount;
import com.example.model.vo.SponsorVO;

/**
 * @author zhexueqi
 * @ClassName SponsorService
 * @since 2024/4/6    10:44
 */
public interface SponsorService {

    Result<SponsorVO> addSponsorAccount(SponsorDTO sponsorDTO);


    Result<String> depositSposor(InsuranceAccountDTO insuranceAccountDTO);

    Result listSponsorCompanys(String userAddress);

    InsuranceAccount getInsuranceAccountDetail(String userAddress, String accountAddress);
}
