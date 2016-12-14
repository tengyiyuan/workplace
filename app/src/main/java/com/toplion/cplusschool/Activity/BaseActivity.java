package com.toplion.cplusschool.Activity;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.umeng.analytics.MobclickAgent;
/**
 * 基础Activity
 * @author WangShengbo
 * @Date 20162-29
 *
 */
public abstract class BaseActivity extends AbActivity{
	public AbHttpUtil abHttpUtil;//网络请求框架

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		abHttpUtil = AbHttpUtil.getInstance(this);

		//init();
		//setListener();
	}
	/**
	 * 设置显示界面
	 */
	protected void init(){}

	/**
	 * 获取数据
	 */
	protected void getData() {

	}
	/**
	 * 设置监听
	 *
	 * @return
	 */
	protected void setListener(){
	}
	@Override
	protected void onResume() {
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
		MobclickAgent.onPause(this);
		super.onPause();
	}
}