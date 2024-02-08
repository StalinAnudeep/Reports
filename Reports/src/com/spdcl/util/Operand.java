package com.spdcl.util;

import java.util.List;

public class Operand {

	private String key;
	private List<OperandData> data;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<OperandData> getData() {
		return data;
	}
	public void setData(List<OperandData> data) {
		this.data = data;
	}
}
