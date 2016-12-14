package com.toplion.cplusschool.PhotoWall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoWall.bean.MyPhotoReleaseListBean;
import com.toplion.cplusschool.PhotoWall.bean.PhotoInfoBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/10/31.
 *
 * @des 鲜花排行榜
 */

public class FlowerRankListActivity extends BaseActivity implements AbPullToRefreshView.OnHeaderRefreshListener{
    private ImageView iv_flist_back;//返回键
    private AbPullToRefreshView abPullToRefreshView;
    private ListView lv_flower_rank_list;//列表控件
    private RelativeLayout rl_nodata;
    private ImageView iv_dis;
    private AbHttpUtil abHttpUtil;
    private int pageIndex = 1;
    private int pageSize = 10;//每页显示的条数
    private MyAdapter myAdapter;
    private List<PhotoInfoBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flower_rank_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        iv_flist_back = (ImageView) findViewById(R.id.iv_flist_back);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        iv_dis = (ImageView) findViewById(R.id.iv_dis);
        rl_nodata.setVisibility(View.GONE);
        abPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.abPullToRefreshView);
        abPullToRefreshView.setOnHeaderRefreshListener(this);
//        abPullToRefreshView.setOnFooterLoadListener(this);
        abPullToRefreshView.setLoadMoreEnable(false);
        // 设置进度条的样式
        abPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        lv_flower_rank_list = (ListView) findViewById(R.id.lv_flower_rank_list);
        list = new ArrayList<PhotoInfoBean>();
        myAdapter = new MyAdapter(this, list);
        lv_flower_rank_list.setAdapter(myAdapter);
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        SharePreferenceUtils share = new SharePreferenceUtils(this);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getPhotowallRankList") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("page", 1);
        params.put("pageCount", pageSize);
        abHttpUtil.post(url, params, new CallBackParent(this, getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                MyPhotoReleaseListBean listBean = AbJsonUtil.fromJson(result, MyPhotoReleaseListBean.class);
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
                            infoBean.setPHH(Function.getInstance().getInteger(jsonObject, "PHH"));
                            mlist.add(infoBean);
                        }
//                        if (mlist.size() >= pageSize) {
//                            pageIndex++;
//                            abPullToRefreshView.setLoadMoreEnable(true);
//                        } else {
//                            abPullToRefreshView.setLoadMoreEnable(false);
//                        }
                        list.addAll(mlist);
                        pageIndex++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    abPullToRefreshView.setLoadMoreEnable(false);
                }
                Log.e("pageIndex", pageIndex + "");
                if (list.size() > 0) {
                    abPullToRefreshView.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    myAdapter.setMlist(list);
                    myAdapter.notifyDataSetChanged();
                } else {
                    abPullToRefreshView.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                }
                abPullToRefreshView.onFooterLoadFinish();
                abPullToRefreshView.onHeaderRefreshFinish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 103) {
            pageIndex = 1;
            list.clear();
            getData();
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        lv_flower_rank_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlowerRankListActivity.this, PhotoContent.class);
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
        iv_flist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        pageIndex = 1;
        list.clear();
        getData();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.zhanwei);
                convertView = View.inflate(mContext, R.layout.flower_rank_list_item, null);
                viewHolder.iv_flower_icon = (ImageView) convertView.findViewById(R.id.iv_flower_icon);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bt.getWidth(), bt.getWidth());
                viewHolder.iv_flower_icon.setLayoutParams(params);
                viewHolder.tv_flower_nickname = (TextView) convertView.findViewById(R.id.tv_flower_nickname);
                viewHolder.iv_flower_sex = (ImageView) convertView.findViewById(R.id.iv_flower_sex);
                viewHolder.tv_flower_school = (TextView) convertView.findViewById(R.id.tv_flower_school);
                viewHolder.iv_flower_mc = (ImageView) convertView.findViewById(R.id.iv_flower_mc);
                viewHolder.tv_flower_number = (TextView) convertView.findViewById(R.id.tv_flower_number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AbImageLoader.getInstance(mContext).display(viewHolder.iv_flower_icon, mlist.get(position).getPWBURL());
            viewHolder.tv_flower_school.setText(mlist.get(position).getSDS_NAME());
            viewHolder.iv_flower_mc.setImageResource(getResource(mlist.get(position).getPHH()));
            viewHolder.tv_flower_nickname.setText(mlist.get(position).getNC() + "");
            String sex = mlist.get(position).getXBM();
            if (sex.equals("1")) {
                viewHolder.iv_flower_sex.setImageResource(R.mipmap.boy);
            } else if (sex.equals("2")) {
                viewHolder.iv_flower_sex.setImageResource(R.mipmap.girl);
            }
            viewHolder.tv_flower_number.setText(mlist.get(position).getPWBFLOWERSNUMBER() + "");
            return convertView;
        }

        class ViewHolder {
            private ImageView iv_flower_icon;
            private TextView tv_flower_nickname;
            private ImageView iv_flower_sex;
            private TextView tv_flower_school;
            private TextView tv_flower_number;
            private ImageView iv_flower_mc;
        }

        private int getResource(int i) {
            int resId = 0;
            switch (i) {
                case 1:
                    resId = R.mipmap.flower_one;
                    break;
                case 2:
                    resId = R.mipmap.flower_two;
                    break;
                case 3:
                    resId = R.mipmap.flower_three;
                    break;
                case 4:
                    resId = R.mipmap.flower_four;
                    break;
                case 5:
                    resId = R.mipmap.flower_five;
                    break;
                case 6:
                    resId = R.mipmap.flower_six;
                    break;
                case 7:
                    resId = R.mipmap.flower_seven;
                    break;
                case 8:
                    resId = R.mipmap.flower_eight;
                    break;
                case 9:
                    resId = R.mipmap.flower_nine;
                    break;
                case 10:
                    resId = R.mipmap.flower_ten;
                    break;
            }
            return resId;
        }
    }
}
