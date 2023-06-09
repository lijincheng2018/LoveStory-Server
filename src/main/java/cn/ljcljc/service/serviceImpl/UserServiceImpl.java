package cn.ljcljc.service.serviceImpl;

import cn.ljcljc.mapper.EventMapper;
import cn.ljcljc.mapper.UserMapper;
import cn.ljcljc.pojo.User;
import cn.ljcljc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户服务层
 * @author 李锦成
 * @version 1.0(2023.06)
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    /**
     * 处理绑定恋人
     * @param code 绑定码
     * @param user 当前用户
     */
    @Override
    public int bindLoverService(String code, User user) {
        User e = userMapper.getUserDataByCode(code);
        if(e == null) return 2; // 用户不存在，不能绑定

        if(user.getUserId() == e.getUserId()) return 3; // 自己，不能绑定


        String love_id = e.getLove_id();
        if(love_id == null) { // 对方没有绑定，可以正常绑定
            System.out.println(user);
            System.out.println(e);
            userMapper.updateLover(user.getUserId(), e.getUserId());
            userMapper.updateLover(e.getUserId(), user.getUserId());
        }
        else {
            return 1; // 对方已经绑定过了，不能绑定
        }
        return 0;
    }
}
