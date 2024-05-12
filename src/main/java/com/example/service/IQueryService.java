package com.example.service;

import com.example.model.DTO.QueryDTO;
import com.example.model.Result;
import com.example.model.bo.SocialSecurityBureauBo;
import com.example.model.vo.InsuranceAccount;
import com.example.model.vo.PersonVO;

public interface IQueryService {



    Result listAllEmployees(String userAddress);

    Result listCompanys(String userAddress);

    Result listBureaus(String userAddress);

    Result getAllApplications(String userAddress);

    SocialSecurityBureauBo searchBureaus(String bureauCityName);


    PersonVO getPersonDetail(String userAddress, String employeeAddress);

    InsuranceAccount getInsuranceAccountDetail(String userAddress, String accountAddress);

    Object getPensionOpenArea(QueryDTO queryDTO);
}
