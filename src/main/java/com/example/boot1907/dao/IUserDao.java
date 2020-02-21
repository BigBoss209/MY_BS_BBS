package com.example.boot1907.dao;

import com.example.boot1907.pojo.User;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Set;

@Mapper
public interface IUserDao {
    @Insert("insert into bbs_user(user_id,user_name,user_password,salt,user_status,user_ex,user_fans,user_concern,isadmin,valid)" +
            " values(seq_user.nextval,#{userName},#{userPassword},#{salt},1,0,0,0,0,1)")
    void save(User user);

    //通过用户名查找用户
    @Select("select * from BBS_user where user_name=#{userName}")
    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "userName", column = "USER_NAME"),
            @Result(property = "userEmail", column = "USER_EMAIL"),
            @Result(property = "userSex", column = "USER_SEX"),
            @Result(property = "userPhone", column = "USER_PHONE"),
            @Result(property = "isadmin", column = "ISADMIN"),
            @Result(property = "userEx", column = "USER_EX"),
            @Result(property = "userStatus", column = "USER_STATUS"),
            @Result(property = "userTime", column = "USER_TIME"),
            @Result(property = "userShow", column = "USER_SHOW"),
            @Result(property = "userBlog", column = "USER_BLOG"),
            @Result(property = "userImg", column = "USER_IMG"),
            @Result(property = "userFans", column = "USER_FANS"),
            @Result(property = "userConcern", column = "USER_CONCERN"),
            @Result(property = "userPassword", column = "USER_PASSWORD"),
            @Result(property = "valid", column = "VALID"),
            @Result(property = "salt", column = "SALT")
    })
    User findOne(String userName);

    @SelectProvider(type = UserDaoProvider.class, method = "pagesSql")
    @ResultMap("userMap")
    List<User> pages(User pojo);


    class UserDaoProvider {
        public String pagesSql(User pojo) {
            StringBuilder sql = new StringBuilder("select * from BBS_user where 1 = 1 ");
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

    @Select("SELECT r.role_name FROM BBS_User u LEFT JOIN BBS_user_role ur\n" +
            "ON u.user_id = ur.user_id LEFT JOIN BBS_role r\n" +
            "ON r.role_id = ur.role_id WHERE u.isadmin = 1 AND\n" +
            "u.user_name=#{userName,jdbcType=VARCHAR}")
    Set<String> getUserRole(String userName);

    @Select("SELECT p.per_name FROM BBS_user u LEFT JOIN BBS_user_role ur\n" +
            "ON u.user_id = ur.user_id LEFT JOIN BBS_permission p\n" +
            "ON ur.role_id = p.role_id WHERE u.isadmin = 1\n" +
            "AND u.user_name = #{userName,jdbcType=VARCHAR}")
    // @ResultType(String.class)
    Set<String> getUserPermission(String userName);

    //更改用户头像
    @Update("UPDATE bbs_user SET user_img = #{userImg} WHERE user_id = #{userId} ")
    void updateImg(User pojo);

    //更改用户信息
    @Update("UPDATE bbs_user SET user_email = #{userEmail}," +
            "user_gender = #{userGender}, user_phone = #{userPhone}," +
            "user_time = #{userTime}, user_show = #{userShow}  WHERE user_id = #{userId} ")
    void changeInfo(User pojo);
}
