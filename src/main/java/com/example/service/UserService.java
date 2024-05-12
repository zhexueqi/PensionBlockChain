package com.example.service;


import com.example.model.DTO.InsuranceAccountDTO;
import com.example.model.DTO.UserDTO;
import com.example.model.Result;
import com.example.model.bo.ApplyBo;
import com.example.model.bo.InsuranceAccountBo;
import com.example.model.bo.PersonBo;

/**
 * @author zhexueqi
 * @ClassName UserService
 * @since 2024/4/11    21:03
 */

public interface UserService {

    PersonBo login(UserDTO userDTO);


    Result<String> addEmployee(InsuranceAccountDTO insuranceAccountDTO);

    Result<String> addPerson(PersonBo personBo);

    Result<String> applyForTransfer(ApplyBo applyBo);

    Result<String> makePayment(InsuranceAccountBo insuranceAccountBo);
}
