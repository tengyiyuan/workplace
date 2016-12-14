package com.toplion.cplusschool.Bean;


import java.io.Serializable;

/**
 * 问题bean
 * @author wang
 */
public class CommonFaultInfo  implements Serializable {
	private String commonFaultId;//id
	private String commonFaultName;//标题
	private String commonFaultDes;//描述
	public String getCommonFaultId() {
		return commonFaultId;
	}
	public void setCommonFaultId(String commonFaultId) {
		this.commonFaultId = commonFaultId;
	}
	public String getCommonFaultName() {
		return commonFaultName;
	}
	public void setCommonFaultName(String commonFaultName) {
		this.commonFaultName = commonFaultName;
	}
	public String getCommonFaultDes() {
		return commonFaultDes;
	}
	public void setCommonFaultDes(String commonFaultDes) {
		this.commonFaultDes = commonFaultDes;
	}

}
