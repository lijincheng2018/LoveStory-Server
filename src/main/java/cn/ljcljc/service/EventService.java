package cn.ljcljc.service;

import cn.ljcljc.pojo.LoveEvents;
import cn.ljcljc.pojo.User;

public interface EventService {
    public void UpdateUserLoveValueByAdd(String token, LoveEvents loveEvents);
    public void UpdateUserLoveValueByUpdate(String token, LoveEvents loveEvents);
    public void UpdateUserLoveValueByDelete(User user, LoveEvents loveEvents);
}
