package cn.ljcljc.service.serviceImpl;

import cn.ljcljc.mapper.EventMapper;
import cn.ljcljc.mapper.UserMapper;
import cn.ljcljc.pojo.LoveEvents;
import cn.ljcljc.pojo.User;
import cn.ljcljc.service.EventService;
import cn.ljcljc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 恋爱事件服务层
 * @author 李锦成
 * @version 1.0(2023.06)
 */

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;


    /**
     * 插入新事件后更新用户可兑换值
     * @param token 获取Header中存放的token，用于身份校验
     * @param loveEvents 插入的新事件
     */
    @Override
    public void UpdateUserLoveValueByAdd(String token, LoveEvents loveEvents) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        loveEvents.setUserId(currentUser.getLove_id());
        eventMapper.insertEvent(loveEvents);

        int LoveValue = userMapper.getUserDataByUserId(currentUser.getLove_id()).getLoveValue();
        String changeMode = loveEvents.getChangeMode();

        if (changeMode.equals("add")) {
            LoveValue += Integer.parseInt(loveEvents.getChangeValue());
        }
        else if (changeMode.equals("minus")) {
            LoveValue -= Integer.parseInt(loveEvents.getChangeValue());
        }
        
        userMapper.updateLoveValue(LoveValue, Integer.parseInt(currentUser.getLove_id()));
    }

    /**
     * 修改事件后更新用户可兑换值
     * @param token 获取Header中存放的token，用于身份校验
     * @param loveEvents 旧事件
     */
    @Override
    public void UpdateUserLoveValueByUpdate(String token, LoveEvents loveEvents) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));

        int LoveValue = userMapper.getUserDataByUserId(currentUser.getLove_id()).getLoveValue();

        LoveEvents e = eventMapper.queryEventById(loveEvents.getId());

        int oldValue = Integer.parseInt(e.getChangeValue());
        int newValue = Integer.parseInt(loveEvents.getChangeValue());

        LoveValue += newValue - oldValue;

        eventMapper.updateEvent(loveEvents);
        userMapper.updateLoveValue(LoveValue, Integer.parseInt(currentUser.getLove_id()));
    }

    /**
     * 删除单个恋爱事件后更新用户可兑换值
     * @param user 传入当前用户
     * @param loveEvents 删除的恋爱事件
     */
    @Override
    public void UpdateUserLoveValueByDelete(User user, LoveEvents loveEvents) {
        User currentUser = user;

        int LoveValue = userMapper.getUserDataByUserId(currentUser.getLove_id()).getLoveValue();

        if (loveEvents.getChangeMode().equals("add")) {
            LoveValue -= Integer.parseInt(loveEvents.getChangeValue());
        }
        else {
            LoveValue += Integer.parseInt(loveEvents.getChangeValue());
        }

        eventMapper.delEvent(loveEvents.getId());
        userMapper.updateLoveValue(LoveValue, Integer.parseInt(currentUser.getLove_id()));
    }


}
