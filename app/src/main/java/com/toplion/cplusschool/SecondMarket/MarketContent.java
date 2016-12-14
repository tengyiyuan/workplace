package com.toplion.cplusschool.SecondMarket;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.MarketBean;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.CallUtil;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.ImageUtil;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.MenuPopupWindow;

/**
 * Created by toplion on 2016/10/12.
 */
public class MarketContent extends BaseActivity {
    private ImageView loadimg;
    private RelativeLayout bottom;
    private ImageView about_iv_return;
    private AbImageLoader mAbImageLoader = null;
    private MarketBean market;
    private LinearLayout imglay;
    private ImageView rentou;
    private TextView myname;
    private TextView schooltime;
    private TextView money;
    private TextView biaoqian;
    private TextView title;
    private TextView content;
    private int style = 1;
    private TextView about_iv_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_content);
        init();
    }

    @Override
    protected void init() {
        super.init();
        about_iv_Title = (TextView) findViewById(R.id.about_iv_Title);
        title = (TextView) findViewById(R.id.title);
        rentou = (ImageView) findViewById(R.id.rentou);
        biaoqian = (TextView) findViewById(R.id.biaoqian);
        money = (TextView) findViewById(R.id.money);
        myname = (TextView) findViewById(R.id.myname);
        schooltime = (TextView) findViewById(R.id.schooltime);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        content = (TextView) findViewById(R.id.content);
        about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        //图片的下载
        mAbImageLoader = AbImageLoader.getInstance(this);
        style = getIntent().getIntExtra("style", 1);
        market = (MarketBean) getIntent().getSerializableExtra("infocontent");
        if (market.getAUICONTACTNAME().length() > 0 && !market.getAUICONTACTNAME().equals("某某某")) {
            myname.setText(market.getAUICONTACTNAME().substring(0, 1) + "同学");
        } else {
            myname.setText(market.getNC() + "");
        }
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.rentou);
        final int roundPx = bt.getWidth() / 2;
        ImageUtil.loadHead(this, market.getTXDZ(), rentou, bt.getWidth(), bt.getHeight(), roundPx);
        schooltime.setText(market.getAUIADDRESS() + " " + market.getAUIRELEASETIME());
        if (style == 2) {
            money.setVisibility(View.VISIBLE);
            money.setText("¥" + market.getAUIPRICE());
        } else if (style == 3 && !TextUtils.isEmpty(market.getUINAME())) {
            money.setVisibility(View.VISIBLE);
            if (market.getUINAME().equals("面议")) {
                money.setText(market.getUINAME());
            } else {
                money.setText("¥ " + market.getAUIPRICE() + "/" + market.getUINAME());
            }
        } else {
            money.setVisibility(View.GONE);
        }
        if (!market.getCINAME().equals("")) {
            biaoqian.setVisibility(View.VISIBLE);
            if (market.getCINAME().equals("生活用品")) {
                biaoqian.setBackgroundResource(R.drawable.biaoqian_red);
                biaoqian.setTextColor(getResources().getColor(R.color.biaoqian_shenghuo));
            } else if (market.getCINAME().equals("学习用品")) {
                biaoqian.setBackgroundResource(R.drawable.biaoqian_lan);
                biaoqian.setTextColor(getResources().getColor(R.color.biaoqian_xuexi));
            } else if (market.getCINAME().equals("电子产品")) {
                biaoqian.setBackgroundResource(R.drawable.biaoqian_green);
                biaoqian.setTextColor(getResources().getColor(R.color.biaoqian_dianzi));
            } else {
                biaoqian.setBackgroundResource(R.drawable.biaoqian);
                biaoqian.setTextColor(getResources().getColor(R.color.biaoqian_qita));
            }
            biaoqian.setText(market.getCINAME());
        } else {
            biaoqian.setVisibility(View.GONE);
        }
        title.setText(market.getAUITITLE());
        content.setText(market.getAUICONTENT());
        imglay = (LinearLayout) findViewById(R.id.imgs);
        int lenth = market.getIRIURL().size();
        imglay.removeAllViews();
        for (int i = 0; i < lenth; i++) {
            RelativeLayout ralay = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                    BaseApplication.ScreenWidth, BaseApplication.ScreenWidth);
            ralay.setLayoutParams(relativeParams);
            ralay.setGravity(Gravity.CENTER);

            ProgressBar pross = new ProgressBar(this);
            pross.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            pross.setProgressDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));


            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams linelay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(linelay);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setBackgroundResource(R.mipmap.zhanwei);
            mAbImageLoader.display(img, pross, market.getIRIURL().get(i).replace("thumb/", ""), BaseApplication.ScreenWidth, BaseApplication.ScreenWidth);

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams tvlay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
            textView.setLayoutParams(tvlay);
            textView.setBackgroundColor(Color.WHITE);
            ralay.addView(img);
            ralay.addView(textView);
            ralay.addView(pross);
            imglay.addView(ralay);
        }
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MenuPopupWindow menuWindow = new MenuPopupWindow(MarketContent.this);
                menuWindow.showAtLocation(MarketContent.this.findViewById(R.id.myshopinfo), Gravity.TOP, 0, 0);
                if (style == 1) {
                    menuWindow.setTitleText("帮助别人,就是帮助自己,拾金不昧,做个好学生");
                } else if (style == 2) {
                    menuWindow.setTitleText("本app仅为二手交易平台,线下交易请注意资金和财产安全!");
                } else {
                    menuWindow.setTitleText("好工作好兼职,从这里开始");
                }
                if (market.getAUIPHONE().equals("")) {
                    menuWindow.setCallboolean(false);
                } else {
                    menuWindow.setCallboolean(true);
                    menuWindow.setCallText("手机号码:" + market.getAUIPHONE());
                }
                if (market.getAUIQQ().equals("")) {
                    menuWindow.setMessageboolean(false);
                } else {
                    menuWindow.setMessageboolean(true);
                    menuWindow.setMessageText("QQ号码:" + market.getAUIQQ());
                }
                if (market.getAUIWEIXIN().equals("")) {
                    menuWindow.setCopyboolean(false);
                } else {
                    menuWindow.setCopyboolean(true);
                    menuWindow.setCopyText("微信:" + market.getAUIWEIXIN());
                }
                menuWindow.setSaveboolean(false);
                menuWindow.setInfo(false);
                menuWindow.setCallOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse("tel:" + market.getAUIPHONE()));
//                        MarketContent.this.startActivity(intent);
                        CallUtil.CallPhone(MarketContent.this, market.getAUIPHONE());
                    }
                });
                menuWindow.setMessageOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        boolean bln = ConnectivityUtils.isQQClientAvailable(MarketContent.this);
                        if (!bln) {
                            ToastManager.getInstance().showToast(MarketContent.this, "未安装qq");
                            return;
                        }
                        try {
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + market.getAUIQQ();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            ToastManager.getInstance().showToast(MarketContent.this, "未安装qq");
                        }
                    }
                });
                menuWindow.setCopyOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        boolean bln = ConnectivityUtils.isWeixinAvilible(MarketContent.this);
                        if (!bln) {
                            ToastManager.getInstance().showToast(MarketContent.this, "未安装微信");
                            return;
                        }
                        CallUtil.copyStrNoToast(MarketContent.this, market.getAUIQQ());
                        final CommDialog dialog = new CommDialog(MarketContent.this);
                        dialog.CreateDialog("确定", "复制成功", "点击确定打开微信并添加好友!", MarketContent.this, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                dialog.cancelDialog();
                                if (flag) {
                                    try {
                                        Intent intent = new Intent();
                                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                        intent.setAction(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setComponent(cmp);
                                        startActivityForResult(intent, 0);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
