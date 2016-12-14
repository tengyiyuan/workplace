package com.toplion.cplusschool.Common;

import java.util.Timer;

/**
 * 常量类
 *
 * @author wang
 */
public class Constants {
    public static String NEWBASE_URL = "http://123.233.121.17:15100/index.php"; //建大测试地址
//    public static String URL = "123.233.121.17:12100";
    public static String BASE_URL = "http://111.14.210.46:12100/index.php"; //移动地址
    public static String BASE_URLB = "http://123.233.121.17:12100/index.php"; //联通地址
    public static String BASE_URLCS= "http://123.233.121.17:15100/index.php"; //测试地址
    public static String HELP_PHONE = "tel:4007661616";                   // 帮助电话
    public static String HELP_TRIP = "400-766-1616";                      // 帮助提示
    public static String ISBIND = "";                                     // 是否强制更新
    public static int STATUSCODE = 200;                                      //成功状态码
    //学校配置参数----start
    public static String SCHOOL_CODE = "sdjzu";                               // 学校Code
    //学校配置参数----end
    public static String PHPSESSID = "PHPSESSID";                       //会话ID

    public static String PHPSESSID_VALUE = "";                          //会话ID 值

    public static String TOKEN = "";                                    //Token值

    public static Timer TIMER;                                         //时间

    public static String JSESSIONID = "";                               //JESSIONID
    public static String userIndex = "";                                //USERINDEX
    public static String baseUrl = "";                                  //一键上网登录和断开网络连接的基础url

    public static String loginURL = "";                                 //登录URL

    public static String BASE64_BEFORE = "WkTf89tYs";                   //BASE64前缀转码
    public static String BESE64_AFTER = "OqN9Ts73u";                    //BASE64后缀转码

    public static final String NOT_WIFI = "您现在使用的网络不是规定WIFI,请切换WIFI后重试";  //不是WIFI后提示
    public static final String NETWORK_ERROR = "网络异常，请检查网络后重试!";                          // 网络错误提示

    public static final int TIME = 30000;                              //定义时间为10秒
    public static boolean ISRUN = false;                               //是否继续执行

    public static int TRY_COUNT = 0;                                    //重试数量
    public static String ORDER_RETURN = "";                             //订单

    public static String FEE = "免费";                                  //免费

    public static String ENCRYPT = "0";                                // 加密方式
    public static String AES_KEY = "ToplionKvmCplus8";                  // Key
    public static String AES_IV_PARAMETER = "toplion123kvm789";         // IV

    public static String ALIPAY_SUCCESS = "支付成功，请尽情享受网络世界。"; // 支付宝支付成功
    public static String ALIPAY_ERROR = "支付失败,订单已保存到我的订单,请重试或更换支付方式。"; // 支付宝支付失败
    public static String ALIPAY_SUC = "支付成功";                         // 支付成功
    public static String ALIPAY_ERR = "支付失败";                         // 支付失败

    public static String USE_HELP_URL = "/help/Helplist.html";           // 使用帮助
    public static String ABOUT_SOFT_URL = "/help/Aboutme.html";          // 关于本软件
    public static String AGREEMENT_URL = "/help/Buy.html";               // 购买协议
    public static String HELP_LOGIN_URL = "/help/Helplogin.html";           // 登录
    public static String HELP_BUY_URL = "/help/Helpbuy.html";          // 套餐购买
    public static String HELP_ONEKEY_URL = "/help/Helponekey.html";    // 一键上网

    public static String ORDER_ALL = "100000";                            // 获取全部订单
    public static String ORDER_NOPAY = "10000";                           // 未支付，未超时
    public static String ORDER_NOPAY_OUTTIME = "20000";                   // 未支付, 已超时 （交易关闭）
    public static String ORDER_PAY = "30000";                             // 已支付

    // 交易关闭
    public static String pay_closed = "720000";                           // 交易关闭

    // 订单状态 ，待支付
    public static String not_pay = "700000";                              // 订单已创建，未提交支付宝，也未支付

    // 交易完成
    public static String ali_not_pay = "800000";                          // 订单已提交支付宝但用户未支付，支付宝已返回异步notify,状态为WAIT_BUYER_PAY，等待用户支付
    public static String ali_payed_not_in_sam = "810000";                 // 订单已支付，支付宝notify提示支付成功，提示状态TRADE_SUCCESS，但sam尚未执行任何操作
    public static String ali_payed_sam_feed = "850000";                   // 订单已支付，支付宝notify提示支付成功，提示状态TRADE_SUCCESS，SAM完成充值但未更改套餐
    public static String ali_payed_sam_pkged = "880000";                  // 订单已支付，支付宝notify提示支付成功，提示状态TRADE_SUCCESS，SAM完成充值同时完成更改套餐

    public static String return_success_to_ali = "900000";                // 订单已支付，SAM端已处理完业务，并已返回success码给支付宝，但尚未收到支付宝notify的TRADE_FINISHED
    public static String ali_trade_finished = "1000000";                  // 支付宝notify返回TRADE_FINISHED状态，表示交易已经成功完成，此应为交易最终状态 （支持退款的功能，只有在最大退款周期过了之后才会有此通知）

    public static String ORDER_WAIT = "待支付";                           // 待支付
    public static String ORDER_SUCEESS = "交易成功";                      // 交易成功
    public static String ORDER_CLOSE = "交易关闭";                        // 交易关闭

    // 设置所需描述
    public static String CHANGE = "修改密码";                             // 修改密码
    public static String LOGOUT = "退出登录";                             // 退出登录
    public static String MY_ORDER = "我的订单";                           // 我的订单
    public static String MY_REPAIR = "我的报修";                          // 我的报修
    public static String MY_REIMBURSEMENT = "我的报销";                   // 我的报销
    public static String USE_HELP = "使用帮助";                           // 使用帮助
    public static String ABOUT_SOFT = "关于本软件";                       // 关于本软件
    public static String SOFT_UP = "软件升级";                            // 软件升级

    /**
     * Date 2016-6-28
     * @author wangshengbo
     * 首页功能描述
     */
    public static String SHUIHUA="18";
    public static String YUEDU="20";
    public static String MANHUA="21";
    public static String KAOLA="19";
    public static String DUANKAI="断开网络";
    public static String SHIKEBIAO="8";
    public static String KONGJIAOSHI="7";
    public static String KAOSHIANPAI="6";
    public static String CHACHENGJI="5";
    public static String KECHENGBIAO="4";
    public static String YIJIANNET="3";
    public static String BAOXIU="2";
    public static String JIAOFEI="1";
    public static String XIAOLI = "9";                                   // 校历
    public static String XINWEN = "10";                         // 新闻
    public static String JIANGZUO = "11";                           // 讲座预告
    public static String YIKATONG = "12";                                  // 一卡通
    public static String BANGONGDIANHUA = "13";                                  // 办公电话表
    public static String TONGXUNLU = "14";                                  // 通讯录
    public static String DITU = "15";                                  // 地图
    public static String BAOXIAO = "16";                                  //  报销
    public static String GONGZICHAXUN = "17";                                  // 工资查询
    public static String XINXIFABU="24";
    public static String GONGWEN="25";
    public static String BANSHI="23";
    public static String GENGDUO = "更多";                             //
    public static String ZHOUHUIYI = "26";                                  // 周会议
    public static String BAOGAOTING = "27";
    public static String JSTXL = "22";
    public static String FEEKBACK = "意见反馈";
    public static  String  ZHONGDIAN="28";
    public static  String  ERSHOUSHICHANG = "29";                          //二手市场
    public static String SHIWUZHAOLING = "30";                             //失物招领
    public static String JIANZHI = "31";                                  //兼职
    public static String MUKE="32";                                      //慕课
    public static String GUIZHANGZHIDU="33";                              //规章制度
    public static String UPDATE_URL = "";                                 // 升级URL

    public static String UPDATE_CONTENT = "";                             // 升级内容

    public static String SYSVERSION = "";    //系统版本号


    public static String queryString = "";


    /**
     * 2016-2-15
     *
     * wangshengbo
     *
     * 图片缓存路径
     */

    /**
     * 根目录 *
     */
    public static String rootPath = "";

    /**
     * 缓存路径 *
     */
    public static String imgLoaderPath = "imageloader/Cache";
    public static String imgPath = "";
    public static String dbPath = "";
    public static String bugPath = "";


	/**
	 * 请求必传参数
	 */
	public static String BASEPARAMS = "";
    public static final int REQUESTCODE = 100;//activity回调标志

    public static boolean isRefresh = false;//是否刷新列表

    /**
     * 通讯录   办公电话
     */

    public static int CONTACTSCODE = 200;//
    public static int PHONECODE = 300;//



}
