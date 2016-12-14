package com.toplion.cplusschool.PhotoWall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.util.AbDateUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbJsonUtil;
import com.google.gson.Gson;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.KongAdapter;
import com.toplion.cplusschool.Bean.Kongitem;
import com.toplion.cplusschool.Bean.PingLunBean;
import com.toplion.cplusschool.Bean.PingLunListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoWall.bean.PhotoInfoBean;
import com.toplion.cplusschool.QianDao.QianDaoActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.Utils.Tools;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.ListViewInScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by toplion on 2016/11/2.
 */
public class PhotoContent extends BaseActivity {
    private RelativeLayout rl_parent;
    private ImageView myphoto;
    private ImageView rentou;//头像
    private TextView card_user_name;//姓名
    private ImageView iv_sex;//性别
    private TextView card_other_description;//学校
    private TextView card_like;//鲜花数量
    private ListViewInScrollView liuyanlist;//留言列表
    private EditText liuyan;//留言输入框
    private TextView fayan;//发表
    private Button btn_song;//送花
    private PhotoInfoBean cardItem;
    private AbHttpUtil abHttpUtil;
    private SharePreferenceUtils share;
    private boolean isRefresh = false;//照片墙主页是否需要刷新
    private int num = 0;//送花数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photocontent);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        cardItem = (PhotoInfoBean) getIntent().getSerializableExtra("cardItem");
        rl_parent = (RelativeLayout) findViewById(R.id.rl_parent);
        myphoto = (ImageView) findViewById(R.id.myphoto);
        btn_song = (Button) findViewById(R.id.btn_song);
        int imgWidth = BaseApplication.ScreenWidth - 20;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imgWidth, imgWidth);
        params.gravity = Gravity.CENTER;
        myphoto.setLayoutParams(params);
        rentou = (ImageView) findViewById(R.id.rentou);
        card_user_name = (TextView) findViewById(R.id.card_user_name);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        card_other_description = (TextView) findViewById(R.id.card_other_description);
        card_like = (TextView) findViewById(R.id.card_like);
        liuyanlist = (ListViewInScrollView) findViewById(R.id.liuyanlist);
        liuyan = (EditText) findViewById(R.id.liuyan);
        fayan = (TextView) findViewById(R.id.fayan);
        if (cardItem.getXBM().equals("2")) {
            iv_sex.setImageResource(R.mipmap.girl);
        } else if (cardItem.getXBM().equals("1")) {
            iv_sex.setImageResource(R.mipmap.boy);
        }
        setTextData();
    }

    private void setTextData() {
        AbImageLoader.getInstance(this).display(myphoto, cardItem.getPWBURL());
        card_user_name.setText(cardItem.getNC());
        card_other_description.setText(cardItem.getSDS_NAME());
        card_like.setText(cardItem.getPWBFLOWERSNUMBER() + "");
        loadHead(rentou, cardItem.getTXDZ());
        getData();
        setListener();
    }

    @Override
    protected void setListener() {
        super.setListener();
        btn_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        //发表评论
        fayan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(liuyan.getText().toString().trim())) {
                    addPhotoComment(liuyan.getText().toString());

                } else {
                    ToastManager.getInstance().showToast(PhotoContent.this, "请填写评论内容");
                }
            }
        });
        ImageView about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRefresh) {
                    Intent intent = new Intent();
                    intent.putExtra("giveFlowerNum", num);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

    }

    private void showPop() {
        final FlowersPopWindow flowersPopWindow = new FlowersPopWindow(PhotoContent.this, rl_parent);
        flowersPopWindow.setSendFlowerList(initFlowerData());
        final int nowFlowerNum = share.getInt("SBIFLOWERSNUMBER", 0);
        flowersPopWindow.setNowFlowersNumber(nowFlowerNum);
//        if (Integer.parseInt(flowersPopWindow.tv_number.getText().toString()) >= nowFlowerNum) {
//            flowersPopWindow.iv_jia.setEnabled(false);
//        } else {
//            flowersPopWindow.iv_jia.setEnabled(true);
//        }
        flowersPopWindow.iv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (num > 1) {
                    num--;
                    flowersPopWindow.tv_number.setText(num + "");
                }
            }
        });
        flowersPopWindow.iv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (num < nowFlowerNum) {
                    num++;
                    flowersPopWindow.tv_number.setText(num + "");
                }
            }
        });
        flowersPopWindow.setGiveFlowerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (num > 0) {
                    if (num <= nowFlowerNum) {
                        giveFlowers(num);//送花
                        flowersPopWindow.dismiss();
                    } else {
                        ToastManager.getInstance().showToast(PhotoContent.this, "超出您拥有的的鲜花数量");
                    }
                } else {
                    ToastManager.getInstance().showToast(PhotoContent.this, "至少送一朵鲜花");
                }
            }
        });
        flowersPopWindow.setZuanFlowerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoContent.this, QianDaoActivity.class);
                startActivity(intent);
                flowersPopWindow.dismiss();
            }
        });
        flowersPopWindow.isShowing();
    }

    private List<Map<String, String>> initFlowerData() {
        List<Map<String, String>> mlist = new ArrayList<Map<String, String>>();
        //给标题栏弹窗添加子类
        Map<String, String> map = new HashMap<String, String>();
        map.put("count", "99");
        map.put("des", "天长地久");
        mlist.add(map);

        map = new HashMap<String, String>();
        map.put("count", "66");
        map.put("des", "六六大顺");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "50");
        map.put("des", "五彩缤纷");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "10");
        map.put("des", "十全十美");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "1");
        map.put("des", "一心一意");
        mlist.add(map);
        return mlist;
    }

    //送鲜花
    private void giveFlowers(final int n) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("giveFlowers") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("photoid", cardItem.getPWBID());//照片id
        params.put("goodid", "1");
        params.put("count", n + "");//每次赠送的数量
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading), "") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String msg = json.getString("msg");
                    int count = json.getInt("data");//现有鲜花数量
                    share.put("SBIFLOWERSNUMBER", count);
//                    cardItem.likeNum = cardItem.likeNum + num;
                    cardItem.setPWBFLOWERSNUMBER(cardItem.getPWBFLOWERSNUMBER() + num);
                    card_like.setText(cardItem.getPWBFLOWERSNUMBER() + "");
                    isRefresh = true;
                    ToastManager.getInstance().showToast(PhotoContent.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //添加评论
    private void addPhotoComment(String content) {
        SharePreferenceUtils share = new SharePreferenceUtils(this);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("addPhotoComment") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("photoid", cardItem.getPWBID());
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("comment", content);
        params.put("replyid", "");
        abHttpUtil.post(url, params, new CallBackParent(this, getString(R.string.loading), "addPhotoComment") {
            @Override
            public void Get_Result(String result) {
                Tools.HideKeyboard(liuyan);
                liuyan.setText("");
                isRefresh = true;
                getData();
            }
        });
    }

    /**
     * 加载头像
     *
     * @param url
     */
    private void loadHead(final ImageView view, String url) {
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.rentou);
        final int roundPx = bt.getWidth() / 2;
        AbImageLoader.getInstance(this).download(url, bt.getWidth(), bt.getWidth(), new AbImageLoader.OnImageListener2() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                bitmap = AbImageUtil.toRoundBitmap(bitmap, roundPx);
                view.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("queryReviewInfo") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("photoid", cardItem.getPWBID());
        abHttpUtil.post(url, params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                PingLunListBean pingLunListBean = AbJsonUtil.fromJson(result, PingLunListBean.class);
                if (pingLunListBean != null && pingLunListBean.getData() != null && !pingLunListBean.getData().equals("[]")) {
                    List<PingLunBean> list = pingLunListBean.getData();
                    liuyanlist.setAdapter(new MyAdapter(PhotoContent.this, list));
                } else {
                    ToastManager.getInstance().showToast(PhotoContent.this, "暂无评论信息");
                }
            }
        });
    }


    class MyAdapter extends BaseAdapter {
        private List<PingLunBean> mlist;
        private Context mContext;

        public MyAdapter(Context context, List<PingLunBean> list) {
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.pinglun_list_item, null);
                viewHolder.iv_pl_head_icon = (ImageView) convertView.findViewById(R.id.iv_pl_head_icon);
                viewHolder.tv_pl_nickname = (TextView) convertView.findViewById(R.id.tv_pl_nickname);
                viewHolder.iv_pl_sex = (ImageView) convertView.findViewById(R.id.iv_pl_sex);
                viewHolder.tv_pl_content = (TextView) convertView.findViewById(R.id.tv_pl_content);
                viewHolder.tv_pl_time = (TextView) convertView.findViewById(R.id.tv_pl_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            loadHead(viewHolder.iv_pl_head_icon, mlist.get(position).getTxdz());
            viewHolder.tv_pl_nickname.setText(mlist.get(position).getNc());
            viewHolder.tv_pl_content.setText(mlist.get(position).getPwe_content());
            if (!TextUtils.isEmpty(mlist.get(position).getPwe_createtime())) {
                Date time = TimeUtils.stringToDate(mlist.get(position).getPwe_createtime(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZZZ");
                viewHolder.tv_pl_time.setText(AbDateUtil.getStringByFormat(time, "yyyy-MM-dd HH:mm"));
            }
            return convertView;
        }

        class ViewHolder {
            private ImageView iv_pl_head_icon;
            private TextView tv_pl_nickname;
            private ImageView iv_pl_sex;
            private TextView tv_pl_content;
            private TextView tv_pl_time;
        }
    }


    @Override
    public void onBackPressed() {
        if (isRefresh) {
            Intent intent = new Intent();
            intent.putExtra("giveFlowerNum", num);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
