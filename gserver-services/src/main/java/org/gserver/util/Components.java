package org.gserver.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Components implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		Components.context = ctx;
	}

	public static Object getBeanById(String beanId) {
		return context.getBean(beanId);
	}

}
