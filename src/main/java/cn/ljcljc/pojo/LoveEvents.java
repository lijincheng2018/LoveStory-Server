package cn.ljcljc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 恋爱事件基本类
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoveEvents {
    private int id;             // 事件id
    private String title;       // 事件标题
    private String time;        // 事件时间
    private String changeMode;  // 事件类型
    private String changeValue; // 心跳值变化量
    private String userId;      // 对象id
    private String content;     // 事件描述
}
