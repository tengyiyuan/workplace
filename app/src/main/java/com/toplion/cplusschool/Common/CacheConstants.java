package com.toplion.cplusschool.Common;

/**
 * 缓存数据常量类
 * @author liyb
 *
 */
public class CacheConstants {
	public static final String LOCAL_SUCCESS = "0";      // 本地更新成功
	public static final String SAM_SUCCESS = "0x000000"; // Sam 更新成功
	public static final String CHKNUM_ERROR = "账户出现错误......"; // 出现错误了
	public static final String TOKEN_FAIL = "Util_class_0x00003500000";      //token失效重新登录

	public static final String SCH_UPDATE = "SchoolModel_class_0x00006050000";  //学校信息有更新
	public static final String SCH_NO_UPDATE = "SchoolModel_class_0x00006100000";//学校信息没有更新

	public static final String USR_SUCESS = "UserModel_class_0x00005100000";      //成功更新用户信息

	public static final String VAL_NO_HAVE = "IndexController_class_0x00003100000";//请求不包含验证码
	public static final String VAL_ERROR_INPUT = "IndexController_class_0x00003200000";  //验证码格式错误
	public static final String VAL_TIME_OUT = "IndexController_class_0x00003300000"; //验证码请求超时
	public static final String VAL_NO_RESULT = "IndexController_class_0x00003400000"; //验证码不匹配
	public static final String VAL_RIGHT = "IndexController_class_0x00003500000";  //成功匹配验证码

	public static final String REPAIR_NO_ARRAY = "RepairModel_class_0x00008100000";  //结果集不为数组
	public static final String REPAIR_IS_NULL = "RepairModel_class_0x00008200000";   //结果集为空
	public static final String REPAIR_UPDATE = "RepairModel_class_0x00008300000";    //常见故障信息有更新
	public static final String REPAIR_NO_UPDATE = "RepairModel_class_0x00008400000"; //常见故障信息无更新

	public static final String MOBILE_NO_SET = "MobileCenterController_class_0x0000a100000"; //用户名或Token未设置
	public static final String MOBILE_ERROR = "MobileCenterController_class_0x0000a200000";  //用户名或Token格式错误
	public static final String MOBILE_NO_HAVE = "MobileCenterController_class_0x0000a300000";//用户Token不存在
	public static final String MOBILE_OUT = "MobileCenterController_class_0x0000a400000";    //用户Token已过期

}
