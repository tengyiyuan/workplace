package com.toplion.cplusschool.Bean;


import java.io.Serializable;
import java.util.List;

public class FaultInfoBean  implements Serializable {
	private List<CommonFaultInfo> commonFaultInfo;

	public List<CommonFaultInfo> getCommonFaultInfo() {
		return commonFaultInfo;
	}

	public void setCommonFaultInfo(List<CommonFaultInfo> commonFaultInfo) {
		this.commonFaultInfo = commonFaultInfo;
	}
}
