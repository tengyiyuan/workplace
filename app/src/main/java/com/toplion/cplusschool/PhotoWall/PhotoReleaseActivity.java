package com.toplion.cplusschool.PhotoWall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ImageUtil;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wang
 * on 2016/11/11.
 *
 * @照片发布
 */

public class PhotoReleaseActivity extends BaseActivity {
    private ImageView iv_phone_release_return;
    private ImageView iv_photo_release_show;//show
    private Button bt_photo_release_confirm;//确认发布
    private Button bt_photo_release_reselect;//重新选择
    private AbHttpUtil abHttpUtil;
    private SharePreferenceUtils share;
    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_release);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        iv_phone_release_return = (ImageView) findViewById(R.id.iv_phone_release_return);
        iv_photo_release_show = (ImageView) findViewById(R.id.iv_photo_release_show);
        bt_photo_release_confirm = (Button) findViewById(R.id.bt_photo_release_confirm);
        bt_photo_release_reselect = (Button) findViewById(R.id.bt_photo_release_reselect);
        imgUrl = getIntent().getStringExtra("imgUri");
        if (!TextUtils.isEmpty(imgUrl)) {
            AbImageLoader.getInstance(this).display(iv_photo_release_show, imgUrl);
        }
        setListener();
    }

    //发布照片
    private void releasePhoto() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("addPhotowallRelease") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("photourl", imgUrl);
        abHttpUtil.post(url, params, new CallBackParent(this, getString(R.string.loading), "addPhotowallRelease") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    final CommDialog commDialog = new CommDialog(PhotoReleaseActivity.this);
                    commDialog.CreateDialogOnlyOk("系统提示", "确定", msg, new CommDialog.CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            Intent intent1 = new Intent(PhotoReleaseActivity.this, MyHomeActivity.class);
                            startActivity(intent1);
                            finish();
                            commDialog.cancelDialog();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        //确认发布
        bt_photo_release_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePhoto();
            }
        });
        //重新选择
        bt_photo_release_reselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        iv_phone_release_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
