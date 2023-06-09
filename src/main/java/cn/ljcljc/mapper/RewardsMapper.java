package cn.ljcljc.mapper;

import cn.ljcljc.pojo.Rewards;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 奖励数据持久层
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Mapper
public interface RewardsMapper {

    /**
     * 查询所有奖励
     * @param id 用户id
     * @return 奖励集合
     */
    @Select("select * from reward where userId = #{id}")
    public List<Rewards> queryRewards(int id);

    /**
     * 查询单个奖励
     * @param id 奖励id
     * @return 奖励内容
     */
    @Select("select * from reward where id = #{id}")
    public Rewards queryRewardsById(int id);

    /**
     * 检验修改权限
     * @param uid 恋人id
     * @param id 奖励id
     * @return 奖励内容
     */
    @Select("select * from reward where userId = #{uid} and id = #{id}")
    public Rewards checkRewards(int id, String uid);

    /**
     * 添加奖励
     * @param rewards 奖励
     */
    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into reward (title, cost, userId, status) VALUES (#{title}, #{cost}, #{userId}, '0')")
    public void insertRewards(Rewards rewards);

    /**
     * 修改奖励
     * @param rewards 奖励
     */
    @Update("update reward set title = #{title}, cost = #{cost} where id = #{id}")
    public void updateRewards(Rewards rewards);

    /**
     * 修改奖励为已兑换
     * @param id 奖励id
     */
    @Update("update reward set status = '1' where id = #{id}")
    public void exchangeReward(int id);

    /**
     * 删除奖励
     * @param id 奖励id
     */
    @Delete("delete from reward where id = #{id}")
    public void delReward(int id);

}
