package com.toplion.cplusschool.Activity;
import com.ab.activity.AbActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Fragment.MyOrderAllFragment;
import com.toplion.cplusschool.Fragment.MyOrderSuccessFragment;
import com.toplion.cplusschool.Fragment.MyOrderWaitFragment;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的订单Activity
 * 实现导航菜单左右滑动效果
 * @author liyb
 *
 */
public class MyOrderActivity extends AbActivity implements OnClickListener{
	// tab布局
	private RelativeLayout allLayout, waitLayout, successLayout;
	// 底部标签切换的Fragment
	private Fragment myOrderAllFragment, myOrderWaitFragment, myOrderSuccessFragment, currentFragment;
	// 底部标签的文本
	private TextView allTv, waitTv, successTv;
	private ImageView iv_return;                    // 返回
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order);
		iv_return = (ImageView) this.findViewById(R.id.my_order_iv_return);         //返回
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 返回上一级界面
				MyOrderActivity.this.finish();
			}
		});
		initUI();
		initTab();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		allLayout = (RelativeLayout) findViewById(R.id.my_order_all);
		waitLayout = (RelativeLayout) findViewById(R.id.my_order_wait);
		successLayout = (RelativeLayout) findViewById(R.id.my_order_success);

		allLayout.setOnClickListener(this);
		waitLayout.setOnClickListener(this);
		successLayout.setOnClickListener(this);

		allTv = (TextView) findViewById(R.id.my_order_tv_all);
		waitTv = (TextView) findViewById(R.id.my_order_tv_wait);
		successTv = (TextView) findViewById(R.id.my_order_tv_success);
	}

	/**
	 * 初始化底部标签
	 */
	private void initTab() {
		if (myOrderAllFragment == null) {
			myOrderAllFragment = new MyOrderAllFragment();
		}

		if (!myOrderAllFragment.isAdded()) {
			// 提交事务
			getSupportFragmentManager().beginTransaction().add(R.id.my_order_content_layout, myOrderAllFragment).commit();

			// 记录当前Fragment
			currentFragment = myOrderAllFragment;
			// 设置文本的变化
			allTv.setTextColor(getResources().getColor(R.color.logo_color));
			waitTv.setTextColor(getResources().getColor(R.color.height_gray));
			successTv.setTextColor(getResources().getColor(R.color.height_gray));
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.my_order_all: // 全部
				clickTabAllLayout();
				break;
			case R.id.my_order_wait: // 待付款
				clickTabWaitLayout();
				break;
			case R.id.my_order_success: // 交易成功
				clickTabSuccessLayout();
				break;
			default:
				break;
		}
	}

	/**
	 * 点击全部
	 */
	private void clickTabAllLayout() {
		if (myOrderAllFragment == null) {
			myOrderAllFragment = new MyOrderAllFragment();
		}
		addOrShowFragment(getSupportFragmentManager().beginTransaction(), myOrderAllFragment);

		// 设置底部tab变化
		allTv.setTextColor(getResources().getColor( R.color.logo_color));
		waitTv.setTextColor(getResources().getColor(R.color.height_gray));
		successTv.setTextColor(getResources().getColor(R.color.height_gray));
	}

	/**
	 * 点击待付款
	 */
	private void clickTabWaitLayout() {
		if (myOrderWaitFragment == null) {
			myOrderWaitFragment = new MyOrderWaitFragment();
		}

		addOrShowFragment(getSupportFragmentManager().beginTransaction(), myOrderWaitFragment);
		allTv.setTextColor(getResources().getColor( R.color.height_gray));
		successTv.setTextColor(getResources().getColor(R.color.height_gray));
		waitTv.setTextColor(getResources().getColor(R.color.logo_color));

	}

	/**
	 * 点击交易成功
	 */
	private void clickTabSuccessLayout() {
		if (myOrderSuccessFragment == null) {
			myOrderSuccessFragment = new MyOrderSuccessFragment();
		}
		addOrShowFragment(getSupportFragmentManager().beginTransaction(), myOrderSuccessFragment);
		allTv.setTextColor(getResources().getColor( R.color.height_gray));
		waitTv.setTextColor(getResources().getColor(R.color.height_gray));
		successTv.setTextColor(getResources().getColor(R.color.logo_color));

	}

	/**
	 * 添加或者显示碎片
	 *
	 * @param transaction
	 * @param fragment
	 */
	private void addOrShowFragment(FragmentTransaction transaction,
								   Fragment fragment) {
		if (currentFragment == fragment) return;

		if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
			transaction.hide(currentFragment).add(R.id.my_order_content_layout, fragment).commit();
		} else {
			transaction.hide(currentFragment).show(fragment).commit();
		}
		currentFragment = fragment;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
