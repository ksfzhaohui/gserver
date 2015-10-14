package org.gserver.gate;

public enum ErrorCode {

	SERVER_ERROR("服务器内部错误", 1000), //
	CREATE_CHANNEL_ERROR("创建通道异常", 1001), //
	CMD_OBJ_NOT_EXIT("协议对象不存在", 1002), //
	CMD_OBJ_TYPE_ERROR("协议对象类型错误", 1003), //
	PACKAGE_TAG_ERROR("非法协议包", 1004); //

	private int value;
	private String name;

	private ErrorCode(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public int value() {
		return value;
	}

	public String getName() {
		return name;
	}

}
