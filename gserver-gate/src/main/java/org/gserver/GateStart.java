package org.gserver;

import org.gserver.gate.GateServer;
import org.gserver.gate.clientServer.ConnectAppServer;

public class GateStart {
	public static void main(String[] args) {
		new Thread((Runnable) GateServer.getInstance()).start();
		new Thread((Runnable) ConnectAppServer.getInstance()).start();
	}
}
