package com.edu.sekill.dao;

import com.edu.sekill.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author : lalalalaqaq
 * @date : 2022-02-25
 **/
public interface UserDAO {

    @Select("select * from user where id = #{id}")
    User select(Integer id);

    @Select("select id, name, password from user where id = #{userid}")
    User findById(Integer userid);
}
