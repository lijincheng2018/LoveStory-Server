package cn.ljcljc.interceptor;

import cn.ljcljc.pojo.Result;
import cn.ljcljc.utils.JwtUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT令牌登录拦截器
 * @author 李锦成
 * @version 1.0(2023.06)
 */

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 获取请求url
        String url = request.getRequestURI().toString();


        // 2. 获取请求头中的令牌（token）
        String jwt = request.getHeader("token");
        response.setContentType("text/json; charset=utf-8");


        // 3. 判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if(!StringUtils.hasLength(jwt)) {
            Result error = Result.error(400, "你还没有登录哦！", null);
            String notLogin = JSONObject.toJSONString(error);

            response.getWriter().write(notLogin);
            return false;
        }

        // 4.解析token，如果解析失败，返回错误结果（未登录）
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            Result error = Result.error(400, "登录状态失效，请重新登录", null);
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        // 5. 放行
        return true;

    }
}
