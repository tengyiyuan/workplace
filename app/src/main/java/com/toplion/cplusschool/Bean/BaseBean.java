package com.toplion.cplusschool.Bean;


import java.io.Serializable;

public class BaseBean<T> implements Serializable {
	private String code;//返回码
	private String msg;//显示信息
	private T data;
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
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
