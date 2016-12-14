package com.toplion.cplusschool.Bean;

import java.util.List;

public class BaseListBean<T> {
	private String code;//返回码
	private String msg;//显示信息
	private List<T> data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}

}
