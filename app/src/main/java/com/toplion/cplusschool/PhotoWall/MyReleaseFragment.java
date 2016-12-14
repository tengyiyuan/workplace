package com.toplion.cplusschool.PhotoWall;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoWall.bean.MyPhotoReleaseListBean;
import com.toplion.cplusschool.PhotoWall.bean.PhotoInfoBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/11/28.
 * 照片墙 -- 我的发布
 */

public class MyReleaseFragment extends Fragment implements AbPullToRefreshView.OnHeaderRefreshListener, AbPullToRefreshView.OnFooterLoadListener {
    private AbPullToRefreshView mPullRefreshView;
    private GridView gv_my_release_list;
    private RelativeLayout rl_nodata;
    private ImageView iv_dis;
    private AbHttpUtil abHttpUtil;
    private int pageIndex = 1;
    private int pageSize = 10;//每页显示的条数
    private List<PhotoInfoBean> list;
    private MyAdapter myAdapter;
    private int imgWidth = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_flower_release_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        abHttpUtil = AbHttpUtil.getInstance(getActivity());
        rl_nodata = (RelativeLayout) view.findViewById(R.id.rl_nodata);
        iv_dis = (ImageView) view.findViewById(R.id.iv_dis);
        mPullRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
        gv_my_release_list = (GridView) view.findViewById(R.id.gv_my_release_list);
        // 设置监听器
        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);
        imgWidth = (BaseApplication.ScreenWidth - 30) / 2;
        // 设置进度条的样式
        mPullRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mPullRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        rl_nodata.setVisibility(View.GONE);
        list = new ArrayList<PhotoInfoBean>();
        myAdapter = new MyAdapter(getActivity(), list);
        gv_my_release_list.setAdapter(myAdapter);
        getData();
        setListener();
    }

    private void getData() {
        SharePreferenceUtils share = new SharePreferenceUtils(getActivity());
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("findPhotowallInfoByUser") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("page", pageIndex);
        params.put("pageCount", pageSize);
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), getString(R.string.loading), "findPhotowallInfoByUser") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    List<PhotoInfoBean> mlist = new ArrayList<PhotoInfoBean>();
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            PhotoInfoBean infoBean = new PhotoInfoBean();
                            infoBean.setPWBID(Function.getInstance().getInteger(jsonObject, "PWBID"));
                            infoBean.setNC(Function.getInstance().getString(jsonObject, "NC"));
                            infoBean.setPWBURL(Function.getInstance().getString(jsonObject, "PWBURL").replace("thumb/", ""));
                            infoBean.setPWBFLOWERSNUMBER(Function.getInstance().getInteger(jsonObject, "PWBFLOWERSNUMBER"));
                            infoBean.setPWBTOTALNUMBER(Function.getInstance().getInteger(jsonObject, "PWBTOTALNUMBER"));
                            infoBean.setSDS_NAME(Function.getInstance().getString(jsonObject, "SDS_NAME"));
                            infoBean.setTXDZ(Function.getInstance().getString(jsonObject, "TXDZ").replace("thumb/", ""));
                            infoBean.setXBM(Function.getInstance().getString(jsonObject, "XBM"));
                            mlist.add(infoBean);
                        }
                        if (mlist.size() >= pageSize) {
                            pageIndex++;
                            mPullRefreshView.setLoadMoreEnable(true);
                        } else {
                            mPullRefreshView.setLoadMoreEnable(false);
                        }
                        list.addAll(mlist);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mPullRefreshView.setLoadMoreEnable(false);
                }
                if (list.size() > 0) {
                    mPullRefreshView.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    myAdapter.setMlist(list);
                    myAdapter.notifyDataSetChanged();
                } else {
                    mPullRefreshView.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                }
                mPullRefreshView.onHeaderRefreshFinish();
                mPullRefreshView.onFooterLoadFinish();
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                mPullRefreshView.onHeaderRefreshFinish();
                mPullRefreshView.onFooterLoadFinish();
                mPullRefreshView.setLoadMoreEnable(false);
                if (pageIndex <= 1) {
                    mPullRefreshView.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        pageIndex = 1;
        list.clear();
        getData();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 103) {
            pageIndex = 1;
            list.clear();
            getData();
        }
    }
    @Override
    public void onFooterLoad(AbPullToRefreshView view) {
        getData();
    }

    private void setListener() {
        gv_my_release_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PhotoContent.class);
                intent.putExtra("cardItem", list.get(position));
                startActivityForResult(intent, 103);
            }
        });
        iv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageIndex = 1;
                list.clear();
                getData();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private List<PhotoInfoBean> mlist;
        private Context mContext;

        public void setMlist(List<PhotoInfoBean> mlist) {
            this.mlist = mlist;
        }

        public MyAdapter(Context context, List<PhotoInfoBean> list) {
            this.mlist = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.my_flower_release_list_item, null);
                viewHolder.iv_fr_icon = (ImageView) convertView.findViewById(R.id.iv_fr_icon);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgWidth, imgWidth);
                viewHolder.iv_fr_icon.setLayoutParams(params);
                viewHolder.tv_my_fr_number = (TextView) convertView.findViewById(R.id.tv_my_fr_number);
                viewHolder.tv_my_fr_lnumber = (TextView) convertView.findViewById(R.id.tv_my_fr_lnumber);
                viewHolder.tv_my_fr_del = (ImageView) convertView.findViewById(R.id.tv_my_fr_del);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AbImageLoader.getInstance(mContext).display(viewHolder.iv_fr_icon, mlist.get(position).getPWBURL().replace("thumb/", ""));
            viewHolder.tv_my_fr_number.setText(mlist.get(position).getPWBFLOWERSNUMBER() + "");
            viewHolder.tv_my_fr_lnumber.setText(mlist.get(position).getPWBTOTALNUMBER() + "");
            viewHolder.tv_my_fr_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CustomDialog dialog = new CustomDialog(mContext);
                    dialog.setlinecolor();
                    dialog.setTitle("刪除信息");
                    dialog.setContentboolean(true);
                    dialog.setDetial("确认删除该条信息吗?");
                    dialog.setLeftText("确定");
                    dialog.setRightText("取消");
                    dialog.setLeftOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            deleteData(list.get(position));
                            dialog.dismiss();
                        }
                    });
                    dialog.setRightOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView iv_fr_icon;
            private TextView tv_my_fr_number;
            private TextView tv_my_fr_lnumber;
            private ImageView tv_my_fr_del;
        }

        /**
         * 删除照片
         *
         * @param photoInfoBean
         */
        private void deleteData(final PhotoInfoBean photoInfoBean) {
            String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("delPhotowall") + Constants.BASEPARAMS;
            AbRequestParams params = new AbRequestParams();
            params.put("photoid", photoInfoBean.getPWBID() + "");
            abHttpUtil.post(url, params, new CallBackParent(getActivity(), getString(R.string.loading), "delPhotowall") {
                @Override
                public void Get_Result(String result) {
                    ToastManager.getInstance().showToast(mContext, "删除成功");
                    mlist.remove(photoInfoBean);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
