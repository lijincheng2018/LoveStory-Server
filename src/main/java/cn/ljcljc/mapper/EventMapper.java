package cn.ljcljc.mapper;

import cn.ljcljc.pojo.LoveEvents;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 恋爱事件数据持久层
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Mapper
public interface EventMapper {

    /**
     * 查询所有恋爱事件
     * @param id 用户id
     * @return 恋爱事件集合
     */
    @Select("select * from events where userId = #{id} order by id desc")
    public List<LoveEvents> queryEvent(int id);

    /**
     * 通过事件id查询恋爱事件
     * @param id 恋爱事件id
     * @return 恋爱事件
     */
    @Select("select * from events where id = #{id}")
    public LoveEvents queryEventById(int id);

    /**
     * 插入恋爱事件
     * @param loveEvents 恋爱事件
     */
    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into events (title, time, content, userId, changeMode, changeValue) VALUES (#{title}, #{time}, #{content}, #{userId}, #{changeMode}, #{changeValue})")
    public void insertEvent(LoveEvents loveEvents);

    /**
     * 更新恋爱事件
     * @param loveEvents 恋爱事件
     */
    @Update("update events set time = #{time}, title = #{title}, changeValue = #{changeValue}, content = #{content} where id = #{id}")
    public void updateEvent(LoveEvents loveEvents);

    /**
     * 删除恋爱事件
     * @param id 恋爱事件id
     */
    @Delete("delete from events where id = #{id}")
    public void delEvent(int id);
}
