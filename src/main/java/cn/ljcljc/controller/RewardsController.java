package cn.ljcljc.controller;

import cn.ljcljc.mapper.RewardsMapper;
import cn.ljcljc.mapper.UserMapper;
import cn.ljcljc.pojo.Result;
import cn.ljcljc.pojo.Rewards;
import cn.ljcljc.pojo.User;
import cn.ljcljc.service.UserService;
import cn.ljcljc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 奖励控制层
 * @author 李锦成
 * @version 1.0(2023.06)
 */



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RewardsController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RewardsMapper rewardsMapper;

    @Autowired
    private UserService userService;


    /**
     * 获取我的奖励列表
     * @param token 获取Header中存放的token，用于身份校验
     * @return 状态码和我的奖励列表
     */
    @GetMapping("/queryMyRewards")
    public Result queryMyRewards(@RequestHeader("token")String token) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        List<Rewards> e = rewardsMapper.queryRewards(currentUser.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("heartValue", currentUser.getLoveValue());
        response.put("rewardList", e);

        return Result.success(response);
    }

    /**
     * 获取Ta的奖励列表
     * @param token 获取Header中存放的token，用于身份校验
     * @return 状态码和Ta的奖励列表
     */
    @GetMapping("/queryTaRewards")
    public Result queryTaRewards(@RequestHeader("token")String token) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        List<Rewards> e = rewardsMapper.queryRewards(Integer.parseInt(currentUser.getLove_id()));

        Map<String, Object> response = new HashMap<>();
        response.put("heartValue", userMapper.getUserDataByUserId(currentUser.getLove_id()).getLoveValue());
        response.put("rewardList", e);

        return Result.success(response);
    }

    /**
     * 添加奖励
     * @param token 获取Header中存放的token，用于身份校验
     * @param rewards 前端传递过来的奖励内容
     * @return 状态码和奖励内容
     */
    @PostMapping("/addRewards")
    public Result addRewards(@RequestHeader("token")String token, @RequestBody Rewards rewards) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        rewards.setUserId(currentUser.getLove_id());
        rewardsMapper.insertRewards(rewards);
        return Result.success(rewards);
    }

    /**
     * 更新奖励
     * @param token 获取Header中存放的token，用于身份校验
     * @param rewards 前端传递过来的奖励内容
     * @return 状态码
     */
    @PostMapping("/updateRewards")
    public Result updateRewards(@RequestHeader("token")String token, @RequestBody Rewards rewards) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        Rewards e = rewardsMapper.checkRewards(rewards.getId(), currentUser.getLove_id());
        if(e == null) return Result.error(100, "无权限", null);
        rewardsMapper.updateRewards(rewards);
        return Result.success();
    }

    /**
     * 兑换奖励
     * @param token 获取Header中存放的token，用于身份校验
     * @param id 前端传递过来的需要兑换的奖励id
     * @return 状态码
     */
    @GetMapping("/exchangeReward")
    public Result exchangeReward(@RequestHeader("token")String token, int id) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        Rewards e = rewardsMapper.checkRewards(id, String.valueOf(currentUser.getUserId()));
        if(e == null) return Result.error(100, "无权限", null);

        int heartValue = currentUser.getLoveValue();
        int rewardCost = Integer.parseInt(rewardsMapper.queryRewardsById(id).getCost());
        if(heartValue < rewardCost) return Result.error(100, "积分不足", null);

        heartValue -= rewardCost;

        userMapper.updateLoveValue(heartValue, currentUser.getUserId());
        rewardsMapper.exchangeReward(id);
        return Result.success();
    }

    /**
     * 一键兑换奖励
     * @param token 获取Header中存放的token，用于身份校验
     * @param duihuanList 前端传递过来的需要兑换的奖励id集合字符串
     * @return 状态码
     */
    @PostMapping("/exchangeAllReward")
    public Result exchangeAllReward(@RequestHeader("token")String token, String duihuanList) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        System.out.println(duihuanList);
        String[] arr = duihuanList.split(",");
        int heartValue = currentUser.getLoveValue();
        int rewardCost = 0;
        for(int i=0; i < arr.length; i++) {
            int id = Integer.parseInt(arr[i]);
            Rewards e = rewardsMapper.checkRewards(id, String.valueOf(currentUser.getUserId()));
            if(e == null) return Result.error(100, "无权限", null);
            rewardsMapper.exchangeReward(id);

            rewardCost += Integer.parseInt(rewardsMapper.queryRewardsById(id).getCost());
        }

        heartValue -= rewardCost;

        userMapper.updateLoveValue(heartValue, currentUser.getUserId());

        return Result.success();
    }

    /**
     * 删除单个奖励
     * @param token 获取Header中存放的token，用于身份校验
     * @param id 前端传递过来的指定奖励id
     * @return 状态码
     */
    @GetMapping("/delReward")
    public Result delReward(@RequestHeader("token")String token, int id) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        Rewards e = rewardsMapper.checkRewards(id, String.valueOf(currentUser.getLove_id()));
        if(e == null) return Result.error(100, "无权限", null);

        rewardsMapper.delReward(id);

        return Result.success();
    }
}
