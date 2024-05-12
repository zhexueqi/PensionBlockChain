package com.example.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName InsuranceAccountDTO
 * @since 2024/4/12    10:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SponsorDTO {

    private String SponsorAddress;//工作单位地址
    private String SponsorName;//工作单位名称
}
