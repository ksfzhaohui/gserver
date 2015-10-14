package org.gserver;

import org.gserver.gate.GateServer;

public class GateStart {

	public static void main(String[] args) {
		GateServer gateServer = new GateServer();
		new Thread((Runnable) gateServer).start();
	}
}
