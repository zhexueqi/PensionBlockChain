package com.example.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhexueqi
 * @ClassName QueryDTO
 * @since 2024/4/30    11:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDTO {
    private String pageSize;
    private String pageNum;
    private String province;
    private String town;
    private String county;
}
