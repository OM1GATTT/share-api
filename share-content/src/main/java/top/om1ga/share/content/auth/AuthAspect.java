package top.om1ga.share.content.auth;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.content.feign.User;
import top.om1ga.share.content.feign.UserService;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月23日 19:57
 * @description AuthAspect
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {

    @Resource
    private UserService userService;

    @Around("@annotation(top.om1ga.share.content.auth.CheckAuth)")
    public Object checkAuth(ProceedingJoinPoint point) throws Throwable {
        try{
            HttpServletRequest request = getHttpServletRequest();
            String token = request.getHeader("token");
            boolean isValid = JwtUtil.validate(token);
            if (!isValid){
                throw new RuntimeException("token 不合法");
            }
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            long userId = Long.parseLong(jsonObject.get("id").toString());
            User user = userService.getUser(userId).getData();
            String roles = user.getRoles();
            log.info("当前角色：>>>>>>>>>>>>>>>>>>>>>>" + roles);
            MethodSignature signature = (MethodSignature)point.getSignature();
            Method method = signature.getMethod();
            CheckAuth annotation = method.getAnnotation(CheckAuth.class);
            String value = annotation.value();
            if (!Objects.equals(roles,value)) {
                throw new RuntimeException("用户无权限访问！");
            }
        }catch (Throwable throwable){
            throw new RuntimeException("用户无权限访问！"+throwable);
        }
        return point.proceed();
    }

    private HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        assert attributes != null;
        return attributes.getRequest();
    }
}
