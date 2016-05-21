package org.gserver.util;

import java.util.HashMap;
import java.util.Map;

public class CommandEnumUtil {

	private static Map<Integer, CommandEnum> commandMap = new HashMap<Integer, CommandEnum>();

	static {
		for (CommandEnum ce : CommandEnum.values()) {
			if (ce.getName().startsWith("C2S")) {
				commandMap.put(ce.getId(), ce);
			}
		}
	}

	public static boolean isValid(int commandId) {
		return commandMap.containsKey(commandId);
	}

	public static CommandEnum getCommand(int commandId) {
		return commandMap.get(commandId);
	}

}
