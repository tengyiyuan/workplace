package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SelectPicUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CameraPopWindow;

/**
 * Created by toplion
 * on 2016/10/18.
 * @des 个人信息展示接界面
 */
public class PersonInfoActivity extends BaseActivity{
    private ImageView iv_pinfo_back;//返回键
    private LinearLayout ll_pinfo_parent;
    private ImageView iv_my_icon;//头像
    private RelativeLayout rl_pinfo_nickname;//昵称
    private RelativeLayout rl_pinfo_sex;//性别
    private RelativeLayout rl_pinfo_phone;//电话
    private RelativeLayout rl_pinfo_major;//专业
    private RelativeLayout rl_pinfo_brithday;//生日
    private RelativeLayout rl_pinfo_address;//地址
    private TextView tv_pinfo_nickname;//
    private TextView tv_pinfo_sex;//
    private TextView tv_pinfo_phone;//
    private TextView tv_pinfo_zhuanye;
    private TextView tv_pinfo_major;//
    private TextView tv_pinfo_brithday;//
    private TextView tv_pinfo_address;//
    private int outputX = 100;// 图片裁剪宽
    private int outputY = 100;//图片裁剪高
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        init();
    }

    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        iv_pinfo_back = (ImageView) findViewById(R.id.iv_pinfo_back);
        ll_pinfo_parent = (LinearLayout) findViewById(R.id.ll_pinfo_parent);
        iv_my_icon = (ImageView) findViewById(R.id.iv_my_icon);
        rl_pinfo_nickname = (RelativeLayout) findViewById(R.id.rl_pinfo_nickname);
        rl_pinfo_sex = (RelativeLayout) findViewById(R.id.rl_pinfo_sex);
        rl_pinfo_phone = (RelativeLayout) findViewById(R.id.rl_pinfo_phone);
        rl_pinfo_major = (RelativeLayout) findViewById(R.id.rl_pinfo_major);
        rl_pinfo_brithday = (RelativeLayout) findViewById(R.id.rl_pinfo_brithday);
        rl_pinfo_address = (RelativeLayout) findViewById(R.id.rl_pinfo_address);

        tv_pinfo_nickname = (TextView) findViewById(R.id.tv_pinfo_nickname);
        tv_pinfo_sex = (TextView) findViewById(R.id.tv_pinfo_sex);
        tv_pinfo_phone = (TextView) findViewById(R.id.tv_pinfo_phone);
        tv_pinfo_zhuanye = (TextView) findViewById(R.id.tv_pinfo_zhuanye);
        tv_pinfo_major = (TextView) findViewById(R.id.tv_pinfo_major);
        tv_pinfo_brithday = (TextView) findViewById(R.id.tv_pinfo_brithday);
        tv_pinfo_address = (TextView) findViewById(R.id.tv_pinfo_address);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.my_default_head);
        outputX = bitmap.getWidth();
        outputY = bitmap.getHeight();
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        tv_pinfo_nickname.setText(share.getString("NICKNAME",""));
        String sex = "";
        if(share.getString("XB","0").equals("1")){
            sex = "男";
        }else if(share.getString("XB","0").equals("2")){
            sex = "女";
        }
        tv_pinfo_sex.setText(sex);
        tv_pinfo_phone.setText(share.getString("SJH",""));
        if(share.getInt("ROLE_TYPE",0)==1){
            tv_pinfo_zhuanye.setText("职位");
            tv_pinfo_major.setText(share.getString("DEPARTNAME",""));
        }else if(share.getInt("ROLE_TYPE",0)==2){
            tv_pinfo_zhuanye.setText("专业");
            tv_pinfo_major.setText(share.getString("ZYMC",""));
        }
        tv_pinfo_brithday.setText(share.getString("CSRQ",""));
        tv_pinfo_address.setText(share.getString("ADDRESS",""));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK){
           switch (requestCode){
               case 1:
                   ToastManager.getInstance().showToast(this,"昵称");
                   break;
               case 2:
                   ToastManager.getInstance().showToast(this,"性别");
                   break;
               case 3:
                   ToastManager.getInstance().showToast(this,"电话");
                   break;
               case 4:
                   ToastManager.getInstance().showToast(this,"地址");
                   break;
               default:
                   Bitmap bitmap = SelectPicUtil.onActivityResult(this, requestCode, resultCode, data, outputX, outputY, 1, 1);
                   if (bitmap != null) {
                       bitmap = AbImageUtil.toRoundBitmap(bitmap);
                       iv_my_icon.setImageBitmap(bitmap);
                   }
                   break;
           }
        }

    }
    @Override
    protected void setListener() {
        super.setListener();
        iv_my_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraPopWindow cameraPopWindow = new CameraPopWindow(PersonInfoActivity.this,ll_pinfo_parent);
                cameraPopWindow.setBtnCameraOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraPopWindow.dismiss();
                        SelectPicUtil.getByCamera(PersonInfoActivity.this);
                    }
                });
                cameraPopWindow.setBtnPictureOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraPopWindow.dismiss();
                        SelectPicUtil.getByAlbum(PersonInfoActivity.this);
                    }
                });
            }
        });
        //昵称
        rl_pinfo_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this,ModifyUserInfoActivity.class);
                intent.putExtra("type",1);
                startActivityForResult(intent,1);
            }
        });
        //性别
        rl_pinfo_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this,ModifyUserInfoActivity.class);
                intent.putExtra("type",2);
                startActivityForResult(intent,2);
            }
        });
        //电话
        rl_pinfo_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this,ModifyUserInfoActivity.class);
                intent.putExtra("type",3);
                startActivityForResult(intent,3);
            }
        });
        //生日
        rl_pinfo_brithday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_pinfo_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this,ModifyUserInfoActivity.class);
                intent.putExtra("type",4);
                startActivityForResult(intent,4);
            }
        });
        iv_pinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
