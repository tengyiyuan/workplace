package com.toplion.cplusschool.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.toplion.cplusschool.R;


/**
 * @author wangshengbo
 * @version 1.0.0 2016-7-4
 * @Description 报修详情撤销框
 */
public class CustMenuCancledialog extends Dialog {
    public static EditText et_repair_cancle_content;
    public static Button bt_repair_cancle_commit;
    private RadioGroup et_repair_cancle;
    public static RadioButton radioButton;
    public CustMenuCancledialog(Context context, int width, int height) {
        super(context, R.style.edit_AlertDialog_style);
        setContentView(R.layout.repaircancle);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        et_repair_cancle_content = (EditText) findViewById(R.id.et_repair_cancle_content);
        et_repair_cancle = (RadioGroup) findViewById(R.id.et_repair_cancle);
        bt_repair_cancle_commit = (Button) findViewById(R.id.bt_repair_cancle_commit);
        radioButton = (RadioButton) findViewById(et_repair_cancle.getCheckedRadioButtonId());
        et_repair_cancle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
            }
        });
    }

}
