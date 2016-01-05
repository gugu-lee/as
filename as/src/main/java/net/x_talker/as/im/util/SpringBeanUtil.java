package net.x_talker.as.im.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import net.x_talker.as.Main;



/**
 * Spring配置Bean获取帮助类
 *
 */
public class SpringBeanUtil implements ApplicationContextAware {

	private static ApplicationContext ac;

	public SpringBeanUtil() {
		setApplicationContext(Main.getApplicationContext());
	}

	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		SpringBeanUtil.ac = ac;
	}

	/**
	 * 根据名称获取Bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getBeanByName(String beanName) {
		return Main.getApplicationContext().getBean(beanName);
	}

	/**
	 * 根据类别Class获取Bean
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBeanByClass(Class<T> clazz) {
		return (T) Main.getApplicationContext().getBean(clazz);
	}

}
