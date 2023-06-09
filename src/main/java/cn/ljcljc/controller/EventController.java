package cn.ljcljc.controller;

import cn.ljcljc.mapper.EventMapper;
import cn.ljcljc.mapper.UserMapper;
import cn.ljcljc.pojo.LoveEvents;
import cn.ljcljc.pojo.Result;
import cn.ljcljc.pojo.User;
import cn.ljcljc.service.EventService;
import cn.ljcljc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 恋爱事件控制层
 * @author 李锦成
 * @version 1.0(2023.06)
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventService eventService;


    /**
     * 获取恋爱事件列表
     * @param token 获取Header中存放的token，用于身份校验
     * @return 状态码、我和Ta的恋爱事件
     */
    @GetMapping("/queryEvent")
    public Result queryEvent(@RequestHeader("token")String token) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        List<LoveEvents> myLoveEvents = eventMapper.queryEvent(currentUser.getUserId());
        List<LoveEvents> taLoveEvents = eventMapper.queryEvent(Integer.parseInt(currentUser.getLove_id()));

        Map<String, Object> response = new HashMap<>();
        response.put("myEvents", myLoveEvents);
        response.put("taEvents", taLoveEvents);


        return Result.success(response);
    }

    /**
     * 添加恋爱事件
     * @param token 获取Header中存放的token，用于身份校验
     * @param loveEvents 前端传递过来的恋爱事件
     * @return 状态码
     */
    @PostMapping("/addEvent")
    public Result addEvent(@RequestHeader("token")String token, @RequestBody LoveEvents loveEvents) {
        if (loveEvents == null) {
            return Result.error(100, "请传入数据", null);
        }
        eventService.UpdateUserLoveValueByAdd(token, loveEvents);
        return Result.success();
    }

    /**
     * 更新恋爱事件
     * @param token 获取Header中存放的token，用于身份校验
     * @param loveEvents 前端传递过来的恋爱事件
     * @return 状态码
     */
    @PostMapping("/updateEvent")
    public Result updateEvent(@RequestHeader("token")String token, @RequestBody LoveEvents loveEvents) {
        if (loveEvents == null) {
            return Result.error(100, "请传入数据", null);
        }
        eventService.UpdateUserLoveValueByUpdate(token, loveEvents);
        return Result.success();
    }

    /**
     * 获取单个恋爱事件内容
     * @param token 获取Header中存放的token，用于身份校验
     * @param id 前端传递过来的指定恋爱事件id
     * @return 状态码、修改权限标识和获取到的恋爱事件内容
     */
    @GetMapping("/queryEventById")
    public Result queryEventById(@RequestHeader("token")String token, int id) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        LoveEvents loveEvents = eventMapper.queryEventById(id);

        Map<String, Object> response = new HashMap<>();

        if (loveEvents == null) {
            return Result.error(100, "没有数据", null);
        }

        if (loveEvents.getUserId().equals(String.valueOf(currentUser.getUserId()))) {
            response.put("power", false);
        }
        else {
            response.put("power", true);
        }

        response.put("data", loveEvents);
        return Result.success(response);
    }

    /**
     * 删除单个恋爱事件
     * @param token 获取Header中存放的token，用于身份校验
     * @param id 前端传递过来的指定恋爱事件id
     * @return 状态码
     */
    @GetMapping("/delEvent")
    public Result delEventById(@RequestHeader("token")String token, int id) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        LoveEvents loveEvents = eventMapper.queryEventById(id);

        if (loveEvents.getUserId().equals(currentUser.getLove_id())) {
            eventService.UpdateUserLoveValueByDelete(currentUser, loveEvents);
        }
        else {
            return Result.error(100, "无权限", null);
        }

        return Result.success();
    }
}
