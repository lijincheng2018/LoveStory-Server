package cn.ljcljc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基本类
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int UserId;        // 用户id
    private String userName;   // 用户名
    private String name;       // 姓名
    private int loveValue;     // 可兑换恋爱值
    private String love_id;    // 对象id
    private String code;       // 绑定码
}
