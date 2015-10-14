package org.gserver.core.server;

/**
 * 服务器类型
 * @author zhaohui
 *
 */
public enum ServerType {

	GATE_SERVER(1, "gate"), //
	WORLD_SERVER(3, "world"), //
	PUBLIC_SERVER(4, "public");

	private int id;
	private String name;

	ServerType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
