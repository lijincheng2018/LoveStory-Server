package cn.ljcljc.mapper;


import cn.ljcljc.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * 用户数据持久层
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Mapper
public interface UserMapper {
    /**
     * 查询用户名和密码是否匹配
     * @param userName 用户名
     * @param passWord 密码
     * @return 用户数据
     */
    @Select("select uid as userId, userName, name, loveValue, love_id, code from user where userName = #{userName} and user.passWord = #{passWord}")
    public User login(String userName, String passWord);

    /**
     * 插入新的用户数据
     * @param username 用户名
     * @param password 密码
     * @param name 姓名
     * @param code 绑定码
     */
    @Insert("insert into user(userName, name, passWord, loveValue, code) VALUES (#{username}, #{name}, #{password}, '0', #{code})")
    public void register(String username, String password, String name, String code);

    /**
     * 通过uid查询用户数据
     * @param userId 用户uid
     * @return 用户数据
     */
    @Select("select uid as userId, userName, name, loveValue, love_id, code from user where uid = #{userId}")
    public User getUserDataByUserId(String userId);

    /**
     * 通过用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    @Select("select uid as userId, userName, name, loveValue, love_id, code from user where username = #{username}")
    public User getUserDataByUserName(String username);

    /**
     * 通过绑定码查询用户
     * @param code 绑定码
     * @return 用户数据
     */
    @Select("select uid as userId, userName, name, loveValue, love_id, code from user where code = #{code}")
    public User getUserDataByCode(String code);

    /**
     * 更新对象
     * @param love_id 对象uid
     * @param uid 我的id
     */
    @Update("update user set love_id=#{love_id} where uid = #{uid}")
    public void updateLover(int love_id, int uid);

    /**
     * 更新兑换恋爱值
     * @param loveValue 可兑换恋爱值
     * @param uid 我的id
     */
    @Update("update user set loveValue=#{loveValue} where uid = #{uid}")
    public void updateLoveValue(int loveValue, int uid);

    /**
     * 更新密码
     * @param passWord 新密码
     * @param uid 我的id
     */
    @Update("update user set passWord=#{passWord} where uid = #{uid}")
    public void updatePassword(String passWord, int uid);
}
