package org.gserver;

import org.gserver.logic.LogicServer;

public class LogicStart {

	public static void main(String[] args) {
		LogicServer logicServer = new LogicServer();
		new Thread((Runnable) logicServer).start();
	}
}
