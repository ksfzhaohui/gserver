package org.gserver.util;
		
public enum CommandEnum {
		
	C2S_LOGIN(100001)
	,S2C_ERROR_CODE(1000)
	,S2C_LOGIN(100001)
	;
	CommandEnum(int id){
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
