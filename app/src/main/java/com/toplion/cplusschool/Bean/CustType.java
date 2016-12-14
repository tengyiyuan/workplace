package com.toplion.cplusschool.Bean;

import java.io.Serializable;

public class CustType implements Serializable {
	private String typeid = "";
	private String typename = "";

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

}
