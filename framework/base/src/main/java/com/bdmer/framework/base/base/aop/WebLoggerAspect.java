package com.bdmer.framework.base.base.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Controller日志切面:输出Controller层日志
 *
 * @author GongDeLang
 * @since 2019/12/4 9:51
 */
@Component
@Aspect
@Slf4j
@Order(4)
public class WebLoggerAspect {
    /**
     * 切面点，所有controller函数
     */
    private static final String POINT_CUT = "execution(* .*.controller..*(..))";

    /**
     * 方法调用前
     *
     * @param joinPoint 切点信息
     */
    @Before(POINT_CUT)
    public void before(JoinPoint joinPoint) {
        try {
            // 获取目标方法的参数信息
            Object[] objs = joinPoint.getArgs();
            // 获取调用信息的签名
            Signature signature = joinPoint.getSignature();
            String interfaceName = signature.getDeclaringTypeName() + "." + signature.getName();
            log.debug("[WEB拦截器] 开始调用 - 接口 :{}, 参数：{}", interfaceName, JSON.toJSONString(objs));
        } catch (Exception e) {
            log.error("[WEB拦截器] WebLoggerAspect - e:", e);
        }
    }

    /**
     * 方法调用后
     *
     * @param joinPoint 切点信息
     */
    @AfterReturning(value = POINT_CUT, returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 获取调用信息的签名
            Signature signature = joinPoint.getSignature();
            String interfaceName = signature.getDeclaringTypeName() + "." + signature.getName();

            log.debug("[WEB拦截器] 结束调用 - 接口 :{}, 返回结果：{}", interfaceName, JSON.toJSONString(result));
        }catch (Exception e) {
            log.error("[WEB拦截器] WebLoggerAspect - e:", e);
        }
    }

}
