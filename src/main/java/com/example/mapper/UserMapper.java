package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.bo.PersonBo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fisco.bcos.sdk.abi.datatypes.Int;

import java.util.List;

/**
 * @author zhexueqi
 * @ClassName UserMapper
 * @since 2024/4/3    22:44
 */
@Mapper
public interface UserMapper extends BaseMapper<PersonBo> {

    /**
     * 根据地址获取用户
     * @param address
     * @return
     */
    @Select("select * from user where employee_address = #{address}")
    PersonBo getPersonByAddress(String address);

    /**
     * 根据身份证号获取用户
     * @param IdNumber
     * @return
     */
    @Select("select * from user where idNumber = #{IdNumber}")
    PersonBo getPersonByIdNumber(String IdNumber);

    /**
     * 添加用户
     * @param personBo
     * @return
     */
    @Insert("insert into user(name, employee_address, phone, sex, age,employer, idNumber, avatar, craete_time, update_time,sign_security)" +
            "VALUES(#{name},#{employeeAddress},#{phone},#{sex},#{age},#{employer},#{idNumber},#{avatar},#{createTime},#{updateTime},#{signSecurity})")
    Integer addPerson(PersonBo personBo);

    /**
     * 获取所有用户身份证
     * @return
     */
    @Select("select idNumber from user")
    List<String> getAllByIdNumberStringList();

    @Update("update user set user_type = #{type},sign_security=#{security}  where employee_address = #{address}")
    void updateUser(String type , String address, String security);
}
