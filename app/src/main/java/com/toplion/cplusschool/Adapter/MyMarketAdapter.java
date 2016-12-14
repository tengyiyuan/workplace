package com.toplion.cplusschool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Bean.MarketBean;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoBrowser.photo.ImagePagerActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.SecondMarket.MyMarket;
import com.toplion.cplusschool.SecondMarket.ReleaseActivity;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ImageUtil;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyMarketAdapter extends BaseAdapter {
    private List<MarketBean> mlist;
    private Context mcontext;
    private AbImageLoader mAbImageLoader = null;
    private AbHttpUtil abHttpUtil;//网络请求工具
    private int style = 1;
    private int auitype = 1;
    private Bitmap bt;

    public void setMlist(List<MarketBean> mlist) {
        this.mlist = mlist;
    }

    public MyMarketAdapter(Context context, List<MarketBean> list, int style, int auitype) {
        this.style = style;
        this.auitype = auitype;
        this.mlist = list;
        this.mcontext = context;
        //图片的下载
        mAbImageLoader = AbImageLoader.getInstance(mcontext);
        abHttpUtil = AbHttpUtil.getInstance(mcontext);//初始化请求工具
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
            convertView = View.inflate(mcontext, R.layout.mymarketitem, null);
            viewHolder.rentou = (ImageView) convertView.findViewById(R.id.rentou);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.schname = (TextView) convertView.findViewById(R.id.school);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.biaoqian = (TextView) convertView.findViewById(R.id.biaoqian);
            viewHolder.imglay = (LinearLayout) convertView.findViewById(R.id.imglay);
            viewHolder.del = (TextView) convertView.findViewById(R.id.del);
            viewHolder.bianji = (TextView) convertView.findViewById(R.id.bianji);
            viewHolder.xiajia = (TextView) convertView.findViewById(R.id.xiajia);
            viewHolder.chexiao = (ImageView) convertView.findViewById(R.id.chexiao);
            bt = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.rentou);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mlist.get(position).getAUICONTACTNAME());
        viewHolder.schname.setText(mlist.get(position).getAUIADDRESS() + "  " + mlist.get(position).getAUIRELEASETIME());
        if (style == 2) {
            viewHolder.money.setVisibility(View.VISIBLE);
            viewHolder.money.setText("¥ " + mlist.get(position).getAUIPRICE() + "");
        } else if (style == 3 && !TextUtils.isEmpty(mlist.get(position).getUINAME())) {
            viewHolder.money.setVisibility(View.VISIBLE);
            if (mlist.get(position).getUINAME().equals("面议")) {
                viewHolder.money.setText(mlist.get(position).getUINAME());
            } else {
                viewHolder.money.setText("¥ " + mlist.get(position).getAUIPRICE() + "/" + mlist.get(position).getUINAME());
            }
        } else {
            viewHolder.money.setVisibility(View.GONE);
        }
        if (mlist.get(position).getAUICONTACTNAME().length() > 0 && !mlist.get(position).getAUICONTACTNAME().equals("某某某")) {
            viewHolder.name.setText(mlist.get(position).getAUICONTACTNAME());
        } else {
            viewHolder.name.setText(mlist.get(position).getNC()+"");
        }
        int roundPx = bt.getWidth()/2;
        ImageUtil.loadHead(mcontext,mlist.get(position).getTXDZ(),viewHolder.rentou,bt.getWidth(),bt.getHeight(),roundPx);
        if (!mlist.get(position).getCINAME().equals("")) {
            viewHolder.biaoqian.setVisibility(View.VISIBLE);
            if (mlist.get(position).getCINAME().equals("生活用品")) {
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_red);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_shenghuo));
            } else if (mlist.get(position).getCINAME().equals("学习用品")) {
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_lan);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_xuexi));
            } else if (mlist.get(position).getCINAME().equals("电子产品")) {
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian_green);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_dianzi));
            } else {
                viewHolder.biaoqian.setBackgroundResource(R.drawable.biaoqian);
                viewHolder.biaoqian.setTextColor(mcontext.getResources().getColor(R.color.biaoqian_qita));
            }
            viewHolder.biaoqian.setText(mlist.get(position).getCINAME());
        } else {
            viewHolder.biaoqian.setVisibility(View.GONE);
        }

        if (mlist.get(position).getAUISTATUS() == 1) {
            viewHolder.xiajia.setText("重新发布");
            viewHolder.chexiao.setVisibility(View.VISIBLE);
        } else {
//            if(style == 2){
//                viewHolder.xiajia.setText("下架");
//                viewHolder.chexiao.setVisibility(View.GONE);
//            }else{
            viewHolder.xiajia.setText("撤销");
            viewHolder.chexiao.setVisibility(View.GONE);
//            }

        }

        viewHolder.title.setText(mlist.get(position).getAUITITLE());
        viewHolder.imglay.removeAllViews();
        int lenth = mlist.get(position).getIRIURL().size();
        for (int i = 0; i < lenth; i++) {
            ImageView imageView = new ImageView(mcontext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseApplication.ScreenWidth / 3 - 24, BaseApplication.ScreenWidth / 3 - 24);
            layoutParams.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundResource(R.mipmap.zhanwei);
            mAbImageLoader.display(imageView, mlist.get(position).getIRIURL().get(i));
            final int ids = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urls = new ArrayList<String>();
                    for (int j = 0; j < mlist.get(position).getIRIURL().size(); j++) {
                        urls.add(mlist.get(position).getIRIURL().get(j).replace("thumb/", ""));
                    }
                    imageBrower(ids, urls);
                }
            });
            viewHolder.imglay.addView(imageView);
        }

        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog dialog = new CustomDialog(
                        mcontext);
                dialog.setlinecolor();
                dialog.setTitle("刪除信息");
                dialog.setContentboolean(true);
                dialog.setDetial("确认删除该条信息吗?");
                dialog.setLeftText("确定");
                dialog.setRightText("取消");
                dialog.setLeftOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        DelMessage(mlist.get(position));
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
        viewHolder.bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateMessage(mlist.get(position));
            }
        });
        viewHolder.xiajia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.get(position).getAUISTATUS() == 1) {
                    XiajiaMessage(mlist.get(position), position, viewHolder.xiajia, 2);
                } else {
                    XiajiaMessage(mlist.get(position), position, viewHolder.xiajia, 1);
                }
            }
        });
        return convertView;
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mcontext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mcontext.startActivity(intent);
    }

    class ViewHolder {
        private ImageView rentou;
        private TextView name;
        private TextView del;
        private TextView bianji;
        private TextView xiajia;
        private TextView biaoqian;
        private TextView title;
        private TextView money;
        private TextView schname;
        private TextView time;
        private LinearLayout imglay;
        private ImageView chexiao;
    }

    /**
     * 删除发布信息
     */
    private void DelMessage(final MarketBean bean) {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("relid", bean.getAUIID());
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("deleteReleaseInfoByUuid") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent((Activity) mcontext, "正在删除数据...") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject obj = new JSONObject(Function.getInstance().getString(object, "data"));
                    int nowresult = Function.getInstance().getInteger(obj, "result");
                    if (nowresult == 1) {
                        ToastManager.getInstance().showToast(mcontext, "删除成功");
                        mlist.remove(bean);
                        notifyDataSetChanged();
                        MyMarket.flag = "true";
                    } else {
                        ToastManager.getInstance().showToast(mcontext, "删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(mcontext, "删除失败");
                }
            }
        });

    }

    /**
     * 编辑发布信息
     */
    private void UpdateMessage(MarketBean bean) {
        Intent intent = new Intent();
        intent.putExtra("markbean", bean);
        intent.putExtra("module", style);
        intent.putExtra("reltype", auitype);
        intent.setClass(mcontext, ReleaseActivity.class);
        ((Activity) mcontext).startActivityForResult(intent, auitype);
    }

    /**
     * 上/下架发布信息
     */
    private void XiajiaMessage(final MarketBean bean, final int index, final TextView textView, int state) {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("relid", bean.getAUIID());
        params.put("state", state);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updateFromSaleByUuid") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent((Activity) mcontext, "正在修改...") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String msg = Function.getInstance().getString(object, "msg");
                    JSONObject obj = new JSONObject(Function.getInstance().getString(object, "data"));
                    int nowresult = Function.getInstance().getInteger(obj, "state");
                    if (nowresult == 1) {
                        final CommDialog dialog = new CommDialog(mcontext);
                        dialog.CreateDialogOnlyOk("提示", "确定", msg, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                // 判断点击按钮
                                if (flag) {
                                    dialog.cancelDialog();
                                }
                            }
                        });
                        MyMarket.flag = "true";
                        textView.setText("重新发布");
                        mlist.get(index).setAUISTATUS(1);
                        notifyDataSetChanged();
                    } else if (nowresult == 2) {
                        final CommDialog dialog = new CommDialog(mcontext);
                        dialog.CreateDialogOnlyOk("提示", "确定", msg, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                // 判断点击按钮
                                if (flag) {
                                    dialog.cancelDialog();
                                }
                            }
                        });
                        MyMarket.flag = "true";
//                        if(style == 2){
//                            textView.setText("下架");
//                        }else{
                        textView.setText("撤销");
//                        }
                        mlist.get(index).setAUISTATUS(2);
                        notifyDataSetChanged();
                    } else {
                        ToastManager.getInstance().showToast(mcontext, "操作失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(mcontext, "操作失败");
                }
            }
        });

    }
}