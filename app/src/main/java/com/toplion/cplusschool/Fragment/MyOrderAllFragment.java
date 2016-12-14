package com.toplion.cplusschool.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.toplion.cplusschool.Activity.MyOrderDetailActivity;
import com.toplion.cplusschool.Activity.PayOrderActivity;
import com.toplion.cplusschool.Adapter.MyOrderListAdapter;
import com.toplion.cplusschool.Bean.OrderBean;
import com.toplion.cplusschool.Bean.OrderListBeanModel;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单 全部显示订单的数据
 * 显示交易完成的订单
 * 显示交易关闭的订单
 * 显示未支付的订单
 * 下拉刷新
 * @version 1.0
 * @author liyb
 *
 */
public class MyOrderAllFragment extends Fragment implements OnHeaderRefreshListener,
		OnFooterLoadListener {
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView listView;                        // 显示订单的ListView
	private SharePreferenceUtils share;
	private LinearLayout my_order_all_datas;          // 订单全部有数据
	private LinearLayout my_order_all_nodatas;        // 订单全部无数据
	private List<OrderBean> orderList = new ArrayList<OrderBean>();//订单列表
	private AbHttpUtil abHttpUtil;//网络请求工具
	private MyOrderListAdapter myOrderListAdapter;//订单列表适配器
	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		mAbPullToRefreshView.onFooterLoadFinish();
	}
	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		getOrderList();
	}

	// 获取Fragment View
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_order_all, container, false);
		abHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		share = new SharePreferenceUtils(getActivity());
		listView = (ListView) view.findViewById(R.id.my_order_all_list);            // 订单集合

		// 页面全部隐藏
		my_order_all_datas = (LinearLayout) view.findViewById(R.id.my_order_all_datas);
		my_order_all_datas.setVisibility(View.GONE);
		my_order_all_nodatas = (LinearLayout) view.findViewById(R.id.my_order_all_nodatas);
		my_order_all_nodatas.setVisibility(View.GONE);

		myOrderListAdapter = new MyOrderListAdapter(getActivity(),orderList,null);
		listView.setAdapter(myOrderListAdapter);
		setListener();
		getOrderList();
		return view;
	}
	/**
	 * 获取订单列表
	 */
	private void getOrderList(){
		String url = Constants.BASE_URL+"?rid=" + ReturnUtils.encode("getUserOrderInfo")+Constants.BASEPARAMS;
		AbRequestParams requestParams = new AbRequestParams();
		requestParams.put("username", share.getString("username", ""));
		// 获取全部订单数据，类型为100000
		requestParams.put("type", Constants.ORDER_ALL);

		abHttpUtil.post(url,requestParams,new CallBackParent(getActivity(),"正在加载,请稍后...") {
			@Override
			public void Get_Result(String content) {
				Log.e("orderList====",content+"");
				AbDialogUtil.removeDialog(getActivity());
				OrderListBeanModel orderListBeanModel = AbJsonUtil.fromJson(content,OrderListBeanModel.class);
				if(orderListBeanModel.getCode().equals(CacheConstants.LOCAL_SUCCESS) || orderListBeanModel.getCode().equals(CacheConstants.SAM_SUCCESS)){
					if(orderListBeanModel.getData()!=null&&orderListBeanModel.getData().getUserOrderInfo()!=null){
						orderList = orderListBeanModel.getData().getUserOrderInfo();//订单列表
						if(orderList.size()<1){
							my_order_all_datas.setVisibility(View.GONE);
							my_order_all_nodatas.setVisibility(View.VISIBLE);
						}else{
							my_order_all_datas.setVisibility(View.VISIBLE);
							my_order_all_nodatas.setVisibility(View.GONE);
							myOrderListAdapter.setMlist(orderList);
							myOrderListAdapter.notifyDataSetChanged();
						}
					}
				}else{
					my_order_all_datas.setVisibility(View.GONE);
					my_order_all_nodatas.setVisibility(View.VISIBLE);
					ToastManager.getInstance().showToast(getActivity(), orderListBeanModel.getMsg());
				}
			}


			@Override
			public void onFinish() {
				AbDialogUtil.removeDialog(getActivity());
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				AbDialogUtil.removeDialog(getActivity());
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}
		});
	}
	//设置点击事件
	private void setListener(){
// 点击具体的订单
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String num = orderList.get(position).getOrderId();// 订单编号
				String state = orderList.get(position).getOrderState();// 订单状态
				String pkgName = orderList.get(position).getOrderPackageName();// 选择的套餐
				String orderPayString = orderList.get(position).getOrderPayString();//订单支付字符串
				Intent intent = null;
				if(state.equals(Constants.not_pay)){  // 待支付
					intent = new Intent(getActivity(), PayOrderActivity.class);
					intent.putExtra("pkgName", pkgName);
					intent.putExtra("orderPayString",orderPayString);
					// 待支付
				}else {
					if(state.equals(Constants.pay_closed)){
						// 交易关闭
						state = Constants.ORDER_CLOSE;
					}else if(state.equals(Constants.ali_not_pay) || state.equals(Constants.ali_payed_not_in_sam)
							|| state.equals(Constants.ali_payed_sam_feed) || state.equals(Constants.return_success_to_ali)
							|| state.equals(Constants.ali_payed_sam_pkged) || state.equals(Constants.ali_trade_finished)){
						// 交易成功
						state = Constants.ORDER_SUCEESS;
					}else {
						state = Constants.ORDER_CLOSE;
					}
                    // 根据状态的不同跳转到不同的Activity
                    intent = new Intent(getActivity(), MyOrderDetailActivity.class);
                    intent.putExtra("state", state);
				}
				//传值到订单详情Activity
				intent.putExtra("orderNum",num);
				startActivity(intent);
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		// 初始化数据
		MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
	}
}
