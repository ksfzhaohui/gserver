package org.gserver.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 加载spring配置文件
 * 
 * @author ksfzhaohui
 * 
 */
public class SpringContainer {
	private static SpringContainer _instance = new SpringContainer();
	private ApplicationContext ctx = null;

	private SpringContainer() {

	}

	public void loadSpring(ServerType type) {
		List<String> cfigList = new ArrayList<String>();
		if (type == ServerType.GATE) {
			cfigList.add("gs-application.xml");
		} else if (type == ServerType.LOGIC) {
			cfigList.add("gs-handlers.xml");
		}
		cfigList.add("gs-context.xml");
		cfigList.add("gs-service.xml");
		cfigList.add("gs-redis.xml");
		ctx = new ClassPathXmlApplicationContext(
				cfigList.toArray(new String[] {}));
	}

	public static SpringContainer getInstance() {
		return _instance;
	}

	public Object getBeanById(String beanId) {
		return ctx.getBean(beanId);
	}
}
