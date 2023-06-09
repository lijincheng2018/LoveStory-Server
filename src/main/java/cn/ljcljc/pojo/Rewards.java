package cn.ljcljc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 奖励基本类
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rewards {
    private int id;         // 奖励id
    private String title;   // 奖励标题
    private String cost;    // 奖励价格
    private String userId;  // 对象id
    private String status;  // 兑换状态
}
