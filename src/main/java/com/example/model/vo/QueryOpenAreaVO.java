package com.example.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName QueryOpenAreaVO
 * @since 2024/4/30    11:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryOpenAreaVO {
    private String cityCode;    // 市编码
    private String cityDesc;    // 市
    private String countyDesc; // 区
    private String isOpened; // 是否开通
}
