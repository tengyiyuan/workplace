package com.toplion.cplusschool.widget;

import java.math.BigDecimal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.R;


/**
 * @Description自定义输入框
 * @author tengyiyuan
 * @version 1.0.0 2016-5-03
 * @reviewer
 */
public class CustEditTextLRTB extends RelativeLayout {
    private EditText edit_content;
    private TextView txt_lable;
    ImageView image_ico;
    ImageView image_ico1;
    private int input_style = 0;
    private int layout_backgroud = 0;
    private int is_select = 0;
    private String hint = "请输入内容";
    private int left_drawble = 0;
    private int right_drawble = 0;
    private int max_length = 0;
    private OnTextChangeListener ontextchangelistener;
    private OnTouchListener onTouchListener;
    private String lable_string = "";
    private int decimallength = 0;
    private int integerlength = 0;

    public CustEditTextLRTB(Context context) {

        this(context, null);
    }

    public CustEditTextLRTB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray type_array = context.obtainStyledAttributes(attrs,
                R.styleable.CustEditTextLRTB);
        try {
            input_style = type_array.getInt(
                    R.styleable.CustEditTextLRTB_input_type, 0);
            layout_backgroud = type_array.getResourceId(
                    R.styleable.CustEditTextLRTB_background,
                    R.drawable.edit_selector);
            is_select = type_array.getInt(
                    R.styleable.CustEditTextLRTB_is_Select, 0);
            hint = type_array.getString(R.styleable.CustEditTextLRTB_hint);
            left_drawble = type_array.getResourceId(
                    R.styleable.CustEditTextLRTB_left_drawble, 0);
            max_length = type_array.getInt(
                    R.styleable.CustEditTextLRTB_max_length, 0);
            lable_string = type_array
                    .getString(R.styleable.CustEditTextLRTB_lable_content);
            right_drawble = type_array.getResourceId(
                    R.styleable.CustEditTextLRTB_right_drawble, 0);
            decimallength = type_array.getInt(
                    R.styleable.CustEditTextLRTB_decimallength, 0);
            integerlength = type_array.getInt(
                    R.styleable.CustEditTextLRTB_integerlength, 0);
        } catch (Exception e) {

        } finally {
            type_array.recycle();
        }
        Ini_View(context);
    }

    public CustEditTextLRTB(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    private void Ini_View(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.cust_edittext,
                null);
        this.setBackgroundResource(layout_backgroud);
        edit_content = (EditText) v.findViewById(R.id.edit_content);
        txt_lable = (TextView) v.findViewById(R.id.txt_lable);
        image_ico = (ImageView) v.findViewById(R.id.image_delete);
        image_ico1 = (ImageView) v.findViewById(R.id.image_pwd);
        if (lable_string != null)
            txt_lable.setText(lable_string + ":");
        if (is_select == 1)
            this.setSelected(true);
        if (decimallength > 0)
            edit_content.setFilters(new InputFilter[] { lengthfilter });
        else if (max_length > 0)
            edit_content
                    .setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                            max_length) });

        edit_content.setCompoundDrawablesWithIntrinsicBounds(left_drawble, 0,
                right_drawble, 0);
        switch (input_style) {
            case 0:
                edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:
                edit_content.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:
               // edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
                 edit_content.setInputType(InputType.TYPE_CLASS_TEXT
                 | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 3:
                edit_content.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                edit_content.setMaxLines(10);
                break;
            case 4:
                edit_content.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            default:
                break;
        }
        edit_content.setHint(hint);
        edit_content.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    image_ico.setVisibility(View.VISIBLE);
                    CustEditTextLRTB.this.setSelected(true);
                    if (input_style == 2)
                        image_ico1.setVisibility(View.VISIBLE);
                } else {
                    try {
                        if (decimallength > 0)
                            edit_content.setText(format_string_two(edit_content
                                    .getText().toString(), decimallength));
                    } catch (Exception e) {

                    }
                    image_ico.setVisibility(View.GONE);
                    CustEditTextLRTB.this.setSelected(false);
                    if (input_style == 2)
                        image_ico1.setVisibility(View.GONE);
                }
            }
        });

        edit_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (ontextchangelistener != null)
                    ontextchangelistener.OnTextChange(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("myw", s.length() + "");
                if (s.length() > 0) {
                    image_ico.setVisibility(View.VISIBLE);
                    if (input_style == 2)
                        image_ico1.setVisibility(View.VISIBLE);
                } else {
                    image_ico.setVisibility(View.GONE);
                    if (input_style == 2)
                        image_ico1.setVisibility(View.GONE);
                }
            }
        });
        edit_content.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (onTouchListener != null)
                    onTouchListener.onTouch(v, event);
                return false;
            }
        });
        if (input_style == 2) {
            image_ico1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (edit_content.getInputType() == InputType.TYPE_CLASS_TEXT) {
                        edit_content.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        image_ico1.setImageResource(R.mipmap.edit_show_pw_on);
                        edit_content.setSelection(edit_content.length());
                    } else {
                        edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
                        image_ico1
                                .setImageResource(R.mipmap.edit_show_pw_off);
                        edit_content.setSelection(edit_content.length());
                    }

                }
            });

        }
        image_ico.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                edit_content.setText("");

            }
        });
        this.addView(v);
    }

    /**
     *
     * @Title: setVisibleDelete
     * @Description: TODO(设置清空图标是否可见)
     * @param @param isvisible 设定文件
     * @return void 返回类型
     * @author tengyiyuan
     * @throws
     */
    public void setVisibleDelete(boolean isvisible) {
        if (isvisible)
            image_ico.setVisibility(View.VISIBLE);
        else image_ico.setVisibility(View.GONE);
    }

    /**
     *
     * @Title: setChildText
     * @Description: TODO(赋值)
     * @param @param c 设定文件
     * @return void 返回类型
     * @author tengyiyuan
     * @throws
     */
    public void setChildText(CharSequence c) {
        if (edit_content != null)
            edit_content.setText(c);
        edit_content.setSelection(edit_content.length());
        image_ico.setVisibility(View.GONE);
    }

    /**
     *
     * @Title: updateColor
     * @Description: TODO(为标题和内容提示颜色赋值)
     * @param @param c 设定文件
     * @return void 返回类型
     * @author tengYiyuan
     * @throws
     */
    public void updateColor(int textcolor, int hitcolor) {
        if (txt_lable != null && edit_content != null) {
            txt_lable.setTextColor(textcolor);
            edit_content.setHintTextColor(hitcolor);
        }
    }

    /**
     *
     * @Title: updateTextsize
     * @Description: TODO(修改提示文字的大小)
     * @param @param c 设定文件
     * @return void 返回类型
     * @author tengYiyuan
     * @throws
     */
    public void updateTextsize(int textsize) {
        if (edit_content != null) {
            edit_content.setTextSize(textsize);
        }
    }

    /**
     *
     * @Title: setChildHint
     * @Description: TODO(设置提示文字)
     * @param @param c 设定文件
     * @return void 返回类型
     * @author tengyiyuan
     * @throws
     */
    public void setChildHint(CharSequence c) {
        if (edit_content != null)
            edit_content.setHint(c);
    }

    /**
     *
     * @Title: getText
     * @Description: TODO(获取内容)
     * @param @return 设定文件
     * @return String 返回类型
     * @author tengyiyuan
     * @throws
     */
    public Editable getText(String ss) {
        if (edit_content != null)
            return edit_content.getText();
        return null;
    }

    public String getText() {
        if (edit_content != null)
            return edit_content.getText().toString();
        return "";
    }

    public String getHint() {
        if (edit_content != null)
            return edit_content.getHint().toString();
        return "";
    }

    public int getSelectionStart() {
        return edit_content.getSelectionStart();
    }

    public void setSelection(int position) {
        edit_content.setSelection(position);
    }

    public int length() {
        return edit_content.length();
    }

    public interface OnTextChangeListener {
        void OnTextChange(CharSequence ss);

    }

    public void setOnTextChangeListener(
            OnTextChangeListener ontextchangelistener) {
        this.ontextchangelistener = ontextchangelistener;
    }

    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setInputTypeAndMax(int type, int maxleng) {
        edit_content.setInputType(type);
        edit_content
                .setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                        maxleng) });
    }

    public void setEnable(boolean temp) {
        edit_content.setEnabled(temp);
    }

    public void setFocusable(boolean temp) {
        edit_content.setFocusable(temp);
    }

    public void setDecimal(int integerlength, int decimallength) {
        this.decimallength = decimallength;
        this.integerlength = integerlength;
    }

    // protected void set
    public EditText getcontent() {
        return edit_content;
    }

    // private static final int DECIMAL_DIGITS = 1;
    /**
     * 设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String tempString = dest + "" + source;
            if (dest.length() == 0 && source.equals("."))
                return "";
            if (dest.length() == integerlength && !source.equals(".")
                    && !tempString.contains(".")) {
                return "";
            }
            if (tempString.contains(".")) {
                String stringDecimal = tempString.substring(tempString
                        .indexOf(".") + 1);
                if (stringDecimal.length() == decimallength + 1)
                    return "";
            }
            Log.i("manyi", tempString);
            return null;
        }
    };

    protected String format_string_two(String temp, int decimal) {
        BigDecimal bigDecimal = new BigDecimal(temp);
        bigDecimal = bigDecimal.setScale(decimal, BigDecimal.ROUND_HALF_UP);
        return bigDecimal + "";

    }

}
