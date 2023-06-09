package cn.ljcljc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应结果基本类
 * @author 李锦成
 * @version 1.0(2023.06)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;       // 相应状态码
    private String msg;     // 响应信息
    private Object data;    // 响应数据

    /**
     * 成功响应
     * @return 状态码和状态信息
     */
    public static Result success() {
        return new Result(200, "success", null);
    }

    /**
     * 成功响应（带响应数据）
     * @return 状态码、状态信息和响应数据
     */
    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    /**
     * 成功响应（自定义状态码、带响应数据）
     * @return 状态码和状态信息
     */
    public static Result success(int code, Object data) {
        return new Result(code, "success", data);
    }

    /**
     * 错误响应
     * @return 状态码和状态信息
     */
    public static Result error(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
