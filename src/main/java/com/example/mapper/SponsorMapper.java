package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.DTO.SponsorDTO;
import com.example.model.vo.SponsorVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhexueqi
 * @ClassName SponsorMapper
 * @since 2024/4/12    12:06
 */
@Mapper
public interface SponsorMapper extends BaseMapper<SponsorDTO> {

    /**
     * 添加员工
     * @param sponsor
     */
    @Insert("insert into sponsor(sponsor_address, sponsor_name, sign_security) VALUES(#{sponsorAddress},#{sponsorName},#{signSecurity}) ")
     void addSponsor(SponsorVO sponsor);

    /**
     * 根据地址查询
     * @param sponsorAddress
     * @return
     */
    @Select("select * from sponsor where sponsor_address=#{sponsorAddress}")
    SponsorVO getSponsorByAddress(String sponsorAddress);

    /**
     * 根据名字查询
     * @param sponsorName
     * @return
     */
    @Select("select * from sponsor where sponsor_name=#{sponsorName}")
    SponsorVO getSponsorByName(String sponsorName);
}
