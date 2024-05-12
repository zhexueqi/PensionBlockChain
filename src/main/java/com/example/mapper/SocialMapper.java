package com.example.mapper;


import com.example.model.DTO.SocialSecurityBureauDTO;
import com.example.model.vo.SocialSecurityBureauVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhexueqi
 * @ClassName SocialMapper
 * @since 2024/4/12    12:45
 */
@Mapper
public interface SocialMapper {

    /**
     * 获取社保局byAddress
     * @param bureauAddress
     * @return
     */
    @Select("select * from social where bureau_address=#{bureauAddress}")
    SocialSecurityBureauVO getSocialByAddress(String bureauAddress);

    /**
     * 获取社保局byName
     * @param socialSecurityBureauDTO
     * @return
     */
    @Select("select * from social where bureau_name=#{bureauCity}")
    SocialSecurityBureauVO getSocialByName(SocialSecurityBureauDTO socialSecurityBureauDTO);

    @Insert("insert into social(bureau_address, bureau_name,bureau_security)VALUES(#{bureauAddress},#{bureauCity},#{signSecurity})")
    Integer addSocial(SocialSecurityBureauDTO socialSecurityBureauDTO);
}
