package com.toplion.cplusschool.Bean;

import java.io.Serializable;

public class Thirdtype implements Serializable {
	private String typeid = "";
	private String typeurl = "";
	private String typename = "";

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTypeurl() {
		return typeurl;
	}

	public void setTypeurl(String typeurl) {
		this.typeurl = typeurl;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

}
