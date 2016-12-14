package com.toplion.cplusschool.QianDao;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.RotateAnimation;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.SignCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by toplion on 2016/10/18.
 */
public class QianDaoActivity extends BaseActivity implements View.OnClickListener {
    private SignCalendar calendar;
    private int years;
    private String months;
    List<String> list = new ArrayList<String>();

    private RelativeLayout rl_layout01;
    private RelativeLayout rl_layout02;
    private ViewGroup mContainer;
    private ImageView about_iv_return;
    private SharePreferenceUtils share;
    private AbHttpUtil abHttpUtil;//网络请求工具
    private int days;
    private int intflows;
    private TextView qiandaodays;
    private TextView flowstext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiandao);
        share = new SharePreferenceUtils(this);
        days = share.getInt("SBICONSECUTIVEDAYS", 0);
        intflows = share.getInt("SBIFLOWERSNUMBER", 0);
        abHttpUtil = AbHttpUtil.getInstance(this);//初始化请求工具
        about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        mContainer = (ViewGroup) findViewById(R.id.container);
        qiandaodays = (TextView) findViewById(R.id.days);
        flowstext = (TextView) findViewById(R.id.flows);
        mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
        rl_layout01 = (RelativeLayout) findViewById(R.id.rl_layout01);
        rl_layout02 = (RelativeLayout) findViewById(R.id.rl_layout02);
        calendar = (SignCalendar) findViewById(R.id.sc_main);
        rl_layout01.setOnClickListener(this);
        rl_layout02.setOnClickListener(this);
        about_iv_return.setOnClickListener(this);
        qiandaodays.setText("已经连续签到" + days + "天");
        flowstext.setText(intflows + "");
        getData();
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("username", share.getString("ROLE_ID", ""));
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getCurrentMonthSign") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在加载数据...") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject obj = new JSONObject(Function.getInstance().getString(object, "data"));
                    JSONArray array = obj.getJSONArray("date");
                    int state = Function.getInstance().getInteger(obj, "status");
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject aryobj = (JSONObject) array.get(i);
                            String data = Function.getInstance().getString(aryobj, "t_date");
                            list.add(data);
                        }
                        calendar.addMarks(list, 0);
                    }
                    if (state == 1) {
                        rl_layout01.setVisibility(View.GONE);
                        rl_layout02.setVisibility(View.VISIBLE);
                    } else {
                        rl_layout01.setVisibility(View.VISIBLE);
                        rl_layout02.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_layout01:
                updateQiandao();
                break;
            case R.id.rl_layout02:
                ToastManager.getInstance().showToast(this, "您今天已经签到,请明天再来");
                break;
            case R.id.about_iv_return:
                finish();
            default:
                break;
        }

    }

    private void updateQiandao() {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("username", share.getString("ROLE_ID", ""));
        params.put("module", 1);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("addSign") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在签到") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String data = Function.getInstance().getString(object, "data");
                    String flows = Function.getInstance().getString(object, "flower");
                    JSONObject flowobj = new JSONObject(flows);
                    intflows = Function.getInstance().getInteger(flowobj, "SBIFLOWERSNUMBER");
                    days = Function.getInstance().getInteger(flowobj, "SBICONSECUTIVEDAYS");
                    if (!data.equals("")) {
                        list.add(data);
                        calendar.addMarks(list, 0);
                        qiandaodays.setText("已经连续签到" + days + "天");
                        share.put("SBICONSECUTIVEDAYS", days);
                        share.put("SBIFLOWERSNUMBER", intflows);
                        flowstext.setText(intflows + "");
                        applyRotation(0, 0, 90);
                    } else {
                        ToastManager.getInstance().showToast(QianDaoActivity.this, "签到失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(QianDaoActivity.this, "签到异常");
                }
            }
        });
    }

    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final RotateAnimation rotation =
                new RotateAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(200);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));
        mContainer.startAnimation(rotation);
    }

    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition));

        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            RotateAnimation rotation;

            if (mPosition > -1) {
                rl_layout01.setVisibility(View.GONE);
                rl_layout02.setVisibility(View.VISIBLE);

                rotation = new RotateAnimation(0, 0, centerX, centerY, 310.0f, false);
            } else {
                rl_layout02.setVisibility(View.GONE);
                rl_layout01.setVisibility(View.VISIBLE);
                rotation = new RotateAnimation(90, 0, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(200);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContainer.startAnimation(rotation);
        }
    }

}
