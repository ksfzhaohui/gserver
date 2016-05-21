package org.gserver.util;

public enum ServerType {

	GATE(1), LOGIC(2);
	ServerType(int id) {
		this.id = id;
	}

	private int id;

	public int getId() {
		return id;
	}

	public String getName() {
		return this.toString();
	}
}
