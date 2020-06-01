package com.bdmer.framework.base.base.aop;

import com.alibaba.fastjson.JSON;
import com.bdmer.framework.base.cmomon.constant.BaseConstant;
import com.bdmer.framework.base.cmomon.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 方法调用跟踪切面
 *
 * @author GongDeLang
 * @since 2019/12/3 13:52
 */
@Component
@Aspect
@Slf4j
@Order(3)
public class MethodTrackAspect {

    /**
     * 自定义配置名称，在配置变量env中使用，可获取其相应的值
     */
    private static final String CUSTOM_CONFIG_KEY = "log.track.switcher";

    /**
     * 配置变量env，会根据配置服务的变化自动刷新配置值
     */
    @Autowired
    private Environment env;

    /**
     * 待跟踪方法的切点,整个项目（除了aop与config文件夹）
     * 同时不需要跟踪@Aspect、@Configuration
     */
    private static final String POINT_CUT = "execution(* .*.*(..)) " +
            "&& !execution(* .*.aop.*.*(..))" +
            "&& !execution(* .*.config.*.*(..))" +
            "&& !@within(org.aspectj.lang.annotation.Aspect) " +
            "&& !@annotation(org.aspectj.lang.annotation.Aspect) " +
            "&& !@within(org.springframework.context.annotation.Configuration) " +
            "&& !@annotation(org.springframework.context.annotation.Configuration)";


    /**
     * 方法调用前
     *
     * @param joinPoint 切点信息
     */
    @Before(POINT_CUT)
    public void before(JoinPoint joinPoint){
        try {
            // 没有开启切面记录，直接退出
            if (this.isNotStart()) {
                return;
            }

            // 获取目标方法的参数信息
            Object[] objs = joinPoint.getArgs();
            // 获取调用信息的签名
            Signature signature = joinPoint.getSignature();
            log.info("[方法拦截器] 方法调用开始: [{} - {}] 参数{}", signature.getDeclaringTypeName(), signature.getName(), JSON.toJSONString(objs));
        }catch (Exception e) {
            log.error("[方法拦截器] MethodTrackAspect - e:", e);
        }
    }

    /**
     * 方法调用后
     * 只是在调用之后打印日志
     *
     * @param joinPoint 切面点信息
     */
    @AfterReturning(value = POINT_CUT, returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result){
        try {
            // 没有开启切面记录，直接退出
            if (this.isNotStart()) {
                return;
            }

            // 获取调用信息的签名
            Signature signature = joinPoint.getSignature();
            log.info("[方法拦截器] 方法调用结束: [{} - {}] 出参{}", signature.getDeclaringTypeName(), signature.getName(), JSON.toJSONString(result));
        }catch (Exception e) {
            log.error("[方法拦截器] MethodTrackAspect - e:", e);
        }
    }

    /**
     * 获取是否开启配置
     *
     * @return 是否启动
     */
    private boolean isNotStart() {
        if (Util.isNull(env)) {
            return true;
        }

        String enable = env.getProperty(CUSTOM_CONFIG_KEY, BaseConstant.NO);

        return Util.isNull(enable) || BaseConstant.NO.equals(enable);
    }
}