package com.bdmer.framework.base.cmomon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContext辅助类
 *
 * @author GongDeLang
 * @since 2020/6/1 14:51
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据bean类型获得bean实例
	 * 
	 * @param clazz bean类型
	 * @return 返回bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return null == applicationContext ? null : applicationContext.getBean(clazz);
	}

	/**
	 * 根据beanName类型获得bean实例
	 * 
	 * @param beanName bean名称
	 * @return 返回bean对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return null == applicationContext ? null : (T) applicationContext.getBean(beanName);
	}

}
