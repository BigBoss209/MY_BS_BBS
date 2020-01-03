package com.example.boot1907.dao;

import com.example.boot1907.pojo.User;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Set;

@Mapper
public interface IUserDao {
    @Insert("insert into t_user(user_id,user_name,passwd,salt) values(seq_user.nextval,#{userName},#{passwd},#{salt})")
    void save(User user);
    @Select("select * from t_user where user_name=#{userName}")
    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "userName", column = "USER_NAME"),
            @Result(property = "birthday", column = "BIRTHDAY"),
            @Result(property = "passwd", column = "PASSWD"),
            @Result(property = "gender", column = "GENDER"),
            @Result(property = "isadmin", column = "ISADMIN"),
            @Result(property = "facePath", column = "FACE_PATH"),
            @Result(property = "valid", column = "VALID"),
            @Result(property = "salt", column = "SALT")
    })
    User findOne(String userName);

    @SelectProvider(type = UserDaoProvider.class, method = "pagesSql")
    @ResultMap("userMap")
    List<User> pages(User pojo);

    class UserDaoProvider {
        public String pagesSql(User pojo) {
            StringBuilder sql = new StringBuilder("select * from t_user where 1 = 1 ");
            if(pojo != null) {
                if(pojo.getUserName() != null && !"".equals(pojo.getUserName())) {
                    sql.append(" and user_name like %${userName}% ");
                }
                if(pojo.getUserGender() != null && !"".equals(pojo.getUserGender())) {
                    sql.append(" and gender = #{gender} ");
                }
            }
            return sql.toString();
        }
    }

    @Select("SELECT r.role_name FROM t_user u LEFT JOIN t_user_role ur\n" +
            "ON u.user_id = ur.user_id LEFT JOIN t_role r\n" +
            "ON r.role_id = ur.role_id WHERE u.isadmin = 1 AND\n" +
            "u.user_name=#{userName,jdbcType=VARCHAR}")
    Set<String> getUserRole(String userName);

    @Select("SELECT p.per_name FROM t_user u LEFT JOIN t_user_role ur\n" +
            "ON u.user_id = ur.user_id LEFT JOIN t_permission p\n" +
            "ON ur.role_id = p.role_id WHERE u.isadmin = 1\n" +
            "AND u.user_name = #{userName,jdbcType=VARCHAR}")
        // @ResultType(String.class)
    Set<String> getUserPermission(String userName);
}
